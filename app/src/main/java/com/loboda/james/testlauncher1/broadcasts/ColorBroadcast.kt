package com.loboda.james.testlauncher1.broadcasts

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log

/**
 * @author James Loboda aka papayev
 * created by James Loboda aka papayev at 4/27/22
 * www.papayev.com
 */


/**
 * @author James Loboda aka papayev
 * created by James Loboda aka papayev at 4/29/22
 * www.papayev.com
 */

/**
 * Use this broadcast to test pending broadcast in widget
 */
class ColorBroadcast: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("Stuff", "received action in Color Broadcast")

        // send broadcast to widget
//        val intent = Intent(p0, RandomWidget::class.java)
        Intent().also {
            intent?.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            intent?.putExtra("ColorType", randomColor())
            context?.sendBroadcast(intent)
        }

    }

    private fun randomColor(): Int {
        return listOf<Int>(Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN).random()
    }

}