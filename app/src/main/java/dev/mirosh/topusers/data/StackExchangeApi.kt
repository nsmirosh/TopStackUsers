package dev.mirosh.topusers.data

import okhttp3.ResponseBody
import retrofit2.http.GET

interface StackExchangeApi {
    @GET("/users?page=1&pagesize=20&order=desc&sort=reputation&site=stackoverflow")
    suspend fun getUsers(): ResponseBody
}

/*

http://api.stackexchange.com/2.2/users?page=1&pagesize=20&order=desc&
sort=reputation&site=stackoverflow
 */