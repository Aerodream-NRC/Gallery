package com.aerodream.Gallery.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Table(name = "creators")
public class CreatorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToMany(mappedBy = "subscriptions")
    private Set<UserEntity> subscribers = new HashSet<>();

    private Set<CollectionEntity> collections = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreatorEntity that = (CreatorEntity) o;
        return id == that.id &&
                Objects.equals(subscribers, that.subscribers) &&
                Objects.equals(collections, that.collections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,
                subscribers,
                collections);
    }
}
