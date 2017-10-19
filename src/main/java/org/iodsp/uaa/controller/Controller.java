package org.iodsp.uaa.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.iodsp.uaa.dto.PageList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class Controller {

    public <T> PageList getListPage(Supplier<List<T>> getFunction, boolean isAll, Integer page, Integer pageSize) {
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }

        PageList result = new PageList();
        List<T> lists = new ArrayList<>();
        if (isAll == false) {
            PageHelper.startPage(page, pageSize, true);
            lists = getFunction.get();
            result.setTotal(((Page) lists).getTotal());
        } else {
            lists = getFunction.get();
        }
        result.setList(lists);

        return  result;
    }
}
