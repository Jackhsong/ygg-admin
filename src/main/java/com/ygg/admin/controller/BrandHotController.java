package com.ygg.admin.controller;

/**
 * @author lorabit
 * @since 16-4-25
 * <p>
 * 大牌汇
 */

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.entity.BrandEntity;
import com.ygg.admin.entity.ResultEntity;
import com.ygg.admin.service.BrandHotService;
import com.ygg.admin.service.BrandService;
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

@Controller
@RequestMapping("/hotBrand")
public class BrandHotController {

    private Logger log = Logger.getLogger(BrandHotController.class);

    @Resource
    private BrandHotService brandHotService;

    @Resource
    private BrandService brandService;

    @RequestMapping("/list")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView("/brand/hot/list");
        return mv;
    }

    @RequestMapping("/jsonInfo")
    @ResponseBody
    public Map<String, Object> jsonInfo(HttpServletRequest req) {
        Map<String, Object> ret = new HashMap<>();
        int isDisplay = CgiUtil.getValue(req, "isDisplay", -1);
        Map<String, Object> para = CgiUtil.getPageParaMap(req, 50);

        if (isDisplay != -1) {
            para.put("isDisplay", isDisplay);
        }
        try {
            List<Map<String, Object>> hotBrands = brandHotService.jsonInfo(para);
            ret.put("rows", hotBrands);
            ret.put("total", brandHotService.countJsonInfo(para));
        } catch (Exception e) {
            log.error("异步获取大牌汇失败", e);
            ret.put("rows", new ArrayList<>());
            ret.put("total", 0);
        }
        return ret;
    }

    @RequestMapping("/saveOrUpdate")
    @ResponseBody
    @ControllerLog(description = "创建编辑大牌汇")
    public ResultEntity save(
            @RequestParam(required = true, value = "brandId") int brandId,
            @RequestParam(required = true, value = "isDisplay") int isDisplay,
            @RequestParam(required = true, value = "sequence") int sequence,
            @RequestParam(required = true, value = "image") String image,
            @RequestParam(required = false, value = "id", defaultValue = "0") int id
    ) {
        Map<String, Object> data = new HashMap<>();
        if (id != 0) {
            data.put("idList", Lists.newArrayList(id));
        }
        data.put("brandId", brandId);
        data.put("isDisplay", isDisplay);
        data.put("sequence", sequence);
        data.put("image", image);
        try {
            checkBrand(brandId, id);
            brandHotService.saveOrUpdate(data);
            return ResultEntity.getSuccessResult();
        } catch (IllegalArgumentException ia) {
            return ResultEntity.getFailResult(ia.getMessage());
        } catch (Exception e) {
            log.error("创建大牌汇失败", e);
            return ResultEntity.getFailResult("创建大牌汇失败");
        }
    }


    @RequestMapping("/update")
    @ResponseBody
    @ControllerLog(description = "更新大牌汇")
    public ResultEntity update(HttpServletRequest req) {
        int id = CgiUtil.getValue(req, "id", -1);
        int isDisplay = CgiUtil.getValue(req, "isDisplay", -1);
        int sequence = CgiUtil.getValue(req, "sequence", -1);
        int brandId = CgiUtil.getValue(req, "brandId", -1);
        String image = CgiUtil.getValue(req, "image", "");

        Map<String, Object> para = new HashMap<>();
        para.put("idList", Lists.newArrayList(id));
        if (isDisplay != -1) {
            para.put("isDisplay", isDisplay);
        }
        if (sequence != -1) {
            para.put("sequence", sequence);
        }
        if(StringUtils.isNotEmpty(image)) {
            para.put("image", image);
        }
        try {
            if (brandId != -1) {
                para.put("brandId", brandId);
                checkBrand(brandId, id);
            }
            brandHotService.saveOrUpdate(para);
            return ResultEntity.getSuccessResult();
        } catch (IllegalArgumentException ia) {
            return ResultEntity.getFailResult(ia.getMessage());
        } catch (Exception e) {
            log.error("更新大牌汇失败", e);
            return ResultEntity.getFailResult("更新大牌汇失败");
        }
    }


    @RequestMapping("/updateDisplay")
    @ResponseBody
    @ControllerLog(description = "批量更新大牌汇显示状态")
    public ResultEntity updateDisplay(HttpServletRequest req) {
        List<Integer> idList = CgiUtil.getSplitToIntegerList(req, "id", "", ",");
        int isDisplay = CgiUtil.getValue(req, "isDisplay", -1);

        Map<String, Object> para = new HashMap<>();
        para.put("idList", idList);
        if (isDisplay != -1) {
            para.put("isDisplay", isDisplay);
        }
        try {
            brandHotService.saveOrUpdate(para);
            return ResultEntity.getSuccessResult();
        } catch (Exception e) {
            log.error("更新大牌汇失败", e);
            return ResultEntity.getFailResult("更新大牌汇失败");
        }
    }

    @RequestMapping("/delete")
    @ResponseBody
    @ControllerLog(description = "删除大牌汇")
    public ResultEntity delete(HttpServletRequest req) {
        List<Integer> ids = CgiUtil.getSplitToIntegerList(req, "id", "", ",");
        try {
            brandHotService.detele(ids);
            return ResultEntity.getSuccessResult();
        } catch (Exception e) {
            log.error("删除大牌汇失败", e);
            return ResultEntity.getFailResult("删除大牌汇失败");
        }
    }

    public void checkBrand(int brandId, int id) throws Exception {
        BrandEntity brand = brandService.findBrandById(brandId);
        Preconditions.checkArgument(brand != null, "品牌id：" + brandId + "尚未创建");
        Preconditions.checkArgument(brand.getIsAvailable() == 1, "品牌id：" + brandId + "不可用");
        Map hotBrand = brandHotService.findHotBrandByBrandId(brandId);
        if (hotBrand != null) {
            if (((Long) hotBrand.get("id")).intValue() != id) {
                throw new IllegalArgumentException("大牌汇中 品牌id：" + brandId + "已经存在");
            }
        }
    }


}
