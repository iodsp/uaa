package org.iodsp.uaa.mapper;

import org.apache.ibatis.annotations.*;
import org.iodsp.uaa.entity.Client;
import org.iodsp.uaa.entity.User;

import java.util.List;

@Mapper
public interface ClientMapper {
    @Select("select * from oauth_client_details where client_id=#{client}")
    @Results({
            @Result(property = "id", column = "client_id"),
            @Result(property = "resourceIds", column = "resource_ids"),
            @Result(property = "clientSecret", column = "client_secret"),
            @Result(property = "scope", column = "scope"),
            @Result(property = "authorizedGrantTypes", column = "authorized_grant_types"),
            @Result(property = "webServerRedirectUri", column = "web_server_redirect_uri"),
            @Result(property = "accessTokenValidity", column = "access_token_validity"),
            @Result(property = "authorities", column = "authorities"),
            @Result(property = "refreshTokenValidity", column = "refresh_token_validity"),
            @Result(property = "additionalInformation", column = "additional_information"),
            @Result(property = "autoApprove", column = "autoapprove")
    })
    Client findClientById(@Param("client") String client);

    @Select("select * from oauth_client_details")
    @Results({
            @Result(property = "id", column = "client_id"),
            @Result(property = "resourceIds", column = "resource_ids"),
            @Result(property = "clientSecret", column = "client_secret"),
            @Result(property = "scope", column = "scope"),
            @Result(property = "authorizedGrantTypes", column = "authorized_grant_types"),
            @Result(property = "webServerRedirectUri", column = "web_server_redirect_uri"),
            @Result(property = "accessTokenValidity", column = "access_token_validity"),
            @Result(property = "authorities", column = "authorities"),
            @Result(property = "refreshTokenValidity", column = "refresh_token_validity"),
            @Result(property = "additionalInformation", column = "additional_information"),
            @Result(property = "autoApprove", column = "autoapprove")
    })
    List<Client> findClient();
}

