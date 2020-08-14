package com.example.bestoffer

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.api.load
import com.example.bestoffer.databinding.ActivityProductDetilsBinding
import com.example.bestoffer.home.HomeActivity
import com.example.bestoffer.utils.getLoggedInUser
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetilsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetilsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val productID = intent.getStringExtra("pid")
        val productName = intent.getStringExtra("productName")
        val productImage = intent.getStringExtra("productImage")
        val productPrice = intent.getStringExtra("productPrice")
        val productSeller = intent.getStringExtra("productSeller")
        val sellerCompanyEmail = intent.getStringExtra("sellerCompanyEmail")
        val productDescription = intent.getStringExtra("productDescription")

        if (productID == null || productName == null || productPrice == null || productSeller == null || sellerCompanyEmail == null) {
            finish()
        } else {
            binding.titleTv.text = productName
            binding.productNameTv.text = productName
            binding.priceTv.text = productPrice
            binding.descriptionTv.text = productDescription
            binding.theSellerTv.text = productSeller
            binding.productImageIv.load(productImage) {
                error(R.drawable.logo)
                placeholder(R.drawable.logo)
            }
            binding.addToCard.setOnClickListener {
                addInCard(productID, productName, productImage, productPrice, productSeller, productDescription, sellerCompanyEmail)
            }
            binding.favProductIv.setOnClickListener {
                addToFav(productID, productName, productImage, productPrice, productSeller, productDescription, sellerCompanyEmail)
            }
        }


    }

    private fun addToFav(
            productID: String,
            productName: String,
            productImage: String?,
            productPrice: String,
            productSeller: String,
            productDescription: String?,
            sellerCompanyEmail: String
    ) {

        showLoading()
        val productMap = HashMap<String, Any?>()
        val productsRef = FirebaseDatabase.getInstance().reference.child("Favorites")
                .child(getLoggedInUser(this).phone).child(productID)

        productMap["pid"] = productID
        productMap["description"] =productDescription
        productMap["image"] =productImage
        productMap["price"] = productPrice
        productMap["pname"] = productName
        productMap["sellerCompany"] = productSeller
        productMap["sellerCompanyEmail"] =sellerCompanyEmail
        productsRef.updateChildren(productMap)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Product is added to favorites successfully..", Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        hideLoading()
                        val message = task.exception.toString()
                        Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    private fun hideLoading() {
        binding.loadingViewBackground.visibility = View.GONE
        binding.loadingView.visibility = View.GONE
    }

    private fun showLoading() {
        binding.loadingViewBackground.visibility = View.VISIBLE
        binding.loadingView.visibility = View.VISIBLE
    }
    private fun addInCard(
            productID: String,
            productName: String,
            productImage: String?,
            productPrice: String,
            productSeller: String,
            productDescription: String?,
            sellerCompanyEmail: String
    ) {
        val calendar = Calendar.getInstance()
        val formatDate = SimpleDateFormat("MM dd, yyyy")
        val date = formatDate.format(calendar.time)
        val formatTime = SimpleDateFormat("HH:mm:ss ")
        val time = formatTime.format(calendar.time)
        val databaseRef = FirebaseDatabase.getInstance().reference.child("Cart List")

        val hashMap = HashMap<String, Any?>()
        hashMap["pid"] = productID
        hashMap["pname"] = productName
        hashMap["price"] = productPrice
        hashMap["date"] = date
        hashMap["image"] = productImage ?: ""
        hashMap["time"] = time
        hashMap["description"] = productDescription ?: ""
        hashMap["quantity"] = binding.numberBtn.number
        hashMap["discount"] = ""
        hashMap["sellerCompany"] = productSeller
        hashMap["sellerCompanyEmail"] = sellerCompanyEmail

        databaseRef.child("User View")
                .child(getLoggedInUser(this@ProductDetailsActivity).phone)
                .child("Products").child(productID)
                .updateChildren(hashMap)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this@ProductDetailsActivity, "Added in Cart List ", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@ProductDetailsActivity, HomeActivity::class.java)
                        startActivity(intent)
                    }
                }
    }
}