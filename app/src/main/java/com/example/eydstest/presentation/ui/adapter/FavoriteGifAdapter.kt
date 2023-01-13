package com.example.eydstest.presentation.ui.adapter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eydstest.R
import com.example.eydstest.data.model.GIFObject
import com.example.eydstest.data.model.Images
import com.example.eydstest.data.model.PreviewWebP
import java.util.*

class FavoriteGifAdapter(
    private val context: Context,
    val onFavoriteClick: (gifObject: GIFObject) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var gifList: ArrayList<GIFObject> = ArrayList()

    fun setData(gifList: List<GIFObject>) {
        this.gifList = gifList as ArrayList<GIFObject>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SearchGIFHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_search_gif, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        gifList[position].apply {
            Glide.with(context)
                .load(images.preview_webp.url)
                .placeholder(R.drawable.placeholder)
                .into((holder as SearchGIFHolder).imageView)

            holder.imgFavorite.tag = this
        }

    }

    override fun getItemCount(): Int {
        return gifList.size
    }

    inner class SearchGIFHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.imageView)
        val imgFavorite = itemView.findViewById<ImageView>(R.id.imgFavorite)

        init {
            imgFavorite.setImageResource(R.drawable.ic_favorite)
            imgFavorite.setOnClickListener {
                val gifObject = it.tag as GIFObject
                onFavoriteClick.invoke(gifObject)
            }
        }
    }
}


