package com.shop.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.shop.domain.TokenDomainObject;

@Repository
public interface TokenDao extends MongoRepository<TokenDomainObject, String> {

}
