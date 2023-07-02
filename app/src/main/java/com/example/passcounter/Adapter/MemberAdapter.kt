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
import com.example.passcounter.Activity.MemberViewActivity
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

        holder.mname.text = list[position].mname
        holder.fletter.text = list[position].mname.first().toString()

        holder.itemView.setOnClickListener {
            var i = Intent(context, MemberViewActivity::class.java)
            i.putExtra("mid",list[position].mid)
            i.putExtra("mname",list[position].mname)
//            i.putExtra("cpage",page)
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(i)
        }
    }
}
class MyView1(itemView: View) : RecyclerView.ViewHolder(itemView)
{
    var fletter : TextView =itemView.findViewById(R.id.firstLetter)
    var mname : TextView =itemView.findViewById(R.id.member_name)
}