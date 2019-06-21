package com.dicoding.naufal.mtoshokan.utils

import android.content.Context
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.dicoding.naufal.mtoshokan.R

class ProgressDialog {
    companion object {
        fun build(context: Context): AlertDialog {
            val builder = AlertDialog.Builder(context)

            builder.setView(View.inflate(context, R.layout.template_progress_dialog, null))
            builder.setCancelable(false)
            return builder.create()
        }

        fun buildOk(context: Context, message: String, callback: () -> Unit): AlertDialog {
            val builder = AlertDialog.Builder(context)

            builder.setCancelable(true)
                .setMessage(message)
                .setPositiveButton("OK") { i, id ->
                    i.dismiss()
                    callback()
                }

            return builder.create()
        }
    }
}