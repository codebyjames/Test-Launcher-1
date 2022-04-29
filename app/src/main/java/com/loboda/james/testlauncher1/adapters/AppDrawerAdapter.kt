package com.loboda.james.testlauncher1.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.loboda.james.testlauncher1.databinding.PackageItemBinding
import com.loboda.james.testlauncher1.models.PackageItem

/**
 * @author James Loboda aka papayev
 * created by James Loboda aka papayev at 4/27/22
 * www.papayev.com
 */
class AppDrawerAdapter(val startPackageLauncher: (PackageItem) -> Unit): ListAdapter<PackageItem, AppDrawerAdapter.PackageAdapterViewHolder>(DiffCallback) {

    inner class PackageAdapterViewHolder(private val binding: PackageItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PackageItem) {
            binding.apply {
                appId.text = "id: ${item.id}"
                appPackageName.text = "${item.packageName}"
                appName.text = "${item.label}"

                if (item.iconRes > 0) {
                    appIcon.setImageDrawable(item.iconDrawable)
                }
            }

        }

        fun onClick(item: PackageItem) {
            binding?.root.setOnClickListener {
                startPackageLauncher(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageAdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PackageAdapterViewHolder(PackageItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: PackageAdapterViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
        holder.onClick(currentItem)
    }

    companion object DiffCallback: DiffUtil.ItemCallback<PackageItem>() {
        override fun areItemsTheSame(oldItem: PackageItem, newItem: PackageItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: PackageItem, newItem: PackageItem): Boolean {
            return oldItem == newItem
        }

    }

}