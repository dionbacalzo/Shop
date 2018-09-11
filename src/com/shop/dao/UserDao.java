package com.shop.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.shop.domain.UserDomainObject;

@Repository
public interface UserDao extends MongoRepository<UserDomainObject, String> {
	
	public UserDomainObject findByUserName(String userName);
	
}
