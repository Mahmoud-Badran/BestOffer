package com.example.bestoffer.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import coil.api.load
import coil.transform.CircleCropTransformation
import com.example.bestoffer.MainActivity
import com.example.bestoffer.ProductDetailsActivity
import com.example.bestoffer.R
import com.example.bestoffer.cart.CartActivity
import com.example.bestoffer.databinding.ActivityHomeBinding
import com.example.bestoffer.favorites.FavoritesActivity
import com.example.bestoffer.models.Category
import com.example.bestoffer.models.Product
import com.example.bestoffer.userprofile.UserProfileActivity
import com.example.bestoffer.utils.clearLoginSession
import com.example.bestoffer.utils.getLoggedInUser
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityHomeBinding

    private var productsRef = FirebaseDatabase.getInstance().reference.child("Products")
    var categoriesList: MutableList<Category> = mutableListOf()
    private val adapter: CategoriesAdapter by lazy {
        CategoriesAdapter(::onCategoryClicked, ::onProductClicked)
    }

    private fun onProductClicked(product: Product) {
        val intent = Intent(this@HomeActivity, ProductDetailsActivity::class.java)
        intent.putExtra("pid", product.pid)
        intent.putExtra("productName", product.pname)
        intent.putExtra("productImage", product.image)
        intent.putExtra("productPrice", product.price)
        intent.putExtra("productSeller", product.sellerCompany)
        intent.putExtra("sellerCompanyEmail", product.sellerCompanyEmail)
        intent.putExtra("productDescription", product.description)
        startActivity(intent)
    }

    private fun onCategoryClicked(category: Category, position: Int) {
        categoriesList[position] = categoriesList[position].copy(isExpanded = !category.isExpanded)
        adapter.submitList(categoriesList)
        adapter.notifyDataSetChanged()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.appBarHome.fab.setOnClickListener {
            val intent = Intent(this@HomeActivity, CartActivity::class.java)
            startActivity(intent)
        }
        val toggle = ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.appBarHome.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navView.setNavigationItemSelectedListener(this)




        binding.appBarHome.contentHome.recyclerMenu.adapter = adapter
        getCategories()
    }

    private fun updateUserDataInHeader() {
        val headerView = binding.navView.getHeaderView(0)
        val userNameTextView = headerView.findViewById<TextView>(R.id.user_profile_name)
        val profileImageView = headerView.findViewById<ImageView>(R.id.user_profile_image)
        profileImageView.setOnClickListener {
            val intent = Intent(this@HomeActivity, UserProfileActivity::class.java)
            startActivity(intent)
        }

        val loggedInUser = getLoggedInUser(this@HomeActivity)
        userNameTextView.text = loggedInUser.name
        profileImageView.load(loggedInUser.image) {
            placeholder(R.drawable.ic_user)
            fallback(R.drawable.ic_user)
            error(R.drawable.ic_user)
            transformations(CircleCropTransformation())
        }
    }

    override fun onResume() {
        super.onResume()
        updateUserDataInHeader()
    }

    private fun getCategories() {
        val products: MutableList<Product> = mutableListOf()
        productsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
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


    override fun onBackPressed() {
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

//        if (id == R.id.action_settings)
//        {
//            return true;
//        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_cart -> {
                val intent = Intent(this@HomeActivity, CartActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_favorite -> {
                val intent = Intent(this@HomeActivity, FavoritesActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_Logout -> {
                val intent = Intent(this@HomeActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                clearLoginSession(this@HomeActivity)
                startActivity(intent)
                finish()
            }
        }
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}