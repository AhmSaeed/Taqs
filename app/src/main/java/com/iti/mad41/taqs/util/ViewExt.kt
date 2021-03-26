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

/**
 * Triggers a snackbar message when the value contained by snackbarTaskMessageLiveEvent is modified.
 */
fun View.setupSnackbar(
    lifecycleOwner: LifecycleOwner,
    snackbarEvent: LiveData<Int>,
    timeLength: Int
) {

    snackbarEvent.observe(lifecycleOwner, Observer { event ->

        showSnackbar(context.getString(event), timeLength)

    })
}