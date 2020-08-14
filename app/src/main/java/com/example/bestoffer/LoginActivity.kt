package com.example.bestoffer

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bestoffer.companyHome.CompanyHomeActivity
import com.example.bestoffer.databinding.ActivityLoginBinding
import com.example.bestoffer.home.HomeActivity
import com.example.bestoffer.models.UiUser
import com.example.bestoffer.models.User
import com.example.bestoffer.models.UserType
import com.example.bestoffer.utils.saveLoginSession
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.logInBtn.setOnClickListener {
            if (validateUserData()) {
                login(binding.PhoneET.text.toString(), binding.passwordET.text.toString())
            }
        }
    }


    private fun validateUserData(): Boolean {
        val phone = binding.PhoneET.text
        if (phone == null || phone.trim().isEmpty()) {
            binding.PhoneIL.isHelperTextEnabled = true
            binding.PhoneIL.helperText = getString(R.string.phone_required)
            return false
        } else {
            binding.PhoneIL.isHelperTextEnabled = false
        }

        val password = binding.passwordET.text
        if (password == null || password.trim().isEmpty()) {
            binding.passwordTI.isHelperTextEnabled = true
            binding.passwordTI.helperText = getString(R.string.password_required)
            return false
        } else {
            binding.passwordTI.isHelperTextEnabled = false
        }

        return true
    }


    private fun login(phone: String, password: String) {
        binding.loadingViewBackground.visibility = View.VISIBLE
        binding.loadingView.visibility = View.VISIBLE
        binding.logInBtn.visibility = View.GONE

        FirebaseDatabase.getInstance().reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.child("Users").child(phone).getValue(User::class.java)?.toUiUser()
                if (user != null) {
                    if (user.password == password) {
                        saveLoginSession(this@LoginActivity.applicationContext, user)
                        when (user.userType) {
                            UserType.USER -> {
                                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                                this@LoginActivity.startActivity(intent)
                                finish()
                            }
                            UserType.ADMIN -> {
                                val intent = Intent(this@LoginActivity, CompanyHomeActivity::class.java)
                                intent.putExtra("AdminType", UserType.ADMIN.name)
                                this@LoginActivity.startActivity(intent)
                                finish()
                            }
                            UserType.COMPANY -> {
                                val intent = Intent(this@LoginActivity, CompanyHomeActivity::class.java)
                                intent.putExtra("AdminType", UserType.COMPANY.name)
                                this@LoginActivity.startActivity(intent)
                                finish()
                            }
                        }
                    } else {
                        binding.loadingViewBackground.visibility = View.GONE
                        binding.loadingView.visibility = View.GONE
                        binding.logInBtn.visibility = View.VISIBLE
                        Toast.makeText(this@LoginActivity, "Password is incorrect.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    binding.loadingViewBackground.visibility = View.GONE
                    binding.loadingView.visibility = View.GONE
                    binding.logInBtn.visibility = View.VISIBLE
                    Toast.makeText(this@LoginActivity, "something went wrong!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }


}

private fun User.toUiUser(): UiUser? {
    if (name == null || email == null || phone == null || userType == null || password == null)
        return null
    return UiUser(
            name = name,
            image = image,
            address = address,
            password = password,
            phone = phone,
            email = email,
            userType = when (userType) {
                UserType.USER.name -> UserType.USER
                UserType.ADMIN.name -> UserType.ADMIN
                else -> UserType.COMPANY
            }
    )
}
