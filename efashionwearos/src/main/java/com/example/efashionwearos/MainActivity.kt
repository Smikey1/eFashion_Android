package com.example.efashionwearos

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.example.efashionwearos.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : Activity() {
    private lateinit var profile: ImageView
    private lateinit var tvGreeting: TextView
    private lateinit var viewCartBtn: Button
    private lateinit var viewOrdersBtn: Button
    private lateinit var logoutBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvGreeting=findViewById(R.id.tvGreeting)
        profile=findViewById(R.id.imageView)
        viewCartBtn=findViewById(R.id.viewCartBtn)
        viewOrdersBtn=findViewById(R.id.viewOrdersBtn)
        logoutBtn=findViewById(R.id.logoutBtn)
        fetchData()

        viewCartBtn.setOnClickListener {
            startActivity(Intent(this,CartActivity::class.java))
        }

        viewOrdersBtn.setOnClickListener {
            startActivity(Intent(this,OrderActivity::class.java))
        }

        logoutBtn.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
    }
    private fun fetchData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userRepository= UserRepository()
                val response=userRepository.getUserProfile()
                if(response.success==true){
                    val user=response.data!!
                    withContext(Dispatchers.Main){
                        tvGreeting.text ="Welcome ${user.fullname},"
                        Glide.with(this@MainActivity).asBitmap().
                        load(user.profilePicUrl)
                            .into(object : BitmapImageViewTarget(profile) {
                                override fun setResource(resource: Bitmap?) {
                                    profile.setImageBitmap(resource)
                                    super.setResource(resource)
                                }
                            })
                    }
                }
            }catch (ex:Exception){
                print(ex)
            }
        }
    }


}