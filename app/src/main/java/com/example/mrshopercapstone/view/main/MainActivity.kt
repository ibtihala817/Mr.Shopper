package com.example.mrshopercapstone.view.main


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.res.Resources
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate
import com.akexorcist.localizationactivity.core.OnLocaleChangedListener
import com.example.mrshopercapstone.R
import com.example.mrshopercapstone.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity(), OnLocaleChangedListener {

    private val localizationDelegate = LocalizationActivityDelegate(this)
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val CHANNEL_ID = "channel_id_example_01"
    private val notificationId = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
        supportFragmentManager.findFragmentById(R.id.fragmentContainerView6) as NavHostFragment
        navController = navHostFragment.navController
        createNotificationChannel()
        sendNotification()
        setupActionBarWithNavController(navController)
        NavigationUI.setupWithNavController(binding.bottomNavigationView,navController )
       ///// gor language
        localizationDelegate.addOnLocaleChangedListener(this)
        localizationDelegate.onCreate()
       ////////////
      //this is for the share
       requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),0)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
    }
    ////////////////////////////////////////////////////
    // if you want to back to the last fragment from where you come
    // here just user the navigateUp methods of NavController
      override fun onSupportNavigateUp(): Boolean {
          return navController.navigateUp()
      }
    ////////////////////////////////////////////////////
    private fun createNotificationChannel() {
        // Create the NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel: NotificationChannel =
                NotificationChannel(CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }

            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification() {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Don’t Miss Out On")
            .setContentText("Buy One, Get One Free")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())

        }
    }

    override fun onAfterLocaleChanged() {

    }

    override fun onBeforeLocaleChanged() {

    }
    public override fun onResume() {
        super.onResume()
        localizationDelegate.onResume(this)
    }

    override fun attachBaseContext(newBase: Context) {
        applyOverrideConfiguration(localizationDelegate.updateConfigurationLocale(newBase))
        super.attachBaseContext(newBase)
    }

    override fun getApplicationContext(): Context {
        return localizationDelegate.getApplicationContext(super.getApplicationContext())
    }

    override fun getResources(): Resources {
        return localizationDelegate.getResources(super.getResources())
    }

    fun setLanguage(language: String?) {
        localizationDelegate.setLanguage(this, language!!)
    }

    fun setLanguage(locale: Locale?) {
        localizationDelegate.setLanguage(this, locale!!)

    }

    val currentLanguage: Locale
        get() = localizationDelegate.getLanguage(this)

}

