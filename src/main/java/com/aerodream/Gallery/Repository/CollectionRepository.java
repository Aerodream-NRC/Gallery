package com.aerodream.Gallery.Repository;

import com.aerodream.Gallery.Entity.CollectionEntity;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CollectionRepository extends JpaRepository<CollectionEntity, Long> {

    Optional<CollectionEntity> findByArtworkId(@NonNull Long artworkId);

    List<CollectionEntity> findByCreatorId(@NonNull Long creatorId);

    List<CollectionEntity> findByTitleContainingIgnoreCase(String title);

    @Query("SELECT COUNT(a) FROM ArtworkEntity a WHERE a.collection.id = :collectionId")
    Long countArtworksByCollectionId(@Param("collectionId") Long collectionId);

    @Query("SELECT c FROM CollectionEntity c LEFT JOIN FETCH c.artworks WHERE c.id = :collectionId")
    Optional<CollectionEntity> findByIdWithArtworks(@Param("collectionId") Long collectionId);

    Page<CollectionEntity> findByCreatedAtAfter(LocalDateTime date, Pageable pageable);

    @Modifying
    @Transactional
    void deleteByIdAndCreatorId(Long id, Long creatorId);
}
