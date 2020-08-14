package com.example.bestoffer

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bestoffer.databinding.ActivityPaymentBinding
import com.example.bestoffer.emailsender.ConfigurationFile
import com.example.bestoffer.emailsender.MailSender
import com.example.bestoffer.home.setVisibility
import com.example.bestoffer.utils.getLoggedInUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.log


class PaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBinding
    private var isPaymentMethodSelected = true
    private var selectedPaymentMethod = ""

    var productID = ""
    var productName = ""
    var productImage = ""
    var productPrice = ""
    var productDescription = ""
    var productSeller = ""
    var sellerCompanyEmail = ""
    var productQuantity = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        productID = intent.getStringExtra("pid") ?: ""
        productName = intent.getStringExtra("productName") ?: ""
        productImage = intent.getStringExtra("productImage") ?: ""
        productPrice = intent.getStringExtra("productPrice") ?: ""
        productSeller = intent.getStringExtra("productSeller") ?: ""
        sellerCompanyEmail = intent.getStringExtra("sellerCompanyEmail") ?: ""
        productDescription = intent.getStringExtra("productDescription") ?: ""
        productQuantity = intent.getStringExtra("productQuantity") ?: ""


        binding.done.setOnClickListener {
            sendEmail()
        }
        binding.loadingViewBackground.setOnClickListener { }

        binding.mastercard.isChecked = true
        setupPaymentSelection()
    }

    private fun sendEmail() {
        binding.loadingViewBackground.setVisibility(true)
        binding.loadingView.setVisibility(true)

        GlobalScope.launch {
            try {
                val sender = MailSender(ConfigurationFile.EMAIL, ConfigurationFile.PASSWORD)
                val loggedInUser = getLoggedInUser(this@PaymentActivity)

                sender.sendMail(ConfigurationFile.EMAIL_SUBJECT, getUserMessage(), loggedInUser.email)
                sender.sendMail(ConfigurationFile.EMAIL_SUBJECT, getAdminMessage(), ConfigurationFile.EMAIL)
                sender.sendMail(ConfigurationFile.EMAIL_SUBJECT, getCompanyMessage(), sellerCompanyEmail)

                deleteOrderFromCart()
            } catch (e: Exception) {
            }
        }
    }

    private fun deleteOrderFromCart() {
        FirebaseDatabase.getInstance().reference.child("Cart List")
                .child("User View")
                .child(getLoggedInUser(this).phone)
                .child("Products")
                .child(productID)
                .removeValue()
                .addOnSuccessListener {
                    Toast.makeText(this@PaymentActivity, "we send you send you an email with order details.", Toast.LENGTH_LONG).show()
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this@PaymentActivity, "firebase ex- " + it.message.toString(), Toast.LENGTH_LONG).show()
                }
    }

    private fun getUserMessage(): String {
        return "You select $selectedPaymentMethod to pay for your order.\n" +
                "\n" +
                "\n" +
                " - Order Details\n" +
                "\n" + "\t\t\t Product Name : $productName\n" +
                "\n" + "\t\t\t Product Price : $productPrice EGP\n" +
                "\n" + "\t\t\t product Description : $productDescription" +
                "\n" + "\t\t\t Quantity : $productQuantity" +
                "\n" + "\t\t\t Product Seller : $productSeller" +
                "\n\n\n\n" + "Your order will be delivered after your confirmation at your address : ${getLoggedInUser(this).address}  \n"
    }


    private fun getAdminMessage(): String {
        val user = getLoggedInUser(this)
        return " - Order Details\n" +
                "\n" + "\t\t\t Product Name : $productName\n" +
                "\n" + "\t\t\t Product Price : $productPrice EGP\n" +
                "\n" + "\t\t\t product Description : $productDescription" +
                "\n" + "\t\t\t Quantity : $productQuantity" +
                "\n" + "\t\t\t Product Seller : $productSeller" +
                "\n\n\n\n" + " - Customer Details  \n" +
                "\n" + "\t\t\t Customer Name : ${user.name}\n" +
                "\n" + "\t\t\t Customer Phone : ${user.phone} \n" +
                "\n" + "\t\t\t Customer Address : ${user.address}" +
                "\n\n\n" + " Customer select $selectedPaymentMethod to pay for the order.\n"
    }

    private fun getCompanyMessage(): String {
        val user = getLoggedInUser(this)
        return " - Order Details\n" +
                "\n" + "\t\t\t Product Name : $productName\n" +
                "\n" + "\t\t\t Product Price : $productPrice EGP\n" +
                "\n" + "\t\t\t product Description : $productDescription" +
                "\n" + "\t\t\t Quantity : $productQuantity" +
                "\n\n\n\n" + " - Customer Details  \n" +
                "\n" + "\t\t\t Customer Name : ${user.name}\n" +
                "\n" + "\t\t\t Customer Phone : ${user.phone} \n" +
                "\n" + "\t\t\t Customer Address : ${user.address}" +
                "\n\n\n" + " Customer select $selectedPaymentMethod to pay for the order.\n"
    }

    private fun setupPaymentSelection() {
        binding.mastercard.setOnClickListener {
            val checked = !binding.mastercard.isChecked
            isPaymentMethodSelected = checked
            selectedPaymentMethod = if (checked) {
                "mastercard"
            } else {
                ""
            }

            binding.mastercard.isChecked = checked
            binding.visa.isChecked = false
            binding.hsbc.isChecked = false
            binding.payPal.isChecked = false

        }

        binding.visa.setOnClickListener {
            val checked = !binding.visa.isChecked
            isPaymentMethodSelected = checked
            selectedPaymentMethod = if (checked) {
                "visa"
            } else {
                ""
            }
            binding.visa.isChecked = checked
            binding.hsbc.isChecked = false
            binding.payPal.isChecked = false
            binding.mastercard.isChecked = false
        }

        binding.hsbc.setOnClickListener {
            val checked = !binding.hsbc.isChecked
            isPaymentMethodSelected = checked
            selectedPaymentMethod = if (checked) {
                "hsbc"
            } else {
                ""
            }

            binding.hsbc.isChecked = checked
            binding.visa.isChecked = false
            binding.payPal.isChecked = false
            binding.mastercard.isChecked = false
        }

        binding.payPal.setOnClickListener {
            val checked = !binding.payPal.isChecked
            isPaymentMethodSelected = checked
            selectedPaymentMethod = if (checked) {
                "payPal"
            } else {
                ""
            }

            binding.payPal.isChecked = checked
            binding.visa.isChecked = false
            binding.hsbc.isChecked = false
            binding.mastercard.isChecked = false
        }
    }
}