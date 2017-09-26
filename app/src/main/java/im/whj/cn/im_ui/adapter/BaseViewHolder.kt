package im.whj.cn.im_ui.adapter

import android.support.annotation.IdRes
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View

/**
 * @author William
 * @Github WHuaJian
 * Created at 2017/9/25 下午3:29
 */
class BaseViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    private var mViews: SparseArray<View> = SparseArray()

    fun <T : View> getView(@IdRes viewId: Int): View? {
        var view: View? = mViews.get(viewId)
        if (view == null) {
            view = itemView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        return view
    }

    fun getConvertView(): View {
        return itemView
    }

}
