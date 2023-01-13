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
import com.bumptech.glide.module.AppGlideModule
import com.example.eydstest.R
import com.example.eydstest.data.model.GIFObject
import com.example.eydstest.data.model.Images
import com.example.eydstest.data.model.PreviewWebP
import com.example.eydstest.presentation.util.MyAppGlideModule
import java.util.*

class SearchGifAdapter(
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
        return when (viewType) {
            TYPE_GIF_ITEM -> SearchGIFHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_search_gif, parent, false)
            )
            else -> BottomLoading(
                LayoutInflater.from(parent.context).inflate(R.layout.bottom_loading, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        gifList[position].apply {
            if (id == "-1")
                return@apply

            Glide.with(context)
                .load(images.preview_webp.url)
                .into((holder as SearchGIFHolder).imageView)

            holder.imgFavorite.setImageResource(if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_not_favorite)
            holder.imgFavorite.tag = this
        }

    }

    override fun getItemCount(): Int {
        return gifList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (gifList[position].id) {
            "-1" -> TYPE_LOADER
            else -> TYPE_GIF_ITEM
        }
    }

    inner class SearchGIFHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.imageView)
        val imgFavorite = itemView.findViewById<ImageView>(R.id.imgFavorite)

        init {
            imgFavorite.setOnClickListener {
                val gifObject = it.tag as GIFObject
                onFavoriteClick.invoke(gifObject)
            }
        }
    }

    class BottomLoading(itemView: View) : RecyclerView.ViewHolder(itemView)

    /***
     *  Add a loading indicator at bottom of the list to fetch the next page
     */
    fun addLoadingView() {
        //add loading item
        Handler(Looper.getMainLooper()).post {
            gifList.add(GIFObject("-1", Images(PreviewWebP())))
            notifyItemInserted(gifList.size - 1)
        }
    }

    companion object {
        const val TYPE_LOADER = 1
        const val TYPE_GIF_ITEM = 2
    }
}


