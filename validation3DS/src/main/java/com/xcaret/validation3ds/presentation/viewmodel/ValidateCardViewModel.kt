package com.xcaret.validation3ds.presentation.viewmodel

import androidx.lifecycle.LiveData
import com.xcaret.validation3ds.domain.interactor.GetGatewayUseCase
import com.xcaret.validation3ds.domain.models.Gateway
import com.xcaret.validation3ds.presentation.utils.CoroutineContextProvider
import com.xcaret.validation3ds.presentation.utils.UiAwareLiveData
import com.xcaret.validation3ds.presentation.utils.UiAwareModel
import kotlinx.coroutines.CoroutineExceptionHandler


sealed class ValidateCardUIModel: UiAwareModel() {
    object Loading: ValidateCardUIModel()
    data class Error(var error: String): ValidateCardUIModel()
    data class Success(val data: Gateway): ValidateCardUIModel()
}

class ValidateCardViewModel constructor(
    contextProvider: CoroutineContextProvider,
    private val gatewayUseCase: GetGatewayUseCase
): BaseViewModel(contextProvider) {

    private val _gateway = UiAwareLiveData<ValidateCardUIModel>()
    private var gateway: LiveData<ValidateCardUIModel> = _gateway

    fun getGateway(): LiveData<ValidateCardUIModel> {
        return gateway
    }

    override val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _gateway.postValue(ValidateCardUIModel.Error(exception.message ?: "Error"))
    }

    fun getCallGateway() {
        _gateway.postValue(ValidateCardUIModel.Loading)
        launchCoroutineIO {
           loadGateway()
        }
    }

    private suspend fun loadGateway() {
        /*gatewayUseCase(Unit).collect {
            _gateway.postValue(ValidateCardUIModel.Success(it))
        }*/
    }

}