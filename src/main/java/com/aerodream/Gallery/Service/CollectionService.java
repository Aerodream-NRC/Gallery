package com.aerodream.Gallery.Service;

import com.aerodream.Gallery.Dto.Collection.CollectionCreateDto;
import com.aerodream.Gallery.Dto.Collection.CollectionResponseDto;
import com.aerodream.Gallery.Dto.Collection.CollectionUpdateDto;
import com.aerodream.Gallery.Entity.ArtworkEntity;
import com.aerodream.Gallery.Entity.CollectionEntity;
import com.aerodream.Gallery.Entity.CreatorEntity;
import com.aerodream.Gallery.Exception.CollectionNotFoundException;
import com.aerodream.Gallery.Repository.CollectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CollectionService {

    private final ModelMapper modelMapper;
    private final CollectionRepository collectionRepository;

    public CollectionResponseDto createCollection(CollectionCreateDto createDto, CreatorEntity creator) {
        log.info("Creating collection for creator ID: {}", creator.getId());

        CollectionEntity collection = modelMapper.map(createDto, CollectionEntity.class);

        CollectionEntity savedCollection = collectionRepository.save(collection);

        log.info("Created collection with ID: {}", savedCollection.getId());
        return convertCollectionToResponseDto(collection);
    }

    public CollectionResponseDto getCollectionsById(Long id) throws CollectionNotFoundException {
        log.info("Fetching collection with ID: {}", id);

        CollectionEntity collection = collectionRepository.findById(id)
                .orElseThrow(() -> new CollectionNotFoundException("Collection not found with ID: " + id));

        return convertCollectionToResponseDto(collection);
    }

    public CollectionResponseDto updateCollection(Long id, CollectionUpdateDto updateDto, Long creatorId) throws CollectionNotFoundException {
        log.info("Updating collection with ID: {}", id);

        CollectionEntity collection = collectionRepository.findById(id)
                .orElseThrow(() -> new CollectionNotFoundException("Collection not found with ID: " + id));

        modelMapper.map(updateDto, collection);
        collectionRepository.save(collection);

        log.info("Updated collection with ID: {}", collection.getId());
        return convertCollectionToResponseDto(collection);
    }

    private CollectionResponseDto convertCollectionToResponseDto(CollectionEntity entity) {
        CollectionResponseDto responseDto = modelMapper.map(entity, CollectionResponseDto.class);
        responseDto.setCreatorId(entity.getCreator().getId());
        responseDto.setArtworksId(convertArtworksSetToLongArtworksId(entity.getArtworks()));

        return responseDto;
    }

    private Set<Long> convertArtworksSetToLongArtworksId(Set<ArtworkEntity> entitySet) {
        Set<Long> artworksId = new HashSet<>();

        for (ArtworkEntity entity : entitySet) {
            artworksId.add(entity.getId());
        }

        return artworksId;
    }
}
