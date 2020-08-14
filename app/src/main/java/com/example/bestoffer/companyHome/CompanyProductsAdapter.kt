package com.example.bestoffer.companyHome

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.bestoffer.R
import com.example.bestoffer.databinding.ViewCompanyProductItemBinding
import com.example.bestoffer.models.Product

class CompanyProductsAdapter(
        private val onDeleteProductClicked: (product: Product) -> Unit
) : ListAdapter<Product, CompanyProductsViewHolder>(
        DiffCallBack()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyProductsViewHolder {
        val itemBinding = ViewCompanyProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CompanyProductsViewHolder(itemBinding, onDeleteProductClicked)
    }

    override fun onBindViewHolder(holder: CompanyProductsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class CompanyProductsViewHolder(
        private val itemBinding: ViewCompanyProductItemBinding,
        private val onDeleteProductClicked: (Product: Product) -> Unit
) : RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(product: Product) {

        itemBinding.deleteProductIv.setOnClickListener {
            onDeleteProductClicked(product)
        }
        itemBinding.name.text = product.pname
        itemBinding.descriptionTv.text = product.description
        itemBinding.priceTv.text = product.price
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


