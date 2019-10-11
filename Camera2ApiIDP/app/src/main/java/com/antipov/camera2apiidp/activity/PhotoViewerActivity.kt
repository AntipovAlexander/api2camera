package com.antipov.camera2apiidp.activity

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import com.antipov.camera2apiidp.R
import kotlinx.android.synthetic.main.activity_photo_viewer.*
import java.io.File

class PhotoViewerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_viewer)
        val folder = File(filesDir, "")
        val photos = arrayListOf<String>()
        if (folder.exists()) {
            photos
                .addAll(
                    folder
                        .listFiles { _, name -> name.endsWith(".jpg") }
                        ?.filterNotNull()
                        ?.map { it.absolutePath }
                        ?.reversed() ?: arrayListOf()
                )
        }
        viewpager.adapter =
            PhotoViewerAdapter(photos)
    }

    private class PhotoViewerAdapter(private val data: ArrayList<String>) : PagerAdapter() {

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val image = ImageView(container.context)
            image.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            image.setImageBitmap(BitmapFactory.decodeFile(data[position]))
            container.addView(image)
            return image
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) =
            container.removeView(`object` as View?)

        override fun isViewFromObject(view: View, `object`: Any) = view == `object`

        override fun getCount(): Int = data.size
    }
}
