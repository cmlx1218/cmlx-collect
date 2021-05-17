package com.cmlx.thread.spring.service;

import java.util.concurrent.Future;

/**
 * @Author CMLX
 * @Date -> 2021/5/17 17:58
 * @Desc ->
 **/
public interface IMainService {

    Future<String> addAsync();
    Future<String> subAsync();
    Future<String> mulAsync();
    Future<String> divAsync();

    String add();

    String sub();

    String mul();

    String div();


}
