package org.iodsp.uaa.controller;

import org.iodsp.uaa.dto.ReturnObject;
import org.iodsp.uaa.dto.UserAdd;
import org.iodsp.uaa.service.UcUserSerivce;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class User {

    private final Logger logger = LoggerFactory.getLogger(User.class);

    @Autowired
    UcUserSerivce ucUserSerivce;

    @RequestMapping(value = "/user/add", method = RequestMethod.POST)
    public ReturnObject add(@RequestBody UserAdd userAdd) {
        ReturnObject returnObject = new ReturnObject();
        org.iodsp.uaa.entity.User user = new org.iodsp.uaa.entity.User();
        user.setName(userAdd.getName());
        user.setPassword(userAdd.getPassword());
        logger.info("password:" + userAdd.getPassword());
        ucUserSerivce.addUser(user);
        return  returnObject;
    }
}
