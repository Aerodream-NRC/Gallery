package com.aerodream.Gallery.Service;

import com.aerodream.Gallery.Dto.Artwork.ArtworkCreateDto;
import com.aerodream.Gallery.Dto.Artwork.ArtworkResponseDto;
import com.aerodream.Gallery.Dto.Artwork.ArtworkUpdateDto;
import com.aerodream.Gallery.Entity.*;
import com.aerodream.Gallery.Exception.ArtworkNotFoundException;
import com.aerodream.Gallery.Exception.CollectionNotFoundException;
import com.aerodream.Gallery.Exception.CreatorNotFoundException;
import com.aerodream.Gallery.Repository.*;
import com.amazonaws.services.s3.AmazonS3;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ArtworkService {

    private final ArtworkRepository artworkRepository;
    private final ModelMapper modelMapper;
    private final CreatorRepository creatorRepository;
    private final AmazonS3 amazonS3;
    private final TagRepository tagRepository;
    private final CollectionRepository collectionRepository;
    private final LikeRepository likeRepository;

    private static final String S3_BUCKET_NAME = "your-bucket-name";

    public ArtworkResponseDto createArtwork(ArtworkCreateDto createDto, Long creatorId) throws CreatorNotFoundException, FileUploadException {
        log.info("Creating artwork for creator ID: {}", creatorId);
        ArtworkEntity artwork = new ArtworkEntity();

        CreatorEntity creator = creatorRepository.findById(creatorId)
                .orElseThrow(() -> new CreatorNotFoundException("Creator not found with ID: " + creatorId));

        String imageS3Key = uploadImageToS3(createDto.getImageFile());

        modelMapper.map(artwork, createDto);
        artwork.setImageS3Key(imageS3Key);
        artwork.setCreator(creator);

        if (createDto.getTags() != null) {
            Set<TagEntity> tags = replaceTags(createDto.getTags());
            artwork.setTags(tags);
        }

        ArtworkEntity savedArtwork = artworkRepository.save(artwork);
        log.info("Artwork created with ID: {}", savedArtwork.getId());

        return convertArtworkToResponseDto(savedArtwork);
    }

    public ArtworkResponseDto getArtworkById(Long id, Long userId) throws ArtworkNotFoundException {
        log.info("Fetching artwork ID: {}", id);

        ArtworkEntity artwork = artworkRepository.findById(id)
                .orElseThrow(() -> new ArtworkNotFoundException("Artwork not found with ID: " + id));

        return convertArtworkToResponseDto(artwork);
    }

    public ArtworkResponseDto updateArtwork(Long id, ArtworkUpdateDto updateDto, Long creatorId) throws ArtworkNotFoundException, AccessDeniedException, CollectionNotFoundException {

        log.info("Updating artwork ID: {}", id);

        ArtworkEntity artwork = artworkRepository.findById(id)
                .orElseThrow(() -> new ArtworkNotFoundException("Artwork not found with ID: " + id));

        if (!artwork.getCreator().getId().equals(creatorId)) {
            throw new AccessDeniedException("You can only update your own artworks");
        }

        modelMapper.map(updateDto, artwork);

        if (updateDto.hasCollectionId() && !Objects.equals(updateDto.getCollectionId(), artwork.getCollection().getId())) {
            CollectionEntity newCollection = collectionRepository.findByArtworkId(id)
                    .orElseThrow(() -> new CollectionNotFoundException("Collection not found with ID: " + artwork.getCollection().getId()));
            artwork.getCollection().removeArtwork(artwork);
            newCollection.getArtworks().add(artwork);
            artwork.setCollection(newCollection);
        }

        if (updateDto.hasTags()) {
            Set<TagEntity> tags = replaceTags(updateDto.getTags());
            artwork.setTags(tags);
        }

        ArtworkEntity updatedArtwork = artworkRepository.save(artwork);

        log.info("Updated artwork ID: {}", id);

        return convertArtworkToResponseDto(updatedArtwork);
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Page<ArtworkResponseDto> getArtworksByTag(String tagBody, Pageable pageable) {

        log.info("Fetching artworks by tag: {}", tagBody);

        Page<ArtworkEntity> artworks = artworkRepository.findByTagBody(tagBody, pageable);

        return artworks.map(this::convertArtworkToResponseDto);
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Page<ArtworkResponseDto> getPopularArtworks(Pageable pageable) {

        log.info("Fetching popular artworks");

        Page<ArtworkEntity> artworks = artworkRepository.findPopularArtworks(pageable);

        return artworks.map(this::convertArtworkToResponseDto);
    }

    public void likeOrUnlikeArtwork(Long artworkId, Long userId) throws ArtworkNotFoundException {

        log.info("User {} like artwork {}", userId, artworkId);

        ArtworkEntity artwork = artworkRepository.findById(artworkId)
                .orElseThrow(() -> new ArtworkNotFoundException("Artwork not found with ID: " + artworkId));

        if (likeRepository.existsById_userIdAndId_ArtworkId(userId, artworkId)) {
            likeRepository.deleteByUserAndArtwork(userId, artworkId);
            artwork.unlike(userId);

            log.info("User {} unliked artwork {}", userId, artworkId);

        } else {
            LikeEntity like = new LikeEntity(userId, artworkId);
            artwork.getLikes().add(like);
            likeRepository.save(like);

            log.info("User {} liked artwork {}", userId, artworkId);
        }
    }

    private String uploadImageToS3(MultipartFile imageFile) throws FileUploadException {
        try {
            String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            amazonS3.putObject(S3_BUCKET_NAME, fileName, imageFile.getInputStream(), null);
            return fileName;
        } catch (IOException e) {
            throw new FileUploadException("Failed to upload image: " + e.getMessage());
        }
    }

    private Set<TagEntity> replaceTags(Set<String> tagStrings) {
        Set<TagEntity> tags = new HashSet<>();

        for (String outerTag : tagStrings) {
            TagEntity tag = tagRepository.findByTagBody(outerTag.toUpperCase())
                    .orElseGet(() -> {
                        TagEntity newTag = new TagEntity(outerTag.toUpperCase());
                        return tagRepository.save(newTag);
                    });
            tags.add(tag);
        }

        return tags;
    }

    private ArtworkResponseDto convertArtworkToResponseDto(ArtworkEntity artwork) {
        ArtworkResponseDto responseDto = modelMapper.map(artwork, ArtworkResponseDto.class);

        String imageS3Key = amazonS3.getUrl(S3_BUCKET_NAME, artwork.getImageS3Key()).toString();
        responseDto.setImageS3Key(imageS3Key);
        return responseDto;
    }
}