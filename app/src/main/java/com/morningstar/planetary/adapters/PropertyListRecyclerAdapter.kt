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
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.morningstar.planetary.R
import com.morningstar.planetary.models.DummyModel
import com.morningstar.planetary.models.SearchPropertyResponse
import com.squareup.picasso.Picasso

class PropertyListRecyclerAdapter(var context: Context,
    var modelArrayList: ArrayList<DummyModel>) :
    RecyclerView.Adapter<PropertyListRecyclerAdapter.PropertyListViewHolder>() {

    private lateinit var view : View

    class PropertyListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewPrice: TextView = itemView.findViewById<TextView>(R.id.textView_property_price)
        var textViewAddress: TextView = itemView.findViewById<TextView>(R.id.textView_property_address)
        var textViewDescription: TextView = itemView.findViewById<TextView>(R.id.textView_property_description)
        var imageViewPropertyImage: ImageView = itemView.findViewById<ImageView>(R.id.property_item_image)
        var textViewDaysOnWebsite: TextView = itemView.findViewById<TextView>(R.id.textView_days_on_website)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyListViewHolder {
        view = LayoutInflater.from(context).inflate(R.layout.item_rv_property, parent, false)

        return PropertyListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return modelArrayList.size
    }

    override fun onBindViewHolder(holder: PropertyListViewHolder, position: Int) {
        val model = modelArrayList[position]

        holder.textViewPrice.text = model.price
        holder.textViewAddress.text = model.address
        holder.textViewDescription.text = model.description

        if (model.imageUrl == null || model.imageUrl.isEmpty())
            Picasso.get().load(R.drawable.ic_launcher_background).into(holder.imageViewPropertyImage)
        else{
            Picasso.get().load(model.imageUrl).into(holder.imageViewPropertyImage)
        }

//        if (model.DaysOnMarket != null)
//            holder.textViewDaysOnWebsite.text = model.DaysOnMarket.toString()
    }
}