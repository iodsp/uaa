package org.iodsp.uaa.dto;

import lombok.Getter;
import lombok.Setter;
import org.iodsp.uaa.entity.Role;

import java.util.List;

@Getter
@Setter
public class User {
    private Integer id;
    private String name;
    private String password;
    private Integer enable = 1;
    private Integer lock = 0;
    private Integer expired = 0;
    private List<Integer> roleIds;
    private List<Role> roles;

    public User(org.iodsp.uaa.entity.User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.password = user.getPassword();
        this.enable = user.getEnable();
        this.lock = user.getLock();
        this.expired = user.getExpired();
    }

    public User() {

    }
}
