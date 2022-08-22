package com.example.neostore.activities
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.neostore.R
import com.example.neostore.api.RetrofitClient
import com.example.neostore.models.DefaultResponse
import com.example.neostore.models.InvalidData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.registration_custom_actionbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class RegistrationScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        getSupportActionBar()?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar()?.setCustomView(R.layout.registration_custom_actionbar)
       regtollbarBack.setOnClickListener(){
            super.onBackPressed()
        }
        btnRegisterButton.setOnClickListener(){
            validateForm()
        }
    }
    private fun validateForm() {
        if(!firstNameValidate() or !lastNameValidate() or !emailAddressValidate() or !passWordValidate()
            or !confirmedPasswordValidate() or !phoneNumberValidate() or !checkGender() or !checkCheckBox()){
            return
        }
        else{
            Log.d(TAG, " successful")
            sendData()

        }
    }

    private fun sendData() {
        val firstName = txtInputFirstName.text.toString().trim()
        val lastName =txtInputLastName.text.toString().trim()
        val emailAdderess = txtInputEmail.text.toString().trim()
        val password = txtInputRegPassword.text.toString().trim()
        val confirmedPassword = txtInputConfirmedPassword.text.toString().trim()
        val phoneNumber :Number = txtInputPhoneNumber.text.toString().trim().toDouble()
        var selectGender : String? = null
        val radioBtnId = radioGroupButton.checkedRadioButtonId
        when(radioBtnId) {
            R.id.radioBtnMale -> selectGender="M"
            R.id.radioBtnFemale ->selectGender="F"
        }
        RetrofitClient.getClient.createUser(firstName,lastName,emailAdderess,password,confirmedPassword,
            selectGender!!,phoneNumber).enqueue(object : Callback<DefaultResponse?> {
            override fun onResponse(
                call: Call<DefaultResponse?>,
                response: Response<DefaultResponse?>
            ) {
                if(response.code() == 500 || response.code()==404 || response.code() == 422){
                    val gson: Gson = GsonBuilder().create()
                    val error : InvalidData
                    try {
                        error = gson.fromJson(response.errorBody()?.string(),InvalidData::class.java)
                        Toast.makeText(this@RegistrationScreen, "${error.user_msg}", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "on404 response: ${error.message} and ${error.status}")
                    }
                    catch (e:Exception){
                        Log.d(TAG, "onResponse: $e")
                    }

                }else{
                    val responseData = response.body()?.user_msg
                    Toast.makeText(this@RegistrationScreen, "$responseData", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@RegistrationScreen , MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            override fun onFailure(call: Call<DefaultResponse?>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }
        })
    }
    private fun checkCheckBox(): Boolean {
        if(checkBox.isChecked){
            return true
        }
        else{
            Toast.makeText(this, "please select term and condition", Toast.LENGTH_SHORT).show()
        }
        return false
    }
    private fun checkGender(): Boolean {
        if(radioGroupButton.checkedRadioButtonId == -1){
            Toast.makeText(this, "please select Gender", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
    private fun phoneNumberValidate(): Boolean {
        val phoneNumber = txtInputPhoneNumber.text.toString()
        if(phoneNumber.isEmpty()){
            txtInputPhoneNumber.apply {
                error="Field cannot be blank"
            }
            return false
        }else if (phoneNumber.length == 10){
            txtInputPhoneNumber.apply {
                error = null
            }
            txtInputPhoneNumber.requestFocus()
            return true
        }
        else{
            txtInputPhoneNumber.apply{
                error = "Please Input valid phone number"
            }
            return false
        }
    }

    private fun confirmedPasswordValidate(): Boolean {
        if(txtInputConfirmedPassword.text.toString().isEmpty()){
            txtInputConfirmedPassword.apply {
                error="Field cannot be blank"
            }
            return false
        }else if(txtInputConfirmedPassword.text.toString() == txtInputRegPassword.text.toString()){
            txtInputConfirmedPassword.apply {
                error = null
            }
            txtInputConfirmedPassword.requestFocus()
            return true
        }
        else{
            txtInputConfirmedPassword.apply{
                    error = "Confirmed password doesn't match"
            }
            return false
        }
    }

    private fun passWordValidate(): Boolean {
        val mPassWord = txtInputRegPassword.text.toString()
        if(txtInputRegPassword.text.toString().isEmpty()){
            txtInputRegPassword.apply {
                error="Field cannot be blank"
            }
            return false
        }else if( mPassWord.length >= 8){
            txtInputRegPassword.apply {
                error = null
            }
            txtInputRegPassword.requestFocus()
            return true
        }
        else{
            txtInputRegPassword.apply{
                error = "Password should be 8 character long"
            }
            return false
        }
    }

    private fun emailAddressValidate(): Boolean {
        val email = txtInputEmail.text.toString()
        if(email.isEmpty()){
            txtInputEmail.apply {
                error = "Field cannot be blank"
            }
            return false
        }
        else if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            txtInputEmail.apply {
                error = null
            }
            txtInputEmail.requestFocus()
            return true
        }
        else{
            txtInputEmail.apply {
                    error="please insert valid Email address"
                }
                txtInputEmail.requestFocus()
            return false
        }
    }

    private fun lastNameValidate(): Boolean {
        if(txtInputLastName.text.toString().isEmpty()){
            txtInputLastName.apply {
                error="Field cannot be blank"
            }
            return false
        }else{
            txtInputLastName.apply{
                error = null
            }
            txtInputLastName.requestFocus()
            return true
        }
    }

    private fun firstNameValidate(): Boolean {
        if(txtInputFirstName.text.toString().isEmpty()){
            txtInputFirstName.apply {
                error="Field cannot be blank"
            }
                return false
        }else{
            txtInputFirstName.apply{
                error = null
            }
            txtInputFirstName.requestFocus()
            return true
        }
    }
}