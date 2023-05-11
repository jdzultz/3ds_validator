package com.xcaret.validation_3ds

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.xcaret.validation3ds.WebViewLibrary
import com.xcaret.validation3ds.domain.models.CardPayload
import com.xcaret.validation3ds.domain.models.GatewayPayload
import com.xcaret.validation3ds.extension.observe

class MainActivity : AppCompatActivity() {

     lateinit var myClass: WebViewLibrary

    private fun onStateChange(event: com.xcaret.validation3ds.ValidateCardStatusModel) {
        if (event.isRedelivered) return
        when (event) {
            is com.xcaret.validation3ds.ValidateCardStatusModel.Error -> Log.e("error", event.error)
            is com.xcaret.validation3ds.ValidateCardStatusModel.Loading -> Log.e("loading", "true")
            is com.xcaret.validation3ds.ValidateCardStatusModel.Success -> {
                event.token.let {
                    Log.e("success", event.token ?: "no hay session")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myClass = WebViewLibrary(this);
        observe(myClass.getGateway(), ::onStateChange)

        val myButton = findViewById<Button>(R.id.my_button)
        myButton.setOnClickListener(::myButtonClick)
    }

    fun myButtonClick(view: View) {
        myClass.getCallGateway(
            GatewayPayload(
                salesChannel = "WEB",
                currencyCode = "GBP",
                card =  CardPayload(
                    cardNumber = "4000000000001091",
                    installmentsPlan = "0",
                    bankId = "49"
                )
            ))
    }
}