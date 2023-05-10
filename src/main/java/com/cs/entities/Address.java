package com.cs.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "address_data")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String state;
    private String village;
    private long pinCode;

    @OneToOne
    @JoinColumn(name = "user_address_key_id")
    @JsonBackReference
    private User user;
}
