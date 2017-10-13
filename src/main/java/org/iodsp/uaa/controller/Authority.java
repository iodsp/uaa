package org.iodsp.uaa.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.iodsp.uaa.dto.PageList;
import org.iodsp.uaa.dto.ReturnObject;
import org.iodsp.uaa.exceptions.AuthorityExceptionEnums;
import org.iodsp.uaa.exceptions.ServiceException;
import org.iodsp.uaa.mapper.AuthorityMapper;
import org.iodsp.uaa.service.UcAuthoritySerivce;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class Authority {

    private final Logger logger = LoggerFactory.getLogger(Authority.class);

    @Autowired
    AuthorityMapper authorityMapper;

    @Autowired
    UcAuthoritySerivce ucAuthoritySerivce;

    @RequestMapping(value = "/authority/add", method = RequestMethod.POST)
    public ReturnObject add(@RequestBody org.iodsp.uaa.dto.Authority authority) {
        ReturnObject returnObject = new ReturnObject();
        try {
            ucAuthoritySerivce.addAuthority(new org.iodsp.uaa.entity.Authority(authority.getName(), authority.getDesc()),
                    authority.getRoleIds());
        } catch (ServiceException e) {
            throw e;
        } catch (Throwable e) {
            e.printStackTrace();
            throw new ServiceException(AuthorityExceptionEnums.ADD_EXCEPTION);
        }
        returnObject.setData(authority);
        return  returnObject;
    }

    @RequestMapping(value = "/authority/info", method = RequestMethod.GET)
    public ReturnObject info(@RequestParam(required = true) Integer id) {
        ReturnObject returnObject = new ReturnObject();
        org.iodsp.uaa.dto.Authority authority;
        try {
            authority = ucAuthoritySerivce.info(id);
        } catch (ServiceException e) {
            throw e;
        } catch (Throwable e) {
            e.printStackTrace();
            throw new ServiceException(AuthorityExceptionEnums.PARAM_EXCEPTION);
        }
        returnObject.setData(authority);
        return  returnObject;
    }

    @RequestMapping(value = "/authority/list", method = RequestMethod.GET)
    public ReturnObject list(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize,
                             @RequestParam(required = false) Integer isAll) {
        ReturnObject returnObject = new ReturnObject();

        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }

        List<org.iodsp.uaa.entity.Authority> authoritys;
        Long total = 0L;
        if (isAll == null || isAll == 0) {
            PageHelper.startPage(page, pageSize, true);
            authoritys = authorityMapper.find();
            total = ((Page) authoritys).getTotal();
        } else {
            authoritys = authorityMapper.find();
        }

        HashMap<Integer, org.iodsp.uaa.dto.Authority> authorityHashMap;
        try {
            authorityHashMap = ucAuthoritySerivce.list(authoritys);
        } catch (ServiceException e) {
            throw e;
        } catch (Throwable e) {
            e.printStackTrace();
            throw new ServiceException(AuthorityExceptionEnums.ADD_EXCEPTION);
        }

        List<org.iodsp.uaa.dto.Authority> result = new ArrayList<>();
        authorityHashMap.forEach((id, auth) -> {
            result.add(auth);
        });

        PageList pageList = new PageList();
        pageList.setTotal(total);
        pageList.setList(result);
        returnObject.setData(pageList);
        return returnObject;
    }

    @RequestMapping(value = "/authority/update", method = RequestMethod.POST)
    public ReturnObject update(@RequestBody org.iodsp.uaa.dto.Authority authority) {
        ReturnObject returnObject = new ReturnObject();
        try {
            org.iodsp.uaa.entity.Authority authEntity = new org.iodsp.uaa.entity.Authority(authority.getName(), authority.getDesc());
            authEntity.setId(authority.getId());
            ucAuthoritySerivce.updateAuthority(authEntity, authority.getRoleIds());
        } catch (ServiceException e) {
            throw e;
        } catch (Throwable e) {
            e.printStackTrace();
            throw new ServiceException(AuthorityExceptionEnums.PARAM_EXCEPTION);
        }
        returnObject.setData(authority);
        return  returnObject;
    }


    @RequestMapping(value = "/authority/delete", method = RequestMethod.DELETE)
    public ReturnObject delete(@RequestParam(required = true) String ids) {
        List<Integer> deleteIds = Arrays.asList(ids.trim().split(",")).stream().map(id -> {
            return Integer.valueOf(id);
        }).collect(Collectors.toList());

        if (deleteIds.isEmpty()) {
            throw new ServiceException(AuthorityExceptionEnums.PARAM_EXCEPTION);
        }

        ReturnObject returnObject = new ReturnObject();
        try {
            ucAuthoritySerivce.delete(deleteIds);
        } catch (ServiceException e) {
            throw e;
        } catch (Throwable e) {
            e.printStackTrace();
            throw new ServiceException(AuthorityExceptionEnums.PARAM_EXCEPTION);
        }
        return  returnObject;
    }
}
