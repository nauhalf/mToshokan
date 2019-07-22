package com.dicoding.naufal.mtoshokan.utils

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.dicoding.naufal.mtoshokan.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.ktx.Firebase
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.internal.entity.CaptureStrategy
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.Days
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

fun getNextTenDays(optionalDay: Int = 10): Date {
    var todayJ = DateTime.now(DateTimeZone.forID("Asia/Jakarta"))
    todayJ = todayJ.plusDays(optionalDay)
    return todayJ.toDate()
}

fun Date.getRemaingDays(): Int {
    val returnDate = DateTime(this)
    val today = DateTime.now()

    return Days.daysBetween(today.toLocalDate(), returnDate.toLocalDate()).days
}

fun getRemaingDays(borrowing: Date, returning: Date): Int {
    val returnDate = DateTime(returning)
    val borrowDate = DateTime(borrowing)

    return Days.daysBetween(borrowDate, returnDate).days
}

fun String?.isValidEmail() : Boolean{
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun <T>MutableLiveData<T>.update() : Unit {
    this.value = this.value
}

fun Activity.openImagePicker(maxItem: Int, requestCode: Int) {
    Matisse.from(this)
        .choose(MimeType.ofImage())
        .maxSelectable(maxItem)
        .gridExpectedSize(resources.getDimensionPixelSize(R.dimen.grid_expected_size))
        .capture(true)
        .captureStrategy(
            CaptureStrategy(true, "com.dicoding.naufal.mtoshokan.fileprovider","test")
        )
        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
        .thumbnailScale(0.85f)
        .imageEngine(Glide4Engine())
        .theme(R.style.Matisse_Dracula)
        .forResult(requestCode);
}

fun Fragment.openImagePicker(maxItem: Int, requestCode: Int) {
    Matisse.from(this)
        .choose(MimeType.ofImage())
        .maxSelectable(maxItem)
        .gridExpectedSize(resources.getDimensionPixelSize(R.dimen.grid_expected_size))
        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
        .capture(true)
        .captureStrategy(CaptureStrategy(true, "com.dicoding.naufal.mtoshokan.fileprovider"))
        .thumbnailScale(0.85f)
        .imageEngine(Glide4Engine())
        .theme(R.style.Matisse_Dracula)
        .forResult(requestCode)
}

fun showPermissionErrorMessage(context: Context, permissions: Array<out String>, grantResults: IntArray){
    if (grantResults.isNotEmpty()) {
        var denied = "Harap izinkan permission dibawah ini :"
        var x = 0
        permissions.forEachIndexed { index, s ->
            if (grantResults[index] == PackageManager.PERMISSION_DENIED) {
                denied += "\n${s}"
                x++
            }
        }
        if (x > 0) {
            Toast.makeText(context, denied, Toast.LENGTH_SHORT).show()
        }
    }
}

val setting = FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build()