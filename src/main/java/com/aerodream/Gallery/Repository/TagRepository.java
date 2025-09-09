package com.aerodream.Gallery.Repository;

import com.aerodream.Gallery.Entity.TagEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository <TagEntity, Long> {
    Optional<TagEntity> findById(@NonNull Long id);

    Optional<TagEntity> findByTagBody(String tagBody);

    @Query("SELECT COUNT(a) FROM ArtworkEntity a JOIN a.tags t WHERE t.id = :tagId")
    long countArtworksByTagId(@Param("tagId") Long tagId);

    boolean existsByTagBody(String tagBody);
}
