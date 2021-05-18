package com.cmlx.thread.ordinary.service.impl;

import com.cmlx.thread.ordinary.service.IMainMethodService;
import com.cmlx.thread.ordinary.service.ISerialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author CMLX
 * @Date -> 2021/5/18 12:31
 * @Desc ->
 **/
@Service
public class SerialService implements ISerialService {

    @Autowired
    private IMainMethodService iMainMethodService;

    @Override
    public void testSerial() {
        iMainMethodService.add();
        iMainMethodService.sub();
        iMainMethodService.mul();
        iMainMethodService.div();
    }

}
