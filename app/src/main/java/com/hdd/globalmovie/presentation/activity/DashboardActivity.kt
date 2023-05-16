package com.hdd.globalmovie.presentation.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.hdd.globalmovie.R

class DashboardActivity : AppCompatActivity() {
    //Initialize
    private lateinit var btnNavigateToSignup: Button
    private lateinit var btnNavigateToLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        //Binding
        btnNavigateToSignup = findViewById(R.id.btnNavigateToSignup)
        btnNavigateToLogin = findViewById(R.id.btnNavigateToLogin)

        btnNavigateToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        btnNavigateToSignup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
    //on backpress triger
    override fun onBackPressed() {
        showAlertDialog()
    }

    private fun showAlertDialog() {
        val builder= AlertDialog.Builder(this)
        builder.setTitle("Close App")
        builder.setIcon(R.drawable.exit)
        builder.setMessage("Are you sure to close app ?")
//        builder.setIcon(R.id.nav_sign_out)
        //performing Positive action
        builder.setPositiveButton("Yes"){ _, _ ->
            super.onBackPressed()
        }
        //performing cancel action
        builder.setNeutralButton("Cancel"){ _, _ ->
            Toast.makeText(this,"clicked cancel", Toast.LENGTH_SHORT).show()
        }
        //performing negative action
        builder.setNegativeButton("No"){ _, _ ->
            Toast.makeText(this,"clicked No", Toast.LENGTH_SHORT).show()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()

    }

}