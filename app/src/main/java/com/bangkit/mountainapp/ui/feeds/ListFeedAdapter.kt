package com.bangkit.mountainapp.ui.feeds

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.mountainapp.R
import com.bangkit.mountainapp.data.local.GunungFeeds

class ListFeedAdapter(private val listGunung: ArrayList<GunungFeeds>) : RecyclerView.Adapter<ListFeedAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.mountain_item, parent, false)
        return ListViewHolder(view)

    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (userNameFeeds, gunungPhotoFeeds, feedsUploadedAt, feedsDescription) = listGunung[position]
        holder.imgPhoto.setImageResource(gunungPhotoFeeds)
        holder.tvName.text = userNameFeeds
        holder.tvUploadedAt.text = feedsUploadedAt
        holder.tvDescriptionFeeds.text = feedsDescription
    }

    override fun getItemCount(): Int = listGunung.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgPhoto: ImageView = itemView.findViewById(R.id.img_top_mountain1)
        var tvName: TextView = itemView.findViewById(R.id.tv_mountain_name_2)
        var tvUploadedAt: TextView = itemView.findViewById(R.id.tv_mountain_loc1)
        var tvDescriptionFeeds: TextView = itemView.findViewById(R.id.tv_description)
    }
}