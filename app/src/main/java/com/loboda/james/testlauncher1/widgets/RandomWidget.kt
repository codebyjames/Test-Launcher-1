package com.loboda.james.testlauncher1.widgets

import android.app.Activity
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import com.loboda.james.testlauncher1.MainActivity
import com.loboda.james.testlauncher1.R
import kotlin.random.Random

/**
 * Implementation of App Widget functionality.
 */

const val TAG_ONCLICK_RED = "com.loboda.james.click.red"
//const val TAG_ONCLICK_DUMMY = "com.loboda.james.click.DUMMY"
class RandomWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }

    }

    override fun onAppWidgetOptionsChanged(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        if (context != null && appWidgetManager != null) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        Log.d("Stuff", "action: ${intent?.action}")
        if (TAG_ONCLICK_RED == intent?.action) {
            Log.d("Stuff", "received action: ${intent?.action}")
        }
        else if (intent?.action == AppWidgetManager.ACTION_APPWIDGET_UPDATE) {

            /** sent broadcast from RedBroadcast to here to update widget **/
            val color = intent.getIntExtra("ColorType", Color.WHITE)

            if (context != null) {
                val appWidgetManager = AppWidgetManager.getInstance(context)
                val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(context, RandomWidget::class.java))

                for (appWidgetId in appWidgetIds) {
                    val views = RemoteViews(context?.packageName, R.layout.random_widget)
                    views.setTextColor(R.id.appwidget_text, color)
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
            }

        }
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.random_widget)
    views.setTextViewText(R.id.appwidget_text, "$widgetText : ${(0..1000).random()}")

    // clicks
    views.setOnClickPendingIntent(R.id.buttonChangeText, createOnClickPendingIntentActivity(context))
    views.setOnClickPendingIntent(R.id.buttonChangeColor, createOnClickPendingIntentBroadcast(context, TAG_ONCLICK_RED))

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}

private fun createOnClickPendingIntentBroadcast(context: Context, action: String): PendingIntent {
//    val intent = Intent(context, RandomWidget::class.java)
//    intent.action = action
    val intent = Intent(action)
    return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
}

private fun createOnClickPendingIntentActivity(context: Context): PendingIntent {
    val intent = Intent(context, MainActivity::class.java)
    return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
}

