package com.example.bestoffer.cart

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bestoffer.AddProductActivity
import com.example.bestoffer.PaymentActivity
import com.example.bestoffer.databinding.ActivityCartBinding
import com.example.bestoffer.models.Cart
import com.example.bestoffer.utils.getLoggedInUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding

    private val adapter: CartAdapter by lazy {
        CartAdapter(::onItemClicked)
    }

    private fun onItemClicked(cart: Cart) {
        val intent = Intent(this@CartActivity, PaymentActivity::class.java)
        intent.putExtra("pid", cart.pid)
        intent.putExtra("productName", cart.pname)
        intent.putExtra("productImage", cart.image)
        intent.putExtra("productPrice", cart.price)
        intent.putExtra("productSeller", cart.sellerCompany)
        intent.putExtra("sellerCompanyEmail", cart.sellerCompanyEmail)
        intent.putExtra("productDescription", cart.description)
        intent.putExtra("productQuantity", cart.quantity)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.cartRv.adapter = adapter
        binding.backIv.setOnClickListener {
            finish()
        }
        getCartList()
    }

    private fun getCartList() {
        FirebaseDatabase.getInstance()
                .reference.child("Cart List")
                .child("User View")
                .child(getLoggedInUser(this@CartActivity).phone)
                .child("Products")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val cartList: MutableList<Cart> = mutableListOf()
                        for (postSnapshot in dataSnapshot.children) {
                            val item = postSnapshot.getValue(Cart::class.java)
                            item?.let { cartList.add(it) }
                        }
                        adapter.submitList(cartList)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                    }
                })
    }

}