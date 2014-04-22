package com.yuanxd.test.jbpm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuanxd.test.jbpm.dao.UserDao;
import com.yuanxd.test.jbpm.entity.UserEntity;

@Service
public class UserServiceImpl {
	@Autowired
	private UserDao dao;

	public void save(UserEntity u) {
		dao.save(u);
	}
}
