package com.example.passcounter.API

import com.example.passcounter.Model.Model
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {

    @GET("viewmember.php")
    fun viewmember(): Call<List<Model>>

    @GET("viewpass.php")
    fun viewpass(): Call<List<Model>>

    @GET("viewrempass.php")
    fun viewrempass(): Call<List<Model>>


    @GET("viewentrybymidpassno.php")
    fun viewentrybymidpassno(
        @Query("mid") member_id:Int,
        @Query("pid") pid:Int,
    ): Call<List<Model>>

    @FormUrlEncoded
    @POST("insertentry.php")
    fun insertentry(
        @Field("mid") member_id:Int,
        @Field("pid") pass_id:Int,
        @Field("date") date:String,
    ): Call<Void>

    @FormUrlEncoded
    @POST("insertpass.php")
    fun insertpass(
        @Field("passno") pass_no:Int,
        @Field("date") date:String,
    ): Call<Void>

}