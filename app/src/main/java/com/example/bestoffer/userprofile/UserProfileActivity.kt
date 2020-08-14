package com.example.bestoffer.userprofile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.api.load
import coil.transform.CircleCropTransformation
import com.example.bestoffer.R
import com.example.bestoffer.databinding.ActivityUserProfileBinding
import com.example.bestoffer.home.setVisibility
import com.example.bestoffer.utils.getLoggedInUser
import com.example.bestoffer.utils.saveLoginSession
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding
    var isEditEnabled = false
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        changeEdibility(false)
        setUserData()

        binding.backIv.setOnClickListener {
            finish()
        }
        binding.editProfileBtn.setOnClickListener {
            changeEdibility(true)
        }
        binding.save.setOnClickListener {
            validate()
        }
        binding.userProfileImv.setOnClickListener {
            if (isEditEnabled) {
                openGallery()
            }
        }
    }


    private fun openGallery() {
        val galleryIntent = Intent()
        galleryIntent.action = Intent.ACTION_GET_CONTENT
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, 1)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data

            binding.userProfileImv.setBackgroundResource(R.drawable.ic_user_profile_bg)
            binding.userProfileImv.load(imageUri) {
                transformations(CircleCropTransformation())

            }
        }
    }

    private fun setUserData() {
        val user = getLoggedInUser(this)

        if (user.image != null) {
            imageUri = Uri.parse(user.image)
        }
        binding.titleTv.text = user.name
        binding.emailET.setText(user.email)
        binding.nameET.setText(user.name)
        binding.phoneET.setText(user.phone)
        binding.addressET.setText(user.address)
    }

    private fun changeEdibility(isEnabled: Boolean) {
        isEditEnabled = isEnabled
        val user = getLoggedInUser(this)

        binding.emailET.isEnabled = isEnabled
        binding.nameET.isEnabled = isEnabled
        binding.addressET.isEnabled = isEnabled

        binding.nameTI.boxStrokeWidth = if (isEnabled) 1 else 0
        binding.emailTI.boxStrokeWidth = if (isEnabled) 1 else 0
        binding.addressTI.boxStrokeWidth = if (isEnabled) 1 else 0

        binding.save.setVisibility(isEnabled)

        if (isEnabled) {
            binding.userProfileImv.load(R.drawable.ic_camera_rounded) {
                transformations(CircleCropTransformation())
            }
        } else {
            binding.userProfileImv.load(user.image) {
                transformations(CircleCropTransformation())
                error(R.drawable.ic_user)
                fallback(R.drawable.ic_user)
                placeholder(R.drawable.ic_user)
            }
        }
    }

    private fun saveUserData(
            name: String,
            phone: String,
            address: String,
            email: String,
            image: String = ""
    ) {
        val ref: DatabaseReference = FirebaseDatabase.getInstance().reference

        val userdataMap = HashMap<String, Any>()
        userdataMap["phone"] = phone
        userdataMap["name"] = name
        userdataMap["address"] = address
        userdataMap["email"] = email
        userdataMap["image"] = image
        ref.child("Users").child(phone).updateChildren(userdataMap)
                .addOnSuccessListener {
                    Toast.makeText(this@UserProfileActivity, "Your data updated successfully.", Toast.LENGTH_SHORT).show()
                    updateLocalLUser(name, phone, address, email, image)
                }.addOnFailureListener {
                    hideLoading()
                    Toast.makeText(this@UserProfileActivity, "Failed to update your data", Toast.LENGTH_SHORT).show()
                }

    }

    private fun updateLocalLUser(name: String, phone: String, address: String, email: String, image: String) {
        val user = getLoggedInUser(this)

        saveLoginSession(this,
                user.copy(
                        name = name,
                        email = email,
                        image = image,
                        phone = phone,
                        address = address
                ))

        hideLoading()
        changeEdibility(false)
    }

    private fun validate() {
        val name = binding.nameET.text
        if (name == null || name.trim().isEmpty()) {
            binding.nameTI.isHelperTextEnabled = true
            binding.nameTI.helperText = getString(R.string.name_required)
            return
        } else {
            binding.nameTI.isHelperTextEnabled = false
        }

        val email = binding.emailET.text
        if (email == null || email.trim().isEmpty()) {
            binding.emailTI.isHelperTextEnabled = true
            binding.emailTI.helperText = getString(R.string.email_required)
            return
        } else {
            binding.emailTI.isHelperTextEnabled = false
        }

        val phone = binding.phoneET.text
        if (phone == null || phone.trim().isEmpty()) {
            binding.phoneTI.isHelperTextEnabled = true
            binding.phoneTI.helperText = getString(R.string.phone_required)
            return
        } else {
            binding.phoneTI.isHelperTextEnabled = false
        }

        val address = binding.addressET.text
        if (address == null || address.trim().isEmpty()) {
            binding.addressTI.isHelperTextEnabled = true
            binding.addressTI.helperText = getString(R.string.address_required)
            return
        } else {
            binding.addressTI.isHelperTextEnabled = false
        }


        uploadImage(name.toString(), phone.toString(), address.toString(), email.toString())
    }

    private fun uploadImage(
            name: String,
            phone: String,
            address: String,
            email: String
    ) {
        showLoading()
        val imagesRef = FirebaseStorage.getInstance().reference.child("Product Images")
        val filePath = imagesRef.child(imageUri?.lastPathSegment + UUID.randomUUID().toString() + ".jpg")

        if (imageUri != null) {
            imageUri?.let {
                try {
                    filePath.putFile(it).continueWithTask { task ->
                        if (!task.isSuccessful) {
                            throw task.exception!!
                        }
                        filePath.downloadUrl
                    }.addOnFailureListener {
                        hideLoading()
                        Toast.makeText(this@UserProfileActivity, "Failed to update your data", Toast.LENGTH_SHORT).show()
                    }.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val downUri = task.result
                            saveUserData(name, phone, address, email, downUri.toString())
                        }
                    }
                } catch (e: Exception) {
                    hideLoading()
                    Toast.makeText(this@UserProfileActivity, "Failed to update your data", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            saveUserData(name, phone, address, email)
        }
    }

    private fun showLoading() {
        binding.loadingViewBackground.visibility = View.VISIBLE
        binding.loadingView.visibility = View.VISIBLE
        binding.save.visibility = View.GONE
    }

    private fun hideLoading() {
        binding.loadingViewBackground.visibility = View.GONE
        binding.loadingView.visibility = View.GONE
        binding.save.visibility = View.VISIBLE
    }

}