package com.pxkdxvan.auth.local.model;

import com.pxkdxvan.auth.shared.model.Account;

import jakarta.persistence.*;

import lombok.*;

import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "verification",
        uniqueConstraints = {@UniqueConstraint(name = "uq_account_id_type", columnNames = {"account_id", "type"})},
        indexes = {@Index(name = "inx_token_hash", columnList = "key")})
public class Verification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(nullable = false)
    private String key;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private VerificationMethod method;

    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id", nullable = false, updatable = false)
    private Account account;

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime expiresAt;

}
