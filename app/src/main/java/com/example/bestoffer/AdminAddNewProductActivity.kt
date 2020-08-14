package com.example.bestoffer

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.*

class AdminAddNewProductActivity : AppCompatActivity() {
    private var CategoryName: String? = null
    private var Description: String? = null
    private var Price: String? = null
    private var Pname: String? = null
    private var saveCurrentDate: String? = null
    private var saveCurrentTime: String? = null
    private var AddNewProductButton: Button? = null
    private var InputProductImage: ImageView? = null
    private var InputProductName: EditText? = null
    private var InputProductDescription: EditText? = null
    private var InputProductPrice: EditText? = null

    //private static final int GalleryPick = 1;
    private var ImageUri: Uri? = null
    private var productRandomKey: String? = null
    private var downloadImageUrl: String? = null
    private var ProductImagesRef: StorageReference? = null
    private var ProductsRef: DatabaseReference? = null
    private var loadingBar: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_add_new_product)
        CategoryName = intent.extras!!["category"].toString()
        ProductImagesRef = FirebaseStorage.getInstance().reference.child("Product Images")
        ProductsRef = FirebaseDatabase.getInstance().reference.child("Products")
        AddNewProductButton = findViewById<View>(R.id.add_new_product) as Button
        InputProductImage = findViewById<View>(R.id.select_product_image) as ImageView
        InputProductName = findViewById<View>(R.id.product_name) as EditText
        InputProductDescription = findViewById<View>(R.id.product_description) as EditText
        InputProductPrice = findViewById<View>(R.id.product_price) as EditText
        loadingBar = ProgressDialog(this)
        InputProductImage!!.setOnClickListener { OpenGallery() }
        AddNewProductButton!!.setOnClickListener { ValidateProductData() }
    }

    private fun OpenGallery() {
        val galleryIntent = Intent()
        galleryIntent.action = Intent.ACTION_GET_CONTENT
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            ImageUri = data.data
            InputProductImage!!.setImageURI(ImageUri)
        }
    }

    private fun ValidateProductData() {
        Description = InputProductDescription!!.text.toString()
        Price = InputProductPrice!!.text.toString()
        Pname = InputProductName!!.text.toString()
        if (ImageUri == null) {
            Toast.makeText(this, "Product image is mandatory...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(Description)) {
            Toast.makeText(this, "Please write product description...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(Price)) {
            Toast.makeText(this, "Please write product Price...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(Pname)) {
            Toast.makeText(this, "Please write product name...", Toast.LENGTH_SHORT).show()
        } else {
            StoreProductInformation()
        }
    }

    private fun StoreProductInformation() {
        loadingBar!!.setTitle("Add New Product")
        loadingBar!!.setMessage("Dear Admin, please wait while we are adding the new product.")
        loadingBar!!.setCanceledOnTouchOutside(false)
        loadingBar!!.show()
        val calendar = Calendar.getInstance()
        val currentDate = SimpleDateFormat("MMM dd, yyyy")
        saveCurrentDate = currentDate.format(calendar.time)
        val currentTime = SimpleDateFormat("HH:mm:ss a")
        saveCurrentTime = currentTime.format(calendar.time)
        productRandomKey = saveCurrentDate + saveCurrentTime
        val filePath = ProductImagesRef!!.child(ImageUri!!.lastPathSegment + productRandomKey + ".jpg")
        val uploadTask = filePath.putFile(ImageUri!!)
        uploadTask.addOnFailureListener { e ->
            val message = e.toString()
            Toast.makeText(this@AdminAddNewProductActivity, "Error: $message", Toast.LENGTH_SHORT).show()
            loadingBar!!.dismiss()
        }.addOnSuccessListener {
            Toast.makeText(this@AdminAddNewProductActivity, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show()
            val urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception!!
                }
                downloadImageUrl = filePath.downloadUrl.toString()
                filePath.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    downloadImageUrl = task.result.toString()
                    Toast.makeText(this@AdminAddNewProductActivity, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show()
                    SaveProductInfoToDatabase()
                }
            }
        }
    }

    private fun SaveProductInfoToDatabase() {
        val productMap = HashMap<String, Any?>()
        productMap["pid"] = productRandomKey
        productMap["date"] = saveCurrentDate
        productMap["time"] = saveCurrentTime
        productMap["description"] = Description
        productMap["image"] = downloadImageUrl
        productMap["category"] = CategoryName
        productMap["price"] = Price
        productMap["pname"] = Pname
        ProductsRef!!.child(productRandomKey!!).updateChildren(productMap)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this@AdminAddNewProductActivity, AdminCategoryActivity::class.java)
                        startActivity(intent)
                        loadingBar!!.dismiss()
                        Toast.makeText(this@AdminAddNewProductActivity, "Product is added successfully..", Toast.LENGTH_SHORT).show()
                    } else {
                        loadingBar!!.dismiss()
                        val message = task.exception.toString()
                        Toast.makeText(this@AdminAddNewProductActivity, "Error: $message", Toast.LENGTH_SHORT).show()
                    }
                }
    }
}