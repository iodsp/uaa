package org.iodsp.uaa.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleUser {
    private Integer id;
    private Integer roleId;
    private Integer userId;

    public RoleUser(Integer roleId, Integer userId) {
        this.roleId = roleId;
        this.userId = userId;
    }

    public RoleUser() {

    }
}
