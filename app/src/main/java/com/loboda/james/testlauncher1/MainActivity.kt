package com.loboda.james.testlauncher1

import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.loboda.james.testlauncher1.adapters.AppDrawerAdapter
import com.loboda.james.testlauncher1.broadcasts.ColorBroadcast
import com.loboda.james.testlauncher1.databinding.ActivityMainBinding
import com.loboda.james.testlauncher1.models.PackageItem
import com.loboda.james.testlauncher1.widgets.ACTION_ONCLICK_COLOR
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

/**
 * @author James Loboda aka papayev
 * created by James Loboda aka papayev at 4/27/22
 * www.papayev.com
 */


const val CHROME_PACKAGE = "com.android.chrome"

// this is just an app of mine
const val IDEA_PACKAGE = "com.loboda.james.ideageneratoreye"
class MainActivity : AppCompatActivity() {

    /**
     * Note: In order to receive implicit intents, you must include the CATEGORY_DEFAULT category
     * in the intent filter [AndroidManifest]. The methods startActivity() and startActivityForResult() treat all
     * intents as if they declared the CATEGORY_DEFAULT category. If you do not declare it in your
     * intent filter, no implicit intents will resolve to your activity.
     */

    private lateinit var binding: ActivityMainBinding
    private lateinit var appDrawerAdapter: AppDrawerAdapter
    private lateinit var broadcast: ColorBroadcast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // test widget broadcast
        setWidgetBroadcastOnClick()

        /** IF WANT TO LAUNCH AN APP BY DEFAULT ON CREATE **/
        // launch default app
//        launchDefaultApp()

        // set app drawer
        appDrawerAdapter = AppDrawerAdapter { packageItem ->
            // onclick event
            startActivity(packageManager.getLaunchIntentForPackage(packageItem.packageName))
        }

        // set adapter list
        lifecycleScope.launch() {
            appDrawerAdapter.submitList(appDrawerList())
        }

        binding.apply {

            // set chrome icon
            chromeIcon.setImageDrawable(getChromeIcon())

            // set chrome button label
            buttonChrome.text = getChromeLabel()

            // launch Google Chrome
            buttonChrome.setOnClickListener {
                launchChrome()
            }

            // launch Settings
            buttonSettings.setOnClickListener {
                launchSettings()
            }

            // launch pick launcher
            buttonPickLauncher.setOnClickListener {
                launchPickLauncherSettings()
            }

            // launch pick wallpaper
            buttonPickWallpaper.setOnClickListener {
                launchPickWallpaper()
            }

            // set recycler
            appDrawer.adapter = appDrawerAdapter

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcast)
    }

    /**
     * Use this broadcast with pending broadcast in widget [RandomWidget]
     */
    private fun setWidgetBroadcastOnClick() {
        val broadcast = ColorBroadcast()
        IntentFilter(ACTION_ONCLICK_COLOR).also {
            registerReceiver(broadcast, it) }
    }

    private fun launchSettings() {
        // Android version > Lollipop
        val settingsIntent = Intent(Settings.ACTION_SETTINGS)
        startActivity(settingsIntent)
    }

    private fun launchPickLauncherSettings() {
        val settingsIntent = Intent(Settings.ACTION_HOME_SETTINGS)
        startActivity(settingsIntent)
    }

    private fun launchPickWallpaper() {
        val widgetsIntent = Intent(Intent.ACTION_SET_WALLPAPER)
        startActivity(widgetsIntent)
    }

    /**
     * Get all apps & create a list in background
     */
    private suspend fun appDrawerList(): List<PackageItem> {
        return withContext(coroutineContext + Dispatchers.IO) {
            getAllApps()
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onBackPressed() {
        if (onBackPressedDispatcher.hasEnabledCallbacks()) {
            super.onBackPressed()
        } else {
            // do nothing
        }
    }

    /**
     * Get All Installed Apps
     * @return list of Package Items
     */
    private fun getAllApps(): List<PackageItem> {
        var list = mutableListOf<PackageItem>()
        packageManager.getInstalledApplications(PackageManager.GET_META_DATA).forEach { appInfo ->

            if (appInfo.flags and ApplicationInfo.FLAG_SYSTEM === ApplicationInfo.FLAG_SYSTEM) {
                Log.d("Stuff", "system app")
            } else {
                Log.d("Stuff", "non system app")
                list.add(PackageItem(appInfo.packageName, appInfo.icon,
                    appInfo.loadIcon(packageManager), appInfo.uid,
                    appInfo.loadLabel(packageManager).toString()))
            }
        }

        return list
    }

    /**
     * Get Chrome info
     */
    private fun getChromeIcon(): Drawable {
        return getInfoFromPackage(CHROME_PACKAGE).loadIcon(packageManager)
    }

    private fun getChromeLabel(): String {
        return getInfoFromPackage(CHROME_PACKAGE).loadLabel(packageManager).toString()
    }

    /**
     * Get Application info about a package
     * @param packageName name of package
     */
    private fun getInfoFromPackage(packageName: String): ApplicationInfo {
        return packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
    }

    /**
     * Launch the default app: idea generator
     */
    private fun launchDefaultApp() {
        // chrome default
        val launchIntent = packageManager.getLaunchIntentForPackage(IDEA_PACKAGE)
        if (launchIntent != null) {
            startActivity(launchIntent)
        }
    }

    /**
     * Launch com.android.chrome Intent from package
     */
    private fun launchChrome() {
        // chrome
        val launchIntent = packageManager.getLaunchIntentForPackage(CHROME_PACKAGE)
        if (launchIntent != null) {
            startActivity(launchIntent)
        }
    }
}