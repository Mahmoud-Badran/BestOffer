package com.example.bestoffer.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bestoffer.databinding.FavoritesItemLayoutBinding
import com.example.bestoffer.models.Product

class FavoritesAdapter(
        private val onItemClicked: (product: Product) -> Unit,
        private val onDeleteClicked: (product: Product) -> Unit
) : ListAdapter<Product, FavoritesViewHolder>(
        DiffCallBack()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val itemBinding = FavoritesItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoritesViewHolder(itemBinding, onItemClicked,onDeleteClicked)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class FavoritesViewHolder(
        private val itemBinding: FavoritesItemLayoutBinding,
        private val onItemClicked: (Product: Product) -> Unit,
        private val onDeleteClicked: (product: Product) -> Unit
) : RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(product: Product) {
        itemBinding.productNameTv.text = product.pname
        itemBinding.priceTv.text = product.price
        itemBinding.theSellerTv.text = product.sellerCompany

        itemBinding.root.setOnClickListener {
            onItemClicked(product)
        }
        itemBinding.deleteIv.setOnClickListener {
            onDeleteClicked(product)
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


