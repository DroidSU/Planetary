/*
 * Created by Sujoy Datta. Copyright (c) 2020. All rights reserved.
 *
 * To the person who is reading this..
 * When you finally understand how this works, please do explain it to me too at sujoydatta26@gmail.com
 * P.S.: In case you are planning to use this without mentioning me, you will be met with mean judgemental looks and sarcastic comments.
 */

package com.morningstar.planetary.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.morningstar.planetary.R

class LifeStyleChoicesAdapter(
    private var context: Context,
    private var lifeStyleChoice: ArrayList<String>,
    private var drawableArrayList: ArrayList<Drawable>,
    private var selectedDrawableArrayList: ArrayList<Drawable>
) : RecyclerView.Adapter<LifeStyleChoicesAdapter.LifeStyleChoicesViewHolder>() {

    private lateinit var view : View
    private var selectedPosition : Int = -1

    class LifeStyleChoicesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView = itemView.findViewById<ImageView>(R.id.lifestyle_imageView)
        var textView = itemView.findViewById<TextView>(R.id.lifestyle_textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LifeStyleChoicesViewHolder {
        view = LayoutInflater.from(context).inflate(R.layout.rv_item_box_image, parent, false)
        return LifeStyleChoicesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return lifeStyleChoice.size
    }

    override fun onBindViewHolder(holder: LifeStyleChoicesViewHolder, position: Int) {
        holder.textView.text = lifeStyleChoice[position]

        if (position == selectedPosition)
            holder.imageView.setImageDrawable(selectedDrawableArrayList[position])
        else
            holder.imageView.setImageDrawable(drawableArrayList[position])

        holder.itemView.setOnClickListener {
            selectedPosition = position
            notifyDataSetChanged()
        }
    }
}