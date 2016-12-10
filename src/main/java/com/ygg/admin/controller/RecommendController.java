package com.ygg.admin.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 格格推荐相关控制器
 * @author xiongl
 *
 */
@Controller
@RequestMapping("/recommend")
public class RecommendController
{
    private static Logger logger = Logger.getLogger(RecommendController.class);
    
    @RequestMapping("/list")
    public ModelAndView list()
    {
        ModelAndView mv = new ModelAndView("recommend/list");
        return mv;
    }
    
}
