package com.example.bestoffer.companyHome

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bestoffer.R
import com.example.bestoffer.databinding.ViewCompanyCategoryItemBinding
import com.example.bestoffer.models.Category
import com.example.bestoffer.models.Product


class CompanyCategoriesAdapter(
        private val onAddProductUnderCategoryClicked: (category: Category, position: Int) -> Unit,
        private val onCategoryClicked: (category: Category, position: Int) -> Unit,
        private val onDeleteProductClicked: (product: Product) -> Unit
) : ListAdapter<Category, CompanyCategoriesViewHolder>(
        CategoriesDiffCallBack()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyCategoriesViewHolder {
        val itemBinding = ViewCompanyCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CompanyCategoriesViewHolder(itemBinding, onAddProductUnderCategoryClicked, onCategoryClicked,onDeleteProductClicked)
    }

    override fun onBindViewHolder(holder: CompanyCategoriesViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

}

class CompanyCategoriesViewHolder(
        private val itemBinding: ViewCompanyCategoryItemBinding,
        private val onAddProductUnderCategoryClicked: (category: Category, position: Int) -> Unit,
        private val onCategoryClicked: (category: Category, position: Int) -> Unit,
        private val onDeleteProductClicked: (product: Product) -> Unit
) : RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(category: Category, position: Int) {
        itemBinding.arrowIv.setImageResource(
                if (category.isExpanded) R.drawable.ic_arrow_drop_up
                else R.drawable.ic_arrow_drop_down
        )
        itemBinding.arrowIv.setOnClickListener {
            onCategoryClicked(category, position)
        }

        itemBinding.addProductUnderCategory.setOnClickListener {
            onAddProductUnderCategoryClicked(category, position)

        }
        itemBinding.categoryNameTv.text = category.name

        val productsAdapter = CompanyProductsAdapter {
            onDeleteProductClicked(it)
        }

        itemBinding.productsRv.setVisibility(category.isExpanded)
        if (category.isExpanded) {
            itemBinding.productsRv.adapter = productsAdapter
            productsAdapter.submitList(category.products)
        }

    }
}

class CategoriesDiffCallBack : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }
}


fun View.setVisibility(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE
    else View.GONE
}


