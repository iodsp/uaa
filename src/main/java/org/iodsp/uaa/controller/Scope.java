package org.iodsp.uaa.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.models.auth.In;
import org.iodsp.uaa.dto.PageList;
import org.iodsp.uaa.dto.ReturnObject;
import org.iodsp.uaa.exceptions.ScopeExceptionEnums;
import org.iodsp.uaa.exceptions.ServiceException;
import org.iodsp.uaa.mapper.ScopeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
public class Scope {

    private final Logger logger = LoggerFactory.getLogger(Scope.class);

    @Autowired
    ScopeMapper scopeMapper;

    @RequestMapping(value = "/scope/add", method = RequestMethod.POST)
    public ReturnObject add(@RequestBody org.iodsp.uaa.entity.Scope scope) {
        ReturnObject returnObject = new ReturnObject();
        org.iodsp.uaa.entity.Scope hasScope;
        try {
            hasScope = scopeMapper.findScopeById(scope.getId());
        } catch (Throwable e) {
            e.printStackTrace();
            throw new ServiceException(ScopeExceptionEnums.ADD_EXCEPTION);
        }
        if (hasScope != null) {
            throw new ServiceException(ScopeExceptionEnums.HAS_EXCEPTION);
        }

        try {
            scopeMapper.insert(scope);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new ServiceException(ScopeExceptionEnums.ADD_EXCEPTION);
        }
        returnObject.setData(scope);
        return  returnObject;
    }

    @RequestMapping(value = "/scope/info", method = RequestMethod.GET)
    public ReturnObject info(@RequestParam(required = true) String id) {
        ReturnObject returnObject = new ReturnObject();
        org.iodsp.uaa.entity.Scope scope;
        try {
            scope = scopeMapper.findScopeById(id);
        } catch (Throwable e) {
            throw new ServiceException(ScopeExceptionEnums.NOSUCH_EXCEPTION);
        }
        returnObject.setData(scope);
        return returnObject;
    }

    @RequestMapping(value = "/scope/list", method = RequestMethod.GET)
    public ReturnObject list(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize,
                             @RequestParam(required = false) Integer isAll) {
        ReturnObject returnObject = new ReturnObject();

        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }

        List<org.iodsp.uaa.entity.Scope> scopes;
        Long total = 0L;
        if (isAll == null || isAll == 0) {
            PageHelper.startPage(page, pageSize, true);
            scopes = scopeMapper.findScope();
            total = ((Page) scopes).getTotal();
        } else {
            scopes = scopeMapper.findScope();
        }
        PageList pageList = new PageList();
        pageList.setTotal(total);
        pageList.setList(scopes);
        returnObject.setData(pageList);
        return returnObject;
    }

    @RequestMapping(value = "/scope/update", method = RequestMethod.POST)
    public ReturnObject update(@RequestBody org.iodsp.uaa.entity.Scope scope) {
        ReturnObject returnObject = new ReturnObject();
        org.iodsp.uaa.entity.Scope hasScope;
        try {
            hasScope = scopeMapper.findScopeById(scope.getId());
        } catch (Throwable e) {
            e.printStackTrace();
            throw new ServiceException(ScopeExceptionEnums.INVALID_EXCEPTION);
        }
        if (hasScope == null) {
            throw new ServiceException(ScopeExceptionEnums.INVALID_EXCEPTION);
        }

        try {
            scopeMapper.update(scope);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new ServiceException(ScopeExceptionEnums.ADD_EXCEPTION);
        }
        returnObject.setData(scope);
        return  returnObject;
    }


    @Transactional
    @RequestMapping(value = "/scope/delete", method = RequestMethod.DELETE)
    public ReturnObject delete(@RequestParam(required = true) String ids) {
        List<String> deleteIds = Arrays.asList(ids.trim().split(","));
        if (deleteIds.isEmpty()) {
            throw new ServiceException(ScopeExceptionEnums.PARAM_EXCEPTION);
        }

        ReturnObject returnObject = new ReturnObject();

        deleteIds.forEach(id -> {
            try {
                scopeMapper.deleteScope(id);
            } catch (Throwable e) {
                e.printStackTrace();
                throw new ServiceException(ScopeExceptionEnums.NOSUCH_EXCEPTION);
            }
        });

        return returnObject;
    }
}
