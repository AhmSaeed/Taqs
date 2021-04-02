package com.iti.mad41.taqs.util

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.android.material.snackbar.Snackbar
import com.iti.mad41.taqs.R

/**
 * Transforms static java function Snackbar.make() to an extension function on View.
 */
fun View.showSnackbar(snackbarText: String, timeLength: Int) {
    var snackBar = Snackbar.make(this, snackbarText, timeLength)
    var snackBarView = snackBar.view
    snackBarView.setBackgroundColor(resources.getColor(R.color.blue))
    snackBar.show()
}

fun View.showSnackbarWithButton(snackbarText: String, timeLength: Int, btnListener : View.OnClickListener) {
    var snackBar = Snackbar.make(this, snackbarText, timeLength)
    snackBar.setAction(resources.getString(R.string.snack_bar_btn_Lbl), btnListener)
    var snackBarView = snackBar.view
    snackBarView.setBackgroundColor(resources.getColor(R.color.blue))
    snackBar.show()
}

/**
 * Triggers a snackbar message when the value contained by snackbarTaskMessageLiveEvent is modified.
 */
fun View.setupSnackbar(
        lifecycleOwner: LifecycleOwner,
        snackbarEvent: LiveData<Int>,
        timeLength: Int,
        actionListener: View.OnClickListener? = null
) {

    snackbarEvent.observe(lifecycleOwner, Observer { event ->

        if(actionListener != null)
            showSnackbarWithButton(context.getString(event), timeLength, actionListener)
        else
            showSnackbar(context.getString(event), timeLength)

    })
}