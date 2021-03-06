package org.iodsp.uaa.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    public static final Integer USER_ENABLE = 1;
    public static final Integer USER_LOCKED = 1;
    public static final Integer USER_EXPIRED = 1;
    private Integer id;
    private String name;
    private String password;
    private Integer enable = 1;
    private Integer lock = 0;
    private Integer expired = 0;

    public User(org.iodsp.uaa.dto.User user) {
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
