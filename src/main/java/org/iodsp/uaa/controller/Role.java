package org.iodsp.uaa.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.models.auth.In;
import org.iodsp.uaa.dto.PageList;
import org.iodsp.uaa.dto.ReturnObject;
import org.iodsp.uaa.exceptions.RoleExceptionEnums;
import org.iodsp.uaa.exceptions.ScopeExceptionEnums;
import org.iodsp.uaa.exceptions.ServiceException;
import org.iodsp.uaa.mapper.RoleMapper;
import org.iodsp.uaa.mapper.ScopeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
public class Role {

    private final Logger logger = LoggerFactory.getLogger(Role.class);

    @Autowired
    RoleMapper roleMapper;

    @RequestMapping(value = "/role/add", method = RequestMethod.POST)
    public ReturnObject add(@RequestBody org.iodsp.uaa.entity.Role role) {
        ReturnObject returnObject = new ReturnObject();
        org.iodsp.uaa.entity.Role hasRole;
        try {
            hasRole = roleMapper.findByName(role.getName());
        } catch (Throwable e) {
            e.printStackTrace();
            throw new ServiceException(RoleExceptionEnums.ADD_EXCEPTION);
        }
        if (hasRole != null) {
            throw new ServiceException(RoleExceptionEnums.HAS_EXCEPTION);
        }

        try {
            roleMapper.insert(role);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new ServiceException(RoleExceptionEnums.ADD_EXCEPTION);
        }
        returnObject.setData(role);
        return  returnObject;
    }

    @RequestMapping(value = "/role/info", method = RequestMethod.GET)
    public ReturnObject info(@RequestParam(required = true) Integer id) {
        ReturnObject returnObject = new ReturnObject();
        org.iodsp.uaa.entity.Role role;
        try {
            role = roleMapper.findById(id);
        } catch (Throwable e) {
            throw new ServiceException(RoleExceptionEnums.NOSUCH_EXCEPTION);
        }
        returnObject.setData(role);
        return returnObject;
    }

    @RequestMapping(value = "/role/list", method = RequestMethod.GET)
    public ReturnObject list(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize,
                             @RequestParam(required = false) Integer isAll) {
        ReturnObject returnObject = new ReturnObject();

        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }

        List<org.iodsp.uaa.entity.Role> roles;
        Long total = 0L;
        if (isAll == null || isAll == 0) {
            PageHelper.startPage(page, pageSize, true);
            roles = roleMapper.find();
            total = ((Page) roles).getTotal();
        } else {
            roles = roleMapper.find();
        }
        PageList pageList = new PageList();
        pageList.setTotal(total);
        pageList.setList(roles);
        returnObject.setData(pageList);
        return returnObject;
    }

    @RequestMapping(value = "/role/update", method = RequestMethod.POST)
    public ReturnObject update(@RequestBody org.iodsp.uaa.entity.Role role) {
        ReturnObject returnObject = new ReturnObject();
        org.iodsp.uaa.entity.Role hasRole;
        try {
            hasRole = roleMapper.findById(role.getId());
        } catch (Throwable e) {
            e.printStackTrace();
            throw new ServiceException(RoleExceptionEnums.INVALID_EXCEPTION);
        }
        if (hasRole == null) {
            throw new ServiceException(RoleExceptionEnums.INVALID_EXCEPTION);
        }

        try {
            roleMapper.update(role);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new ServiceException(RoleExceptionEnums.ADD_EXCEPTION);
        }
        returnObject.setData(role);
        return  returnObject;
    }


    @Transactional
    @RequestMapping(value = "/role/delete", method = RequestMethod.DELETE)
    public ReturnObject delete(@RequestParam(required = true) String ids) {
        List<String> deleteIds = Arrays.asList(ids.trim().split(","));
        if (deleteIds.isEmpty()) {
            throw new ServiceException(RoleExceptionEnums.PARAM_EXCEPTION);
        }

        ReturnObject returnObject = new ReturnObject();

        deleteIds.forEach(id -> {
            try {
                roleMapper.delete(id);
            } catch (Throwable e) {
                e.printStackTrace();
                throw new ServiceException(RoleExceptionEnums.NOSUCH_EXCEPTION);
            }
        });

        return returnObject;
    }
}
