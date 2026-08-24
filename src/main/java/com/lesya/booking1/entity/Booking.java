package com.lesya.booking1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

// EN: This class represents a hotel room booking.
//UA: Цей клас є сутністю (Entity), яка представляє бронювання номера в готелі.

// EN: Each booking connects one user with one room for a specific period of time.
// UA: Кожне бронювання пов'язує одного користувача з одним номером на певний період часу.
@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
public class Booking {
    // EN: Unique identifier of the booking. PostgreSQL automatically generates this value.
    //UA: Унікальний ідентифікатор бронювання. PostgreSQL автоматично генерує це значення.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // EN: User who made the booking. Many bookings can belong to one user.
    //UA: Користувач, який зробив бронювання. Один користувач може мати багато бронювань.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //EN: Room that was booked. One room can have many bookings at different periods of time.
    //UA: Номер, який було заброньовано. Один номер може мати багато бронювань у різні періоди часу.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    // EN: Date when the guest checks in. UA: Дата заселення гостя.
    @Column(nullable = false)
    private LocalDate checkIn;

    // EN: Date when the guest checks out. UA: Дата виселення гостя.
    @Column(nullable = false)
    private LocalDate checkOut;


    // EN: Total price of the booking. BigDecimal is used because this value represents money.
    //UA: Загальна вартість бронювання. BigDecimal використовується тому, що це грошове значення.
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalPrice;

    //EN: Current status of the booking. UA: Поточний статус бронювання.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;
}