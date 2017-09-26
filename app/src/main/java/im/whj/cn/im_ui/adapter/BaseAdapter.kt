package im.whj.cn.im_ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * @author William
 * @Github WHuaJian
 * Created at 2017/9/25 下午3:28
 */
abstract class BaseAdapter : RecyclerView.Adapter<BaseViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {


        if (clickable()) {
            holder.getConvertView().isClickable = true
            holder.getConvertView().setOnClickListener( { v -> onItemClick(v, position) })
        }

        onBindView(holder, holder.layoutPosition)

    }

    abstract fun onBindView(holder: BaseViewHolder, position: Int)

    override fun getItemViewType(position: Int): Int {
        return getLayoutID(position)
    }


    abstract fun getLayoutID(position: Int): Int

    abstract fun clickable(): Boolean

    private fun onItemClick(v: View, position: Int) {}


}