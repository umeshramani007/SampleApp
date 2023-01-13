package com.example.eydstest.presentation.ui.adapter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
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
    private val layoutInflater = LayoutInflater.from(context)
    private var gifList: ArrayList<GIFObject> = ArrayList()

    fun setData(gifList: List<GIFObject>) {
        this.gifList = gifList as ArrayList<GIFObject>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SearchGIFHolder(
            DataBindingUtil.inflate<ViewDataBinding>(
                layoutInflater,
                R.layout.item_search_gif,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        gifList[position].apply {
            isFavorite = true
            (holder as SearchGIFHolder).mBinding.setVariable(BR.gifObject, this)
        }
    }

    override fun getItemCount(): Int {
        return gifList.size
    }

    inner class SearchGIFHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        val mBinding = binding
        private val imgFavorite: ImageView = itemView.findViewById(R.id.imgFavorite)

        init {
            imgFavorite.setOnClickListener {
                val gifObject = it.tag as GIFObject
                onFavoriteClick.invoke(gifObject)
            }
        }
    }
}


