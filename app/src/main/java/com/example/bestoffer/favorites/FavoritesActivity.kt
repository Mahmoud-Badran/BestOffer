package com.example.bestoffer.favorites

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bestoffer.ProductDetailsActivity
import com.example.bestoffer.databinding.ActivityFavoritesBinding
import com.example.bestoffer.models.Product
import com.example.bestoffer.utils.getLoggedInUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding

    private val adapter: FavoritesAdapter by lazy {
        FavoritesAdapter(::onItemClicked, ::onDeleteFavClicked)
    }

    private fun onDeleteFavClicked(product: Product) {
        FirebaseDatabase.getInstance()
                .reference
                .child("Favorites")
                .child(getLoggedInUser(this).phone)
                .child(product.pid ?: "")
                .removeValue()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.favoritesRv.adapter = adapter
        binding.backIv.setOnClickListener {
            finish()
        }
        getFavorites()
    }

    private fun getFavorites() {
        FirebaseDatabase.getInstance()
                .reference
                .child("Favorites")
                .child(getLoggedInUser(this).phone)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val favList: MutableList<Product> = mutableListOf()
                        for (postSnapshot in dataSnapshot.children) {
                            val item = postSnapshot.getValue(Product::class.java)
                            item?.let { favList.add(it) }
                        }
                        adapter.submitList(favList)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                    }
                })
    }


    private fun onItemClicked(product: Product) {
        val intent = Intent(this, ProductDetailsActivity::class.java)
        intent.putExtra("pid", product.pid)
        intent.putExtra("productName", product.pname)
        intent.putExtra("productImage", product.image)
        intent.putExtra("productPrice", product.price)
        intent.putExtra("productSeller", product.sellerCompany)
        intent.putExtra("sellerCompanyEmail", product.sellerCompanyEmail)
        intent.putExtra("productDescription", product.description)
        startActivity(intent)
    }
}