@file:Suppress("DEPRECATION")

package com.example.pos.Toast
import android.app.Activity
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import com.example.pos.R

fun Toast.showCustomToast(activity: Activity)
{
    val layout = activity.layoutInflater.inflate (
        R.layout.custom_toast,
        activity.findViewById(R.id.toast_container)
    )

    // use the application extension function
    this.apply {
        setGravity(Gravity.CENTER, 0, 0)
        duration = Toast.LENGTH_LONG
        view = layout
        show()
    }
}