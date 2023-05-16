package com.hdd.globalmovie.presentation.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.hdd.globalmovie.R
import com.hdd.globalmovie.data.models.User
import com.hdd.globalmovie.data.localDataSource.UserDatabase
import com.hdd.globalmovie.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.*

class SignUpActivity : AppCompatActivity() {
    //Initialize
    private lateinit var et_sign_up_full_name: EditText
    private lateinit var et_sign_up_email: EditText
    private lateinit var et_sign_up_password: EditText
    private lateinit var et_sign_up_confirm_password: EditText
    private lateinit var btnSignup: Button
    private lateinit var pb_sign_up: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        et_sign_up_full_name = findViewById(R.id.et_sign_up_full_name)
        et_sign_up_email = findViewById(R.id.et_sign_up_email)
        et_sign_up_password = findViewById(R.id.et_sign_up_password)
        et_sign_up_confirm_password = findViewById(R.id.et_sign_up_confirm_password)
        btnSignup = findViewById(R.id.btnSignup)
        pb_sign_up = findViewById(R.id.pb_sign_up)

        // validation Layer
        et_sign_up_full_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInputProvidedByUser()
            }
        })
        et_sign_up_email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInputProvidedByUser()
            }
        })
        et_sign_up_password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInputProvidedByUser()
            }
        })
        et_sign_up_confirm_password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInputProvidedByUser()
            }
        })

        btnSignup.setOnClickListener {
            checkForEmailAndPassword()
        }
    }


    @SuppressLint("ResourceAsColor")
    private fun checkForEmailAndPassword() {
        if (Patterns.EMAIL_ADDRESS.matcher(et_sign_up_email.text).matches()) {
            if (et_sign_up_password.text.toString() == et_sign_up_confirm_password.text.toString()) {
                pb_sign_up.isVisible = true
                btnSignup.isVisible = false
                btnSignup.setTextColor(R.color.textDisabledColor)
                signUpWithEmailAndPassword()
            } else {
                et_sign_up_confirm_password.error = "Password doesn't match"

            }
        } else {
            et_sign_up_email.error = "Please enter a valid email address"
            et_sign_up_email.requestFocus()
            return
        }
    }

    private fun signUpWithEmailAndPassword() {
        val user = User(
            fullname = et_sign_up_full_name.text.toString(),
            email = et_sign_up_email.text.toString(),
            password = et_sign_up_password.text.toString(),
        )
        // signup with room database
//        withRoomDB(user)

        // signup with Api-Retrofit
        withApiRetrofit(user)
    }

    private fun withApiRetrofit(user: User) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userRepository = UserRepository()
                val response = userRepository.registerUser(user)
                if (response.success == true) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@SignUpActivity,"Successfully Registered",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                    }
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    pb_sign_up.isVisible=false
                    btnSignup.isVisible=true
                    Toast.makeText(this@SignUpActivity, "Error $ex", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun withRoomDB(user: User) {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                UserDatabase.getInstance(this@SignUpActivity).userDao().registerUser(user)

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@SignUpActivity, "Sign up Successful", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        } catch (ex: Exception) {}
    }

    private fun createUsername(): String? {
        return et_sign_up_full_name.text.toString().split("")[0].plus("@eFashion.com").toLowerCase(
            Locale.getDefault())
    }

// input check
    @SuppressLint("ResourceAsColor")
    private fun checkInputProvidedByUser() {
        if (!TextUtils.isEmpty(et_sign_up_email.text)) {
            if (!TextUtils.isEmpty(et_sign_up_full_name.text)) {
                if (!TextUtils.isEmpty(et_sign_up_password.text) && et_sign_up_password.length() >= 5) {
                    if (!TextUtils.isEmpty(et_sign_up_confirm_password.text) && et_sign_up_password.length() >= 5) {
                        btnSignup.isEnabled = true
                        btnSignup.setBackgroundColor(application.resources.getColor(R.color.buttonColor))
                        btnSignup.setTextColor(application.resources.getColor(R.color.white))
                        pb_sign_up.isEnabled = true
                    } else {
                        btnSignup.isEnabled = false
                        btnSignup.setTextColor(R.color.textDisabledColor)
                    }
                } else {
                    btnSignup.isEnabled = false
                    btnSignup.setTextColor(R.color.textDisabledColor)
                }
            } else {
                btnSignup.isEnabled = false
                btnSignup.setTextColor(R.color.textDisabledColor)
            }
        } else {
            btnSignup.isEnabled = false
            btnSignup.setTextColor(R.color.textDisabledColor)
        }
    }

    fun loginActivity(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun skipToMainActivity(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}



