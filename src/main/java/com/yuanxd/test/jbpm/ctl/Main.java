package com.yuanxd.test.jbpm.ctl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yuanxd.test.jbpm.dao.UserDao;
import com.yuanxd.test.jbpm.entity.UserEntity;
import com.yuanxd.test.jbpm.service.impl.UserServiceImpl;

@Controller
public class Main {
	@Autowired
	private UserServiceImpl service;

	/**
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public String list(Model model) {
		UserEntity u = new UserEntity();
		u.setName("test");
		u.setPassword("testpwd");
		service.save(u);
		return "main";
	}
}
