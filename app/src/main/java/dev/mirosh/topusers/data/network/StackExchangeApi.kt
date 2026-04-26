package dev.mirosh.topusers.data.network

import dev.mirosh.topusers.data.model.UsersResponseDto
import okhttp3.ResponseBody
import retrofit2.http.GET

interface StackExchangeApi {
    //Not extracting the params here - since we're only doing this request
    @GET("/users?page=1&pagesize=20&order=desc&sort=reputation&site=stackoverflow")
    suspend fun getUsers(): UsersResponseDto
}
/*

http://api.stackexchange.com/2.2/users?page=1&pagesize=20&order=desc&
sort=reputation&site=stackoverflow
 */