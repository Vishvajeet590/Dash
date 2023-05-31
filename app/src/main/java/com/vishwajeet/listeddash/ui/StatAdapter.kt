package com.vishwajeet.listeddash.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.vishwajeet.listeddash.R
import com.vishwajeet.listeddash.databinding.FragmentDashboardBinding
import com.vishwajeet.listeddash.databinding.StatsCardBinding
import com.vishwajeet.listeddash.models.Stats

class StatAdapter() :ListAdapter<Stats,StatAdapter.StatViewHolder>(ComparatorDiffUtil()) {
    inner class StatViewHolder(private val binding: StatsCardBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(stat : Stats){
            if (stat.value.isEmpty()){
                binding.statValue.text = "-"
            }else binding.statValue.text = stat.value

            when(stat.name){
                "total_clicks" ->{
                    binding.statName.text = "Total clicks"
                    binding.statIcon.load(R.drawable.sum_total)
                }"today_clicks" ->{
                    binding.statName.text = "Today clicks"
                    binding.statIcon.load(R.drawable.avatar)
                }

                "top_source" ->{
                    binding.statName.text = "Top source"
                    binding.statIcon.load(R.drawable.source)
                }

                "top_location" ->{
                    binding.statName.text = "Top location"
                    binding.statIcon.load(R.drawable.location)
                }

            }
        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<Stats>() {
        override fun areItemsTheSame(oldItem: Stats, newItem: Stats): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Stats, newItem: Stats): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatViewHolder {
        val binding = StatsCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StatViewHolder, position: Int) {
        val stat = getItem(position)
        stat?.let {
            holder.bind(it)
        }
    }


}