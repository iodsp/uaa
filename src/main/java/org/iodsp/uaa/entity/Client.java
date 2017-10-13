package org.iodsp.uaa.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Client {
    private String id;
    private String resourceIds;
    private String clientSecret;
    private String scope;
    private String authorizedGrantTypes;
    private String webServerRedirectUri;
    private Integer accessTokenValidity;
    private Integer refreshTokenValidity;
    private String additionalInformation;
    private String autoApprove;
    private String authorities;
}