package TestPackage;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test1
{
    
    /**
     * 
     * order_logistics 物流单号
     * 
     * order_source_channel 订单来源
     *
     * @param args
     * @throws ClassNotFoundException
     * @throws UnsupportedEncodingException
     */
    public static void main(String[] args)
        throws ClassNotFoundException, UnsupportedEncodingException
    {
        /*
         * Class c = Class.forName("com.ygg.admin.controller.AccountController"); Method[] methods =
         * c.getDeclaredMethods(); System.out.println(methods.length); for (Method method : methods) { if
         * (method.isAnnotationPresent(RequestMapping.class)) { RequestMapping ann =
         * method.getAnnotation(RequestMapping.class); String[] values = ann.value();
         * System.out.println(Arrays.toString(values)); } //System.out.println(method.getName()); }
         */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = sdf.format(new Date(Long.valueOf("1449906337000")));
        System.out.println(date);
        
    }
    
}
