package org.iodsp.uaa.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.iodsp.uaa.dto.PageList;
import org.iodsp.uaa.dto.ReturnObject;
import org.iodsp.uaa.exceptions.ResourceExceptionEnums;
import org.iodsp.uaa.exceptions.ServiceException;
import org.iodsp.uaa.mapper.ResourceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class Resource {

    private final Logger logger = LoggerFactory.getLogger(Resource.class);

    @Autowired
    ResourceMapper resourceMapper;

    @RequestMapping(value = "/resource/add", method = RequestMethod.POST)
    public ReturnObject add(@RequestBody org.iodsp.uaa.entity.Resource resource) {
        ReturnObject returnObject = new ReturnObject();
        org.iodsp.uaa.entity.Resource hasResource;
        try {
            hasResource = resourceMapper.findResourceById(resource.getId());
        } catch (Throwable e) {
            e.printStackTrace();
            throw new ServiceException(ResourceExceptionEnums.ADD_EXCEPTION);
        }
        if (hasResource != null) {
            throw new ServiceException(ResourceExceptionEnums.HAS_EXCEPTION);
        }

        try {
            resourceMapper.insert(resource);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new ServiceException(ResourceExceptionEnums.ADD_EXCEPTION);
        }
        returnObject.setData(resource);
        return  returnObject;
    }

    @RequestMapping(value = "/resource/info", method = RequestMethod.GET)
    public ReturnObject info(@RequestParam(required = true) String id) {
        ReturnObject returnObject = new ReturnObject();
        org.iodsp.uaa.entity.Resource resource;
        try {
            resource = resourceMapper.findResourceById(id);
        } catch (Throwable e) {
            throw new ServiceException(ResourceExceptionEnums.NOSUCH_EXCEPTION);
        }
        returnObject.setData(resource);
        return returnObject;
    }

    @RequestMapping(value = "/resource/list", method = RequestMethod.GET)
    public ReturnObject list(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize,
                             @RequestParam(required = false) Integer isAll) {
        ReturnObject returnObject = new ReturnObject();

        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }


        List<org.iodsp.uaa.entity.Resource> resources;
        Long total = 0L;
        if (isAll == null || isAll == 0) {
            PageHelper.startPage(page, pageSize, true);
            resources = resourceMapper.findResource();
            total = ((Page) resources).getTotal();
        } else {
            resources = resourceMapper.findResource();
        }
        PageList pageList = new PageList();
        pageList.setTotal(total);
        pageList.setList(resources);
        returnObject.setData(pageList);
        return returnObject;
    }

    @RequestMapping(value = "/resource/update", method = RequestMethod.POST)
    public ReturnObject update(@RequestBody org.iodsp.uaa.entity.Resource resource) {
        ReturnObject returnObject = new ReturnObject();
        org.iodsp.uaa.entity.Resource hasResource;
        try {
            hasResource = resourceMapper.findResourceById(resource.getId());
        } catch (Throwable e) {
            e.printStackTrace();
            throw new ServiceException(ResourceExceptionEnums.INVALID_EXCEPTION);
        }
        if (hasResource == null) {
            throw new ServiceException(ResourceExceptionEnums.INVALID_EXCEPTION);
        }

        try {
            resourceMapper.update(resource);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new ServiceException(ResourceExceptionEnums.ADD_EXCEPTION);
        }
        returnObject.setData(resource);
        return  returnObject;
    }

    @Transactional
    @RequestMapping(value = "/resource/delete", method = RequestMethod.DELETE)
    public ReturnObject delete(@RequestParam(required = true) String ids) {
        List<String> deleteIds = Arrays.asList(ids.trim().split(","));
        if (deleteIds.isEmpty()) {
            throw new ServiceException(ResourceExceptionEnums.PARAM_EXCEPTION);
        }

        ReturnObject returnObject = new ReturnObject();

        deleteIds.forEach(id -> {
            try {
                resourceMapper.deleteResource(id);
            } catch (Throwable e) {
                e.printStackTrace();
                throw new ServiceException(ResourceExceptionEnums.NOSUCH_EXCEPTION);
            }
        });
        return returnObject;
    }
}
