package com.example.eydstest.data.model

sealed class Resource {
    class Success(val data: Any?) : Resource()
    class Error(val errMessage: String?) : Resource()
}