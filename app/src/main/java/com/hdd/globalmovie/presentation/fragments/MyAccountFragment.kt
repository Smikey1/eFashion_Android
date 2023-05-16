package com.hdd.globalmovie.presentation.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.hdd.globalmovie.presentation.activity.LoginActivity
import com.hdd.globalmovie.R
import com.hdd.globalmovie.data.models.User
import com.hdd.globalmovie.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class MyAccountFragment : Fragment(),SensorEventListener{
    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null

    private lateinit var fullname:TextView
    private lateinit var etFullname:EditText
    private lateinit var progressBar: ProgressBar

    private lateinit var role:TextView
    private lateinit var username:EditText
    private lateinit var phone:EditText
    private lateinit var email:TextView
    private lateinit var address:EditText
    private lateinit var password:EditText
    private lateinit var confirmPassword:EditText
    private lateinit var btnUpdate:Button
    private lateinit var btnCancel:Button

    private lateinit var profileImage:ImageView
    private lateinit var iv_add_img:ImageView

    private var userId:String?=null
    private var requiredPassword:String?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sensorManager = requireContext().getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager

        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_my_account, container, false)
        fullname=view.findViewById(R.id.fma_fullname)
        progressBar=view.findViewById(R.id.fma_pb)

        etFullname=view.findViewById(R.id.fma_user_full_name)
        role=view.findViewById(R.id.fma_role)
        username=view.findViewById(R.id.fma_username)
        phone=view.findViewById(R.id.fma_user_phone_number)
        email=view.findViewById(R.id.fma_user_email)
        address=view.findViewById(R.id.fma_user_address)
        password=view.findViewById(R.id.fma_password)
        confirmPassword=view.findViewById(R.id.fma_confirm_password)
        btnUpdate=view.findViewById(R.id.btnUpdateProfile)
        btnCancel=view.findViewById(R.id.fma_btnCancel)

        profileImage=view.findViewById(R.id.fma_profile_image)
        iv_add_img=view.findViewById(R.id.fma_add_profile)

        iv_add_img.setOnClickListener {
            loadPopUpMenu()
        }
        btnCancel.setOnClickListener {
           requireActivity().onBackPressed()
        }

        btnUpdate.setOnClickListener {
            progressBar.visibility=View.VISIBLE
             checkForEmailAndPassword()
            updateUser()
        }
        fetchData()

        if (!checkSensor()) {
         return null
        }
        else {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }

        return view
    }

    private fun fetchData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userRepository= UserRepository()
                val response=userRepository.getUserProfile()
                if(response.success==true){
                    val user=response.data!!
                    userId=user._id
                    withContext(Dispatchers.Main){
                        Glide.with(requireContext()).load(user.profilePicUrl).circleCrop().into(profileImage)
                        fullname.text=user.fullname
                        etFullname.setText(user.fullname)
                        role.text = user.role
                        username.setText(user.username)
                        phone.setText(user.phone)
                        email.text = user.email
                        address.setText(user.address)
                    }
                }
            }catch (ex:Exception){
                print(ex)
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun checkForEmailAndPassword() {
        if (!TextUtils.isEmpty(password.text)&&!TextUtils.isEmpty(confirmPassword.text)) {
            isPasswordChange=true
        if ( password.length() >= 5 && confirmPassword.length() >= 5) {
            if (password.text.toString() == confirmPassword.text.toString()) {
                requiredPassword = password.text.toString()
            } else {
                confirmPassword.error = "Password doesn't match"
                progressBar.visibility=View.INVISIBLE
            }
        } else {
            confirmPassword.error = "Password length should be greater than 4 characters"
            progressBar.visibility=View.INVISIBLE
        }
    } else {
       isPasswordChange=false
        }
    }

    private fun updateUser() {
        val user = User(
            fullname = etFullname.text.toString(),
            address = address.text.toString(),
            password = requiredPassword,
            username = username.text.toString(),
            phone = phone.text.toString(),
            role = "Customer"
        )
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userRepository = UserRepository()
                val response = userRepository.updateUserProfile(userId!!,user)
                if (response.success == true) {
                    withContext(Dispatchers.Main) {
                        fullname.text = etFullname.text.toString()
                        progressBar.visibility=View.GONE
                        Toast.makeText(requireContext(), "Profile Updated", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                if (imageUrl != null) {
                            uploadImage()
                        }
                if (isPasswordChange) {
                        changePassword(userId!!, User(password = requiredPassword!!))
                    }

            } catch (ex: Exception) {
                print(ex)
            }
        }
    }
    private fun closeFragment() {
        requireActivity().finish()
    }
    // Load pop up menu
    private fun loadPopUpMenu() {
        val popupMenu = PopupMenu(requireContext(), iv_add_img)
        popupMenu.menuInflater.inflate(R.menu.gallery_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menuCamera -> openCamera()
                R.id.menuGallery -> openGallery()
            }
            true
        }
        popupMenu.show()
    }

    private var REQUEST_GALLERY_CODE = 0
    private var REQUEST_CAMERA_CODE = 1
    private var imageUrl: String? = null
    private var isPasswordChange: Boolean = false

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_GALLERY_CODE)
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_CAMERA_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_GALLERY_CODE && data != null) {
                val selectedImage:Uri? = data.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val contentResolver = requireActivity().contentResolver
                val cursor = contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
                cursor!!.moveToFirst()
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                imageUrl = cursor.getString(columnIndex)
                profileImage.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
                cursor.close()
            } else if (requestCode == REQUEST_CAMERA_CODE && data != null) {
                val imageBitmap = data.extras?.get("data") as Bitmap
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val file = bitmapToFile(imageBitmap, "$timeStamp.jpg")
                imageUrl = file!!.absolutePath
                profileImage.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
            }
        }
    }

    private fun changePassword(id:String,user: User){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userRepository = UserRepository()
                val response = userRepository.changePassword(id,user)
                if (response.success == true) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Session has expired, Login Again!", Toast.LENGTH_SHORT)
                            .show()
                        Toast.makeText(requireContext(), "Password Changed", Toast.LENGTH_SHORT)
                            .show()
                        startActivity(Intent(requireContext(), LoginActivity::class.java))
                        closeFragment()
                    }
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        ex.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun getMimeType(url: String?): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }
    private fun uploadImage() {
        if (imageUrl != null) {
            val file = File(imageUrl!!)
            val mimeType = getMimeType(file.path);
            val reqFile = RequestBody.create(mimeType!!.toMediaTypeOrNull(), file)
//            val reqFile = RequestBody.create(MediaType.parse(mimeType!!), file)
            val body = MultipartBody.Part.createFormData("file", file.name, reqFile)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val userRepository = UserRepository()
                    val response = userRepository.uploadImage(userId!!, body)
                    if (response.success == true) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireContext(), "Uploaded", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                } catch (ex: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            ex.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        }
    }

    private fun bitmapToFile(bitmap: Bitmap, fileNameToSave: String): File? {
        var file: File? = null
        return try {
            file = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    .toString() + File.separator + fileNameToSave)
            file.createNewFile()
            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
            val bitMapData = bos.toByteArray()
            //write the bytes in file
            val fos = FileOutputStream(file)
            fos.write(bitMapData)
            fos.flush()
            fos.close()
            file

        } catch (e: Exception) {
            e.printStackTrace()
            file // it will return null
        }
    }

    //for sensor
    private fun checkSensor(): Boolean {
        var flag = true
        if (sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) == null) {
            flag = false
        }
        return flag
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val values = event!!.values[1]
        if (values < 5)
        else if (values > 10){
            openCamera()
        }

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}