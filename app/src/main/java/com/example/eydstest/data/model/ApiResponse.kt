package com.example.eydstest.data.model

import com.example.eydstest.presentation.util.Constant

data class ApiResponse(
    val data: List<GIFObject>? = null,
    val meta: MetaData? = null,
    val pagination: Pagination? = null
) {

    fun isSuccessful(): Boolean {
        return meta?.status == Constant.HTTP_STATUS_SUCCESS
    }
}