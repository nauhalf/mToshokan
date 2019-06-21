package com.dicoding.naufal.mtoshokan.ui.main.profile


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dicoding.naufal.mtoshokan.BR
import com.dicoding.naufal.mtoshokan.R
import com.dicoding.naufal.mtoshokan.base.BaseFragment
import com.dicoding.naufal.mtoshokan.databinding.FragmentProfileBinding
import com.dicoding.naufal.mtoshokan.ui.login.LoginActivity
import com.dicoding.naufal.mtoshokan.ui.main.profile.fullname.EditFullNameActivity
import com.dicoding.naufal.mtoshokan.ui.main.profile.password.EditPasswordActivity
import com.dicoding.naufal.mtoshokan.utils.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.yalantis.ucrop.UCrop
import com.zhihu.matisse.Matisse
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.civ_image_profile
import kotlinx.android.synthetic.main.fragment_profile.txt_email
import kotlinx.android.synthetic.main.fragment_profile.txt_full_name
import kotlinx.android.synthetic.main.shimmer_fragment_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {

    lateinit var mProfileViewModel: ProfileViewModel
    lateinit var binding: FragmentProfileBinding
    lateinit var mDialog: AlertDialog

    val scope = CoroutineScope(Dispatchers.IO)

    override val bindingVariable: Int
        get() = BR.profileViewModel
    override val layoutId: Int
        get() = R.layout.fragment_profile

    override fun getViewModel(): ProfileViewModel {
        mProfileViewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        return mProfileViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = getViewDataBinding()
        setUp()
        mDialog = ProgressDialog.build(requireContext())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQ_CODE_FULL_NAME) {

                mProfileViewModel.profileLiveData.value?.userFullName = data?.getStringExtra("fullName")
                mProfileViewModel.profileLiveData.update()
                Snackbar.make(root, "Nama berhasil diubah", Snackbar.LENGTH_SHORT).show()

            } else if (requestCode == REQ_CODE_PASSWORD) {

                Snackbar.make(root, "Password berhasil diubah", Snackbar.LENGTH_SHORT).show()

            } else if (requestCode == REQ_CODE_IMAGE) {

                val uri = Matisse.obtainResult(data)
                UCrop.of(
                    uri[0], ImageSaver(requireContext())
                        .setFileName(ImageSaver.generateFileName())
                        .setDirectoryName("userPhoto")
                        .getUri()
                )
                    .withAspectRatio(1f, 1f)
                    .withMaxResultSize(500, 500)
                    .withOptions(
                        UCrop.Options().apply {
                            setToolbarColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                            setActiveWidgetColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                        }
                    )
                    .start(requireActivity(), this)

            } else if (requestCode == UCrop.REQUEST_CROP) {

                val uri = data?.let { UCrop.getOutput(it) }
                mDialog.show()
                mProfileViewModel.uploadImage(uri, {
                    mProfileViewModel.profileLiveData.value?.userPhoto = it.toString()
                    mProfileViewModel.profileLiveData.update()
                    mDialog.dismiss()
                    Snackbar.make(root, "Foto berhasil diubah", Snackbar.LENGTH_SHORT).show()
                    scope.launch {
                        uri?.lastPathSegment?.let { it1 -> ImageSaver(requireContext()).setFileName(it1).deleteFile() }
                    }
                }, {
                    mDialog.dismiss()
                    Snackbar.make(root, "Gagal mengubah foto profil", Snackbar.LENGTH_SHORT).show()
                    scope.launch {
                        uri?.lastPathSegment?.let { it1 -> ImageSaver(requireContext()).setFileName(it1).deleteFile() }
                    }
                })
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_PROFILE -> {

                showPermissionErrorMessage(requireContext(), permissions, grantResults)
            }
        }
    }


    fun setUp() {
        btn_logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(LoginActivity.newIntent(requireContext()))
            activity?.finish()
        }

        mProfileViewModel.loadData()
        subscribeToLiveData()

        img_icon_name_edit.setOnClickListener {
            startActivityForResult(
                EditFullNameActivity.newIntent(
                    requireContext(),
                    mProfileViewModel.profileLiveData.value?.userFullName
                ), REQ_CODE_FULL_NAME
            )
        }

        img_icon_password_edit.setOnClickListener {
            startActivityForResult(EditPasswordActivity.newIntent(requireContext()), REQ_CODE_PASSWORD)
        }

        img_change_photo.setOnClickListener {
            if (checkPermission(
                    listOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), PERMISSION_PROFILE
                )
            ) {

                if (mProfileViewModel.profileLiveData.value?.userPhoto.isNullOrEmpty()) {
                    this.openImagePicker(1, REQ_CODE_IMAGE)
                } else {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle(getString(R.string.profile_photo))
                    builder.setItems(arrayOf("Ubah", "Hapus")) { dialog, which ->
                        when (which) {
                            0 -> {
                                this.openImagePicker(1, REQ_CODE_IMAGE)
                            }
                            1 -> {
                                mDialog.show()
                                mProfileViewModel.deleteUserPhoto {
                                    mDialog.dismiss()
                                }
                            }
                        }
                    }
                    val dialog = builder.create()
                    dialog.show()
                }
            }
        }

    }

    private fun subscribeToLiveData() {
        mProfileViewModel.profileLiveData.observe(viewLifecycleOwner, Observer {

            txt_full_name.text = if (it.userFullName.isNullOrEmpty()) {
                "[Nama belum dibuat]"
            } else {
                it.userFullName
            }

            txt_email.text = it.userEmail
            txt_full_name_2.text = it.userFullName
            it.userPhoto?.let {
                if (!it.isEmpty()) {
                    Glide.with(requireContext())
                        .load(it)
                        .placeholder(ColorDrawable(ContextCompat.getColor(requireContext(), R.color.colorInactive)))
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .into(civ_image_profile)

                }
            } ?: run {
                Glide.with(requireContext())
                    .load(ColorDrawable(ContextCompat.getColor(requireContext(), R.color.colorInactive)))
                    .into(civ_image_profile)
            }
        })

        mProfileViewModel.loading.observe(viewLifecycleOwner, Observer {
            if (it) {
                shimmer_fragment_profile.startShimmer()
                shimmer_fragment_profile.visibility = View.VISIBLE
            } else {
                shimmer_fragment_profile.stopShimmer()
                shimmer_fragment_profile.visibility = View.GONE
            }
        })
    }

    companion object {
        const val REQ_CODE_FULL_NAME = 1
        const val REQ_CODE_PASSWORD = 2
        const val REQ_CODE_IMAGE = 3
        const val PERMISSION_PROFILE = 1
    }

}
