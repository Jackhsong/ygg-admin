package TestPackage;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.util.CommonUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangyb on 2015/8/25 0025.
 */
public class JSONTest
{
    public static void main(String[] args) throws Exception
    {
        Map<String,Object> map = new HashMap<>();
        map.put("accountId","70");
        map.put("appCustomActivitiesId","1000");
        String params = JSON.toJSONString(map);
        System.out.println("params：" + params);
        String sign = CommonUtil.strToMD5(params+"yangegegegeyan");
        System.out.println("sign：" + sign);
    }



}
/**

 params：{"accountId":0,"page":"1","pageCount":"10","word":"德国 奶粉"}
 sign：88F8601D649E553F

 params：{"accountId":0,"page":"1","pageCount":"10","word":"奶粉"}
 sign：6D7E816D4CEC47C7

 */
