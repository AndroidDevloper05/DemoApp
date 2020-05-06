package com.demo.ui.adapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.demo.R
import com.demo.databinding.RowLoadingBinding
import com.demo.databinding.RowUserBinding
import com.demo.model.UsersResponse


/**
 * List of id types adapter
 */
class UsersAdapter(val userList: MutableList<UsersResponse>) : RecyclerView.Adapter<BaseViewHolder>() {

    private val VIEW_TYPE_LOADING = 0
    private val VIEW_TYPE_NORMAL = 1
    private var isLoaderVisible = false

    /**
     * onCreateViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        var baseViewHolder: BaseViewHolder?=null
        when (viewType) {
            VIEW_TYPE_NORMAL ->
            {
                val binding: RowUserBinding = DataBindingUtil.inflate(
                    layoutInflater,
                    R.layout.row_user, parent, false
                )
                baseViewHolder= UserViewHolder(binding)
            }

            VIEW_TYPE_LOADING -> {
                val binding: RowLoadingBinding = DataBindingUtil.inflate(
                    layoutInflater,
                    R.layout.row_loading, parent, false
                )
                baseViewHolder= ProgressHolder(binding)
            }
        }
        return baseViewHolder!!

    }
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBindData(position)
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoaderVisible) {
            if (position == userList.size - 1) VIEW_TYPE_LOADING else VIEW_TYPE_NORMAL
        } else {
            VIEW_TYPE_NORMAL
        }
    }
    /**
     * Get item count
     */
    override fun getItemCount(): Int {
        return userList.size
    }

    fun addItems(list: MutableList<UsersResponse>) {
        userList.addAll(list)
        notifyDataSetChanged()
    }

    fun addLoading() {
        isLoaderVisible = true
        userList.add(UsersResponse("0","","",0,0, arrayListOf(), arrayListOf()))
        notifyItemInserted(userList.size - 1)
    }

    fun removeLoading() {
        isLoaderVisible = false
        val position: Int = userList.size - 1
        val item: UsersResponse? = getItem(position)
        if (item != null) {
            userList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clear() {
        userList.clear()
        notifyDataSetChanged()
    }

    private fun getItem(position: Int): UsersResponse? {
        return userList[position]
    }
    /**
     * Holder for each row
     */
    inner class UserViewHolder(private val binding: RowUserBinding) :
        BaseViewHolder(binding.root)
         {
        /**
         * Binds a ids into the view
         */
        override fun onBindData(position: Int) {
            super.onBindData(position)
            binding.model = userList[position]
            binding.executePendingBindings()
        }


    }
    class ProgressHolder(binding: RowLoadingBinding) : BaseViewHolder(binding.root) {

    }
}