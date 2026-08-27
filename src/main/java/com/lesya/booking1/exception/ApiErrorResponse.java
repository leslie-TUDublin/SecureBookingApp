package com.lesya.booking1.exception;

// UA: Стандартна відповідь API при помилці.
public record ApiErrorResponse(String message, int status) {
}
