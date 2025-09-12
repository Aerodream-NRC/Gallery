package com.aerodream.Gallery.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tags")
@Getter
@Setter
@NoArgsConstructor
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "tag_body",
            nullable = false,
            unique = true
    )
    private String tagBody;

    @ManyToMany(mappedBy = "tags")
    private Set<ArtworkEntity> artworks = new HashSet<>();

    public TagEntity(String tagBody) {
        this.tagBody = tagBody;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagEntity tagEntity = (TagEntity) o;
        return id != null && Objects.equals(id, tagEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
