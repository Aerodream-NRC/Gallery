package com.aerodream.Gallery.Entity;

import com.aerodream.Gallery.Enum.RoleEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToOne(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private CreatorEntity creator;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<RoleEnum> roles = new HashSet<>();

    @ManyToMany(mappedBy = "subscribers")
    private Set<CreatorEntity> subscribers = new HashSet<>();

    public boolean isCreator() {
        return this.creator != null &&
                this.roles.contains(RoleEnum.ROLE_CREATOR);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void subscribe(CreatorEntity creator) {
        subscribers.add(creator);
        creator.getSubscribers().add(this);
    }

    public void unSubscribe(CreatorEntity creator) {
        subscribers.remove(creator);
        creator.getSubscribers().remove(this);
    }
}
