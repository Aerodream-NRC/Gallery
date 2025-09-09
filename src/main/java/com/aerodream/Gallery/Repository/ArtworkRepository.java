package com.aerodream.Gallery.Repository;

import com.aerodream.Gallery.Entity.ArtworkEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtworkRepository extends JpaRepository<ArtworkEntity, Long> {
    Optional<ArtworkEntity> findById(@NonNull Long id);

    List<ArtworkEntity> findByCreatorId(Long creatorId);

    List<ArtworkEntity> findByCollectionId(Long collectionId);

    List<ArtworkEntity> findByTitleContainingIgnoreCase(String title);

    boolean existByTitleAndCreatorId(String title, Long creatorId);

    @Query("SELECT a FROM ArtworkEntity a JOIN FETCH a.creator WHERE a.id = :id")
    Optional<ArtworkEntity> findByIdWithCreator(@Param("id") Long id);

    @Query("SELECT a FROM ArtworkEntity a LEFT JOIN FETCH a.comments WHERE a.id = :id")
    Optional<ArtworkEntity> findByIdWithComments(@Param("id") Long id);

    Page<ArtworkEntity> findByOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT a FROM ArtworkEntity a " +
            "LEFT JOIN a.likes l " +
            "GROUP BY a.id " +
            "ORDER BY COUNT(l) DESC")
    Page<ArtworkEntity> findPopularArtworks(Pageable pageable);


    @Query("SELECT a FROM ArtworkEntity a JOIN a.tags t WHERE t.tagBody = :tagBody")
    Page<ArtworkEntity> findByTagBody(@Param("tagBody") String tagBody, Pageable pageable);
}