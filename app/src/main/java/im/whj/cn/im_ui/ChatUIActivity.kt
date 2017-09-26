package im.whj.cn.im_ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tbruyelle.rxpermissions2.RxPermissions
import im.whj.cn.im_ui.adapter.BaseAdapter
import im.whj.cn.im_ui.adapter.BaseViewHolder
import im.whj.cn.im_ui.adapter.Utils
import im.whj.cn.im_ui.db.entity.MsgDbModel
import im.whj.cn.im_ui.db.helper.DbUtils
import kotlinx.android.synthetic.main.activity_chat_ui.*
import kotlinx.android.synthetic.main.im_bottom.*
import java.util.ArrayList

/**
 * @author William
 * @Github WHuaJian
 * Created at 2017/9/26 下午3:02
 */
class ChatUIActivity : AppCompatActivity() , View.OnClickListener {

    private var mAdapter: BaseAdapter? = null
    private var mMsgList: MutableList<MsgDbModel> = mutableListOf()
    private var sqlOffset = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_ui)

        viewClick(chat_send_btn,img_select)

        initRefresh()
        getDBHistoryMsg()
        initEditListener()
        initAdapter()

    }

    private fun initRefresh() {
        refresh.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorAccent))
        refresh.setOnRefreshListener {
            if (sqlOffset < DbUtils.getInstance().queryMsgCount()) {
                sqlOffset++
                getDBMoreHistoryMsg()
            }
            refresh.postDelayed({ refresh.isRefreshing = false }, 500)
        }
    }

    private fun initEditListener() {
        imChatInput.addTextChangedListener(object : TextWatcher {
            private var temp: CharSequence? = null

            override fun afterTextChanged(p0: Editable?) {
                if (temp.toString().isNullOrEmpty()) {
                    chat_send_btn.setTextColor(Color.parseColor("#CCCCCC"))
                    chat_send_btn.isClickable = false
                    img_select.visibility = View.VISIBLE
                    chat_send_btn.visibility = View.GONE
                } else {
                    chat_send_btn.setTextColor(Color.parseColor("#D63E3E"))
                    chat_send_btn.isClickable = true
                    img_select.visibility = View.GONE
                    chat_send_btn.visibility = View.VISIBLE
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                temp = p0

            }

        })
    }

    private fun initAdapter() {
        if (mAdapter == null) {
            mAdapter = object : BaseAdapter() {
                override fun onBindView(holder: BaseViewHolder, position: Int) {
                    var item_content = holder?.getView<TextView>(R.id.text_message) as TextView
                    var item_date = holder?.getView<TextView>(R.id.date) as TextView
                    var item_avatar = holder?.getView<ImageView>(R.id.avatar) as ImageView
                    var item_image = holder?.getView<ImageView>(R.id.img_message) as ImageView
                    var item_name = holder?.getView<ImageView>(R.id.name) as TextView

                    if (position > 0) {

                        if (position == mMsgList.size - 1) {
                            item_date.visibility = View.VISIBLE
                            item_date.text = Utils.toDateDistance(mMsgList[position].time)
                        } else {
                            if ((mMsgList[position - 1].time.toLong() - mMsgList[position].time.toLong()) > 60000) {
                                item_date.visibility = View.VISIBLE
                                item_date.text = Utils.toDateDistance(mMsgList[position].time)
                            } else {
                                item_date.visibility = View.GONE
                            }
                        }

                    } else {
                        item_date.visibility = View.VISIBLE
                        item_date.text = Utils.toDateDistance((java.lang.Long.parseLong(mMsgList[position].time)).toString())
                    }
                    item_name.text = mMsgList[position].user_name
                    Utils.showAvatar(item_avatar, mMsgList[position].user_name)
                    if(mMsgList[position].type == 1){
                        item_content.text = mMsgList[position].content
                        item_content.visibility = View.VISIBLE
                        item_image.visibility = View.GONE

                    }else{
                        item_image.setImageURI(Uri.parse(mMsgList[position].content))
                        item_image.visibility = View.VISIBLE
                        item_content.visibility = View.GONE
                        item_image.setOnClickListener(PreviewListener(DbUtils.getInstance().getImgPosition(mMsgList[position].msg_id)))
                    }
                }

                override fun getLayoutID(position: Int): Int {
                    return when {
                        mMsgList[position].isFrom -> R.layout.item_chat_send_layout
                        else -> R.layout.item_chat_rec_layout
                    }
                }

                override fun clickable(): Boolean {
                    return false
                }

                override fun getItemCount(): Int {
                    return mMsgList.size
                }

            }
            recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
            recycler.adapter = mAdapter
        } else {
            mAdapter?.notifyDataSetChanged()
        }
    }


    /**
     * 图片预览
     */
    private inner class PreviewListener(private val position: Int) : View.OnClickListener {

        override fun onClick(v: View) {
            val lists = DbUtils.getInstance().queryAllPhoto()
            val intent = Intent()
            intent.putExtra(PhotoPagerActivity._this().PHOTO_LIST, lists)
            intent.putExtra(PhotoPagerActivity._this().PHOTO_INDEX, position)
            intent.setClass(this@ChatUIActivity, PhotoPagerActivity::class.java)
            startActivity(intent)
        }
    }

    private fun viewClick(vararg view: View) {
        view.forEach {
            it.setOnClickListener(this)
        }
    }


    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.chat_send_btn -> sendMessage()
            R.id.img_select -> startPhoto()
        }
    }

    private fun startPhoto() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(Intent.createChooser(intent, "select pic"), 100)
        }

    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == 100 && data != null) {
            val url: Uri = data.data
            val msgModel = createMsgModel(true, Utils.getRealFilePath(this,url)!!, 2)
            mMsgList.add(0, msgModel)
            DbUtils.getInstance().insertMessage2Table(msgModel)
            initAdapter()

            refresh.postDelayed({ receiveMessage("图片") }, 500)


        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * 模拟发送消息
     * */
    private fun sendMessage() {
        var input_msg = imChatInput.text.toString().trim()
        if (!input_msg.isNullOrEmpty()) {
            mMsgList.add(0, createMsgModel(true, input_msg, 1))

            initAdapter()

            refresh.postDelayed({ receiveMessage(input_msg) }, 500)

            imChatInput.text.clear()
        }
    }

    /**
     * 模拟接收消息
     **/
    private fun receiveMessage(content: String) {
        mMsgList.add(0, createMsgModel(false, "[回复]" + content, 1))
        initAdapter()
    }

    private fun createMsgModel(isFrom: Boolean, content: String, type: Int): MsgDbModel {
        var dbModel = MsgDbModel()
        if (isFrom) dbModel.user_name = "William" else dbModel.user_name = "Jack"
        dbModel.isFrom = isFrom
        dbModel.time = System.currentTimeMillis().toString()
        dbModel.msg_id = Utils.generateMsgID()
        dbModel.content = content
        dbModel.type = type

        DbUtils.getInstance().insertMessage2Table(dbModel)
        return dbModel
    }

    /**
     * 历史消息
     */
    private fun getDBHistoryMsg() {
        val history = DbUtils.getInstance().getHistoryMessage(sqlOffset)
        mMsgList.addAll(0, history)
        initAdapter()
        recycler.scrollToPosition(mMsgList.size)
    }

    /**
     * 更多历史消息
     */
    private fun getDBMoreHistoryMsg() {
        val history = DbUtils.getInstance().getHistoryMessage(sqlOffset)
        mMsgList.addAll(history)
        initAdapter()
    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val x = ev.rawX.toInt()
        val y = ev.rawY.toInt()
        if (!Utils.isTouchPointInView(im_bottom, x, y)) {
            Utils.hideSoftKeyboard(this)
        }
        return super.dispatchTouchEvent(ev)
    }

}