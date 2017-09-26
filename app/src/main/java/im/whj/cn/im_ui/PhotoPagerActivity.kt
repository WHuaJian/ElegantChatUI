package im.whj.cn.im_ui

import android.net.Uri
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_photo_pager.*
import com.github.chrisbanes.photoview.PhotoView


/**
 * @author William
 * @Github WHuaJian
 * Created at 2017/9/26 下午3:51
 */
class PhotoPagerActivity : AppCompatActivity(){
    val PHOTO_LIST = "PHOTO_LIST"
    val PHOTO_INDEX = "PHOTO_INDEX"

    private lateinit var mPhotoLists : ArrayList<String>
    private var current_position : Int = 0
    private var mViews : ArrayList<PhotoView> = arrayListOf()

    companion object {
        fun _this(): PhotoPagerActivity {
            return PhotoPagerActivity()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_pager)

        mPhotoLists = intent.getStringArrayListExtra(PHOTO_LIST)
        current_position = intent.extras.getInt(PHOTO_INDEX)
        addPhotoView()
        initViewPagerAdapter()
    }

    private fun initViewPagerAdapter(){
        System.gc()
        val mAdapter = ViewPagerAdapter()
        view_pager.adapter = mAdapter
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                current_position = position
                indicator.text = "${current_position + 1}/${mPhotoLists.size}"
            }

        })

        view_pager.currentItem = current_position
        indicator.text = "${current_position + 1}/${mPhotoLists.size}"

    }

    inner class ViewPagerAdapter : PagerAdapter() {
        override fun getCount(): Int {
            return mPhotoLists.size
        }

        override fun instantiateItem(container: ViewGroup?, position: Int): Any {
            container!!.addView(mViews[position])
            return mViews[position]
        }
        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }
    }


    private fun addPhotoView(){
        for(i in 0 until mPhotoLists.size){
            val view = PhotoView(this)
            view.setImageURI(Uri.parse(mPhotoLists[i]))
            mViews.add(view)
        }
    }

    override fun onBackPressed() {
        if(mViews.isNotEmpty()) mViews.clear()
        System.gc()
        super.onBackPressed()
    }
}