package im.whj.cn.im_ui

import android.Manifest
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.tbruyelle.rxpermissions2.RxPermissions
import im.whj.cn.im_ui.db.helper.DbUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewClick(tvStart,tvClearDb)

        val rxPermission = RxPermissions(this)
        rxPermission.request(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe({

                })

    }

    private fun viewClick(vararg view: View) {
        view.forEach {
            it.setOnClickListener(this)
        }
    }


    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tvStart -> startChat()
            R.id.tvClearDb -> DbUtils.getInstance().deleteAll()
        }
    }


    private fun startChat() {
        val intent = Intent()
        intent.setClass(this, ChatUIActivity::class.java)
        startActivity(intent)
    }

}

