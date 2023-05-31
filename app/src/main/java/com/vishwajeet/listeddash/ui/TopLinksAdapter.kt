package com.vishwajeet.listeddash.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.vishwajeet.listeddash.databinding.FragmentTopLinksBinding
import com.vishwajeet.listeddash.databinding.LinkCardBinding
import com.vishwajeet.listeddash.databinding.StatsCardBinding
import com.vishwajeet.listeddash.models.Stats
import com.vishwajeet.listeddash.models.TopLink
import java.time.format.DateTimeFormatter

class TopLinksAdapter : ListAdapter<TopLink, TopLinksAdapter.TopLinkViewHolder>(LinkComparatorDiffUtil()) {

    inner class TopLinkViewHolder(private val binding: LinkCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(link: TopLink) {

            Log.d("dashRespFROM","HEREEEEE")
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

    class LinkComparatorDiffUtil : DiffUtil.ItemCallback<TopLink>() {
        override fun areItemsTheSame(oldItem: TopLink, newItem: TopLink): Boolean {
            return oldItem.web_link == newItem.web_link
        }

        override fun areContentsTheSame(oldItem: TopLink, newItem: TopLink): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopLinkViewHolder {
        val binding = LinkCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TopLinkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TopLinkViewHolder, position: Int) {
        val link = getItem(position)
        link?.let {
            holder.bind(it)
        }
    }

}