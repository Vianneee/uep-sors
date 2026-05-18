package com.uep.sors.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(unique = true, nullable = false)
    private String studentId; // 6-digit student ID

    private String password; // hashed birthday

    @Enumerated(EnumType.STRING)
    private Role role;
}