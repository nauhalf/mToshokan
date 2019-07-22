package com.dicoding.naufal.mtoshokan.imageviewer

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.dicoding.naufal.mtoshokan.R
import com.github.piasy.biv.BigImageViewer
import com.github.piasy.biv.indicator.progresspie.ProgressPieIndicator
import com.github.piasy.biv.loader.glide.GlideImageLoader
import kotlinx.android.synthetic.main.activity_image_viewer.*
import kotlinx.android.synthetic.main.template_toolbar_gradient_rtl.*

class ImageViewerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BigImageViewer.initialize(GlideImageLoader.with(applicationContext))

        setContentView(R.layout.activity_image_viewer)
        setUp()
        bigImageView.setProgressIndicator(ProgressPieIndicator());
        bigImageView.showImage(intent.getParcelableExtra("uri"));
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUp(){
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title =""
    }

    companion object{
        fun newIntent(context: Context, uri: Uri): Intent = Intent(context, ImageViewerActivity::class.java).apply {
            putExtra("uri", uri)
        }
    }
}
