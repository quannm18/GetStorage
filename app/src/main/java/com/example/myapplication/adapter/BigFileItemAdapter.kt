package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.core.net.toUri
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.customview.ItemSubCell
import com.example.myapplication.model.AsyncCell
import com.example.myapplication.model.BigFileItem

class BigFileItemAdapter :
    PagingDataAdapter<BigFileItem, BigFileItemAdapter.BigFileViewHolder>(diffCallback) {

    inner class BigFileViewHolder(parent: View) : RecyclerView.ViewHolder(
        parent
    ) {
        val imgLogoCleanSubRow: ImageView by lazy { itemView.findViewById<ImageView>(R.id.imgLogoCleanSubRow) }
        val tvTitleCleanSubRow: TextView by lazy { itemView.findViewById<TextView>(R.id.tvTitleCleanSubRow) }
        val tvSizeCleanSubRow: TextView by lazy { itemView.findViewById<TextView>(R.id.tvSizeCleanSubRow) }
        val chkCleanSubRow: CheckBox by lazy { itemView.findViewById<CheckBox>(R.id.chkCleanSubRow) }

        @SuppressLint("ResourceAsColor")
        fun bind(item: BigFileItem?) {
//            imgLogoCleanSubRow.setImageURI(item?.pathUri?.toUri())
            Glide.with(itemView)
                .load(item?.pathUri?.toUri() ?: R.drawable.ic_launcher_foreground)
                .centerCrop()
                .override(dpToPx(24).toInt())
                .into(imgLogoCleanSubRow)
            tvTitleCleanSubRow.text = item?.name
            tvSizeCleanSubRow.text = item?.size.toString()
//            chkCleanSubRow.isChecked = item!!.isChecked
        }

        fun dpToPx(dp: Int): Float {
            return (dp * Resources.getSystem().displayMetrics.density)
        }
    }

    override fun onBindViewHolder(holder: BigFileViewHolder, position: Int) {
        (holder.itemView as ItemSubCell).bindWhenInflated {
            holder.bind(getItem(position))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BigFileViewHolder {

        return BigFileViewHolder(
            ItemSubCell(parent.context).apply {
                inflate()
            }
        )
    }
    private inner class SmallItemCell(context: Context) : AsyncCell(context) {
        override val layoutId = R.layout.sub_item_virus_clean
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<BigFileItem>() {
            override fun areItemsTheSame(oldItem: BigFileItem, newItem: BigFileItem): Boolean {
                return (oldItem.name == newItem.name && oldItem.id == newItem.id)
            }

            override fun areContentsTheSame(oldItem: BigFileItem, newItem: BigFileItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}