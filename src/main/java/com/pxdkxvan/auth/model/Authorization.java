package com.pxdkxvan.auth.model;

import jakarta.persistence.*;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "\"authorization\"")
public class Authorization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST, optional = false)
    @JoinColumn(name = "account_id", nullable = false, updatable = false)
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(name="provider", nullable = false, updatable = false)
    private Provider provider;

    @Column(name = "provider_id", unique = true, nullable = false, updatable = false)
    private String providerId;

    private String password;

    @Override
    public String toString() {
        return "{Account -> %s / Provider -> %s}".formatted(account, provider);
    }
}
