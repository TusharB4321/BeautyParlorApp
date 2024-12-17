package com.example.beautyparlorapp.data

data class ServiceModel(
    var documentId:String?="",
    val userId: String? = "",
    val serviceName:String?="",
    val serviceCategory:String?="",
    val serviceDescription:String?="",
    val serviceDuration:String?="",
    val servicePrice:String?=""
)