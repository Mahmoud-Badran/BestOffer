/*
 * Decompiled with CFR 0.0.
 *
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Bundle
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.widget.Button
 *  androidx.appcompat.app.AppCompatActivity
 *  java.lang.Class
 *  java.lang.Object
 */
package com.example.bestoffer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.bestoffer.companyHome.CompanyHomeActivity
import com.example.bestoffer.home.HomeActivity
import com.example.bestoffer.models.UserType
import com.example.bestoffer.utils.getLoggedInUser
import com.example.bestoffer.utils.getLoginSession

class MainActivity : AppCompatActivity() {
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        this.setContentView(R.layout.activity_main)
        if (getLoginSession(this)) {
            val user = getLoggedInUser(this)
            when (user?.userType) {
                UserType.USER -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                UserType.ADMIN -> {
                    val intent = Intent(this, CompanyHomeActivity::class.java)
                    intent.putExtra("AdminType", UserType.ADMIN.name)
                    this.startActivity(intent)
                    finish()
                }
                UserType.COMPANY -> {
                    val intent = Intent(this, CompanyHomeActivity::class.java)
                    intent.putExtra("AdminType", UserType.COMPANY.name)
                    this.startActivity(intent)
                    finish()
                }
            }
        }
        val joinNowButton = findViewById<Button>(R.id.main_Regist_btn)
        val button = findViewById<Button>(R.id.main_login_btn)
        button.setOnClickListener { view: View? ->
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            this@MainActivity.startActivity(intent)
            finish()
        }
        joinNowButton.setOnClickListener {
            val intent = Intent(this@MainActivity as Context, SignUpActivity::class.java)
            this@MainActivity.startActivity(intent)
            finish()
        }
    }
}