package com.aerodream.Gallery.Config;

import com.aerodream.Gallery.Dto.Artwork.ArtworkCreateDto;
import com.aerodream.Gallery.Dto.Artwork.ArtworkResponseDto;
import com.aerodream.Gallery.Dto.Comment.CommentCreateDto;
import com.aerodream.Gallery.Dto.Comment.CommentResponseDto;
import com.aerodream.Gallery.Dto.User.UserCreateDto;
import com.aerodream.Gallery.Dto.User.UserResponseDto;
import com.aerodream.Gallery.Entity.ArtworkEntity;
import com.aerodream.Gallery.Entity.CommentEntity;
import com.aerodream.Gallery.Entity.UserEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        configureArtworkMappings(modelMapper);
        configureUserMappings(modelMapper);
        configureCommentMappings(modelMapper);

        return modelMapper;
    }

    private void configureUserMappings(ModelMapper modelMapper) {
        modelMapper.typeMap(UserEntity.class, UserResponseDto.class)
                .addMappings(mapping -> {
                    mapping.map(UserEntity::getLogin, UserResponseDto::setLogin);
                    mapping.map(UserEntity::getEmail, UserResponseDto::setEmail);
                    mapping.map(UserEntity::getId, UserResponseDto::setId);
                    mapping.map(context -> context.getCreator().getId(), UserResponseDto::setCreatorId);
                    mapping.map(UserEntity::getRoles, UserResponseDto::setRoles);
                    mapping.map(UserEntity::getCreatedAt, UserResponseDto::setCreatedAt);
                });

        modelMapper.typeMap(UserCreateDto.class, UserEntity.class)
                .addMappings(mapping -> {
                    mapping.map(UserCreateDto::getLogin, UserEntity::setLogin);
                    mapping.map(UserCreateDto::getEmail, UserEntity::setEmail);
                    mapping.map(UserCreateDto::getPassword, UserEntity::setPassword);
                    mapping.skip(UserEntity::setId);
                    mapping.skip(UserEntity::setCreatedAt);
                });
    }

    private void configureArtworkMappings(ModelMapper modelMapper) {
        modelMapper.typeMap(ArtworkEntity.class, ArtworkResponseDto.class)
                .addMappings(mapping -> {
                    mapping.map(ArtworkEntity::getId, ArtworkResponseDto::setId);
                    mapping.map(ArtworkEntity::getTitle, ArtworkResponseDto::setTitle);
                    mapping.map(ArtworkEntity::getDescription, ArtworkResponseDto::setDescription);
                    mapping.map(ArtworkEntity::getImageS3Key, ArtworkResponseDto::setImageS3Key);
                    mapping.map(context -> context.getLikes().size(), ArtworkResponseDto::setLikeCount);
                    mapping.map(context -> context.getComments().size(), ArtworkResponseDto::setCommentCount);
                    mapping.map(ArtworkEntity::isHiddenComments, ArtworkResponseDto::setHiddenComments);
                    mapping.map(ArtworkEntity::isSold, ArtworkResponseDto::setSold);
                    mapping.map(ArtworkEntity::getCreatedAt, ArtworkResponseDto::setCreatedAt);
                });
        modelMapper.typeMap(ArtworkCreateDto.class, ArtworkEntity.class)
                .addMappings(mapping -> {
                    mapping.skip(ArtworkEntity::setId);
                    mapping.skip(ArtworkEntity::setCreatedAt);
                    mapping.skip(ArtworkEntity::setLikes);
                    mapping.skip(ArtworkEntity::setComments);
                });
    }

    private void configureCommentMappings(ModelMapper modelMapper) {
        modelMapper.typeMap(CommentCreateDto.class, CommentEntity.class)
                .addMappings(mapping -> {
                    mapping.map(CommentCreateDto::getCommentBody, CommentEntity::setCommentBody);
                    mapping.map(CommentCreateDto::getArtworkId, CommentEntity::setArtwork);
                    mapping.map(CommentCreateDto::getUserId, CommentEntity::setUser);
                });
        modelMapper.typeMap(CommentEntity.class, CommentResponseDto.class)
                .addMappings(mapping -> {
                    mapping.map(CommentEntity::getId, CommentResponseDto::setId);
                    mapping.map(context -> context.getArtwork().getId(), CommentResponseDto::setArtworkId);
                    mapping.map(context -> context.getUser().getId(), CommentResponseDto::setUserId);
                    mapping.map(CommentEntity::getCreatedAt, CommentResponseDto::setCreatedAt);
                    mapping.map(CommentEntity::getCommentBody, CommentResponseDto::setCommentBody);
                    mapping.map(CommentEntity::isHidden, CommentResponseDto::setHidden);
                    mapping.map(CommentEntity::isLikedByCreator, CommentResponseDto::setLikedByCreator);
                });
    }
}
