package com.example.bestoffer

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.api.load
import com.example.bestoffer.databinding.ActivityAddProductBinding
import com.example.bestoffer.models.Product
import com.example.bestoffer.utils.getLoggedInUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*


class AddProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddProductBinding
    private var imageUri: Uri? = null
    private val productsRef = FirebaseDatabase.getInstance().reference.child("Products")
    private var categoryName: String? = ""




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        categoryName = intent.getStringExtra("categoryName")
        binding.categoryNameILET.setText(categoryName)

        binding.addProductImage.setOnClickListener {
            openGallery()
        }
        binding.backIv.setOnClickListener {
            finish()
        }
        binding.saveBtn.setOnClickListener {
            val product = validate()
            if (product != null) {
                uploadImage(product)
            }
        }
    }

    private fun uploadImage(product: Product) {
        showLoading()
        val imagesRef = FirebaseStorage.getInstance().reference.child("Product Images")
        val filePath = imagesRef.child(imageUri?.lastPathSegment + UUID.randomUUID().toString() + ".jpg")

        if (imageUri != null) {
            imageUri?.let {
                filePath.putFile(it).continueWithTask { task ->
                    if (!task.isSuccessful) {
                        throw task.exception!!
                    }
                    filePath.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downUri = task.result
                        saveProduct(product.copy(image = downUri.toString()))
                    }
                }
            }
        } else {
            saveProduct(product)
        }
    }

    private fun saveProduct(product: Product) {
        val calendar = Calendar.getInstance()
        val currentDate = SimpleDateFormat("MMM dd, yyyy")
        val saveCurrentDate = currentDate.format(calendar.time)
        val currentTime = SimpleDateFormat("HH:mm:ss a")
        val saveCurrentTime = currentTime.format(calendar.time)

        val productMap = HashMap<String, Any?>()
        val key = productsRef.push().key

        key?.let {
            productMap["pid"] = key
            productMap["date"] = saveCurrentDate
            productMap["time"] = saveCurrentTime
            productMap["description"] = product.description
            productMap["image"] = product.image
            productMap["category"] = product.category
            productMap["price"] = product.price
            productMap["pname"] = product.pname
            productMap["categoryId"] = product.categoryId
            productMap["sellerCompany"] = product.sellerCompany
            productMap["sellerCompanyEmail"] = product.sellerCompanyEmail
            productsRef.child(key).updateChildren(productMap)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this@AddProductActivity, "Product is added successfully..", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            hideLoading()
                            val message = task.exception.toString()
                            Toast.makeText(this@AddProductActivity, "Error: $message", Toast.LENGTH_SHORT).show()
                        }
                    }
        }

    }

    private fun hideLoading() {
        binding.loadingViewBackground.visibility = View.GONE
        binding.loadingView.visibility = View.GONE
        binding.saveBtn.visibility = View.VISIBLE
    }

    private fun showLoading() {
        binding.loadingViewBackground.visibility = View.VISIBLE
        binding.loadingView.visibility = View.VISIBLE
        binding.saveBtn.visibility = View.GONE
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
            binding.productImageIv.load(imageUri)
        }
    }


    private fun validate(): Product? {
        val name = binding.categoryNameILET.text
        if (name == null || name.trim().isEmpty()) {
            binding.categoryNameIL.isHelperTextEnabled = true
            binding.categoryNameIL.helperText = getString(R.string.categoryName_required)
            return null
        } else {
            binding.categoryNameIL.isHelperTextEnabled = false
        }

        val description = binding.descriptionET.text
        if (description == null || description.trim().isEmpty()) {
            binding.descriptionIL.isHelperTextEnabled = true
            binding.descriptionIL.helperText = getString(R.string.description_required)
            return null
        } else {
            binding.descriptionIL.isHelperTextEnabled = false
        }

        val productName = binding.productNameEt.text
        if (productName == null || productName.trim().isEmpty()) {
            binding.productNameIL.isHelperTextEnabled = true
            binding.productNameIL.helperText = getString(R.string.productName_required)
            return null
        } else {
            binding.productNameIL.isHelperTextEnabled = false
        }

        val price = binding.priceET.text
        if (price == null || price.trim().isEmpty()) {
            binding.priceTI.isHelperTextEnabled = true
            binding.priceTI.helperText = getString(R.string.price_required)
            return null
        } else {
            binding.priceTI.isHelperTextEnabled = false
        }

        val loggedInUser = getLoggedInUser(this@AddProductActivity)
        return Product(
                category = name.toString(),
                description = description.toString(),
                pname = productName.toString(),
                price = price.toString(),
                categoryId = loggedInUser.phone,
                sellerCompany = loggedInUser.name,
                sellerCompanyEmail = loggedInUser.email
        )
    }


}

