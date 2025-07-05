package com.sil.jmongo.domain.user.repository;

import com.sil.jmongo.domain.user.entity.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<Users, String> {
    Optional<Users> findByUsername(String username);
    boolean existsByUsername(String username);
}