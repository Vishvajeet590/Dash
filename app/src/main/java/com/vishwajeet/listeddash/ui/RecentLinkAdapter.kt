package com.vishwajeet.listeddash.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.vishwajeet.listeddash.databinding.LinkCardBinding
import com.vishwajeet.listeddash.models.RecentLink
import com.vishwajeet.listeddash.models.TopLink
import java.time.format.DateTimeFormatter

class RecentLinkAdapter : ListAdapter<RecentLink, RecentLinkAdapter.RecentLinkViewHolder>(LinkComparatorDiffUtil()) {

    inner class RecentLinkViewHolder(private val binding: LinkCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(link: RecentLink) {

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val date = formatter.parse(link.created_at)
            val desiredFormat = DateTimeFormatter.ofPattern("MMM dd").format(date)

            binding.linkIc.load(link.original_image)
            binding.linkName.text = link.title
            binding.linkDate.text = desiredFormat
            binding.clicks.text = link.total_clicks.toString()
            binding.link.text = link.web_link
        }
    }

    class LinkComparatorDiffUtil : DiffUtil.ItemCallback<RecentLink>() {
        override fun areItemsTheSame(oldItem: RecentLink, newItem: RecentLink): Boolean {
            return oldItem.web_link == newItem.web_link
        }

        override fun areContentsTheSame(oldItem: RecentLink, newItem: RecentLink): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentLinkViewHolder {
        val binding = LinkCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecentLinkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecentLinkViewHolder, position: Int) {
        val link = getItem(position)
        link?.let {
            holder.bind(it)
        }
    }

}