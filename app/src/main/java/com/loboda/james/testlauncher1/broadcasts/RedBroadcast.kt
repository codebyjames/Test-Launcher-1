package com.loboda.james.testlauncher1.broadcasts

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import com.loboda.james.testlauncher1.widgets.RandomWidget


/**
 * @author James Loboda aka papayev
 * created by James Loboda aka papayev at 4/29/22
 * www.papayev.com
 */

/**
 * Use this broadcast to test pending broadcast in widget
 */
class RedBroadcast: BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        Log.d("Stuff", "received action in red broadcast")

        // send broadcast to widget
//        val intent = Intent(p0, RandomWidget::class.java)
        val intent = Intent()
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        intent.putExtra("ColorType", randomColor())
        p0?.sendBroadcast(intent)
    }

    private fun randomColor(): Int {
        return listOf<Int>(Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN).random()
    }

}