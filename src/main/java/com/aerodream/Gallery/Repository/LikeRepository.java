package com.aerodream.Gallery.Repository;

import com.aerodream.Gallery.Entity.LikeEntity;
import com.aerodream.Gallery.Entity.LikeId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, LikeId> {

    boolean existById_userIdAndId_ArtworkId(Long userId, Long artworkId);

    long countById_ArtworkId(Long artworkId);

    @Modifying
    @Transactional
    @Query("DELETE FROM LikeEntity l WHERE l.id.userId = :userId AND l.id.artworkId = :artworkId")
    void deleteByUserAndArtwork(@Param("userId") Long userId, @Param("artworkId") Long artworkId);


    @Query("SELECT COUNT(l) FROM LikeEntity l WHERE l.id.artworkId = :artworkId AND l.createdAt BETWEEN :startDate AND :endDate")
    long countByArtworkIdAndCreatedAtBetween(@Param("artworkId") Long artworkId,
                                             @Param("startDate") LocalDateTime startDate,
                                             @Param("endDate") LocalDateTime endDate);
}
