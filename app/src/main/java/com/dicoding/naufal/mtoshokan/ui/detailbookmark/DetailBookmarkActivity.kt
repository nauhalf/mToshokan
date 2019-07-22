package com.dicoding.naufal.mtoshokan.ui.detailbookmark

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.naufal.mtoshokan.BR
import com.dicoding.naufal.mtoshokan.R
import com.dicoding.naufal.mtoshokan.base.BaseActivity
import com.dicoding.naufal.mtoshokan.databinding.ActivityDetailBookmarkBinding
import com.dicoding.naufal.mtoshokan.imageviewer.ImageViewerActivity
import com.dicoding.naufal.mtoshokan.itemdecoration.MarginItemGridDecoration
import com.dicoding.naufal.mtoshokan.model.Bookmark
import com.dicoding.naufal.mtoshokan.ui.detailbookmark.adapter.PhotoBookmarkAdapter
import com.dicoding.naufal.mtoshokan.utils.ProgressDialog
import kotlinx.android.synthetic.main.activity_detail_bookmark.*
import kotlinx.android.synthetic.main.template_toolbar_gradient_rtl.*

class DetailBookmarkActivity : BaseActivity<ActivityDetailBookmarkBinding, DetailBookmarkViewModel>() {

    lateinit var mPhotoBookmarkAdapter: PhotoBookmarkAdapter
    lateinit var mDetailBookmarkViewModel: DetailBookmarkViewModel
    lateinit var dialog: AlertDialog
    lateinit var binding: ActivityDetailBookmarkBinding

    override fun getLayoutId(): Int {
        return R.layout.activity_detail_bookmark
    }

    override fun getViewModel(): DetailBookmarkViewModel {
        mDetailBookmarkViewModel = ViewModelProviders.of(this).get(DetailBookmarkViewModel::class.java)
        return mDetailBookmarkViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.detailBookmarkViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewDataBinding()
        setUp()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            android.R.id.home->{
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun setUp() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        mPhotoBookmarkAdapter = PhotoBookmarkAdapter(photoListener = {
            startActivity(ImageViewerActivity.newIntent(this, Uri.parse(it)))
        })


        recycler_images.apply {

            adapter = mPhotoBookmarkAdapter
            layoutManager = GridLayoutManager(this@DetailBookmarkActivity, 3)
            addItemDecoration(MarginItemGridDecoration(resources.getDimension(R.dimen.card_horizontal_margin),3))
        }
        dialog = ProgressDialog.build(this)
        mDetailBookmarkViewModel.bookmarkLiveData.value = intent.getParcelableExtra("bookmark")
        subscribeToLiveData()
    }

    private fun subscribeToLiveData(){
        mDetailBookmarkViewModel.loading.observe(this, Observer {
            if(it){
                dialog.show()
            } else {
                dialog.dismiss()
            }
        })

        mDetailBookmarkViewModel.bookmarkLiveData.observe(this, Observer {
            if(it != null){
                txt_title_value.text = it.bookmarkTitle
                txt_description_value.text = it.bookmarkDescription
                it.bookmarkImages?.let { it1 -> mPhotoBookmarkAdapter.setData(it1) }
            }
        })
    }

    companion object{
        fun newIntent(context: Context, bookmark: Bookmark): Intent = Intent(context, DetailBookmarkActivity::class.java).apply{
            putExtra("bookmark", bookmark)
        }
    }
}
