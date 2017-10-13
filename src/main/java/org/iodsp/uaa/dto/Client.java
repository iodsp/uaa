package org.iodsp.uaa.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Client {
    private String id;
    private String clientSecret;
    private List<String> resourceIds;
    private List<String> authorizedGrantTypes;
    private List<String> scope;
    private List<String> authorities;
    private String webServerRedirectUri;
    private List<String> autoApprove;
    private Integer refreshTokenValidity;
    private Integer accessTokenValidity;
    private String additionalInformation;
}

