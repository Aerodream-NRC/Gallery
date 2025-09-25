package com.aerodream.Gallery.Service;

import com.aerodream.Gallery.Dto.Artwork.ArtworkUpdateDto;
import com.aerodream.Gallery.Dto.Comment.CommentCreateDto;
import com.aerodream.Gallery.Dto.Comment.CommentResponseDto;
import com.aerodream.Gallery.Dto.Comment.CommentUpdateBodyDto;
import com.aerodream.Gallery.Dto.Comment.CommentUpdateDto;
import com.aerodream.Gallery.Entity.ArtworkEntity;
import com.aerodream.Gallery.Entity.CommentEntity;
import com.aerodream.Gallery.Entity.UserEntity;
import com.aerodream.Gallery.Exception.ArtworkNotFoundException;
import com.aerodream.Gallery.Exception.CommentNotFoundException;
import com.aerodream.Gallery.Exception.UserNotFoundException;
import com.aerodream.Gallery.Repository.ArtworkRepository;
import com.aerodream.Gallery.Repository.CommentRepository;
import com.aerodream.Gallery.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final ModelMapper modelMapper;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ArtworkRepository artworkRepository;

    public CommentResponseDto writeComment(CommentCreateDto createDto) throws ArtworkNotFoundException, UserNotFoundException {
        log.info("User with ID: {} writing comment to artwork with ID: {}", createDto.getUserId(), createDto.getArtworkId());

        CommentEntity comment = modelMapper.map(createDto, CommentEntity.class);

        UserEntity user = userRepository.findById(createDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + createDto.getUserId()));

        ArtworkEntity artwork = artworkRepository.findById(createDto.getArtworkId())
                .orElseThrow(() -> new ArtworkNotFoundException("Artwork not found with ID: " + createDto.getArtworkId()));

        comment.setArtwork(artwork);
        comment.setUser(user);
        artwork.addComment(comment);

        CommentEntity savedComment = commentRepository.save(comment);

        log.info("User with ID: {} wrote comment with ID: {}", savedComment.getUser().getId(), savedComment.getId());
        return convertEntityToResponseDto(savedComment);
    }

    @Transactional
    public CommentResponseDto updateCommentBody(CommentUpdateBodyDto updateBodyDto, Long userId) throws CommentNotFoundException {
        log.info("User with ID: {} edits comment with ID : {}", userId, updateBodyDto.getId());

        CommentEntity comment = commentRepository.findById(updateBodyDto.getId())
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with ID: " + updateBodyDto.getId()));

        if (!comment.getUser().getId().equals(userId))
            throw new AccessDeniedException("You can change only yours comments");

        modelMapper.map(updateBodyDto, comment);

        log.info("User with ID: {} edited comment with ID: {}", userId, updateBodyDto.getId());
        return convertEntityToResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(CommentUpdateDto updateDto, Long userId) throws CommentNotFoundException, UserNotFoundException {
        log.info("User with ID: {} updating comment with ID: {}", userId, updateDto.getId());

        CommentEntity comment = commentRepository.findById(updateDto.getId())
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with ID: " + updateDto.getId()));

        ArtworkEntity artwork = comment.getArtwork();

        if (!artwork.getCreator().getUser().getId().equals(userId))
            throw new AccessDeniedException("You can update comment only if you author of artwork");

        artwork.removeComment(comment);
        modelMapper.map(updateDto, comment);
        artwork.addComment(comment);

        log.info("User with ID: {} updated comment with ID: {}", userId, updateDto.getId());
        return convertEntityToResponseDto(comment);
    }

    @Transactional(readOnly = true)
    public Page<CommentResponseDto> getCommentsOfArtwork(Pageable pageable, Long artworkId) {
        log.info("Fetching comments to artwork with ID: {}", artworkId);

        Page<CommentEntity> comments = commentRepository.findByArtworkId(artworkId, pageable);

        return comments.map(this::convertEntityToResponseDto);
    }

    private CommentResponseDto convertEntityToResponseDto(CommentEntity entity) {
        return modelMapper.map(entity, CommentResponseDto.class);
    }
}