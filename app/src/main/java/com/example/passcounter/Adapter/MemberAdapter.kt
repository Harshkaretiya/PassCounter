package com.example.passcounter.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.passcounter.Model.Model
import com.example.passcounter.R

class MemberAdapter(var context: Context, var list: MutableList<Model>) : RecyclerView.Adapter<MyView1>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView1 {
        var data = LayoutInflater.from(context)
        var view = data.inflate(R.layout.member_design,parent,false)
        return MyView1(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyView1, @SuppressLint("RecyclerView") position: Int) {


    }
}
class MyView1(itemView: View) : RecyclerView.ViewHolder(itemView)
{
    var fletter : TextView =itemView.findViewById(R.id.firstLetter)
    var mname : TextView =itemView.findViewById(R.id.member_name)
}