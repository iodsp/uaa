package org.iodsp.uaa.dto;

import lombok.Getter;
import lombok.Setter;
import org.iodsp.uaa.entity.Role;

import java.util.List;

@Getter
@Setter
public class Authority {
    private Integer id;
    private String name;
    private String desc;
    private List<Integer> roleIds;
    private List<Role> roles;
}

