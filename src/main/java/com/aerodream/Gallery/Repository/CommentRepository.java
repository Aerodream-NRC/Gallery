package com.aerodream.Gallery.Repository;

import com.aerodream.Gallery.Entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    List<CommentEntity> findByArtworkId(Long artworkId);

    List<CommentEntity> findByUserId(Long userId);

    Page<CommentEntity> findByArtworkId(Long artworkId, Pageable pageable);

    long countByArtworkId(Long artworkId);

    @Query("SELECT c FROM CommentEntity c WHERE c.artwork.id = :artworkId AND c.createdAt BETWEEN :startDate AND :endDate")
    Page<CommentEntity> findByArtworkIdAndCreatedAtBetween(@Param("artworkId") Long artworkId,
                                                           @Param("startDate") LocalDateTime startDate,
                                                           @Param("endDate") LocalDateTime endDate,
                                                           Pageable pageable);

    @Query("SELECT COUNT(c) FROM CommentEntity c WHERE c.artwork.id = :artworkId AND c.createdAt BETWEEN :startDate AND :endDate")
    long countByArtworkIdAndCreatedAtBetween(@Param("artworkId") Long artworkId,
                                             @Param("startDate") LocalDateTime startDate,
                                             @Param("endDate") LocalDateTime endDate);

    @Query("SELECT c.artwork, COUNT(c) as commentCount FROM CommentEntity c " +
            "WHERE c.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY c.artwork " +
            "ORDER BY commentCount DESC")
    Page<Object[]> findTopCommentedArtworksByPeriod(@Param("startDate") LocalDateTime startDate,
                                                    @Param("endDate") LocalDateTime endDate,
                                                    Pageable pageable);

    Page<CommentEntity> findByIsLikedByCreatorTrueAndCreatedAtBetween(LocalDateTime startDate,
                                                                      LocalDateTime endDate,
                                                                      Pageable pageable);


    @Query("SELECT c FROM CommentEntity c WHERE c.artwork.id = :artworkId AND c.isHidden = false ORDER BY c.createdAt DESC")
    List<CommentEntity> findVisibleByArtworkId(@Param("artworkId") Long artworkId);

    @Query("SELECT c FROM CommentEntity c WHERE c.artwork.id = :artworkId AND c.isHidden = false AND c.isLikedByCreator = true ORDER BY c.createdAt DESC")
    List<CommentEntity> findVisibleAndLikedByCreator(@Param("artworkId") Long artworkId);

    @Query("SELECT COUNT(c) FROM CommentEntity c WHERE c.artwork.id = :artworkId AND c.isHidden = false")
    long countVisibleByArtworkId(@Param("artworkId") Long artworkId);
}
