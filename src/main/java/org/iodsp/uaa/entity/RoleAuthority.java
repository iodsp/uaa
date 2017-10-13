package org.iodsp.uaa.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleAuthority {
    private Integer id;
    private Integer roleId;
    private Integer authId;

    public RoleAuthority(Integer roleId, Integer authId) {
        this.roleId = roleId;
        this.authId = authId;
    }

    public RoleAuthority() {

    }
}
