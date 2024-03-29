package com.jimbonlemu.fundamental_android.view.view_model_factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.jimbonlemu.fundamental_android.utils.SettingPreference
import com.jimbonlemu.fundamental_android.view.view_model.SettingViewModel

@Suppress("UNCHECKED_CAST")
class SettingViewModelFactory (private val pref:SettingPreference):NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)){
            return SettingViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class:" + modelClass.name)
    }

}