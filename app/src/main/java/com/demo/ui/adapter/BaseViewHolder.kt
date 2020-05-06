package com.demo.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder(itemView: View?) :
    RecyclerView.ViewHolder(itemView!!) {
    var currentPosition = 0
        private set

    open fun onBindData(position: Int) {
        currentPosition = position

    }

}