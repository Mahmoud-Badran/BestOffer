package com.example.bestoffer.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.bestoffer.R
import com.example.bestoffer.databinding.ViewProductItemBinding
import com.example.bestoffer.models.Product

class ProductsAdapter(
        private val onProductClicked: (product: Product) -> Unit
) : ListAdapter<Product, ProductsViewHolder>(
        DiffCallBack()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val itemBinding = ViewProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductsViewHolder(itemBinding, onProductClicked)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class ProductsViewHolder(
        private val itemBinding: ViewProductItemBinding,
        private val onProductClicked: (Product: Product) -> Unit
) : RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(product: Product) {

        itemBinding.root.setOnClickListener {
            onProductClicked(product)
        }
        itemBinding.name.text = product.pname
        itemBinding.descriptionTv.text = product.description
        itemBinding.priceTv.text = product.price
        itemBinding.sellerTv.text = product.sellerCompany
        itemBinding.productImageIv.load(product.image) {
            error(R.drawable.logo)
            placeholder(R.drawable.logo)
        }
    }
}

class DiffCallBack : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return true
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}


