package com.lesya.booking1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// UA: Викидається, коли кімната вже заброньована на обрані дати.
@ResponseStatus(HttpStatus.CONFLICT)
public class RoomAlreadyBookedException extends RuntimeException {
    public RoomAlreadyBookedException(String message) {
        super(message);
    }
}
