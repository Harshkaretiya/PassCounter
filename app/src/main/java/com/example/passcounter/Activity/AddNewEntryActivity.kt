package com.example.passcounter.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.example.khatabook.API.ApiClient
import com.example.passcounter.API.ApiInterface
import com.example.passcounter.Adapter.MemberAdapter
import com.example.passcounter.Adapter.MemberViewAdapter
import com.example.passcounter.Model.Model
import com.example.passcounter.R
import com.example.passcounter.databinding.ActivityAddNewEntryBinding
import com.example.passcounter.databinding.ActivityAddNewPassBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddNewEntryActivity : AppCompatActivity() {


    lateinit var binding : ActivityAddNewEntryBinding
    lateinit var apiInterface: ApiInterface
    lateinit var list : MutableList<Model>
    lateinit var list2 : MutableList<Model>
    var selectedOption = 0
    var pid = 0
    var mid = 0
    var selectedDate = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        list = ArrayList()
        list2 = ArrayList()

        val calendar = Calendar.getInstance()

        selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
        binding.date.text = formatDateForDisplay(selectedDate)

        binding.date.setOnClickListener {
            showDatePickerDialog()
        }

        apiInterface = ApiClient.getapiclient().create(ApiInterface::class.java)

        var call: Call<List<Model>> = apiInterface.viewrempass()
        call.enqueue(object : Callback<List<Model>> {
            override fun onResponse(call: Call<List<Model>>, response: Response<List<Model>>) {
                if (this != null) {

                    list = response.body() as MutableList<Model>

                    binding!!.passNo.text = list[0].passno.toString()
                    pid = list[0].pid

                }
            }

            override fun onFailure(call: Call<List<Model>>, t: Throwable) {
                Toast.makeText(
                    this@AddNewEntryActivity,
                    "No Internet in months",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        binding.passNo.setOnClickListener {
            var call: Call<List<Model>> = apiInterface.viewrempass()
            call.enqueue(object : Callback<List<Model>> {
                override fun onResponse(call: Call<List<Model>>, response: Response<List<Model>>) {
                    if (this != null) {

                        list = response.body() as MutableList<Model>

                        val passNumber: MutableList<String> = mutableListOf()

                        for (pass in list) {
                            passNumber.add(pass.passno.toString())
                        }

                        val builder = MaterialAlertDialogBuilder(this@AddNewEntryActivity)
                        builder.setTitle("Choose an Pass No")
                        val optionNames = passNumber.toTypedArray()

                        builder.setSingleChoiceItems(optionNames, selectedOption) { dialog, which ->
                            selectedOption =
                                which // Update the selected option when a radio button is clicked
                        }
                        builder.setPositiveButton("OK") { dialog, _ ->
                            pid = list[selectedOption].pid

                        }
                        builder.setNegativeButton("Cancel") { dialog, _ ->
                            dialog.dismiss()
                        }
                        val radioDialog = builder.create()
                        radioDialog.show()


                    }
                }

                override fun onFailure(call: Call<List<Model>>, t: Throwable) {
                    Toast.makeText(
                        this@AddNewEntryActivity,
                        "No Internet in months",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
        binding.selectName.setOnClickListener {
            var call: Call<List<Model>> = apiInterface.viewmember()
            call.enqueue(object: Callback<List<Model>>
            {
                override fun onResponse(call: Call<List<Model>>, response: Response<List<Model>>) {
                    if (this != null) {
                        list2 = response.body() as MutableList<Model>
                        val passNumber: MutableList<String> = mutableListOf()

                        for (member in list2) {
                            passNumber.add(member.mname.toString())
                        }

                        val builder = MaterialAlertDialogBuilder(this@AddNewEntryActivity)
                        builder.setTitle("Choose an Pass No")
                        val optionNames = passNumber.toTypedArray()

                        builder.setSingleChoiceItems(optionNames, selectedOption) { dialog, which ->
                            selectedOption =
                                which // Update the selected option when a radio button is clicked
                        }
                        builder.setPositiveButton("OK") { dialog, _ ->
                            mid = list2[selectedOption].mid
                            binding.selectName.text = list2[selectedOption].mname

                        }
                        builder.setNegativeButton("Cancel") { dialog, _ ->
                            dialog.dismiss()
                        }
                        val radioDialog = builder.create()
                        radioDialog.show()
                    }
                }
                override fun onFailure(call: Call<List<Model>>, t: Throwable) {
                    Toast.makeText(this@AddNewEntryActivity, "No Internet", Toast.LENGTH_LONG).show()
                }
            })
        }
        binding.insertPass.setOnClickListener {

            var call2: Call<Void> = apiInterface.insertentry(mid,pid,selectedDate)
            call2.enqueue(object: Callback<Void>
            {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (this != null) {
                        if (response.isSuccessful){
                            Toast.makeText(this@AddNewEntryActivity, "Done", Toast.LENGTH_SHORT).show()
                            onBackPressed()
//                            Toast.makeText(this@NewEntryActivity, "done", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(applicationContext,"Fail", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
    private fun showDatePickerDialog() {

        val calendar = Calendar.getInstance()
        val today = calendar.timeInMillis
        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointBackward.before(today)).build()

        val builder = MaterialDatePicker.Builder.datePicker().setCalendarConstraints(constraintsBuilder)
        val datePicker = builder.build()

        datePicker.addOnPositiveButtonClickListener { timestamp ->
            selectedDate = formatDateForStorage(timestamp)
            val displayDate = formatDateForDisplay(selectedDate)
            binding.date.text = displayDate
        }

        datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
    }

    private fun formatDateForDisplay(date: String): String {
        val displayFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val parsedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date)
        return displayFormat.format(parsedDate)
    }

    private fun formatDateForStorage(timestamp: Long): String {
        val storageFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        return storageFormat.format(calendar.time)
    }
}