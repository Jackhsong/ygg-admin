package com.ygg.admin.controller;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.entity.ResultEntity;
import com.ygg.admin.service.MallCountryService;
import com.ygg.admin.util.CgiUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-4-26
 */
@Controller
@RequestMapping("/mallCountry")
public class MallCountryController {

    private Logger log = Logger.getLogger(MallCountryController.class);

    @Resource
    private MallCountryService mallCountryService;

    @RequestMapping("/list")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView("/mall/countryList");
        return mv;
    }

    @RequestMapping("/jsonInfo")
    @ResponseBody
    public Map jsonInfo(HttpServletRequest req) {
        Map<String, Object> ret = new HashMap<>();
        int isDisplay = CgiUtil.getValue(req, "isDisplay", -1);
        Map<String, Object> para = CgiUtil.getPageParaMap(req, 50);
        if (isDisplay != -1) {
            para.put("isDisplay", isDisplay);
        }
        try {
            List<Map<String, Object>> mallCountries = mallCountryService.jsonInfo(para);
            ret.put("rows", mallCountries);
            ret.put("total", mallCountryService.countJsonInfo(para));
        } catch (Exception e) {
            log.error("异步获取商城国家信息失败", e);
            ret.put("rows", new ArrayList<>());
            ret.put("total", 0);
        }
        return ret;
    }

    @RequestMapping("/saveOrUpdate")
    @ResponseBody
    @ControllerLog(description = "创建编辑商城国家")
    public ResultEntity saveOrUpdate(
            @RequestParam(required = true, value = "saleFlagId") int saleFlagId,
            @RequestParam(required = true, value = "image") String image,
            @RequestParam(required = true, value = "sequence") int sequence,
            @RequestParam(required = true, value = "isDisplay") int isDisplay,
            @RequestParam(required = false, value = "id", defaultValue = "0") int id
    ) {
        Map<String, Object> data = new HashMap<>();
        if (id != 0) {
            data.put("idList", Lists.newArrayList(id));
        }
        data.put("saleFlagId", saleFlagId);
        data.put("image", image);
        data.put("isDisplay", isDisplay);
        data.put("sequence", sequence);
        try {
            Map<String, Object> country = mallCountryService.findMallCountryBySaleFlagId(saleFlagId);
            if (country != null) {
                if (((Long) country.get("id")).intValue() != id)
                    return ResultEntity.getFailResult("国家Id" + saleFlagId + "已存在");
            }
            mallCountryService.saveOrUpdate(data);
            return ResultEntity.getSuccessResult();
        } catch (Exception e) {
            log.error("创建编辑商城国家失败", e);
            return ResultEntity.getFailResult("创建编辑商城国家失败");
        }
    }

    @RequestMapping("/update")
    @ResponseBody
    @ControllerLog(description = "更新商城国家")
    public ResultEntity update(HttpServletRequest req) {
        int id = CgiUtil.getValue(req, "id", -1);
        int isDisplay = CgiUtil.getValue(req, "isDisplay", -1);
        int sequence = CgiUtil.getValue(req, "sequence", -1);
        String image = CgiUtil.getValue(req, "image", "");

        Map<String, Object> para = new HashMap<>();
        para.put("idList", Lists.newArrayList(id));
        if (isDisplay != -1) {
            para.put("isDisplay", isDisplay);
        }
        if (sequence != -1) {
            para.put("sequence", sequence);
        }
        if (StringUtils.isNotEmpty(image)) {
            para.put("image", image);
        }
        try {
            Preconditions.checkArgument(id >= 0);
            mallCountryService.saveOrUpdate(para);
            return ResultEntity.getSuccessResult();
        } catch (Exception e) {
            log.error("更新大牌汇失败", e);
            return ResultEntity.getFailResult("更新大牌汇失败");
        }
    }


    @RequestMapping("/delete")
    @ResponseBody
    @ControllerLog(description = "删除商城国家")
    public ResultEntity delete(HttpServletRequest req) {
        List<Integer> ids = CgiUtil.getSplitToIntegerList(req, "id", "", ",");
        try {
            mallCountryService.detele(ids);
            return ResultEntity.getSuccessResult();
        } catch (Exception e) {
            log.error("删除商城国家失败" + ids, e);
            return ResultEntity.getFailResult("删除商城国家失败");
        }
    }

}
