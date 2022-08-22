package com.example.neostore.activities

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.example.neostore.R
import com.example.neostore.api.RetrofitClient
import com.example.neostore.models.ForgotPasswordResponse
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_registration.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        btnForgotEmailSubmit.setOnClickListener(){
            validateForm()
        }
    }

    private fun validateForm() {
        if(!forgotEmailValidate()){
            return
        }else{
            submitEmail()
        }
    }

    private fun submitEmail() {
        val emailAddress = txtInputForgotEmail.text.toString()

        RetrofitClient.getClient.forgotPassword(emailAddress).enqueue(object : Callback<ForgotPasswordResponse?> {
            override fun onResponse(
                call: Call<ForgotPasswordResponse?>,
                response: Response<ForgotPasswordResponse?>
            ) {
                if(response.isSuccessful){
                    Toast.makeText(this@ForgotPasswordScreen,"${response.body()?.user_msg}",Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@ForgotPasswordScreen,MainActivity::class.java))
                }else{
                    Toast.makeText(this@ForgotPasswordScreen,"Something went wrong",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ForgotPasswordResponse?>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun forgotEmailValidate(): Boolean {
        val email = txtInputForgotEmail.text.toString()
        if(email.isEmpty()){
            txtInputForgotEmail.apply {
                error = "Field cannot be blank"
            }
            return false
        }
        else if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            txtInputForgotEmail.apply {
                error = null
            }
            txtInputForgotEmail.requestFocus()
            return true
        }
        else{
            txtInputForgotEmail.apply {
                error="please insert valid Email address"
            }
            txtInputForgotEmail.requestFocus()
            return false
        }

    }



}