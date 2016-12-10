package com.ygg.admin.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.entity.MemberBannerEntity;
import com.ygg.admin.service.MemberService;
import com.ygg.admin.util.CommonUtil;

/**
 * 会员中心管理
 * @author xiongl
 *
 */
@Controller
@RequestMapping("/member")
public class MemberController
{
    @Resource
    private MemberService memberService;
    
    private static Logger logger = Logger.getLogger(MemberController.class);
    
    @RequestMapping("/levelList")
    public ModelAndView levelList()
    {
        ModelAndView mv = new ModelAndView("member/memberLevelList");
        return mv;
    }
    
    @RequestMapping(value = "/jsonMemberLevelInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonMemberLevelInfo(//
        @RequestParam(value = "page", required = false, defaultValue = "1") int page,//
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows,// 
        @RequestParam(value = "remark", required = false, defaultValue = "") String remark,//
        @RequestParam(value = "isDisplay", required = false, defaultValue = "-1") int isDisplay)
    {
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            
            return memberService.jsonMemberLevelInfo(para);
        }
        catch (Exception e)
        {
            logger.error("异步加载会员等级体系列表出错", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("totals", 0);
            result.put("rows", new ArrayList<Object>());
            return JSON.toJSONString(result);
        }
    }
    
    @RequestMapping(value = "/updateMemberLevelDisplayStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "积分换购商品管理-更改积分等级展现状态")
    public String updateMemberLevelDisplayStatus(@RequestParam(value = "ids", required = false, defaultValue = "") String ids,
        @RequestParam(value = "isDisplay", required = true) int isDisplay)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            if (StringUtils.isEmpty(ids))
            {
                result.put("status", 0);
                result.put("msg", "操作失败");
                return JSON.toJSONString(result);
            }
            return memberService.updateMemberLevelDisplayStatus(Arrays.asList(ids.split(",")), isDisplay);
        }
        catch (Exception e)
        {
            result.put("status", 0);
            result.put("msg", "操作失败");
            logger.error(e.getMessage(), e);
            return JSON.toJSONString(result);
        }
    }
    
    @RequestMapping("/manageProduct/{levelId}/{level}")
    public ModelAndView manageProduct(@PathVariable("levelId") int levelId, @PathVariable("level") int level)
    {
        ModelAndView mv = new ModelAndView("member/memberProductList");
        mv.addObject("levelId", levelId + "");
        mv.addObject("level", level + "");
        return mv;
    }
    
    @RequestMapping(value = "/jsonMemberProductInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonMemberProductInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "30") int rows, //
        @RequestParam(value = "productId", required = false, defaultValue = "0") int productId,//
        @RequestParam(value = "productCode", required = false, defaultValue = "") String productCode,//
        @RequestParam(value = "productName", required = false, defaultValue = "") String productName,//
        @RequestParam(value = "shortName", required = false, defaultValue = "") String shortName,//
        @RequestParam(value = "remark", required = false, defaultValue = "") String remark, //
        @RequestParam(value = "brandId", required = false, defaultValue = "0") int brandId,//
        @RequestParam(value = "sellerId", required = false, defaultValue = "0") int sellerId, //
        @RequestParam(value = "levelId", required = false, defaultValue = "") int levelId)
    {
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            para.put("levelId", levelId);
            if (productId != 0)
            {
                para.put("productId", productId);
            }
            if (!"".equals(productCode))
            {
                para.put("productCode", productCode);
            }
            if (!"".equals(productName))
            {
                para.put("productName", "%" + productName + "%");
            }
            if (!"".equals(shortName))
            {
                para.put("shortName", "%" + shortName + "%");
            }
            if (!"".equals(remark))
            {
                para.put("remark", "%" + remark + "%");
            }
            if (brandId != 0)
            {
                para.put("brandId", brandId);
            }
            if (sellerId != 0)
            {
                para.put("sellerId", sellerId);
            }
            return memberService.jsonMemberProductInfo(para);
        }
        catch (Exception e)
        {
            logger.error("异步加载积分换购商品列表出错", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("total", 0);
            result.put("rows", new ArrayList<Object>());
            return JSON.toJSONString(result);
        }
    }
    
    @RequestMapping("/editMemberBanner/{id}")
    public ModelAndView editMemberBanner(@PathVariable("id") int id)
    {
        ModelAndView mv = new ModelAndView("member/memberBanner");
        try
        {
            MemberBannerEntity banner = memberService.findMemberBannerById(id);
            if (banner == null)
            {
                banner = new MemberBannerEntity();
            }
            mv.addObject("banner", banner);
        }
        catch (Exception e)
        {
            logger.error("编辑积分商城Banner出错", e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    @RequestMapping(value = "/saveOrUpdateMemberProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "积分换购商品管理-编辑商品")
    public String saveOrUpdateMemberProduct(//
        @RequestParam(value = "id", required = false, defaultValue = "0") int id,//
        @RequestParam(value = "levelId", required = false, defaultValue = "0") int levelId,//
        @RequestParam(value = "productId", required = false, defaultValue = "0") int productId,//
        @RequestParam(value = "point", required = false, defaultValue = "0") int point,//
        @RequestParam(value = "sequence", required = false, defaultValue = "0") int sequence,//
        @RequestParam(value = "limitNum", required = false, defaultValue = "0") int limitNum,//
        @RequestParam(value = "isSupportCashBuy", required = false, defaultValue = "0") int isSupportCashBuy,//
        @RequestParam(value = "level", required = false, defaultValue = "0") int level)
    {
        try
        {
            if (id == 0)
            {
                return memberService.insertMemberProduct(levelId, level, productId, point, sequence, limitNum, isSupportCashBuy);
            }
            else
            {
                return memberService.updateMemberProduct(id, levelId, level, productId, point, sequence, limitNum, isSupportCashBuy);
            }
        }
        catch (Exception e)
        {
            logger.error("积分换购商品管理-编辑商品失败", e);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", "保存出错");
            return JSON.toJSONString(resultMap);
        }
    }
    
    @RequestMapping(value = "/deleteMemberProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "积分换购商品管理-删除商品")
    public String deleteMemberProduct(@RequestParam(value = "ids", required = false, defaultValue = "") String ids)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (ids.isEmpty())
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "请选择要删除的商品");
            return JSON.toJSONString(resultMap);
        }
        try
        {
            return memberService.deleteMemberProduct(Arrays.asList(ids.split(",")));
        }
        catch (Exception e)
        {
            logger.error("积分换购商品管理-删除商品失败", e);
            resultMap.put("status", 0);
            resultMap.put("msg", "删除出错");
            return JSON.toJSONString(resultMap);
        }
    }
    
    @RequestMapping(value = "/updateMemberProductDisplayStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "积分换购商品管理-更新商品展现状态")
    public String updateMemberProductDisplayStatus(@RequestParam(value = "ids", required = false, defaultValue = "") String ids,
        @RequestParam(value = "isDisplay", required = true) int isDisplay)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            if (StringUtils.isEmpty(ids))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "请选择要操作的商品");
                return JSON.toJSONString(resultMap);
            }
            return memberService.updateMemberProductDisplayStatus(Arrays.asList(ids.split(",")), isDisplay);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
            return JSON.toJSONString(resultMap);
        }
    }
    
    @RequestMapping(value = "/updateMemberProductSequence", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "积分换购商品管理-更新商品排序值")
    public String updateMemberProductSequence(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "sequence", required = true) int sequence)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            return memberService.updateMemberProductSequence(id, sequence);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
            return JSON.toJSONString(resultMap);
        }
    }
    
    @RequestMapping("/bannerList")
    public ModelAndView bannerList()
    {
        ModelAndView mv = new ModelAndView("member/memberBannerList");
        return mv;
    }
    
    @RequestMapping(value = "/jsonMemberBannerInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonMemberBannerInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "30") int rows, //
        @RequestParam(value = "bannerType", required = false, defaultValue = "-1") int bannerType,//
        @RequestParam(value = "bannerDesc", required = false, defaultValue = "") String bannerDesc)
    {
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            if (bannerType != -1)
            {
                para.put("bannerType", bannerType);
            }
            if (!"".equals(bannerDesc))
            {
                para.put("bannerDesc", "%" + bannerDesc + "%");
            }
            return memberService.jsonMemberBannerInfo(para);
        }
        catch (Exception e)
        {
            logger.error("异步加载积分商城首页Banner列表出错", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("total", 0);
            result.put("rows", new ArrayList<Object>());
            return JSON.toJSONString(result);
        }
    }
    
    @RequestMapping(value = "/saveOrUpdateMemberBanner", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "积分商城首页Banner管理-编辑Banner")
    public String saveOrUpdateMemberBanner(HttpServletRequest request)
    {
        try
        {
            String productId = request.getParameter("productId") == null ? "0" : request.getParameter("productId");
            String groupSaleId = request.getParameter("groupSaleId") == null ? "0" : request.getParameter("groupSaleId");
            String customSaleId = request.getParameter("customSaleId") == null ? "0" : request.getParameter("customSaleId");
            String customPageId = request.getParameter("customPageId") == null ? "0" : request.getParameter("customPageId");
            MemberBannerEntity banner = new MemberBannerEntity();
            CommonUtil.wrapParamter2Entity(banner, request);
            if (banner.getType() == 1)
            {
                banner.setDisplayId(Integer.parseInt(productId));
            }
            else if (banner.getType() == 2)
            {
                banner.setDisplayId(Integer.parseInt(groupSaleId));
            }
            else if (banner.getType() == 3)
            {
                banner.setDisplayId(Integer.parseInt(customSaleId));
            }
            else if (banner.getType() == 5)
            {
                banner.setDisplayId(Integer.parseInt(customPageId));
            }
            if (banner.getId() == 0)
            {
                return memberService.saveMemberBanner(banner);
            }
            else
            {
                return memberService.updateMemberBanner(banner);
            }
        }
        catch (Exception e)
        {
            logger.error("积分商城首页Banner管理-编辑Banner失败", e);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", "保存出错");
            return JSON.toJSONString(resultMap);
        }
    }
    
    @RequestMapping(value = "/deleteMemberBanner", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "积分商城首页Banner管理-删除Banner")
    public String deleteMemberBanner(@RequestParam(value = "ids", required = false, defaultValue = "") String ids)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (ids.isEmpty())
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "请选择要删除的Banner");
            return JSON.toJSONString(resultMap);
        }
        try
        {
            return memberService.deleteMemberBanner(Arrays.asList(ids.split(",")));
        }
        catch (Exception e)
        {
            logger.error("积分商城首页Banner管理-删除Banner失败", e);
            resultMap.put("status", 0);
            resultMap.put("msg", "删除出错");
            return JSON.toJSONString(resultMap);
        }
    }
    
    @RequestMapping(value = "/updateMemberBannerDisplayStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "积分商城首页Banner管理-更新Banner展现状态")
    public String updateMemberBannerDisplayStatus(@RequestParam(value = "ids", required = false, defaultValue = "") String ids,
        @RequestParam(value = "isDisplay", required = true) int isDisplay)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            if (StringUtils.isEmpty(ids))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "请选择要操作的Banner");
                return JSON.toJSONString(resultMap);
            }
            return memberService.updateMemberBannerDisplayStatus(Arrays.asList(ids.split(",")), isDisplay);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
            return JSON.toJSONString(resultMap);
        }
    }
    
    @RequestMapping(value = "/updateMemberBannerSequence", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "积分商城首页Banner管理-更新Banner排序值")
    public String updateMemberBannerSequence(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "sequence", required = true) int sequence)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            return memberService.updateMemberBannerSequence(id, sequence);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
            return JSON.toJSONString(resultMap);
        }
    }
}
