package com.ygg.admin.controller;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ygg.admin.entity.SellerEntity;
import com.ygg.admin.entity.SellerExpandEntity;
import com.ygg.admin.service.SellerExpandService;
import com.ygg.admin.service.SellerService;
import com.ygg.admin.util.SessionUtil;
import com.ygg.admin.util.SignUtil;

/**
 * 商家后台 相关控制器
 * 
 * @author Administrator
 *         
 */
@Controller
@RequestMapping(value = "/sellerAdmin")
public class SellerAdminController
{
    
    Logger log = Logger.getLogger(SellerAdminController.class);
    
    @Resource(name = "sellerExpandService")
    private SellerExpandService sellerExpandService;
    
    @Resource(name = "sellerService")
    private SellerService sellerService;
    
    /**
     * 商家首页
     * @param request
     * @param id
     * @param sign
     * @return
     * @throws Exception
     */
    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request, //
        HttpServletResponse response, @RequestParam(value = "id", required = true) int id, // 商家ID
        @RequestParam(value = "sign", required = true) String sign // 签名
    )
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        SellerExpandEntity sellerExpand = sellerExpandService.findSellerExpandBysid(id);
        if (sellerExpand == null)
        {
            log.info(String.format("【sellerAdmin】商家信息不存在，id=%d,sign=%s", id, sign));
            mv.addObject("errorMsg", "登陆错误");
            mv.setViewName("sellerAdmin/error");
            return mv;
        }
        SellerEntity seller = sellerService.findSellerById(id);
        String originStr = id + sellerExpand.getPassword();
        String signStr = SignUtil.md5Uppercase(originStr);
        if (seller == null || !signStr.equals(sign))
        {
            log.info(String.format("【sellerAdmin】签名错误，id=%d,实际签名=%s,收到签名=%s", id, signStr, sign));
            mv.addObject("errorMsg", "登陆错误");
            mv.setViewName("sellerAdmin/error");
            return mv;
        }
        
        Cookie cookie = new Cookie("sellerinfo", id + "_" + sign);
        cookie.setMaxAge(SessionUtil.SELLER_INFO_COOKIES_KEEP_TIME); // 保留一个月
        cookie.setPath("/");
        response.addCookie(cookie);
        
        SessionUtil.addSellerAdminUserToSession(request.getSession(), seller);
        mv.addObject("sellerName", seller.getSellerName());
        mv.setViewName("sellerAdmin/index");
        return mv;
    }
    
}
