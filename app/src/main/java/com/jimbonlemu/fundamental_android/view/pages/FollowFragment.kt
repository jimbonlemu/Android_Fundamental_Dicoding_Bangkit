package com.jimbonlemu.fundamental_android.view.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jimbonlemu.fundamental_android.databinding.FragmentFollowBinding
import com.jimbonlemu.fundamental_android.view.adapter.ListGithubUserAdapter
import com.jimbonlemu.fundamental_android.view.view_model.FollowersViewModel
import com.jimbonlemu.fundamental_android.view.view_model.FollowingViewModel

class FollowFragment : Fragment() {
    private var _position: Int? = null
    private var _username: String? = null
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding
    private val followingViewModel by viewModels<FollowingViewModel>()
    private val followersViewModel by viewModels<FollowersViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            _position = it.getInt(ARGS_TAB_POSITION)
            _username = it.getString(ARGS_GITHUB_USERNAME)
        }

        val listUserAdapter = ListGithubUserAdapter()

        initRecyclerView()

        if (_position == 1) {
            setFollowingData(listUserAdapter)
        } else {
            setFollowersData(listUserAdapter)
        }

    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(requireActivity())
        binding?.rvFollow?.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding?.rvFollow?.addItemDecoration(itemDecoration)
    }

    private fun setFollowingData(adapter: ListGithubUserAdapter) {
        with(followingViewModel) {
            if (followingData.value == null) {
                getFollowingGithubUser(_username!!)
            }

            spawnSnackBar.observe(requireActivity()) {
                it.getContentIfUnhandled()?.let { textOnSnackBar ->
                    Snackbar.make(binding?.root?.rootView!!, textOnSnackBar, Snackbar.LENGTH_SHORT)
                        .show()
                }
            }

            isError.observe(viewLifecycleOwner) {
                binding?.tvFollowError?.text = it
            }

            followingData.observe(viewLifecycleOwner) {
                adapter.submitList(it)
                binding?.rvFollow?.adapter = adapter
            }

            isLoading.observe(viewLifecycleOwner) {
                initLoader(it)
            }
        }
    }

    private fun setFollowersData(adapter: ListGithubUserAdapter) {
        with(followersViewModel) {
            if (followersData.value == null) {
                getFollowersGithubUser(_username!!)
            }

            isError.observe(viewLifecycleOwner) {
                binding?.tvFollowError?.text = it
            }

            spawnSnackBar.observe(requireActivity()) {
                it.getContentIfUnhandled()?.let { textOnSnackBar ->
                    Snackbar.make(binding?.root?.rootView!!, textOnSnackBar, Snackbar.LENGTH_SHORT)
                        .show()
                }
            }

            followersData.observe(viewLifecycleOwner) {
                adapter.submitList(it)
                binding?.rvFollow?.adapter = adapter
            }

            isLoading.observe(viewLifecycleOwner) {
                initLoader(it)
            }
        }
    }

    private fun initLoader(isLoading: Boolean) {
        if (isLoading) {
            binding?.loaderRvFollow?.root?.startShimmer()
            binding?.loaderRvFollow?.root?.visibility = View.VISIBLE
            binding?.rvFollow?.visibility = View.INVISIBLE
        } else {
            binding?.loaderRvFollow?.root?.stopShimmer()
            binding?.loaderRvFollow?.root?.visibility = View.GONE
            binding?.rvFollow?.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val ARGS_TAB_POSITION = "args_tab_position"
        const val ARGS_GITHUB_USERNAME = "args_github_username"
    }
}
