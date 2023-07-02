package com.example.passcounter.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.passcounter.Activity.MemberViewActivity
import com.example.passcounter.Model.Model
import com.example.passcounter.R

class MemberViewAdapter(var context: Context, var list: MutableList<Model>) : RecyclerView.Adapter<MyView2>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView2 {
        var data = LayoutInflater.from(context)
        var view = data.inflate(R.layout.member_view_design,parent,false)
        return MyView2(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyView2, @SuppressLint("RecyclerView") position: Int) {

        holder.srno.text = (position+1).toString()
        holder.date.text = list[position].date.toString()

    }
}
class MyView2(itemView: View) : RecyclerView.ViewHolder(itemView)
{
    var srno : TextView =itemView.findViewById(R.id.srno)
    var date : TextView =itemView.findViewById(R.id.date)
}