package com.example.bestoffer.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bestoffer.R
import com.example.bestoffer.databinding.ViewCategoryItemBinding
import com.example.bestoffer.models.Category
import com.example.bestoffer.models.Product


class CategoriesAdapter(
        private val onCategoryClicked: (category: Category, position: Int) -> Unit,
        private val onProductClicked: (product: Product) -> Unit
) : ListAdapter<Category, CategoriesViewHolder>(
        CategoriesDiffCallBack()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val itemBinding = ViewCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoriesViewHolder(itemBinding, onCategoryClicked, onProductClicked)
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

}

class CategoriesViewHolder(
        private val itemBinding: ViewCategoryItemBinding,
        private val onCategoryClicked: (category: Category, position: Int) -> Unit,
        private val onProductClicked: (product: Product) -> Unit
) : RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(category: Category, position: Int) {
        itemBinding.arrowIv.setImageResource(
                if (category.isExpanded) R.drawable.ic_arrow_drop_up
                else R.drawable.ic_arrow_drop_down
        )
        itemBinding.arrowIv.setOnClickListener {
            onCategoryClicked(category, position)
        }
        itemBinding.categoryNameTv.text = category.name

        val productsAdapter = ProductsAdapter {
            onProductClicked(it)
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


