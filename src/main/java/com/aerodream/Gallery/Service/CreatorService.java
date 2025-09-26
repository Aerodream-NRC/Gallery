package com.aerodream.Gallery.Service;

import com.aerodream.Gallery.Dto.Creator.CreatorResponseDto;
import com.aerodream.Gallery.Dto.Creator.CreatorUpdateDto;
import com.aerodream.Gallery.Entity.CreatorEntity;
import com.aerodream.Gallery.Entity.UserEntity;
import com.aerodream.Gallery.Enum.RoleEnum;
import com.aerodream.Gallery.Exception.CreatorNotFoundException;
import com.aerodream.Gallery.Exception.UserNotFoundException;
import com.aerodream.Gallery.Repository.CreatorRepository;
import com.aerodream.Gallery.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CreatorService {

    private final UserRepository userRepository;
    private final CreatorRepository creatorRepository;
    private final ModelMapper modelMapper;

    public CreatorResponseDto makeUserCreator(Long userId) throws UserNotFoundException {
        log.info("Making user with ID: {} creator", userId);

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        if (user.isCreator())
            throw new AccessDeniedException("You already is creator");

        CreatorEntity creator = new CreatorEntity();
        creator.setUser(user);
        CreatorEntity savedCreator = creatorRepository.save(creator);
        user.setCreator(savedCreator);
        user.getRoles().add(RoleEnum.ROLE_CREATOR);

        log.info("Made user with ID: {} creator with ID: {}", userId, savedCreator.getId());
        return convertCreatorEntityToResponseDto(savedCreator);
    }

    @Transactional(readOnly = true)
    public CreatorResponseDto getCreator(Long id) throws CreatorNotFoundException {
        log.info("Fetching creator with ID: {}", id);

        CreatorEntity creator = creatorRepository.findById(id)
                .orElseThrow(() -> new CreatorNotFoundException("Creator not found with ID: " + id));
        return convertCreatorEntityToResponseDto(creator);
    }

    @Transactional(rollbackFor = {CreatorNotFoundException.class, AccessDeniedException.class})
    public CreatorResponseDto updateCreator(CreatorUpdateDto updateDto, Long userId) throws CreatorNotFoundException {
        log.info("Updating creator with ID: {}", updateDto.getId());

        CreatorEntity creator = creatorRepository.findById(updateDto.getId())
                .orElseThrow(() -> new CreatorNotFoundException("Creator not found with ID: " + updateDto.getId()));

        if (!creator.getUser().getId().equals(userId))
            throw new AccessDeniedException("You can update only your profile");

        creator.setReadyForOrder(updateDto.isReadyForOrder());

        log.info("Updated creator with ID: {}", updateDto.getId());
        return convertCreatorEntityToResponseDto(creator);
    }

    private CreatorResponseDto convertCreatorEntityToResponseDto(CreatorEntity entity) {
        return modelMapper.map(entity, CreatorResponseDto.class);
    }
}