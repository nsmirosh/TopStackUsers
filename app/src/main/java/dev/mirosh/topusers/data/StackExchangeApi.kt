package dev.mirosh.topusers.data

import dev.mirosh.topusers.data.model.UsersResponseDTO
import retrofit2.http.GET

interface StackExchangeApi {
    @GET("/users?page=1&pagesize=20&order=desc&sort=reputation&site=stackoverflow")
    suspend fun getUsers(): UsersResponseDTO
}

/*

http://api.stackexchange.com/2.2/users?page=1&pagesize=20&order=desc&
sort=reputation&site=stackoverflow
 */