package alexrnov.worldtime

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar

/**
 * View snackbar
 * [view] - root view;
 * [message] - message text.
 */
fun showSnackBar(view: View, message: CharSequence) {
    val snackbar = Snackbar.make(view, message, 2000)
    snackbar.setAction("OK") { snackbar.dismiss() } // when you click on the button, the snackbar just hides
    snackbar.setActionTextColor(Color.parseColor("#95d7ff")) // button color
    val snackbarView = snackbar.view
    snackbarView.setBackgroundColor(Color.parseColor("#656565")) // background color
    snackbarView.setPadding(0, 0, 0, 0)
    // set the width of the snackbar to the screen - this is necessary, since on the tablet the snackbar by default occupies only part of the screen
    //val params = snackbarView.layoutParams as CoordinatorLayout.LayoutParams
    val params = snackbarView.layoutParams as FrameLayout.LayoutParams
    // another option is to expand the snackbar
    //snackbarView.getLayoutParams().width = AppBarLayout.LayoutParams.MATCH_PARENT;
    params.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
    params.width = FrameLayout.LayoutParams.MATCH_PARENT
    snackbarView.layoutParams = params

    // invoke for android support library
    //val textView = snackbarView.findViewById<TextView>(android.support.design.R.id.snackbar_text)
    // invoke for library androidx
    val textView = snackbarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
    textView.setTextColor(Color.parseColor("#95d7ff")) // message color
    snackbar.show()
}