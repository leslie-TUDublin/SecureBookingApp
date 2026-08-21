package com.lesya.booking1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User implements UserDetails { //  implements UserDetails

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // додати унікальність, щоб не було двох користувачів з одним email
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;


    // --- Методи з інтерфейсу UserDetails для Spring Security ---

    //This function will show Spring Security what rights or role this user has.
    //fe: the User-argument is private Role role; and in the database, the user might have: ADMIN
    // The method converts this: ADMIN into: ROLE_ADMIN and returns it to Spring Security.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()//Назва методу Дослівно: отримати повноваження / права
    //GrantedAuthority is a Spring Security interface that represents a single right or authority.
    //<? extends GrantedAuthority> means:
    //The Collection can contain GrantedAuthority objects or objects of classes that implement it.
    //In this case, such a class is SimpleGrantedAuthority
    //In other words, roughly:
    // Collection ->  GrantedAuthority -> SimpleGrantedAuthority getAuthorities
    {
        // Перетворюємо роль (напр. ADMIN) у формат "ROLE_ADMIN", який очікує Spring Security
        // EN: The "ROLE_" prefix is added because Spring Security uses this prefix when working with roles.
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {

        return email;
        // логіном є email
        // EN: Returns the username used by Spring Security to identify the user.
        // the user's email is used as the username.
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return true;// EN: Indicates whether the user's credentials (for example, password) have not expired.
    }

    @Override
    public boolean isEnabled() {

        return true;
        // EN: Indicates whether the user account is enabled and can be used
        // for authentication.
        // UA: Вказує, чи активний обліковий запис користувача і чи може він
        // використовуватися для автентифікації.
    }
}
