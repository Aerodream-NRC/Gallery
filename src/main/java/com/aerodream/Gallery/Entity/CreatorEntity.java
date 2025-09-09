package com.aerodream.Gallery.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "creators")
public class CreatorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "subscriptions",
            joinColumns = @JoinColumn(name = "creator_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<UserEntity> subscribers = new HashSet<>();

    @OneToMany(mappedBy = "creator")
    private Set<CollectionEntity> collections = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreatorEntity that = (CreatorEntity) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
