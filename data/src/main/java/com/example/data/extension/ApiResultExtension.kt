package com.example.data.extension

import com.example.domain.model.ActionResult
import com.example.domain.model.ApiResult

fun ApiResult.Fail.toMessage(): String {
    return when (this) {
        is ApiResult.Fail.Error -> {
            if (this.code == 500) {
                "서버 내부 오류"
            } else {
                this.message ?: "알수 없는 오류가 발생했습니다."
            }
        }

        is ApiResult.Fail.Exception -> {
            this.e.localizedMessage ?: "알수 없는 예외가 발생했습니다."
        }
    }
}

/**
 * ApiResult가 [ApiResult.Success]인 경우 data를 반환하고 이외는 에러 메시지를 반환 합니다.
 */
inline fun <T> ApiResult<T>.covertApiResultToActionResultIfSuccess(
    onSuccess: (data: T) -> Unit,
    onFail: (msg: String) -> Unit
) {
    when (this) {
        is ApiResult.Success -> {
            onSuccess(this.data)
        }

        is ApiResult.Fail -> {
            onFail(this.toMessage())
        }

        else -> {
            onFail("알수 없는 예외")
        }
    }
}

inline fun <T> ApiResult<T>.covertApiResultToActionResultIfSuccess(): ActionResult<T> {
    return when (this) {
        is ApiResult.Success -> {
            ActionResult.Success(this.data)
        }

        is ApiResult.Fail -> {
            ActionResult.Fail(this.toMessage())
        }

        else -> {
            ActionResult.Fail("알수 없는 예외")
        }
    }
}


/**
 * ApiResult가 [ApiResult.SuccessEmpty]인 경우 성공 이외는 에러 메시지를 반환 합니다.
 */
inline fun <T> ApiResult<T>.covertApiResultToActionResultIfSuccessEmpty(
    onSuccess: () -> Unit,
    onFail: (msg: String) -> Unit
) {
    when (this) {
        is ApiResult.SuccessEmpty -> {
            onSuccess()
        }

        is ApiResult.Fail -> {
            onFail(this.toMessage())
        }

        else -> {
            onFail("알수 없는 예외")
        }
    }
}


inline fun <T> ApiResult<T>.covertApiResultToActionResultIfSuccessEmpty(): ActionResult<Any> {
    return when (this) {
        is ApiResult.SuccessEmpty -> {
            ActionResult.Success(Unit)
        }
        is ApiResult.Success -> {
            ActionResult.Success(Unit)
        }

        is ApiResult.Fail -> {
            ActionResult.Fail(this.toMessage())
        }

        else -> {
            ActionResult.Fail("알수 없는 예외")
        }
    }
}