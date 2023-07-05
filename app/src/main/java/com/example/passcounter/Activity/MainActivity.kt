package com.example.passcounter.Activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.khatabook.API.ApiClient
import com.example.passcounter.API.ApiInterface
import com.example.passcounter.Adapter.MemberAdapter
import com.example.passcounter.Extra.NotificationService
import com.example.passcounter.Model.Model
import com.example.passcounter.R
import com.example.passcounter.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    lateinit var apiInterface: ApiInterface
    lateinit var list : MutableList<Model>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        list = ArrayList()

        binding.addNewEntry.setOnClickListener {

            startActivity(Intent(this,AddNewEntryActivity::class.java))
        }
        binding.addNewPass.setOnClickListener {

            startActivity(Intent(this,AddNewPassActivity::class.java))
        }
        binding.settings.setOnClickListener {
            startActivity(Intent(this,SettingActivity::class.java))
        }
        ProgressBarUtils.showProgressBar(this)

        var manager : RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding!!.recyclerMember.layoutManager=manager

        apiInterface = ApiClient.getapiclient().create(ApiInterface::class.java)
        var call: Call<List<Model>> = apiInterface.viewmember()
        call.enqueue(object: Callback<List<Model>>
        {
            override fun onResponse(call: Call<List<Model>>, response: Response<List<Model>>) {
//                customProgressBar(this@MainActivity,false)
                if (this != null) {
                    list = response.body() as MutableList<Model>

                    var adapter2 = MemberAdapter(this@MainActivity, list)
                    binding!!.recyclerMember.adapter = adapter2

                    ProgressBarUtils.hideProgressBar()
                }
            }
            override fun onFailure(call: Call<List<Model>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "No Internet", Toast.LENGTH_LONG).show()
            }
        })
    }

}
object ProgressBarUtils {
    private var progressDialog: Dialog? = null

    fun showProgressBar(context: Context) {
        progressDialog = Dialog(context)
        progressDialog?.setContentView(R.layout.custom_progress_dialog)
        progressDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        progressDialog?.show()
    }

    fun hideProgressBar() {
        progressDialog?.dismiss()
        progressDialog = null
    }
}