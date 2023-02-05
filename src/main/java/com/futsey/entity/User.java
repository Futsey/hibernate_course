package com.futsey.entity;

import com.futsey.convereter.BirthdayConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users", schema = "public")
public class User {

    @Id
    private String username;
    private String firstname;
    private String lastname;

    /**
     * Инициализация конвертера ВАРИАНТ 1
     * @Convert(converter = BirthdayConverter.class)
     */
    @Column(name ="birthdate")
    private Birthday birthDate;

    @Enumerated(EnumType.STRING)
    private Role role;
}
