package com.aerodream.Gallery.Service;

import com.aerodream.Gallery.Dto.User.UserCreateDto;
import com.aerodream.Gallery.Dto.User.UserResponseDto;
import com.aerodream.Gallery.Dto.User.UserUpdateDto;
import com.aerodream.Gallery.Entity.UserEntity;
import com.aerodream.Gallery.Enum.RoleEnum;
import com.aerodream.Gallery.Exception.UserAlreadyExistException;
import com.aerodream.Gallery.Exception.UserNotFoundException;
import com.aerodream.Gallery.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserResponseDto createUser(UserCreateDto createDto) throws MatchException, AccessDeniedException, UserAlreadyExistException {
        log.info("Creating new user with login {} and email {}", createDto.getLogin(), createDto.getEmail());

        if (!createDto.getPassword().equals(createDto.getConfirmPassword()))
            throw new AccessDeniedException("Password and Confirm password not match");
        if (userRepository.existsByLogin(createDto.getLogin()))
            throw new UserAlreadyExistException("User already exist with login: " + createDto.getLogin());
        if (userRepository.existsByEmail(createDto.getEmail()))
            throw new UserAlreadyExistException("User already exist with email: " + createDto.getEmail());

        UserEntity user = modelMapper.map(createDto, UserEntity.class);
        user.getRoles().add(RoleEnum.ROLE_USER);
        UserEntity savedUser = userRepository.save(user);

        log.info("Created new user with ID: {}", savedUser.getId());
        return modelMapper.map(savedUser, UserResponseDto.class);
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUser(Long id) throws UserNotFoundException {
        log.info("Fetching user with ID: {}", id);

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

        return modelMapper.map(user, UserResponseDto.class);
    }

    @Transactional
    public UserResponseDto updateUser(UserUpdateDto updateDto, Long userId) throws AccessDeniedException, UserNotFoundException {
        if (!updateDto.getId().equals(userId))
            throw new AccessDeniedException("You can change only your profile");

        UserEntity user = userRepository.findById(updateDto.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + updateDto.getId()));

        modelMapper.map(updateDto, user);

        return modelMapper.map(user, UserResponseDto.class);
    }
}
