package com.fashionstoreserver.fashion_store_server.repository;

import com.fashionstoreserver.fashion_store_server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("""
    SELECT u FROM User u 
        LEFT JOIN FETCH u.roles r
    WHERE u.username = :identifier OR u.email = :identifier OR u.phone = :identifier
    """)
    User findByIdentifierWithRoles(@Param("identifier") String identifier);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.id = :id")
    Optional<User> findByIdWithRoles(@Param("id") Long id);

}
