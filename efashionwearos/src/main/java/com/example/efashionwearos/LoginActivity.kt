package com.example.efashionwearos

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.efashionwearos.models.User
import com.example.efashionwearos.repository.UserRepository
import com.example.efashionwearos.retrofit.ServiceBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : Activity() {
    //Initialize
    private lateinit var etWearEmail: EditText
    private lateinit var etWearPassword: EditText
    private lateinit var btnWearLogin: Button
    private lateinit var pb_wear: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        etWearEmail = findViewById(R.id.etWearEmail)
        etWearPassword = findViewById(R.id.etWearPassword)
        btnWearLogin = findViewById(R.id.btnWearLogin)
        pb_wear = findViewById(R.id.pb_wear)

        etWearEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInputProvidedByUser()
            }
        })
        etWearPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInputProvidedByUser()
            }
        })
        btnWearLogin.setOnClickListener {
            checkForEmailAndPassword()
        }
    }


    @SuppressLint("ResourceAsColor")
    private fun checkForEmailAndPassword() {
        if (Patterns.EMAIL_ADDRESS.matcher(etWearEmail.text).matches()) {
            if (etWearPassword.length() >= 5) {
                pb_wear.isVisible = true
                btnWearLogin.isVisible = false
                btnWearLogin.setTextColor(R.color.textDisabledColor)
                logInWithEmailAndPassword()
            } else {
                etWearPassword.error = "Password doesn't match"

            }
        } else {
            etWearEmail.error = "Please enter a valid email address"
            etWearEmail.requestFocus()
            return
        }
    }

    private fun logInWithEmailAndPassword() {
        // login with Api-Retrofit
        withApiRetrofit()
    }

    private fun withApiRetrofit() {
        val email = etWearEmail.text.toString()
        val password = etWearPassword.text.toString()
        // from retrofit-model
        val user = User(email = email, password = password)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userRepository = UserRepository()
                val response = userRepository.loginUser(user)

                if (response.success == true) {

                    ServiceBuilder.token = "Bearer ${response.accessToken!!}"
                    withContext(Dispatchers.Main) {
                        addUserDetailInfo()
                        Toast.makeText(this@LoginActivity, "Successfully Login", Toast.LENGTH_SHORT)
                            .show()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@LoginActivity,
                            "Invalid username or password",
                            Toast.LENGTH_SHORT).show()
                        pb_wear.isVisible = false
                        btnWearLogin.isVisible = true
                    }
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    pb_wear.isVisible = false
                    btnWearLogin.isVisible = true
                    Toast.makeText(this@LoginActivity,
                        "Invalid username or password",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

        private fun addUserDetailInfo() {
        val sharedPreferences = getSharedPreferences("userAuth", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("email", etWearEmail.text.toString())
        editor.putString("password", etWearPassword.text.toString())
        editor.putString("token", ServiceBuilder.token!!)
        editor.apply()
    }

    // check user details
    @SuppressLint("ResourceAsColor")
    private fun checkInputProvidedByUser() {
        if (!TextUtils.isEmpty(etWearEmail.text)) {
            if (etWearPassword.length() >= 5) {
                btnWearLogin.isEnabled = true
                btnWearLogin.setBackgroundColor(application.resources.getColor(R.color.buttonColor))
                btnWearLogin.setTextColor(application.resources.getColor(R.color.white))
                pb_wear.isEnabled = true
            } else {
                btnWearLogin.isEnabled = false
                btnWearLogin.setTextColor(R.color.textDisabledColor)
            }
        } else {
            btnWearLogin.isEnabled = false
            btnWearLogin.setTextColor(R.color.textDisabledColor)
        }
    }

}