package com.lesya.booking1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// UA: Викидається, коли дата заїзду більша або дорівнює даті виїзду.
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidBookingDatesException extends RuntimeException {
    public InvalidBookingDatesException(String message) {
        super(message);
    }
}
