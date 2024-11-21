package com.moreira.picpaychallenge.domain.entities;

import com.moreira.picpaychallenge.domain.enums.UserType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    //cpf ou cnpj
    private String document;

    @Column(unique = true, nullable = false)
    @Email
    private String email;


    private String password;

    private BigDecimal balance;

    @Column(nullable = false)
    private UserType userType;
}
