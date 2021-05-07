package com.cmlx.test.controller;

import com.cmlx.commons.springExtension.view.MVF;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 11:16
 * @Desc -> MVF测试Controller
 **/
@RestController
@RequestMapping("/mvf")
public class MVFController {

    @RequestMapping("/hello")
    public ModelAndView hello() {
        return MVF.filterData("Hello World!!!");
    }

}
