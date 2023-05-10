package com.cs.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "advertisement_details")
public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int advertisementId;
    private String title;

    @Column(length = 500)
    private String description;
    private LocalDateTime localDateTime;
    private String image;

    @ManyToOne
    @JoinColumn(name = "user_advertisement_key_id")
    @JsonBackReference
    private User user;
}
