package com.dicoding.naufal.mtoshokan.ui.addbookmark

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dicoding.naufal.mtoshokan.BR
import com.dicoding.naufal.mtoshokan.R
import com.dicoding.naufal.mtoshokan.base.BaseActivity
import com.dicoding.naufal.mtoshokan.databinding.ActivityAddBookmarkBinding
import com.dicoding.naufal.mtoshokan.imageviewer.ImageViewerActivity
import com.dicoding.naufal.mtoshokan.itemdecoration.MarginItemGridDecoration
import com.dicoding.naufal.mtoshokan.model.Book
import com.dicoding.naufal.mtoshokan.ui.addbookmark.adapter.AddPhotoBookmarkAdapter
import com.dicoding.naufal.mtoshokan.utils.ProgressDialog
import com.dicoding.naufal.mtoshokan.utils.openImagePicker
import com.google.android.material.snackbar.Snackbar
import com.zhihu.matisse.Matisse
import kotlinx.android.synthetic.main.activity_add_bookmark.*
import kotlinx.android.synthetic.main.template_toolbar_gradient_rtl.*

class AddBookmarkActivity : BaseActivity<ActivityAddBookmarkBinding, AddBookmarkViewModel>() {

    lateinit var mAddPhotoBookmarkAdapter: AddPhotoBookmarkAdapter
    lateinit var binding: ActivityAddBookmarkBinding
    lateinit var mAddBookmarkViewModel: AddBookmarkViewModel
    lateinit var dialog: AlertDialog

    override fun getLayoutId(): Int {
        return R.layout.activity_add_bookmark
    }

    override fun getViewModel(): AddBookmarkViewModel {
        mAddBookmarkViewModel = ViewModelProviders.of(this).get(AddBookmarkViewModel::class.java)
        return mAddBookmarkViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.addBookmarkViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewDataBinding()
        setUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            android.R.id.home ->{
                finish()
                true
            }
            R.id.menu_save -> {
                if(validate()){
                    mAddBookmarkViewModel.save(isNetworkConnected, {
                        ProgressDialog.buildOk(this, "Penanda telah berhasil ditambahkan"){
                            setResult(Activity.RESULT_OK)
                            finish()
                        }.show()

                    },{
                        when(it){
                            -1 -> {
                                Snackbar.make(
                                    findViewById(android.R.id.content),
                                    "Gagal mengunggah file ke server",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                            -2 -> {
                                Snackbar.make(
                                    findViewById(android.R.id.content),
                                    "Gagal memperbarui database",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                            -99 -> {
                                Snackbar.make(
                                    findViewById(android.R.id.content),
                                    getString(R.string.offline_network),
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                        }
                    })
                }
                true
            }
            else ->super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            mAddBookmarkViewModel.addImage(Matisse.obtainResult(data))
        }
    }

    private fun setUp(){
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        dialog = ProgressDialog.build(this)

        mAddPhotoBookmarkAdapter = AddPhotoBookmarkAdapter(addListener = {
            mAddBookmarkViewModel.imageLiveData.value?.size?.let { 5.minus(it) }?.let { this.openImagePicker(it, 1) }
        }, photoListener = {
            startActivity(ImageViewerActivity.newIntent(this, it))
        }, deleteListener = {
            mAddBookmarkViewModel.delete(it)
        })
        recycler_bookmark_photo.apply {
            adapter = mAddPhotoBookmarkAdapter
            layoutManager = GridLayoutManager(this@AddBookmarkActivity, 3)
            addItemDecoration(MarginItemGridDecoration(resources.getDimension(R.dimen.card_horizontal_margin),3))
        }
        mAddBookmarkViewModel.bookLiveData.value = intent.getParcelableExtra("book")
        subscribeToLiveData()
    }

    private fun subscribeToLiveData(){
        mAddBookmarkViewModel.bookLiveData.observe(this, Observer {
            if(it != null){
                txt_title.text = it.bookTitle
                txt_writer.text = it.bookWriter
                txt_isbn.text = resources.getString(R.string.isbn_n, it.bookISBN)

                Glide.with(this)
                    .load(it.bookCover)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(img_cover)
            }
        })

        mAddBookmarkViewModel.imageLiveData.observe(this, Observer {
            mAddPhotoBookmarkAdapter.setData(it)
        })

        mAddBookmarkViewModel.loading.observe(this, Observer {
            if(it){
                dialog.show()
            } else {
                dialog.dismiss()
            }
        })
    }

    fun validate():Boolean{
        mAddBookmarkViewModel.errorBookmarkTitleLiveData.value =
            if (mAddBookmarkViewModel.bookmarkTitleLiveData.value.isNullOrEmpty()) {
                "Judul penanda tidak boleh kosong"
            } else {
                null

            }

        val size = mAddBookmarkViewModel.imageLiveData.value?.size
        return size!! > 0 && mAddBookmarkViewModel.errorBookmarkTitleLiveData.value.isNullOrEmpty()
    }

    companion object {

        const val REQ_CODE_ADD_BOOKMARK = 1
        fun newIntent(context: Context, book: Book) : Intent {
            return Intent(context, AddBookmarkActivity::class.java).apply {
                putExtra("book", book)
            }
        }
    }
}
