package com.lesya.booking1.entity;

// These roles will be stored in the database as strings because User.java uses @Enumerated(EnumType.STRING).
// Ці ролі будуть зберігатися в базі даних як текст,оскільки в User.java використовується @Enumerated(EnumType.STRING).
public enum Role {

    // EN: Administrator - full access.

    ADMIN,

    // EN: Regular user who can browse rooms and create bookings.

    USER
}
