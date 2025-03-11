package com.pxkdxvan.auth.shared.model;

import jakarta.persistence.*;

import lombok.*;

@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @ToString.Include
    @Column(unique = true, nullable = false, updatable = false)
    private String name;

}
