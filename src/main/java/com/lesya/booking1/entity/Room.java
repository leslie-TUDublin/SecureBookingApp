package com.lesya.booking1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal; // Імпортуємо BigDecimal для точної роботи з грошима

@Entity
// EN: This class represents a database entity for hotel rooms.
// UA: Цей клас є сутністю (Entity), яка відображає таблицю номерів готелю в базі даних.
@Table(name = "rooms")
// EN: Map this entity to the "rooms" database table.
// UA: Прив'язати цю сутність до таблиці "rooms" у базі даних.
@Getter
@Setter
// EN: Lombok automatically generates getters, setters for all fields.
// UA: Lombok автоматично генерує гетери, сетери для всіх полів.
@NoArgsConstructor
// EN: JPA requires a no-argument constructor to create instances from the database.
// UA: JPA обов'язково потребує конструктора без параметрів для створення об'єктів з БД.
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    // EN: PostgreSQL automatically generates room ID (auto-increment PRIMARY KEY).
    // UA: PostgreSQL автоматично генерує ID номера (автоінкрементний первинний ключ).
    private Long id;

    @Column(nullable = false, unique = true)

    // EN: Unique room number (e.g., "101", "202"). Cannot be null or duplicated.
    // UA: Унікальний номер кімнати (наприклад, "101", "202"). Не може бути порожнім чи повторюватися.
    private String number;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)

    // EN: Room type is stored as text in the database using the RoomType enum. (fe - STANDARD, DELUXE, SUITE)
    // UA: Тип номера зберігається в базі даних як текстове значення enum RoomType (Напр: STANDARD, DELUXE, SUITE).
    private RoomType type;

    @Column(nullable = false)

    // EN: Price per night using BigDecimal to prevent rounding errors with money.
    // UA: Ціна за одну ніч. Використовуємо BigDecimal для максимальної точності розрахунків без похибок.
    private BigDecimal price;

    @Column(nullable = false)

    // EN: Maximum number of guests allowed in this room. Cannot be null.
    // UA: Максимальна кількість гостей у номері. Не може бути порожнім.
    private Integer capacity;

    @Column(nullable = false)

    // EN: Shows whether the room is currently available for booking. Defaults to true.
    // UA: Показує, чи доступний номер для бронювання. За замовчуванням — true (доступний).
    private Boolean available = true;
}