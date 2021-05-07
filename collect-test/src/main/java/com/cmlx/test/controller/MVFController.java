package com.cmlx.test.controller;

import com.cmlx.commons.constant.ErrorCode;
import com.cmlx.commons.controller.BaseController;
import com.cmlx.commons.exception.EXPF;
import com.cmlx.commons.hibernateExtension.annotation.Phone;
import com.cmlx.commons.springExtension.view.MVF;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.NotNull;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 11:16
 * @Desc -> MVF测试Controller
 **/
@Validated
@RestController
@RequestMapping("/mvf")
public class MVFController extends BaseController {

    @RequestMapping("/hello")
    public ModelAndView hello(@NotNull @Phone String phone) throws Exception {
        if (true) {
            throw EXPF.exception(ErrorCode.ResourceNotFound.getCode(), "你错了", true);
        }
        return MVF.filterData("Hello World!!!");
    }

}
