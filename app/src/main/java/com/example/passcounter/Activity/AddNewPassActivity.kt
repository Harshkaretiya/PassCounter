package com.example.passcounter.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.khatabook.API.ApiClient
import com.example.passcounter.API.ApiInterface
import com.example.passcounter.R
import com.example.passcounter.databinding.ActivityAddNewPassBinding
import com.example.passcounter.databinding.ActivityMainBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddNewPassActivity : AppCompatActivity() {

    lateinit var binding : ActivityAddNewPassBinding
    lateinit var apiInterface: ApiInterface
    var selectedDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewPassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiInterface = ApiClient.getapiclient().create(ApiInterface::class.java)

        val calendar = Calendar.getInstance()

        selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
        binding.date.text = formatDateForDisplay(selectedDate)

        binding.date.setOnClickListener {
            showDatePickerDialog()
        }

        binding.addPass.setOnClickListener {

            var passno = binding.passNo.text.toString().toInt()

            var call2: Call<Void> = apiInterface.insertpass(passno,selectedDate)
            call2.enqueue(object: Callback<Void>
            {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (this != null) {
                        if (response.isSuccessful){
                            Toast.makeText(this@AddNewPassActivity, "Done", Toast.LENGTH_SHORT).show()
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