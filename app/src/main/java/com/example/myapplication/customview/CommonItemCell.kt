package com.example.myapplication.customview

import android.content.Context
import android.provider.SyncStateContract
import android.widget.FrameLayout
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import com.example.myapplication.R


class ItemSubCell(context: Context) : CommonItemCell(context) {
    override val layoutId = R.layout.sub_item_virus_clean
}


open class CommonItemCell(context: Context) : FrameLayout(context) {
    open val param: LayoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    open val layoutId = -1
    private var isInflated = false
    private val bindingFunctions: MutableList<CommonItemCell.() -> Unit> = mutableListOf()

    fun inflate() {
        layoutParams = param
        AsyncLayoutInflater(context).inflate(layoutId, this) { view, _, _ ->
            isInflated = true
            addView(view)
            bindView()
        }
    }

    private fun bindView() {
        with(bindingFunctions) {
            forEach { it() }
            clear()
        }
    }

    fun bindWhenInflated(bindFunc: CommonItemCell.() -> Unit) {
        if (isInflated) {
            bindFunc()
        } else {
            bindingFunctions.add(bindFunc)
        }
    }
}

