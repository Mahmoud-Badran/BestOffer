package com.example.bestoffer

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.bestoffer.databinding.ActivitySignUpBinding
import com.example.bestoffer.models.UiUser
import com.example.bestoffer.models.UserType
import com.google.firebase.database.*
import java.util.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private var selectedUserType: UserType = UserType.USER

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.userTypeToggle.check(R.id.typeUserBtn)
        binding.userTypeToggle.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            when (checkedId) {
                R.id.typeUserBtn -> {
                    activateUser()
                }
                R.id.typeCompanyBtn -> {
                    activateCompany()
                }
            }
        }

        binding.signUpBtn.setOnClickListener {
            val user = validate()
            if (user != null) {
                saveUser(user)
            }
        }
        binding.loadingViewBackground.setOnClickListener { }

    }

    private fun saveUser(user: UiUser) {
        binding.loadingViewBackground.visibility = View.VISIBLE
        binding.loadingView.visibility = View.VISIBLE
        binding.signUpBtn.visibility = View.GONE
        val ref: DatabaseReference = FirebaseDatabase.getInstance().reference

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.child("Users").child(user.phone).exists()) {
                    val userdataMap = HashMap<String, Any>()
                    userdataMap["phone"] = user.phone
                    userdataMap["password"] = user.password
                    userdataMap["name"] = user.name
                    userdataMap["address"] = user.address ?: ""
                    userdataMap["email"] = user.email ?: ""
                    userdataMap["userType"] = user.userType.name
                    ref.child("Users").child(user.phone).updateChildren(userdataMap)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this@SignUpActivity, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    binding.loadingViewBackground.visibility = View.GONE
                                    binding.loadingView.visibility = View.GONE
                                    binding.signUpBtn.visibility = View.VISIBLE

                                    Toast.makeText(this@SignUpActivity, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show()
                                }
                            }
                } else {
                    Toast.makeText(this@SignUpActivity, "This $user.phone already exists.", Toast.LENGTH_SHORT).show()
                    Toast.makeText(this@SignUpActivity, "Please try again using another phone number.", Toast.LENGTH_SHORT).show()

                    binding.loadingViewBackground.visibility = View.GONE
                    binding.loadingView.visibility = View.GONE
                    binding.signUpBtn.visibility = View.VISIBLE

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }


    private fun activateUser() {
        selectedUserType = UserType.USER
        binding.typeUserBtn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(binding.typeUserBtn.context, R.color.orange))
        binding.typeCompanyBtn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(binding.typeUserBtn.context, R.color.gray))
    }

    private fun activateCompany() {
        selectedUserType = UserType.COMPANY
        binding.typeUserBtn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(binding.typeUserBtn.context, R.color.gray))
        binding.typeCompanyBtn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(binding.typeUserBtn.context, R.color.orange))
    }

    private fun validate(): UiUser? {
        val name = binding.nameET.text
        if (name == null || name.trim().isEmpty()) {
            binding.NameIL.isHelperTextEnabled = true
            binding.NameIL.helperText = getString(R.string.name_required)
            return null
        } else {
            binding.NameIL.isHelperTextEnabled = false
        }

        val email = binding.emailET.text
        if (email == null || email.trim().isEmpty()) {
            binding.emailIL.isHelperTextEnabled = true
            binding.emailIL.helperText = getString(R.string.name_required)
            return null
        } else {
            binding.emailIL.isHelperTextEnabled = false
        }

        val phone = binding.PhoneET.text
        if (phone == null || phone.trim().isEmpty()) {
            binding.PhoneIL.isHelperTextEnabled = true
            binding.PhoneIL.helperText = getString(R.string.phone_required)
            return null
        } else {
            binding.PhoneIL.isHelperTextEnabled = false
        }

        val address = binding.AddressET.text
        if (address == null || address.trim().isEmpty()) {
            binding.AddressIL.isHelperTextEnabled = true
            binding.AddressIL.helperText = getString(R.string.address_required)
            return null
        } else {
            binding.AddressIL.isHelperTextEnabled = false
        }

        val password = binding.passwordET.text
        if (password == null || password.trim().isEmpty()) {
            binding.passwordTI.isHelperTextEnabled = true
            binding.passwordTI.helperText = getString(R.string.password_required)
            return null
        } else {
            binding.passwordTI.isHelperTextEnabled = false
        }

        val confirmPassword = binding.confirmPasswordET.text
        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            binding.confirmPasswordTI.isHelperTextEnabled = true
            binding.confirmPasswordTI.helperText =
                    getString(R.string.password_confirmation_required)
            return null
        } else {
            binding.confirmPasswordTI.isHelperTextEnabled = false
        }


        if (confirmPassword.toString() != password.toString()) {
            binding.confirmPasswordTI.isHelperTextEnabled = true
            binding.confirmPasswordTI.helperText = getString(R.string.passwords_not_matched)
            return null
        } else {
            binding.confirmPasswordTI.isHelperTextEnabled = false
        }


        return UiUser(
                name = name.toString(),
                phone = phone.toString(),
                password = password.toString(),
                address = address.toString(),
                image = "",
                email = email.toString(),
                userType = selectedUserType
        )
    }
}