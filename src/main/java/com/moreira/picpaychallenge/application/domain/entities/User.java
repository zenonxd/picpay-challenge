package com.moreira.picpaychallenge.application.domain.entities;

import com.moreira.picpaychallenge.application.domain.enums.UserType;
import jakarta.persistence.*;
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
    private String fullName;

    @Column(unique = true, nullable = false)
    //cpf ou cnpj
    private String identificationNumber;

    @Column(unique = true, nullable = false)
    private String email;
    private String password;

    private BigDecimal balance;

    private UserType userType;
}
