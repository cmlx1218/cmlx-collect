package com.cmlx.commons.jsoup;

import com.cmlx.commons.constant.ErrorCode;
import com.cmlx.commons.exception.EXPF;
import lombok.experimental.UtilityClass;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.concurrent.TimeUnit;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 17:56
 * @Desc -> java实现简单爬虫
 **/
@UtilityClass
public class HttpUtil {
    private static OkHttpClient okHttpClient;
    private static String USER_AGENT = "User-Agent";
    private static String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0";

    static {
        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(3, TimeUnit.SECONDS)
                .connectTimeout(3, TimeUnit.SECONDS)
                .build();
    }

    public static String get(String path) throws Exception {
        //创建连接客户端
        Request request = new Request.Builder()
                .url(path)
                .header(USER_AGENT,USER_AGENT_VALUE)
                .build();
        //创建"调用"对象
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();//执行
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (Exception e) {
            throw EXPF.exception(ErrorCode.ErrorResolveUrl.getCode(),"URL解析失败",true);
        }
        return null;
    }
}

