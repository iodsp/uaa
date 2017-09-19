package org.iodsp.uaa.controller;

import org.iodsp.uaa.dto.ClientAdd;
import org.iodsp.uaa.dto.ClientUpdate;
import org.iodsp.uaa.dto.ReturnObject;
import org.iodsp.uaa.exceptions.ClientExceptionEnums;
import org.iodsp.uaa.exceptions.ServiceException;
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

import java.util.LinkedHashSet;

@RestController
public class Client {

    @Autowired
    JdbcClientDetailsService jdbcClientDetailsService;

    @RequestMapping(value = "/client/add", method = RequestMethod.POST)
    public ReturnObject add(@RequestBody ClientAdd client) {
        ReturnObject returnObject = new ReturnObject();
        BaseClientDetails baseClientDetails = new BaseClientDetails(client.getAppkey(),
                client.getResourceIds(), client.getScopes(),
                client.getGrantTypes(), client.getAuthorities(),
                client.getRedirectUris()
        );
        try {
            baseClientDetails.setRefreshTokenValiditySeconds(client.getRefreshTokenValiditySeconds());
            baseClientDetails.setAccessTokenValiditySeconds(client.getAccessTokenValiditySeconds());
            baseClientDetails.setAutoApproveScopes(StringUtils.commaDelimitedListToSet(client.getAutoApproveScopes()));
            baseClientDetails.setClientSecret(client.getSecret());
            jdbcClientDetailsService.addClientDetails(baseClientDetails);
        } catch (Throwable e) {
            throw new ServiceException(ClientExceptionEnums.ADD_EXCEPTION);
        }
        returnObject.setData(baseClientDetails);
        return returnObject;
    }

    @RequestMapping(value = "/client/info/{appkey}", method = RequestMethod.GET)
    public ReturnObject info(@PathVariable("appkey") String appkey) {
        ReturnObject returnObject = new ReturnObject();
        ClientDetails clientDetails;
        try {
            clientDetails = jdbcClientDetailsService.loadClientByClientId(appkey);
        } catch (Throwable e) {
            throw new ServiceException(ClientExceptionEnums.INVALID_CLIENT_EXCEPTION);
        }
        returnObject.setData(clientDetails);
        return returnObject;
    }

    @RequestMapping(value = "/client/all", method = RequestMethod.GET)
    public ReturnObject all() {
        ReturnObject returnObject = new ReturnObject();
        returnObject.setData(jdbcClientDetailsService.listClientDetails());
        return returnObject;
    }

    @RequestMapping(value = "/client/update/{appkey}", method = RequestMethod.POST)
    public ReturnObject update(@PathVariable("appkey") String appkey, @RequestBody ClientUpdate update) {
        ReturnObject returnObject = new ReturnObject();
        ClientDetails clientDetails;
        try {
            clientDetails = jdbcClientDetailsService.loadClientByClientId(appkey);
        } catch (InvalidClientException e) {
            throw new ServiceException(ClientExceptionEnums.INVALID_CLIENT_EXCEPTION);
        }

        BaseClientDetails baseClientDetails = new BaseClientDetails(clientDetails);
        baseClientDetails.setAccessTokenValiditySeconds(update.getAccessTokenValiditySeconds());
        baseClientDetails.setAutoApproveScopes(StringUtils.commaDelimitedListToSet(update.getAutoApproveScopes()));
        baseClientDetails.setAuthorizedGrantTypes(StringUtils.commaDelimitedListToSet(update.getGrantTypes()));
        baseClientDetails.setRegisteredRedirectUri(StringUtils.commaDelimitedListToSet(update.getRedirectUris()));
        baseClientDetails.setScope(StringUtils.commaDelimitedListToSet(update.getScopes()));
        baseClientDetails.setResourceIds(StringUtils.commaDelimitedListToSet(update.getResourceIds()));
        LinkedHashSet<GrantedAuthority> authorities = new LinkedHashSet<GrantedAuthority>();
        for (String auth : StringUtils.commaDelimitedListToSet(update.getAuthorities())) {
            authorities.add(new SimpleGrantedAuthority(auth));
        }
        baseClientDetails.setAuthorities(authorities);

        try {
            jdbcClientDetailsService.updateClientDetails(baseClientDetails);
        } catch (Throwable e) {
            throw new ServiceException(ClientExceptionEnums.NOSUCH_CLIENT_EXCEPTION);
        }
        returnObject.setData(baseClientDetails);
        return returnObject;
    }

    @RequestMapping(value = "/client/delete/{appkey}", method = RequestMethod.DELETE)
    public ReturnObject delete(@PathVariable("appkey") String appkey) {
        ReturnObject returnObject = new ReturnObject();
        try {
            jdbcClientDetailsService.removeClientDetails(appkey);
        } catch (NoSuchClientException e) {
            throw new ServiceException(ClientExceptionEnums.NOSUCH_CLIENT_EXCEPTION);
        }
        returnObject.setData(appkey);
        return returnObject;
    }
}
