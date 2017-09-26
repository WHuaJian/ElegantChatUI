package im.whj.cn.im_ui.adapter

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import im.whj.cn.im_ui.widget.TextDrawable
import java.text.SimpleDateFormat
import java.util.*
import android.provider.MediaStore.Images.ImageColumns
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.DocumentsContract
import android.support.annotation.RequiresApi


/**
 * @author William
 * @Github WHuaJian
 * Created at 2017/9/25 下午5:22
 */
object Utils{

    /**
     * 显示头像
     * */
    fun showAvatar(view : ImageView, name : String){
        val drawable = TextDrawable.builder()
                .beginConfig().textColor(Color.WHITE).fontSize(60).endConfig()
                .buildRound(name.substring(0, 1), Color.parseColor("#E27070"))
        view.setImageDrawable(drawable)
    }


    /**
     * 将时间戳转为时间字符串
     *
     * 格式为用户自定义
     */
    private fun milliseconds2String(milliseconds: Long, format: SimpleDateFormat): String {
        return format.format(Date(milliseconds))
    }

    /**
     * 计算两个日期型的时间相差多少时间
     *
     * @param startTime 开始日期
     * @return
     */
    fun toDateDistance(startTime: String): String {
        val curTime = System.currentTimeMillis() / 1000.toLong()
        val time = curTime - if (startTime.length > 10) startTime.toLong() / 1000 else startTime.toLong()
        return if (time <= 10) {
            "刚刚"
        } else if (time in 11..60) {
            time.toString() + "秒前"
        } else if (time in 61..3600) {
            (time / 60).toString() + "分钟前"
        } else if (time > 3600 && time <= 3600 * 24) {
            (time / 3600).toString() + "小时前"
        }
        else if (time > 3600 * 24) {
            milliseconds2String(startTime.toLong(), SimpleDateFormat("yyyy-MM-dd"))
        } else {
            "刚刚"
        }
    }

    /**
    * 隐藏键盘
    * */
    fun hideSoftKeyboard(activity: Activity?) {
        try {
            if (activity != null) {
                val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm?.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 判断触摸点是否在view内
    * */
    fun isTouchPointInView(view: View?, x: Int, y: Int): Boolean {
        if (view == null) {
            return false
        }
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val left = location[0]
        val top = location[1]
        val right = left + view.measuredWidth
        val bottom = top + view.measuredHeight
        return y in top..bottom && x >= left
                && x <= right
    }

    fun generateMsgID(): String {
        val uuid = UUID.randomUUID()
        return uuid.toString()
    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getRealFilePath(context: Context, uri: Uri): String? {
        var filePath: String? = null
        try {
            val wholeID = DocumentsContract.getDocumentId(uri)
            val id = wholeID.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]

            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val selection = MediaStore.Images.Media._ID + "=?"
            val selectionArgs = arrayOf(id)

            val cursor = context.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, //
                    projection, selection, selectionArgs, null)
            val columnIndex = cursor.getColumnIndex(projection[0])
            if (cursor.moveToFirst()) filePath = cursor.getString(columnIndex)
            cursor.close()
        }catch (e : Exception){
            e.printStackTrace()
        }
        return filePath
    }

}