package im.whj.cn.im_ui

import android.app.Application
import im.whj.cn.im_ui.db.helper.DbCore

/**
 * @author William
 * @Github WHuaJian
 * Created at 2017/9/25 下午4:25
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        configDb()
    }

    private fun configDb() {
        DbCore.init(this, "IM_DB")
        DbCore.enableQueryBuilderLog()
    }
}