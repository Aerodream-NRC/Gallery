package com.aerodream.Gallery.Config;

import com.aerodream.Gallery.Dto.Artwork.ArtworkCreateDto;
import com.aerodream.Gallery.Dto.Artwork.ArtworkResponseDto;
import com.aerodream.Gallery.Dto.Artwork.ArtworkUpdateDto;
import com.aerodream.Gallery.Dto.Collection.CollectionCreateDto;
import com.aerodream.Gallery.Dto.Collection.CollectionResponseDto;
import com.aerodream.Gallery.Dto.Collection.CollectionUpdateDto;
import com.aerodream.Gallery.Dto.Comment.CommentCreateDto;
import com.aerodream.Gallery.Dto.Comment.CommentResponseDto;
import com.aerodream.Gallery.Dto.Comment.CommentUpdateBodyDto;
import com.aerodream.Gallery.Dto.Comment.CommentUpdateDto;
import com.aerodream.Gallery.Dto.Creator.CreatorResponseDto;
import com.aerodream.Gallery.Dto.User.UserCreateDto;
import com.aerodream.Gallery.Dto.User.UserResponseDto;
import com.aerodream.Gallery.Dto.User.UserUpdateDto;
import com.aerodream.Gallery.Entity.*;
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
        modelMapper.typeMap(UserUpdateDto.class, UserEntity.class)
                .addMappings(mapping -> {
                    mapping.map(UserUpdateDto::getEmail, UserEntity::setEmail);
                    mapping.map(UserUpdateDto::getLogin, UserEntity::setLogin);
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
                    mapping.map(ArtworkCreateDto::getTitle, ArtworkEntity::setTitle);
                    mapping.map(ArtworkCreateDto::getDescription, ArtworkEntity::setDescription);
                    mapping.skip(ArtworkEntity::setId);
                    mapping.skip(ArtworkEntity::setCreatedAt);
                    mapping.skip(ArtworkEntity::setLikes);
                    mapping.skip(ArtworkEntity::setComments);
                    mapping.skip(ArtworkEntity::setImageS3Key);
                    mapping.skip(ArtworkEntity::setTags);
                    mapping.skip(ArtworkEntity::setCreator);
                    mapping.skip(ArtworkEntity::setCollection);
                });
        modelMapper.typeMap(ArtworkUpdateDto.class, ArtworkEntity.class)
                .addMappings(mapping -> {
                    mapping.map(ArtworkUpdateDto::getTitle, ArtworkEntity::setTitle);
                    mapping.map(ArtworkUpdateDto::getDescription, ArtworkEntity::setDescription);
                    mapping.map(ArtworkUpdateDto::isHiddenComments, ArtworkEntity::setHiddenComments);
                    mapping.map(ArtworkUpdateDto::isSold, ArtworkEntity::setSold);
                    mapping.skip(ArtworkEntity::setCreatedAt);
                    mapping.skip(ArtworkEntity::setId);
                    mapping.skip(ArtworkEntity::setLikes);
                    mapping.skip(ArtworkEntity::setComments);
                    mapping.skip(ArtworkEntity::setTags);
                    mapping.skip(ArtworkEntity::setCollection);
                });
    }

    private void configureCommentMappings(ModelMapper modelMapper) {
        modelMapper.typeMap(CommentCreateDto.class, CommentEntity.class)
                .addMappings(mapping -> {
                    mapping.map(CommentCreateDto::getCommentBody, CommentEntity::setCommentBody);
                    mapping.skip(CommentEntity::setArtwork);
                    mapping.skip(CommentEntity::setUser);
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
        modelMapper.typeMap(CommentUpdateDto.class, CommentEntity.class)
                .addMappings(mapping -> {
                    mapping.map(CommentUpdateDto::isLikedByCreator, CommentEntity::setLikedByCreator);
                    mapping.map(CommentUpdateDto::isHidden, CommentEntity::setHidden);
                });
        modelMapper.typeMap(CommentUpdateBodyDto.class, CommentEntity.class)
                .addMappings(mapping -> {
                    mapping.map(CommentUpdateBodyDto::getCommentBody, CommentEntity::setCommentBody);
                    mapping.map(CommentUpdateBodyDto::getUpdatedAt, CommentEntity::setUpdatedAt);
                });
    }

    private void configureCollectionMapping(ModelMapper modelMapper) {
        modelMapper.typeMap(CollectionCreateDto.class, CollectionEntity.class)
                .addMappings(mapping -> {
                    mapping.map(CollectionCreateDto::getTitle, CollectionEntity::setTitle);
                    mapping.map(CollectionCreateDto::getDescription, CollectionEntity::setDescription);
                });
        modelMapper.typeMap(CollectionEntity.class, CollectionResponseDto.class)
                .addMappings(mapping -> {
                    mapping.map(CollectionEntity::getId, CollectionResponseDto::setId);
                    mapping.map(CollectionEntity::getTitle, CollectionResponseDto::setTitle);
                    mapping.map(CollectionEntity::getDescription, CollectionResponseDto::setDescription);
                    mapping.map(CollectionEntity::getCreatedAt, CollectionResponseDto::setCreatedAt);
                    mapping.map(CollectionEntity::getUpdatedAt, CollectionResponseDto::setUpdatedAt);
                    mapping.skip(CollectionResponseDto::setArtworksId);
                });
        modelMapper.typeMap(CollectionUpdateDto.class, CollectionEntity.class)
                .addMappings(mapping -> {
                    mapping.map(CollectionUpdateDto::getTitle, CollectionEntity::setTitle);
                    mapping.map(CollectionUpdateDto::getDescription, CollectionEntity::setDescription);
                    mapping.skip(CollectionUpdateDto::getUpdatedAt, CollectionEntity::setUpdatedAt);
                    mapping.skip(CollectionEntity::setArtworks);
                });
    }

    private void configureCreatorMapping(ModelMapper modelMapper) {
        modelMapper.typeMap(CreatorEntity.class, CreatorResponseDto.class)
                .addMappings(mapping -> {
                    mapping.map(context -> context.getSubscribers().size(), CreatorResponseDto::setSubscribersCount);
                    mapping.map(context -> context.getCollections().size(), CreatorResponseDto::setCollectionsCount);
                    mapping.map(CreatorEntity::getId, CreatorResponseDto::setCreatorId);
                    mapping.map(context -> context.getUser().getId(), CreatorResponseDto::setUserId);
                    mapping.map(CreatorEntity::isReadyForOrder, CreatorResponseDto::setReadyForOrder);
                });
    }
}
