package com.kotlin.mobile.reciclajeapp.services

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call

// Data class para el cuerpo de la solicitud
data class LoginRequest(
    val email: String,
    val pin: String
)

// Data class para la respuesta del servidor
data class LoginResponse(
    val id: String,
    val name: String,
    val token: String,
    val state: String
)

// Interfaz de Retrofit para el servicio de login
interface LoginService {

    @POST("appVentas/api/login.php")
    suspend fun login(@Body request: LoginRequest): LoginResponse

}

object RetrofitServicesFactory {

        //private const val BASE_URL = "http://localhost"
        private const val BASE_URL = "https://appventasglobal.com/"


        fun create(): LoginService {
            val retrofit = retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .build()
            return retrofit.create(LoginService::class.java)
        }
}
