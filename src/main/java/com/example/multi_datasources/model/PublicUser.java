package com.example.multi_datasources.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "user", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PublicUser {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
}
