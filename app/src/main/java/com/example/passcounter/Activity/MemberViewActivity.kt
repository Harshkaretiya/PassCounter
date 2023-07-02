package com.example.passcounter.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.khatabook.API.ApiClient
import com.example.passcounter.API.ApiInterface
import com.example.passcounter.Adapter.MemberAdapter
import com.example.passcounter.Adapter.MemberViewAdapter
import com.example.passcounter.Model.Model
import com.example.passcounter.databinding.ActivityMemberViewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class MemberViewActivity : AppCompatActivity() {

    lateinit var binding : ActivityMemberViewBinding
    lateinit var apiInterface: ApiInterface
    lateinit var list : MutableList<Model>
    lateinit var list2 : MutableList<Model>
    var selectedOption = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemberViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var manager : RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding!!.recyclerMemberView.layoutManager=manager

        list = ArrayList()
        list2 = ArrayList()

        var i = intent
        var mid= i.getIntExtra("mid",0)


        apiInterface = ApiClient.getapiclient().create(ApiInterface::class.java)

        var call: Call<List<Model>> = apiInterface.viewpass()
        call.enqueue(object : Callback<List<Model>> {
            override fun onResponse(call: Call<List<Model>>, response: Response<List<Model>>) {
                if (this != null) {

                    list2 = response.body() as MutableList<Model>

                    binding!!.passNo.text = list2[0].passno.toString()

                    var call: Call<List<Model>> = apiInterface.viewentrybymidpassno(mid,list2[0].pid)
                    call.enqueue(object: Callback<List<Model>>
                    {
                        override fun onResponse(call: Call<List<Model>>, response: Response<List<Model>>) {
                            if (this != null) {
                                list = response.body() as MutableList<Model>

                                var adapter2 = MemberViewAdapter(this@MemberViewActivity, list)
                                binding!!.recyclerMemberView.adapter = adapter2

                                var totalPass = list.size
                                var totalRupees = totalPass*70

                                binding.totalPass.text = totalPass.toString()
                                binding.totalRupees.text = totalRupees.toString()
                            }
                        }
                        override fun onFailure(call: Call<List<Model>>, t: Throwable) {
                            Toast.makeText(this@MemberViewActivity, "No Internet", Toast.LENGTH_LONG).show()
                        }
                    })
                }
            }
            override fun onFailure(call: Call<List<Model>>, t: Throwable) {
                Toast.makeText(this@MemberViewActivity, "No Internet in months", Toast.LENGTH_SHORT).show()
            }
        })

        binding.passNo.setOnClickListener {
            var call: Call<List<Model>> = apiInterface.viewpass()
            call.enqueue(object : Callback<List<Model>> {
                override fun onResponse(call: Call<List<Model>>, response: Response<List<Model>>) {
                    if (this != null) {

                        list2 = response.body() as MutableList<Model>

                        val passNumber: MutableList<String> = mutableListOf()

                        for (pass in list2)
                        {
                            passNumber.add(pass.passno.toString())
                        }

                        val builder = MaterialAlertDialogBuilder(this@MemberViewActivity)
                        builder.setTitle("Choose an Option")
                        val optionNames = passNumber.toTypedArray() // Replace "name" with the appropriate property of your Model object
                        builder.setSingleChoiceItems(optionNames, selectedOption) { dialog, which ->
                            selectedOption = which // Update the selected option when a radio button is clicked
                        }
                        builder.setPositiveButton("OK") { dialog, _ ->

                            var call: Call<List<Model>> = apiInterface.viewentrybymidpassno(mid,list2[selectedOption].pid)
                            call.enqueue(object: Callback<List<Model>>
                            {
                                override fun onResponse(call: Call<List<Model>>, response: Response<List<Model>>) {
                                    if (this != null) {
                                        list = response.body() as MutableList<Model>

                                        var adapter2 = MemberViewAdapter(this@MemberViewActivity, list)
                                        binding!!.recyclerMemberView.adapter = adapter2

                                        var totalPass = list.size
                                        var totalRupees = totalPass*70

                                        binding.totalPass.text = totalPass.toString()
                                        binding.totalRupees.text = totalRupees.toString()

                                    }
                                }
                                override fun onFailure(call: Call<List<Model>>, t: Throwable) {
                                    Toast.makeText(this@MemberViewActivity, "No Internet", Toast.LENGTH_LONG).show()
                                }
                            })


                            // Access the selected option using the `selectedOption` variable
                        }
                        builder.setNegativeButton("Cancel") { dialog, _ ->
                            // Cancel operation
                            dialog.dismiss()
                        }

                        // ...
                        val radioDialog = builder.create()
                        radioDialog.show()


                    }
                }
                override fun onFailure(call: Call<List<Model>>, t: Throwable) {
                    Toast.makeText(this@MemberViewActivity, "No Internet in months", Toast.LENGTH_SHORT).show()
                }
            })
        }



    }
}