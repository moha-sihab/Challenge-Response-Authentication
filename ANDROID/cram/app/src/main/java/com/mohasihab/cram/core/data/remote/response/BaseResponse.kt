package com.mohasihab.cram.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
	@SerializedName("data")
	val data: T? = null,
	@SerializedName("success")
	val success: Boolean? = null,
	@SerializedName("message")
	val message: String? = null,
	@SerializedName("errors")
	val errors: String? = null
){
	companion object {
		fun <T> failed(error: String?): BaseResponse<T> {
			return BaseResponse(
				success = false,
				message = error,
				errors = error
			)
		}
	}
}
