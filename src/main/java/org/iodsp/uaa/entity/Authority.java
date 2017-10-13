package org.iodsp.uaa.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Authority {
    private Integer id;
    private String name;
    private String desc;

    public Authority(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public Authority() {

    }
}
