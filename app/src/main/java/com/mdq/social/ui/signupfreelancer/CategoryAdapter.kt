package com.mdq.social.ui.signupfreelancer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mdq.social.R
import com.mdq.social.app.data.response.category.DataItem
import com.mdq.social.databinding.ItemCategoryBinding

class CategoryAdapter(
    var context: Context,
    var dataItemList: List<DataItem>?,
    var clickManager: ClickManager
) :
    RecyclerView.Adapter<CategoryAdapter.ExpHolder>(), CompoundButton.OnCheckedChangeListener {

    public interface ClickManager {
        fun onItemClick(dataItemList: DataItem, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpHolder {
        return ExpHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_category,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataItemList?.size!!
    }

    override fun onBindViewHolder(holder: ExpHolder, position: Int) {
        holder.getBinding().tvCategory.setText(dataItemList?.get(position)?.name.toString())
//        holder.getBinding().cbCategroy.setTag(position)
//
//        holder.getBinding().cbCategroy.setOnCheckedChangeListener(this)


    }

    inner class ExpHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private var binding: ItemCategoryBinding? = null

        init {
            binding = DataBindingUtil.bind(itemView)
            itemView.setOnClickListener(this)
        }

        fun getBinding(): ItemCategoryBinding {
            return binding!!
        }

        override fun onClick(p0: View?) {
            clickManager.onItemClick(
                dataItemList!!.get(absoluteAdapterPosition),
                absoluteAdapterPosition
            )
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {

        var pos = buttonView?.getTag() as Int
    }


}