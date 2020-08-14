package com.example.bestoffer.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bestoffer.databinding.CartItemLayoutBinding
import com.example.bestoffer.models.Cart

class CartAdapter(
        private val onItemClicked: (cart: Cart) -> Unit
) : ListAdapter<Cart, CartViewHolder>(
        DiffCallBack()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val itemBinding = CartItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(itemBinding, onItemClicked)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class CartViewHolder(
        private val itemBinding: CartItemLayoutBinding,
        private val onItemClicked: (cart: Cart) -> Unit
) : RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(cart: Cart) {
        itemBinding.productNameTv.text = cart.pname
        itemBinding.priceTv.text = cart.price
        itemBinding.productQuantityTv.text = cart.quantity
        itemBinding.theSellerTv.text = cart.sellerCompany

        itemBinding.root.setOnClickListener {
            onItemClicked(cart)
        }
    }

}

class DiffCallBack : DiffUtil.ItemCallback<Cart>() {
    override fun areItemsTheSame(oldItem: Cart, newItem: Cart): Boolean {
        return true
    }

    override fun areContentsTheSame(oldItem: Cart, newItem: Cart): Boolean {
        return oldItem == newItem
    }
}


