package com.shop.dao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.client.result.UpdateResult;
import com.shop.constant.AppConstant;
import com.shop.domain.UserDomainObject;
import com.shop.dto.Result;
import com.shop.dto.User;

public class UserDaoCustomImpl implements UserDaoCustom {
	
	protected final Logger logger = Logger.getLogger(getClass());
	
	private final MongoTemplate mongoTemplate;
	 
	@Autowired
	public UserDaoCustomImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public UserDomainObject UpdateNameByUserName(String username, User user) {
		logger.debug(AppConstant.METHOD_IN);
		
		UserDomainObject userDomainObj = null;
		
		final Query query = new Query();
		query.addCriteria(Criteria.where("userName").is(username));
		
		Update update = new Update();
		update.set("firstname", user.getFirstname());
		update.set("lastname", user.getLastname());
		
		UpdateResult updateResult = mongoTemplate.updateFirst(query, update, UserDomainObject.class);
		
		if (updateResult.wasAcknowledged()) {
			userDomainObj = mongoTemplate.findOne(query, UserDomainObject.class);
		}
		
		logger.debug(AppConstant.METHOD_OUT);
		return userDomainObj;
	}

	@Override
	public Result UpdatePasswordByUserName(String username, String newPassword) {
		logger.debug(AppConstant.METHOD_IN);
		
		Result result = new Result();
		
		final Query query = new Query();
		query.addCriteria(Criteria.where("userName").is(username));
		
		Update update = new Update();
		update.set("password", newPassword);
		
		UpdateResult updateResult = mongoTemplate.updateFirst(query, update, UserDomainObject.class);
		
		if (updateResult.wasAcknowledged()) {
			result = new Result(AppConstant.SHOP_PASSWORD_UPDATE_SUCCESSFUL_STATUS,
					AppConstant.SHOP_PASSWORD_UPDATE_MESSAGE_SUCCESS);
		} else {
			result = new Result(AppConstant.SHOP_PASSWORD_UPDATE_UNSUCCESSFUL_STATUS,
					AppConstant.SHOP_PASSWORD_UPDATE_MESSAGE_FAIL);
		}
		
		logger.debug(AppConstant.METHOD_OUT);
		return result;
	}

}
