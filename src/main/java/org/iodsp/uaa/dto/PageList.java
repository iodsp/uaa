package org.iodsp.uaa.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageList {
    private Long total;
    private Object list;
}
