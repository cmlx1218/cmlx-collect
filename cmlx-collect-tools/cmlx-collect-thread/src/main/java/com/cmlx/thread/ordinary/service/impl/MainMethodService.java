package com.cmlx.thread.ordinary.service.impl;

import com.cmlx.thread.ordinary.service.IMainMethodService;
import org.springframework.stereotype.Service;

/**
 * @Author CMLX
 * @Date -> 2021/5/18 12:30
 * @Desc ->
 **/
@Service
public class MainMethodService implements IMainMethodService {

    @Override
    public String add() {
        long startTime = System.currentTimeMillis();
        int result = 0;
        for (int i = 0; i < 1000000; i++) {
            for (int j = 0; j < 10000; j++) {
                result += i;
            }
        }
        System.out.println("加法执行完毕，result=" + result + "执行时间=" + (System.currentTimeMillis() - startTime));
        return "add";
    }

    @Override
    public String sub() {
        long startTime = System.currentTimeMillis();
        int result = 0;
        for (int i = 0; i < 1000000; i++) {
            for (int j = 0; j < 10000; j++) {
                result -= i;
            }
        }
        System.out.println("减法执行完毕，result=" + result + "执行时间=" + (System.currentTimeMillis() - startTime));
        return "sub";
    }

    @Override
    public String mul() {
        long startTime = System.currentTimeMillis();
        int result = 1;
        for (int i = 0; i < 1000000; i++) {
            for (int j = 0; j < 10000; j++) {
                result = result * 2;
            }
        }
        System.out.println("乘法执行完毕，result=" + result + "执行时间=" + (System.currentTimeMillis() - startTime));
        return "mul";
    }

    @Override
    public String div() {
        long startTime = System.currentTimeMillis();
        int result = 10000000;
        for (int i = 0; i < 1000000; i++) {
            for (int j = 0; j < 1000; j++) {
                result = result / 2;
            }
        }
        System.out.println("除法执行完毕，result=" + result + "执行时间=" + (System.currentTimeMillis() - startTime));
        return "div";
    }


}
