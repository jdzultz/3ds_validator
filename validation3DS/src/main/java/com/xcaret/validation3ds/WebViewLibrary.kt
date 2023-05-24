package com.xcaret.validation3ds

import android.content.Context
import androidx.lifecycle.LiveData
import com.xcaret.validation3ds.data.GatewayRepositoryImp
import com.xcaret.validation3ds.data.mapper.GatewayMapper
import com.xcaret.validation3ds.data.mapper.GatewayPayloadMapper
import com.xcaret.validation3ds.data.source.GatewayDataSourceFactory
import com.xcaret.validation3ds.data.source.GatewayRemoteDataSource
import com.xcaret.validation3ds.domain.interactor.GetGatewayUseCase
import com.xcaret.validation3ds.domain.models.Gateway
import com.xcaret.validation3ds.domain.models.GatewayPayload
import com.xcaret.validation3ds.domain.models.PostMessage
import com.xcaret.validation3ds.presentation.utils.*
import com.xcaret.validation3ds.presentation.viewmodel.BaseViewModel
import com.xcaret.validation3ds.remote.api.ServiceFactory
import com.xcaret.validation3ds.remote.mappers.GatewayEntityMapper
import com.xcaret.validation3ds.remote.mappers.GatewayPayloadEntityMapper
import com.xcaret.validation3ds.remote.repository.GatewayRemoteImp
import com.xcaret.validation3ds.ui.dialogs.DialogGetSessionId
import com.xcaret.validation3ds.ui.dialogs.DialogGetView3DS
import com.xcaret.validation3ds.ui.interfaces.IframeHidden
import com.xcaret.validation3ds.ui.interfaces.IframeShow
import kotlinx.coroutines.*

sealed class ValidateCardStatusModel: UiAwareModel() {
    object Loading: ValidateCardStatusModel()
    data class Error(var error: String): ValidateCardStatusModel()
    data class Success(val token: String): ValidateCardStatusModel()
}

sealed class ValidateFormOneStatusModel: UiAwareModel() {
    object Loading: ValidateFormOneStatusModel()
    data class Error(var error: String): ValidateFormOneStatusModel()
    data class Success(val data: PostMessage): ValidateFormOneStatusModel()
}

sealed class ValidateFormTwoStatusModel: UiAwareModel() {
    object Loading: ValidateFormTwoStatusModel()
    data class Error(var error: String): ValidateFormTwoStatusModel()
    object Success: ValidateFormTwoStatusModel()
}

class WebViewLibrary(context: Context, gateWayApiBaseUrl: String): BaseViewModel(CoroutineContextProviderImp())
{
    private val myWebviewDialogListener = object : IframeHidden {
        override fun onIframeFinished(data: PostMessage) {
            _formOne.postValue(ValidateFormOneStatusModel.Success(data))
        }
        override fun onError() {
            _formOne.postValue(ValidateFormOneStatusModel.Error("Error"))
        }
    }

    private val view3DSDialogListener = object : IframeShow {
        override fun onIframeFinished() {
            _formTwo.postValue(ValidateFormTwoStatusModel.Success)
        }
        override fun onError() {
            _formTwo.postValue(ValidateFormTwoStatusModel.Error("Error"))
        }
    }
    //implementation to di
    private val gatewayRemoteImp = GatewayRemoteImp(
        ServiceFactory.create(true, gateWayApiBaseUrl),
        GatewayEntityMapper(), GatewayPayloadEntityMapper())
    private val gatewayRemoteDataSource = GatewayRemoteDataSource(gatewayRemoteImp)
    private val gatewayDataSource = GatewayDataSourceFactory(gatewayRemoteDataSource)
    private val gatewayRepository = GatewayRepositoryImp(gatewayDataSource, GatewayMapper(), GatewayPayloadMapper())
    private val gatewayUseCase = GetGatewayUseCase(gatewayRepository)

    private val dialogGetSessionId = DialogGetSessionId(context, myWebviewDialogListener)
    private val dialogGetView3DS = DialogGetView3DS(context, view3DSDialogListener)
    private val _gateway = UiAwareLiveData<ValidateCardStatusModel>()
    private var gateway: LiveData<ValidateCardStatusModel> = _gateway

    private val _formOne = UiAwareLiveData<ValidateFormOneStatusModel>()
    private var formOne: LiveData<ValidateFormOneStatusModel> = _formOne

    private val _formTwo = UiAwareLiveData<ValidateFormTwoStatusModel>()
    private var formTwo: LiveData<ValidateFormTwoStatusModel> = _formTwo

    override val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _gateway.postValue(ValidateCardStatusModel.Error(exception.message ?: "Error"))
    }

    fun getGateway(): LiveData<ValidateCardStatusModel> {
        return gateway
    }

    fun getFormOneStatus(): LiveData<ValidateFormOneStatusModel> {
        return formOne
    }

    fun getFormTwoStatus(): LiveData<ValidateFormTwoStatusModel> {
        return formTwo
    }

    fun getCallGateway(params: GatewayPayload, callback: ((data: Gateway) -> Unit)? = null) {
        _gateway.postValue(ValidateCardStatusModel.Loading)
        launchCoroutineIO{
            loadGateway(params) {
                if (callback != null) {
                    callback(it)
                }
            }
        }
    }

    private suspend fun loadGateway(params: GatewayPayload, callback: ((data: Gateway) -> Unit)? = null) {
        gatewayUseCase(params).collect {
            launchCoroutineMain {
                _gateway.postValue(ValidateCardStatusModel.Success(it.token.toString()))
                if(callback != null) {
                    callback(it)
                }
            }
        }
    }

    fun launchFormOne (cardNumber: String, token: String, urlForm: String, callback: ((data: PostMessage?) -> Unit)? = null) {
        dialogGetSessionId.show(cardNumber, token, urlForm) {
            response ->
            if (callback != null) {
                callback(response)
            }
        }
    }

    fun launchFormTwo (md: String, jwt: String,  urlForm: String, title:String? = null, subtitle:String? = null, callback: (() -> Unit)? = null) {
        dialogGetView3DS.show(md, jwt, urlForm, title, subtitle){
            if (callback != null) {
                callback()
            }
        }
    }

}