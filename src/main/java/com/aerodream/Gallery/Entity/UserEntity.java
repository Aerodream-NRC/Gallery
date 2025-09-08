package com.aerodream.Gallery.Entity;

import com.aerodream.Gallery.Enum.RoleEnum;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String email;
    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private CreatorEntity creatorEntity;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<RoleEnum> roles = new HashSet<>();

    public boolean isCreator() {
        return this.creatorEntity != null && this.roles.contains(RoleEnum.ROLE_CREATOR);
    }
}
