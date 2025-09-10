package com.aerodream.Gallery.Repository;

import com.aerodream.Gallery.Entity.UserEntity;
import com.aerodream.Gallery.Enum.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByLogin(String login);

    Optional<UserEntity> findByEmail(String email);

    boolean existByEmail(String email);

    boolean existByLogin(String login);

    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.roles WHERE u.email = :email")
    Optional<UserEntity> findByEmailWithRoles(@Param("email") String email);

    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.creator WHERE u.id = :id")
    Optional<UserEntity> findByIdWithCreator(@Param("id") Long id);

    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.subscriptions WHERE u.id = :id")
    Optional<UserEntity> findByIdWithSubscriptions(@Param("id") Long id);

    @Query("SELECT u FROM UserEntity u JOIN u.roles r WHERE r = :role")
    List<UserEntity> findAllByRole(@Param("role") RoleEnum role);

}