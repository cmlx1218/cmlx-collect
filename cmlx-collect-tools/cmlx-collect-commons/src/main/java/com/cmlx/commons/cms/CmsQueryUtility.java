package com.cmlx.commons.cms;

import com.cmlx.commons.support.JsonUtility;
import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 14:31
 * @Desc -> 运营后台查询参数处理工具类
 **/
@UtilityClass
public class CmsQueryUtility {
    public Map<String,CmsQuery> JsonToMap(String json)throws Exception{
        if(StringUtils.isEmpty(json))   return new HashMap<>();
        List<CmsQuery> list = JsonUtility.toArrayList(json,CmsQuery.class);
        HashMap<String,CmsQuery> result = new HashMap<>();
        for(CmsQuery query:list){
            result.put(query.getParamName(),query);
        }
        return result;
    }

    public String MapToJson(Map<String, CmsQuery> queryMap) throws Exception {
        if (queryMap.size() == 0) return new String();
        List<CmsQuery> list = new ArrayList<>();
        queryMap.forEach((key, value) -> {
            list.add(value);
        });
        return JsonUtility.toString(list);
    }
}
