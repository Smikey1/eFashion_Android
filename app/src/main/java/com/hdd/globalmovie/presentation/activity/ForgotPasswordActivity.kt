package com.hdd.globalmovie.presentation.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
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
import com.hdd.globalmovie.R

class ForgotPasswordActivity : AppCompatActivity() {
    //Initialize

    private lateinit var et_fp_email: EditText
    private lateinit var btnFP: Button
    private lateinit var pb_fp: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        et_fp_email = findViewById(R.id.et_fp_email)
        btnFP = findViewById(R.id.btnFP)
        pb_fp = findViewById(R.id.pb_fp)

        et_fp_email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInputProvidedByUser()
            }
        })

        btnFP.setOnClickListener {
            checkForEmailAndPassword()
        }
    }


    @SuppressLint("ResourceAsColor")
    private fun checkForEmailAndPassword() {
        if (Patterns.EMAIL_ADDRESS.matcher(et_fp_email.text).matches()
            // TODO 6: &&  et_fp_email.text.toString() == db_register_email

                ) {
                pb_fp.isVisible = true

                btnFP.setTextColor(R.color.textDisabledColor)
                            Toast.makeText(this, "Check your email", Toast.LENGTH_SHORT).show()

        } else {
            et_fp_email.error = "Please enter a valid email address"
            et_fp_email.requestFocus()
            return
        }
    }


    @SuppressLint("ResourceAsColor")
    private fun checkInputProvidedByUser() {
        if (!TextUtils.isEmpty(et_fp_email.text)) {
                btnFP.isEnabled = true
            btnFP.setBackgroundColor(application.resources.getColor(R.color.buttonColor))
            btnFP.setTextColor(application.resources.getColor(R.color.white))
                pb_fp.isEnabled = true

        } else {
            btnFP.isEnabled = false
            btnFP.setTextColor(R.color.textDisabledColor)
        }
    }


}