package com.example.mystoryapps.data.retrofit

import com.example.mystoryapps.data.response.*
import okhttp3.*
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): GeneralResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @Multipart
    @POST("stories")
    suspend fun addNewStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): GeneralResponse

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") token: String
    ): GetAllStoryResponse

    @GET("stories/{id}")
    suspend fun getDetailStories (
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): DetailStoryResponse
}