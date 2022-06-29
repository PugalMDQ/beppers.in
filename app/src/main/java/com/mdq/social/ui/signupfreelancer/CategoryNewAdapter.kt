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
import com.mdq.social.databinding.ItemNewCategoryBinding

class CategoryNewAdapter(
    var context: Context,
    var dataItemList: List<DataItem>,
    var clickManager: ClickManager
) :
    RecyclerView.Adapter<CategoryNewAdapter.ExpHolder>(), CompoundButton.OnCheckedChangeListener {

    public interface ClickManager {
        fun onItemClick(dataItemList: DataItem, position: Int)
        fun onItemSelectedClick(dataItemList: DataItem, position: Int, isSelected: Boolean,text:String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpHolder {
        return ExpHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_new_category,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataItemList.size
    }

    override fun onBindViewHolder(holder: ExpHolder, position: Int) {
        holder.getBinding().tvCategory.setText(dataItemList.get(position).name.toString())
//        holder.getBinding().cbCategroy.setTag(position)

////        holder.getBinding().cbCategroy.setOnCheckedChangeListener(null)
//
//        holder.getBinding().cbCategroy.isChecked=dataItemList.get(position).isSelected!!
//        holder.getBinding().tvcategory.isChecked=dataItemList.get(2).isSelected!!
//        holder.getBinding().cbCategroy.setOnCheckedChangeListener(this)

    }

    inner class ExpHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private var binding: ItemNewCategoryBinding? = null

        init {
            binding = DataBindingUtil.bind(itemView)
            itemView.setOnClickListener(this)
        }

        fun getBinding(): ItemNewCategoryBinding {
            return binding!!
        }

        override fun onClick(p0: View?) {
            binding!!.tick.visibility=View.VISIBLE
            clickManager.onItemClick(
                dataItemList.get(absoluteAdapterPosition),
                absoluteAdapterPosition
            )
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {

        var pos = buttonView?.getTag() as Int
        var text = buttonView?.text as String

        clickManager.onItemSelectedClick(dataItemList.get(pos),pos,isChecked,text)

    }


}