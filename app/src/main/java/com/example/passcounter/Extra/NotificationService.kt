package com.example.passcounter.Extra

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.khatabook.API.ApiClient
import com.example.passcounter.API.ApiInterface
import com.example.passcounter.Model.Model
import com.example.passcounter.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class NotificationService : Service() {

    private val mHandler: Handler = Handler()
    lateinit var apiInterface: ApiInterface
    private lateinit var mRunnable: Runnable
    private val INTERVAL_MINUTES = 20
    private val START_HOUR = 14 // 2 PM
    private val END_HOUR = 17 // 5 PM
    private val START_HOUR_N = 21 // 2 PM
    private val END_HOUR_N = 24 // 5 PM
    lateinit var list : MutableList<Model>


    override fun onCreate()
    {
        super.onCreate()
        createNotificationChannel()
//        Toast.makeText(applicationContext,"Created",Toast.LENGTH_LONG).show()
        mRunnable = Runnable {
            val calendar = Calendar.getInstance()
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)

            if (currentHour in START_HOUR..END_HOUR) {
                checkData()
            }
            if (currentHour in START_HOUR_N..END_HOUR_N) {
                checkDataN()
            }

            mHandler.postDelayed(mRunnable, 60 * 1000 * 30) // Check every 10 seconds
        }
    }

    override fun onStart(intent: Intent?, startId: Int)
    {
//        Toast.makeText(applicationContext,"Start",Toast.LENGTH_LONG).show()
        super.onStart(intent, startId)
    }

    private fun checkData() {
        apiInterface = ApiClient.getapiclient().create(ApiInterface::class.java)

        list = ArrayList()

        val call : Call<List<Model>> = apiInterface.getEntryCount()
        call.enqueue(object : Callback<List<Model>> {
            override fun onResponse(call: Call<List<Model>>, response: Response<List<Model>>) {
//                if (response.isSuccessful) {
                if (this != null) {
                    list = response.body() as MutableList<Model>

                    if (list.size == 0)
                        showNotification()
                    else {
                        val sharedPreferences = getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putInt("Morning", list.size)
                        editor.apply()
                    }

//                }
                }
            }

            override fun onFailure(call: Call<List<Model>>, t: Throwable) {
                // Handle network failure or other errors
            }
        })
    }
    private fun checkDataN() {
        apiInterface = ApiClient.getapiclient().create(ApiInterface::class.java)

        list = ArrayList()

        val call : Call<List<Model>> = apiInterface.getEntryCount()
        call.enqueue(object : Callback<List<Model>> {
            override fun onResponse(call: Call<List<Model>>, response: Response<List<Model>>) {
//                if (response.isSuccessful) {
                if (this != null) {
                    list = response.body() as MutableList<Model>

                    val sharedPreferences = getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)
                    val listSize = sharedPreferences.getInt("Morning", 99)

                    if (list.size-listSize == 0)
                        showNotification()
                    else {
                        val editor = sharedPreferences.edit()
                        editor.putInt("Morning", 0)
                        editor.apply()
                    }

//                }
                }
            }

            override fun onFailure(call: Call<List<Model>>, t: Throwable) {
                // Handle network failure or other errors
            }
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int
    {
//        Toast.makeText(applicationContext,"Start Command",Toast.LENGTH_LONG).show()
        mHandler.postDelayed(mRunnable, 0)
        return START_STICKY
    }
    override fun onBind(intent: Intent): IBinder
    {
        TODO()
    }

    override fun onDestroy()
    {
        Toast.makeText(applicationContext,"Destroy",Toast.LENGTH_LONG).show()
        super.onDestroy()
        mHandler.removeCallbacks(mRunnable)
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "notification_channel"
            val channelName = "Notification Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(channelId, channelName, importance)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun showNotification() {
        val channelId = "notification_channel"
        val notificationId = 123

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("New Entry")
            .setContentText("Make a new entry of today's pass")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        startForeground(notificationId, notificationBuilder.build())
    }
}