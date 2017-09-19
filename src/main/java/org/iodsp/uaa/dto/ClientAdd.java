package org.iodsp.uaa.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientAdd {
    private String appkey;
    private String secret;
    private String scopes;
    private String resourceIds;
    private String grantTypes;
    private String authorities;
    private String redirectUris;
    private String autoApproveScopes;
    private Integer refreshTokenValiditySeconds;
    private Integer accessTokenValiditySeconds;
    private String additionalInformation;
}
