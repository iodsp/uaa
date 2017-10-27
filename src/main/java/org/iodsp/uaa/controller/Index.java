package org.iodsp.uaa.controller;

import org.iodsp.uaa.configure.FeHashConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class Index {

    @Autowired
    FeHashConfig feHashConfig;

    private final Logger logger = LoggerFactory.getLogger(Index.class);

    @RequestMapping(value = "/")
    public String index(Model model) {
        model.addAttribute("feHash", feHashConfig.getValue());
        model.addAttribute("feUrl", feHashConfig.getUrl());
        return "index";
    }
}
