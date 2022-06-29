package com.mdq.social.utils

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import androidx.core.content.ContextCompat
import android.util.Base64
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.mdq.social.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*


object UiUtils {

    fun showSnackBar(view: View, message: String, length: Int) {
        val snackbar = Snackbar.make(view, message, length)
        val v = snackbar.view
        val textView = v.findViewById<View>(R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        textView.maxLines = 4
        snackbar.show()
    }

    fun showSnackBarWithAction(
        view: View, messageResId: Int, length: Int,
        actionResId: Int, actionClickListener: View.OnClickListener
    ) {
        val snackbar = Snackbar.make(view, messageResId, length)
        val v = snackbar.view
        val textView = v.findViewById<View>(R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        textView.maxLines = 4
        snackbar.setAction(actionResId, actionClickListener)
        snackbar.show()
    }

    fun showSnackBarWithAction(
        view: View, message: String, length: Int, action: String,
        actionClickListener: View.OnClickListener
    ) {
        val snackbar = Snackbar.make(view, message, length)
        val v = snackbar.view
        val textView = v.findViewById<View>(R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        textView.maxLines = 4
        snackbar.setAction(action, actionClickListener)
        snackbar.show()
    }

    fun showSnackBar(view: View, message: Int, length: Int) {
        val snackbar = Snackbar.make(view, message, length)
        val v = snackbar.view
        val textView = v.findViewById<View>(R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        textView.maxLines = 4
        snackbar.show()
    }

    fun showToast(mActivity: AppCompatActivity, message: String) {
        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show()
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun createAlertDialogWithTwoButtons(
        context: Context, title: String,
        message: String, buttonFirstText: String, buttonSecondText: String,
        firstListener: DialogInterface.OnClickListener,
        secondListener: DialogInterface.OnClickListener
    ): AlertDialog {
        val alertDialog = AlertDialog.Builder(context).setTitle(title)
            .setMessage(message)
            .setPositiveButton(buttonFirstText, firstListener)
            .setNegativeButton(buttonSecondText, secondListener)
            .create()
        if (TextUtils.isEmpty(title)) {
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        }
        return alertDialog
    }

    fun createAlertDialog(
        context: Context, title: String, message: String,
        buttonText: String, onClickListener: DialogInterface.OnClickListener
    ): AlertDialog {
        val alertDialog = AlertDialog.Builder(context).setTitle(title)
            .setMessage(message)
            .setCancelable(true)
            .setPositiveButton(buttonText, onClickListener)
            .create()
        if (TextUtils.isEmpty(title)) {
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        }
        return alertDialog
    }

    fun createAlertDialogWithText(context: Context, title: String, message: String): AlertDialog {
        val alertDialog = AlertDialog.Builder(context).setTitle(title)
            .setMessage(message)
            .setCancelable(true)
            .create()
        if (TextUtils.isEmpty(title)) {
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        }
        alertDialog.show()
        return alertDialog
    }


//    fun showLoadingAlertDialog(context: Context): AlertDialog {
//        val dialogBuilder = AlertDialog.Builder(context)
//        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null)
//        dialogBuilder.setView(dialogView)
//
//        val rotateLoading = dialogView.findViewById<RotateLoading>(R.id.rotateView)
//        rotateLoading.start()
//
//        val alertDialog = dialogBuilder.create()
//        alertDialog.setCancelable(false)
//
//        alertDialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
//        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//
//        return alertDialog
//    }

//    fun getDateDifference(fromDate: String, toDate: String): Int {
//        try {
//            val from = AppConstants.mServiceReturnFormat.parse(fromDate)
//            val to = AppConstants.mServiceReturnFormat.parse(toDate)
//            val diffmili = to.getTime() - from.getTime()
//
//            val days = TimeUnit.MILLISECONDS.toDays(diffmili)
//
//            return days.toInt()
//        } catch (e: ParseException) {
//            e.printStackTrace()
//        }
//
//        return 0
//    }

    fun encodeimage(bitmap: Bitmap): String {
        try {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val b = baos.toByteArray()
            val encodedImage = Base64.encodeToString(b, Base64.DEFAULT)
            return "data:image/jpeg;base64," + encodedImage.replace("\n", "")
        } catch (e: Exception) {
            return ""
        }

    }

    fun stringTruncate(text: String): String {
        var text = text

        if (text.length > 3)
            text = text.substring(0, 3) + ""
        return text.toUpperCase()
    }

    fun getCurrentDayName(): String {
        var name = ""
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("EEE")
        name = dateFormat.format(calendar.timeInMillis)
        return name.toUpperCase()
    }

    /*Dialog method with two button listener*/
    fun messageSingleBtn(
        context: Context,
        message: String,
        positivemsg: String,
        onClickListener: View.OnClickListener
    ) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Message")
        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setPositiveButton(
            positivemsg
        ) { arg0, arg1 -> onClickListener.onClick(null) }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }


    fun getColorCode(context: Context, id: Int): Int {
        val version = Build.VERSION.SDK_INT
        return if (version >= 23) {
            ContextCompat.getColor(context, id)
        } else {
            context.resources.getColor(id)
        }
    }

    fun formattedDate(date: Date?): String? {
        @SuppressLint("SimpleDateFormat") val sdfDate =
            SimpleDateFormat("yyyy-MM-dd")
        return sdfDate.format(date)
    }


    fun datePickerDialog(
        context: Context,
        maxmillisecond: Long,
        minmillisecond: Long,
        clickManager: ClickManager
    ) {


        val c = Calendar.getInstance()
        val mYear = c[Calendar.YEAR]
        val mMonth = c[Calendar.MONTH]
        val mDay = c[Calendar.DAY_OF_MONTH]

        var datePickerDialog = DatePickerDialog(
            context,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                clickManager.onDatePickDialog(year, monthOfYear, dayOfMonth)

            }, mYear, mMonth, mDay
        )
        if (maxmillisecond.toString().equals("0"))
            datePickerDialog.datePicker.minDate = minmillisecond
        else if (minmillisecond.toString().equals("0"))
            datePickerDialog.datePicker.maxDate = maxmillisecond

        datePickerDialog.show()


    }


    fun getDate(date: String): String {
        var simpleDateFormat = SimpleDateFormat("HH:mm")
        var simpleDateFormat1 = SimpleDateFormat("hh:mm a")
        return simpleDateFormat1.format(simpleDateFormat.parse(date))


    }

    fun getDateReverse(minutesandHours: String): String {

        if (minutesandHours.isNullOrEmpty())
            return ""
        var simpleDateFormat = SimpleDateFormat("hh:mm a")
        var simpleDateFormat1 = SimpleDateFormat("HH:mm")
        return simpleDateFormat1.format(simpleDateFormat.parse(minutesandHours))
    }


    fun getDobReverse(dob: String): String {
        var simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
        var simpleDateFormat1 = SimpleDateFormat("yyyy-MM-dd")
        return simpleDateFormat1.format(simpleDateFormat.parse(dob))
    }

    fun getCommentDateTime(dob: String): String {
        var simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        var simpleDateFormat1 = SimpleDateFormat("hh:mm a , MMM dd")
        return simpleDateFormat1.format(simpleDateFormat.parse(dob))
    }

    fun getCommentDateTimes(dob: String): String {
        var simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        var simpleDateFormat1 = SimpleDateFormat("hh:mm a")
        return simpleDateFormat1.format(simpleDateFormat.parse(dob))
    }

    interface ClickManager {
        fun onDatePickDialog(year: Int, monthOfYear: Int, dayofMonth: Int)
    }

    fun convertRequestBody(data: String): RequestBody {
        return data.toRequestBody("text/plain".toMediaTypeOrNull())
    }

}



