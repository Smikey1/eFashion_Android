package com.hdd.globalmovie.presentation.activity

import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.hdd.globalmovie.R
import com.hdd.globalmovie.data.remoteDataSource.ServiceBuilder
import com.hdd.globalmovie.databinding.ActivityMainBinding
import com.hdd.globalmovie.presentation.fragments.*
import java.util.*
import kotlin.math.sqrt

class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener,SensorEventListener{
    private var sensor: Sensor? = null
    private lateinit var currentSensorManager: SensorManager
    private val channelID = "newChannel"
    private var notificationManager: NotificationManager? = null

    private var sensorManager: SensorManager? = null
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f


    //for nav header main profile
    var token: String? = ""

    private val permissions = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainAppBarLogo: ImageView
    private val HOME_FRAGMENT:Int =0
    private val CART_FRAGMENT:Int =1
    private val ORDER_FRAGMENT:Int =2
    private val WISHLIST_FRAGMENT:Int =3
    private val MAP_FRAGMENT:Int =4
    private val MY_ACCOUNT_FRAGMENT:Int =5
    private var currentFragment:Int? = null
    private lateinit var navView: NavigationView
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(channelID, "eFashion Store", "Notification Channel")

        currentSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        checkRunTimePermission()

        super.onCreate(savedInstanceState)
        if (ServiceBuilder.token!=null){
            displayNotification()
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(binding.appBarMain.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        mainAppBarLogo=findViewById(R.id.mainAppBarLogo)
        drawerLayout = binding.drawerLayout
        val toggle = ActionBarDrawerToggle(this,drawerLayout,binding.appBarMain.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView = binding.navView
        navView.setNavigationItemSelectedListener(this)
        navView.menu.getItem(0).isChecked = true

        setFragment(HomeFragment(),HOME_FRAGMENT)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        Objects.requireNonNull(sensorManager)!!.registerListener(sensorListener, sensorManager!!
            .getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
        acceleration = 10f
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH


        if (!checkSensor())
            return
        else {
            sensor = currentSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
            currentSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }


    private fun checkSensor(): Boolean {
        var flag = true
        if (currentSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) == null) {
            flag = false
        }
        return flag
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val values = event!!.values[0]
        if(values<=0){
            showAlertDialog()
        }

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    // checking for runtime permission
    private fun checkRunTimePermission() {
        if (!hasPermission()) {
            requestPermission()
        }
    }
    private fun hasPermission(): Boolean {
        var hasPermission = true
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                hasPermission = false
                break
            }
        }
        return hasPermission
    }

    // requesting permission
    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, permissions, 1)
    }

    private val sensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val currentX = event.values[0]
            val currentY = event.values[1]
            val currentZ = event.values[2]
            lastAcceleration = currentAcceleration
            currentAcceleration = sqrt((currentX * currentX + currentY * currentY + currentZ * currentZ).toDouble()).toFloat()
            val changeValue: Float = currentAcceleration - lastAcceleration
            acceleration = acceleration * 0.9f + changeValue
            if (acceleration > 12) {
                openMyFragment("Our Location", MapsFragment(), MAP_FRAGMENT)
                //Toast.makeText(this@MainActivity, "Shaking of Mobile Detected", Toast.LENGTH_SHORT).show()
            }
        }
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    override fun onResume() {
        sensorManager?.registerListener(sensorListener, sensorManager!!.getDefaultSensor(
            Sensor .TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL
        )
        super.onResume()
    }

    override fun onPause() {
        sensorManager!!.unregisterListener(sensorListener)
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (currentFragment==HOME_FRAGMENT){
            menuInflater.inflate(R.menu.main, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.main_search -> {
                return true
            }
            R.id.main_bell -> {
                return true
            }
            R.id.main_cart -> {

                if (ServiceBuilder.token != null){
                    openMyFragment("My Cart",CartFragment(),CART_FRAGMENT)
                } else{
                    showSignInDialog()
                }

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSignInDialog() {
        val signInDialog:Dialog = Dialog(this)
        signInDialog.setContentView(R.layout.authentication_dialog)
        signInDialog.setCancelable(true)
        signInDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        val ad_sign_in_btn:Button = signInDialog.findViewById(R.id.ad_sign_in_btn)
        val ad_sign_up_btn:Button = signInDialog.findViewById(R.id.ad_sign_up_btn)
        ad_sign_in_btn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            signInDialog.dismiss()
            startActivity(intent)
        }
        ad_sign_up_btn.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            signInDialog.dismiss()
            startActivity(intent)
        }
        signInDialog.show()
    }

    private fun displayNotification() {
        val notificationId = 24
        val notification = NotificationCompat.Builder(this, channelID)
            .setContentTitle("eFashion Store")
            .setContentText("Login from new ${ Build.DEVICE} device detected")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        notificationManager?.notify(notificationId, notification)

    }

    private fun createNotificationChannel(id: String, name: String, channelDescription: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(id, name, importance).apply {
                description = channelDescription
            }
            notificationManager?.createNotificationChannel(channel)

        }

    }

    private fun openMyFragment(fragmentName:String,fragment: Fragment,fragmentId: Int) {
        mainAppBarLogo.visibility = View.GONE
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.title = fragmentName
        // this is used to remove main menu items and re-run onCreateOptionsMenu
        invalidateOptionsMenu()
        setFragment(fragment, fragmentId)
        if (fragmentId == CART_FRAGMENT) {
            navView.menu.getItem(3).isChecked = true
        }
    }

    override fun onNavigationItemSelected(selectedItem: MenuItem): Boolean {
        when (selectedItem.itemId) {
            R.id.nav_store -> {
                supportActionBar?.setDisplayShowTitleEnabled(false)
                mainAppBarLogo.visibility=View.VISIBLE
                invalidateOptionsMenu()
                setFragment(HomeFragment(),HOME_FRAGMENT)
            }
            R.id.nav_orders -> {
                openMyFragment("My Orders",OrderFragment(),ORDER_FRAGMENT)
            }
            R.id.nav_reviews -> {}
            R.id.nav_cart -> {
                openMyFragment("My Cart",CartFragment(),CART_FRAGMENT)
            }
            R.id.nav_wishlist -> {
                openMyFragment("My Wishlist", WishlistFragment(),WISHLIST_FRAGMENT)
            }
            R.id.nav_location -> {
                openMyFragment("Our Location", MapsFragment(), MAP_FRAGMENT)
            }
            R.id.nav_account -> {
                openMyFragment("My Account", MyAccountFragment(), MY_ACCOUNT_FRAGMENT)
            }
            R.id.nav_sign_out -> {
                startActivity(Intent(this,LoginActivity::class.java))
                val sharedPreferences = getSharedPreferences("userAuth", MODE_PRIVATE)
                sharedPreferences.edit().clear().commit()
                finish()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        else {
            if (currentFragment==HOME_FRAGMENT){
                showAlertDialog()
            } else {
                mainAppBarLogo.visibility=View.VISIBLE
                invalidateOptionsMenu()
                supportActionBar?.setDisplayShowTitleEnabled(false)
                setFragment(HomeFragment(),HOME_FRAGMENT)
                navView.menu.getItem(0).isChecked = true
            }
        }
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Logging out?")
        builder.setMessage("Are you sure want to Logout?")
        builder.setIcon(R.mipmap.sign_out)
        //performing Positive action
        builder.setPositiveButton("Yes") { _, _ ->
            currentFragment=-1
            startActivity(Intent(this,LoginActivity::class.java))
            val sharedPreferences = getSharedPreferences("userAuth", MODE_PRIVATE)
            sharedPreferences.edit().clear().commit()
            super.onBackPressed()
            finish()
        }
        //performing cancel action
        builder.setNeutralButton("Cancel") { _, _ ->
        }
        //performing negative action
        builder.setNegativeButton("No") { _, _ ->
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()

    }

    private fun setFragment(fragment: Fragment,fragmentId:Int) {
        currentFragment=fragmentId
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_frameLayout, fragment)
        ft.commit()
    }

}

