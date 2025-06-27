package com.sil.jmongo.domain.token.repository;


import com.sil.jmongo.domain.token.entity.Tokens;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends MongoRepository<Tokens, String> {

	// 메서드 확인(키값등 재설계)
	
	Boolean existsByRefreshToken(String refresh);
	void deleteByUsername(String username);
	void deleteByRefreshToken(String refresh);
	
}
