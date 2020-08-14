package com.example.bestoffer.companyHome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bestoffer.AddProductActivity
import com.example.bestoffer.databinding.ActivityCompanyHomeBinding
import com.example.bestoffer.models.Category
import com.example.bestoffer.models.Product
import com.example.bestoffer.models.UserType
import com.example.bestoffer.utils.clearLoginSession
import com.example.bestoffer.utils.getLoggedInUser
import com.google.firebase.database.*


class CompanyHomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCompanyHomeBinding

    private var productsRef = FirebaseDatabase
            .getInstance()
            .reference

    private var categoriesList: MutableList<Category> = mutableListOf()

    private val adapter: CompanyCategoriesAdapter by lazy {
        CompanyCategoriesAdapter(::onAddProductUnderCategoryClicked, ::onCategoryClicked, ::onDeleteProductClicked)
    }

    var userType: UserType = UserType.ADMIN
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompanyHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        userType = when (intent.getStringExtra("AdminType")) {
            UserType.ADMIN.name -> UserType.ADMIN
            else -> UserType.COMPANY
        }

        binding.companyNameTv.text = if (userType == UserType.COMPANY) {
            val company = getLoggedInUser(this@CompanyHomeActivity)
            company.name
        } else {
            "Admin"
        }
        binding.addProductsFab.setOnClickListener {
            val intent = Intent(this, AddProductActivity::class.java)
            startActivity(intent)
        }
        binding.logOut.setOnClickListener {
            clearLoginSession(this)
            finish()
        }
        binding.productsRv.adapter = adapter
        getCategories()
    }


    private fun getCategories() {
        val query = when (userType) {
            UserType.ADMIN -> {
                productsRef.child("Products")
            }
            else -> {
                productsRef.child("Products").orderByChild("categoryId")
                        .equalTo(getLoggedInUser(this@CompanyHomeActivity).phone)
            }
        }

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val products: MutableList<Product> = mutableListOf()
                for (postSnapshot in dataSnapshot.children) {
                    val product = postSnapshot.getValue(Product::class.java)
                    product?.let { products.add(it) }
                }
                categoriesList = products.groupBy {
                    it.category
                }.mapKeys {
                    Category(
                            name = it.key ?: "",
                            isExpanded = false,
                            products = it.value
                    )
                }.keys.toMutableList()
                adapter.submitList(categoriesList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    private fun onDeleteProductClicked(product: Product) {
        val deleteQuery: Query = productsRef.child("Products").child(product.pid ?: "")

        deleteQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (appleSnapshot in dataSnapshot.children) {
                    appleSnapshot.ref.removeValue()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    private fun onAddProductUnderCategoryClicked(category: Category, position: Int) {
        val intent = Intent(this@CompanyHomeActivity, AddProductActivity::class.java)
        intent.putExtra("categoryName", category.name)
        startActivity(intent)
    }

    private fun onCategoryClicked(category: Category, position: Int) {
        categoriesList[position] = categoriesList[position].copy(isExpanded = !category.isExpanded)
        adapter.submitList(categoriesList)
        adapter.notifyDataSetChanged()
    }


}