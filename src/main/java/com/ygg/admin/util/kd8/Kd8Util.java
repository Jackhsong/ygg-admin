package com.ygg.admin.util.kd8;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.ygg.admin.util.CommonConstant;
import com.ygg.admin.util.CommonUtil;

public class Kd8Util
{
    private static Logger logger = Logger.getLogger(Kd8Util.class);
    
    //快递吧生成签名
    public static String createSign(String outids)
    {
        return CommonUtil.ecodeByMD5(CommonConstant.KD8_SECRET + outids + CommonConstant.KD8_SECRET).toUpperCase();
    }
    
    //快递吧验证签名
    public static boolean validateSign(HttpServletRequest request)
    {
        if (request == null)
        {
            return false;
        }
        String companyname = request.getParameter("companyname");
        String outid = request.getParameter("outid");
        String status = request.getParameter("status");
        String tracklist = request.getParameter("tracklist");
        String sign = request.getParameter("sign");
        logger.debug("companyname：" + companyname);
        logger.debug("outid：" + outid);
        logger.debug("status：" + status);
        logger.debug("tracklist：" + tracklist);
        logger.debug("sign：" + sign);
        StringBuilder sb = new StringBuilder("companyname=");
        sb.append(companyname).append("&outid=").append(outid).append("&status=").append(status).append("&tracklist=").append(tracklist);
        String newSign = createSign(sb.toString());
        logger.debug("远程签名：" + sign);
        logger.debug("本地签名：" + newSign);
        if (newSign.equals(sign))
        {
            return true;
        }
        return false;
    }
}
