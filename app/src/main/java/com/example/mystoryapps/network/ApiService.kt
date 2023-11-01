package com.example.mystoryapps.network

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
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody?,
        @Part("lon") lon: RequestBody?
    ): GeneralResponse

    @GET("stories")
    suspend fun getAllStories(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): GetAllStoryResponse

    @GET("stories")
    suspend fun getAllStoriesWithLocation(
        @Query("location") location : Int = 1
    ): GetAllStoryResponse

    @GET("stories/{id}")
    suspend fun getDetailStories (
        @Path("id") id: String
    ): DetailStoryResponse
}