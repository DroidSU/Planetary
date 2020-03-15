/*
 * Created by Sujoy Datta. Copyright (c) 2020. All rights reserved.
 *
 * To the person who is reading this..
 * When you finally understand how this works, please do explain it to me too at sujoydatta26@gmail.com
 * P.S.: In case you are planning to use this without mentioning me, you will be met with mean judgemental looks and sarcastic comments.
 */

package com.morningstar.planetary.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.morningstar.planetary.R

class BedsRecyclerAdapter(
    private var context : Context,
    private var countArrayList : ArrayList<String>
) : RecyclerView.Adapter<BedsRecyclerAdapter.BedsRecyclerViewHolder>() {

    private lateinit var view : View
    private var selectedItemPos : Int = -1

    class BedsRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textView = itemView.findViewById<TextView>(R.id.textView_count)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BedsRecyclerViewHolder {
        view = LayoutInflater.from(context).inflate(R.layout.rv_item_with_box, parent, false)
        return BedsRecyclerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return countArrayList.size
    }

    override fun onBindViewHolder(holder: BedsRecyclerViewHolder, position: Int) {
        holder.textView.text = countArrayList[position]

        if (position == selectedItemPos)
            holder.textView.background = context.resources.getDrawable(R.drawable.black_border_white_bg)
        else{
            holder.textView.background = context.resources.getDrawable(R.drawable.black_border)
        }

        holder.textView.setOnClickListener {
            selectedItemPos = position
            notifyDataSetChanged()
        }
    }
}