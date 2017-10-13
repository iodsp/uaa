package org.iodsp.uaa.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.iodsp.uaa.dto.PageList;
import org.iodsp.uaa.dto.ReturnObject;
import org.iodsp.uaa.exceptions.ClientExceptionEnums;
import org.iodsp.uaa.exceptions.ServiceException;
import org.iodsp.uaa.mapper.ClientMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class Client {

    final static Logger logger = LoggerFactory.getLogger(Client.class);

    @Autowired
    JdbcClientDetailsService jdbcClientDetailsService;

    @Autowired
    ClientMapper clientMapper;

    @RequestMapping(value = "/client/add", method = RequestMethod.POST)
    public ReturnObject add(@RequestBody org.iodsp.uaa.dto.Client client) {
        ReturnObject returnObject = new ReturnObject();
        BaseClientDetails baseClientDetails = new BaseClientDetails(client.getId(),
                StringUtils.collectionToCommaDelimitedString(client.getResourceIds()),
                StringUtils.collectionToCommaDelimitedString(client.getScope()),
                StringUtils.collectionToCommaDelimitedString(client.getAuthorizedGrantTypes()),
                StringUtils.collectionToCommaDelimitedString(client.getAuthorities()),
                client.getWebServerRedirectUri()
        );
        try {
            baseClientDetails.setRefreshTokenValiditySeconds(client.getRefreshTokenValidity());
            baseClientDetails.setAccessTokenValiditySeconds(client.getAccessTokenValidity());
            baseClientDetails.setAutoApproveScopes(client.getAutoApprove());
            baseClientDetails.setClientSecret(client.getClientSecret());
            jdbcClientDetailsService.addClientDetails(baseClientDetails);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new ServiceException(ClientExceptionEnums.ADD_EXCEPTION);
        }
        returnObject.setData(baseClientDetails);
        return returnObject;
    }

    @RequestMapping(value = "/client/info", method = RequestMethod.GET)
    public ReturnObject info(@RequestParam(required = true) String id) {
        ReturnObject returnObject = new ReturnObject();
        org.iodsp.uaa.entity.Client clientDetails;
        try {
            clientDetails = clientMapper.findClientById(id);
        } catch (Throwable e) {
            throw new ServiceException(ClientExceptionEnums.INVALID_CLIENT_EXCEPTION);
        }
        org.iodsp.uaa.dto.Client client = new org.iodsp.uaa.dto.Client();
        client.setId(clientDetails.getId());
        client.setClientSecret(clientDetails.getClientSecret());
        client.setAccessTokenValidity(clientDetails.getAccessTokenValidity());
        client.setRefreshTokenValidity(clientDetails.getRefreshTokenValidity());
        client.setAdditionalInformation(clientDetails.getAdditionalInformation());
        client.setScope(convertStringToList(clientDetails.getScope()));
        client.setAutoApprove(convertStringToList(clientDetails.getAutoApprove()));
        client.setAuthorities(convertStringToList(clientDetails.getAuthorities()));
        client.setResourceIds(convertStringToList(clientDetails.getResourceIds()));
        client.setAuthorizedGrantTypes(convertStringToList(clientDetails.getAuthorizedGrantTypes()));
        client.setWebServerRedirectUri(clientDetails.getWebServerRedirectUri());
        returnObject.setData(client);
        return returnObject;
    }

    @RequestMapping(value = "/client/all", method = RequestMethod.GET)
    public ReturnObject all() {
        ReturnObject returnObject = new ReturnObject();
        returnObject.setData(jdbcClientDetailsService.listClientDetails());
        return returnObject;
    }

    @RequestMapping(value = "/client/list", method = RequestMethod.GET)
    public ReturnObject list(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize) {
        ReturnObject returnObject = new ReturnObject();

        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        PageHelper.startPage(page, pageSize, true);
        List<org.iodsp.uaa.entity.Client> clients = clientMapper.findClient();
        Long total = ((Page) clients).getTotal();
        PageList pageList = new PageList();
        pageList.setTotal(total);
        pageList.setList(clients);
        returnObject.setData(pageList);
        return returnObject;
    }

    @RequestMapping(value = "/client/update", method = RequestMethod.POST)
    public ReturnObject update(@RequestBody org.iodsp.uaa.dto.Client update) {
        ReturnObject returnObject = new ReturnObject();
        ClientDetails clientDetails;
        try {
            clientDetails = jdbcClientDetailsService.loadClientByClientId(update.getId());
        } catch (InvalidClientException e) {
            throw new ServiceException(ClientExceptionEnums.INVALID_CLIENT_EXCEPTION);
        }

        BaseClientDetails baseClientDetails = new BaseClientDetails(clientDetails);
        baseClientDetails.setAccessTokenValiditySeconds(update.getAccessTokenValidity());
        baseClientDetails.setAutoApproveScopes(update.getAutoApprove());
        baseClientDetails.setAuthorizedGrantTypes(update.getAuthorizedGrantTypes());
        baseClientDetails.setRegisteredRedirectUri(StringUtils.commaDelimitedListToSet(update.getWebServerRedirectUri()));
        baseClientDetails.setScope(update.getScope());
        baseClientDetails.setResourceIds(update.getResourceIds());
        LinkedHashSet<GrantedAuthority> authorities = new LinkedHashSet<GrantedAuthority>();
        update.getAuthorities().stream().forEach(item -> {
            authorities.add(new SimpleGrantedAuthority(item));
        });
        baseClientDetails.setAuthorities(authorities);

        try {
            jdbcClientDetailsService.updateClientDetails(baseClientDetails);
        } catch (Throwable e) {
            throw new ServiceException(ClientExceptionEnums.NOSUCH_CLIENT_EXCEPTION);
        }
        returnObject.setData(baseClientDetails);
        return returnObject;
    }

    @RequestMapping(value = "/client/delete", method = RequestMethod.DELETE)
    public ReturnObject delete(@RequestParam(required = true) String ids) {
        ReturnObject returnObject = new ReturnObject();
        List<String> deleteIds = Arrays.asList(ids.trim().split(","));
        if (deleteIds.isEmpty()) {
            throw new ServiceException(ClientExceptionEnums.PARAM_EXCEPTION);
        }

        deleteIds.forEach(id -> {
            try {
                jdbcClientDetailsService.removeClientDetails(id);
            } catch (NoSuchClientException e) {
                throw new ServiceException(ClientExceptionEnums.NOSUCH_CLIENT_EXCEPTION);
            }
        });
        returnObject.setData(ids);
        return returnObject;
    }

    protected List<String> convertStringToList(String data) {
        return StringUtils.commaDelimitedListToSet(data).stream().collect(Collectors.toList());
    }
}
