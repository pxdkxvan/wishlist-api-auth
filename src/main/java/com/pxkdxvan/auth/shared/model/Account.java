package com.pxkdxvan.auth.shared.model;

import com.pxkdxvan.auth.local.model.Verification;

import jakarta.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import lombok.*;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean verified = Boolean.FALSE;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime createdAt;

    @ManyToMany
    @JoinTable(name = "account_role_mapping",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Authorization> auths = new ArrayList<>();

    public void bindAuthorization(Authorization auth) {
        auths.add(auth);
        auth.setAccount(this);
    }

    @Builder.Default
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Verification> verifications = new ArrayList<>();

    public void bindVerification(Verification verification) {
        verifications.add(verification);
        verification.setAccount(this);
    }

    public void unbindVerification(Verification verification) {
        verifications.remove(verification);
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
