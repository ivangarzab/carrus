package com.ivangarzab.carrus.ui.interstitials

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivangarzab.carrus.ui.interstitials.data.PermissionInterstitialData
import com.ivangarzab.carrus.ui.interstitials.data.PermissionInterstitialEnum

/**
 * Created by Ivan Garza Bermea.
 */
open class PermissionInterstitialViewModel : ViewModel() {

    private val _uiState = MutableLiveData<PermissionInterstitialData>(null)
    val uiState: LiveData<PermissionInterstitialData> = _uiState

    fun init(type: PermissionInterstitialEnum) = _uiState.postValue(type.data)
}