package com.bangkit.mountainapp.ui.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.mountainapp.R
import com.bangkit.mountainapp.data.local.FavoriteData
import com.bangkit.mountainapp.data.local.GunungFeeds

class ListFavoriteAdapter(private val listFavorite: ArrayList<FavoriteData>) : RecyclerView.Adapter<ListFavoriteAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_list_mount, parent, false)
        return ListViewHolder(view)

    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (favoritePhoto, gunungName, gunungLocation) = listFavorite[position]
        holder.imgPhoto.setImageResource(favoritePhoto)
        holder.tvName.text = gunungName
        holder.tvLocation.text = gunungLocation
    }

    override fun getItemCount(): Int = listFavorite.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgPhoto: ImageView = itemView.findViewById(R.id.img_item_photo)
        var tvName: TextView = itemView.findViewById(R.id.tv_item_username)
        var tvLocation: TextView = itemView.findViewById(R.id.fav_location)
    }
}