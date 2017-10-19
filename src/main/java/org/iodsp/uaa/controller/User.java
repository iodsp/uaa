package org.iodsp.uaa.controller;

import org.iodsp.uaa.dto.*;
import org.iodsp.uaa.exceptions.AuthorityExceptionEnums;
import org.iodsp.uaa.exceptions.ServiceException;
import org.iodsp.uaa.exceptions.UserExceptionEnums;
import org.iodsp.uaa.mapper.UserMapper;
import org.iodsp.uaa.service.UcUserSerivce;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class User extends Controller {

    private final Logger logger = LoggerFactory.getLogger(User.class);

    @Autowired
    UcUserSerivce ucUserSerivce;

    @Autowired
    UserMapper userMapper;

    @RequestMapping(value = "/user/add", method = RequestMethod.POST)
    public ReturnObject add(@RequestBody org.iodsp.uaa.dto.User userAdd) {
        ReturnObject returnObject = new ReturnObject();
        org.iodsp.uaa.entity.User user = new org.iodsp.uaa.entity.User();
        user.setName(userAdd.getName());
        user.setPassword(userAdd.getPassword());
        user.setEnable(userAdd.getEnable());
        user.setLock(userAdd.getLock());
        user.setExpired(userAdd.getExpired());
        try {
            ucUserSerivce.addUser(user, userAdd.getRoleIds());
        } catch (ServiceException e) {
            throw e;
        } catch (Throwable e) {
            e.printStackTrace();
            throw new ServiceException(AuthorityExceptionEnums.ADD_EXCEPTION);
        }
        return  returnObject;
    }

    @RequestMapping(value = "/user/list", method = RequestMethod.GET)
    public ReturnObject list(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize,
                             @RequestParam(required = false) Integer isAll) {
        ReturnObject returnObject = new ReturnObject();

        PageList pageList = this.getListPage(() -> {
            return userMapper.find();
        }, (isAll != null && isAll == 1), page, pageSize);

        List<org.iodsp.uaa.entity.User> users = (List<org.iodsp.uaa.entity.User>) pageList.getList();
        HashMap<Integer, org.iodsp.uaa.dto.User> userHashMap;
        try {
            userHashMap = ucUserSerivce.list(users);
        } catch (ServiceException e) {
            throw e;
        } catch (Throwable e) {
            e.printStackTrace();
            throw new ServiceException(UserExceptionEnums.ADD_EXCEPTION);
        }

        List<org.iodsp.uaa.dto.User> result = new ArrayList<>();
        userHashMap.forEach((id, user) -> {
            result.add(user);
        });

        pageList.setList(result);
        returnObject.setData(pageList);
        return returnObject;
    }

    @RequestMapping(value = "/user/info", method = RequestMethod.GET)
    public ReturnObject info(@RequestParam(required = true) Integer id) {
        ReturnObject returnObject = new ReturnObject();
        org.iodsp.uaa.dto.User user;
        try {
            user = ucUserSerivce.info(id);
        } catch (ServiceException e) {
            throw e;
        } catch (Throwable e) {
            e.printStackTrace();
            throw new ServiceException(UserExceptionEnums.PARAM_EXCEPTION);
        }
        returnObject.setData(user);
        return  returnObject;
    }

    @RequestMapping(value = "/user/update", method = RequestMethod.POST)
    public ReturnObject update(@RequestBody org.iodsp.uaa.dto.User user) {
        ReturnObject returnObject = new ReturnObject();
        try {
            org.iodsp.uaa.entity.User userEntity = new org.iodsp.uaa.entity.User(user);
            ucUserSerivce.update(userEntity, user.getRoleIds());
        } catch (ServiceException e) {
            throw e;
        } catch (Throwable e) {
            e.printStackTrace();
            throw new ServiceException(UserExceptionEnums.PARAM_EXCEPTION);
        }
        returnObject.setData(user);
        return  returnObject;
    }
}
