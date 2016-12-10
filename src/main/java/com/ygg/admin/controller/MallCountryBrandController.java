package com.ygg.admin.controller;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.dao.MallCountryDao;
import com.ygg.admin.entity.BrandEntity;
import com.ygg.admin.entity.ResultEntity;
import com.ygg.admin.service.BrandService;
import com.ygg.admin.service.MallCountryBrandService;
import com.ygg.admin.util.CgiUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/mallCountryBrand")
public class MallCountryBrandController {

    private Logger log = Logger.getLogger(MallCountryBrandController.class);

    @Resource
    private MallCountryBrandService mallCountryBrandService;

    @Resource
    private MallCountryDao mallCountryDao;
    @Resource
    private BrandService brandService;


    @RequestMapping("/list/{mallCountryId}")
    public ModelAndView list(@PathVariable int mallCountryId) {
        ModelAndView mv = new ModelAndView("/mall/countryBandList");
        mv.addObject("mallCountryId", mallCountryId);
        return mv;
    }

    @RequestMapping("/jsonInfo")
    @ResponseBody
    public Map jsonInfo(HttpServletRequest req) {
        Map<String, Object> ret = new HashMap<>();
        try {
            int isDisplay = CgiUtil.getValue(req, "isDisplay", -1);
            int mallCountryId = CgiUtil.getRequiredValue(req, "mallCountryId", -1);

            Map<String, Object> para = CgiUtil.getPageParaMap(req, 50);

            para.put("mallCountryId", mallCountryId);
            if (isDisplay != -1) {
                para.put("isDisplay", isDisplay);
            }
            List<Map<String, Object>> mallCountries = mallCountryBrandService.jsonInfo(para);
            ret.put("rows", mallCountries);
            ret.put("total", mallCountryBrandService.countJsonInfo(para));
        } catch (Exception e) {
            log.error("异步获取商城国家品牌信息失败", e);
            ret.put("rows", new ArrayList<>());
            ret.put("total", 0);
        }
        return ret;
    }


    @RequestMapping("/saveOrUpdate")
    @ResponseBody
    @ControllerLog(description = "创建或编辑商城国家下品牌")
    public ResultEntity saveOrUpdate(
            @RequestParam(required = true, value = "mallCountryId") int mallCountryId,
            @RequestParam(required = true, value = "brandId") int brandId,
            @RequestParam(required = true, value = "sequence") int sequence,
            @RequestParam(required = true, value = "isDisplay") int isDisplay,
            @RequestParam(required = false, value = "id", defaultValue = "0") int id
    ) {
        Map<String, Object> data = new HashMap<>();
        if (id != 0) {
            data.put("idList", Lists.newArrayList(id));
        }
        data.put("mallCountryId", mallCountryId);
        data.put("brandId", brandId);
        data.put("isDisplay", isDisplay);
        data.put("sequence", sequence);
        try {
            checkBrand(brandId, mallCountryId, id);
            mallCountryBrandService.saveOrUpdate(data);
            return ResultEntity.getSuccessResult();
        } catch (IllegalArgumentException ia) {
            return ResultEntity.getFailResult(ia.getMessage());
        } catch (Exception e) {
            log.error("创建编辑商城国家失败", e);
            return ResultEntity.getFailResult("创建编辑商城国家失败");
        }
    }

    @RequestMapping("/update")
    @ResponseBody
    @ControllerLog(description = "更新商城国家下品牌")
    public ResultEntity update(HttpServletRequest req) {
        int id = CgiUtil.getValue(req, "id", -1);
        int isDisplay = CgiUtil.getValue(req, "isDisplay", -1);
        int sequence = CgiUtil.getValue(req, "sequence", -1);
        int mallCountryId = CgiUtil.getValue(req, "mallCountryId", -1);
        int brandId = CgiUtil.getValue(req, "brandId", -1);

        Map<String, Object> para = new HashMap<>();
        para.put("idList", Lists.newArrayList(id));
        if (isDisplay != -1) {
            para.put("isDisplay", isDisplay);
        }
        if (sequence != -1) {
            para.put("sequence", sequence);
        }
        if (mallCountryId != -1) {
            para.put("mallCountryId", mallCountryId);
        }
        if (brandId != -1) {
            para.put("brandId", brandId);
        }
        try {
            //todo more strong...
            if (mallCountryId != -1 && brandId != -1) {
                Map m = new HashMap();
                m.put("mallCountryId", mallCountryId);
                m.put("brandId", mallCountryId);
                if (CollectionUtils.isNotEmpty(mallCountryBrandService.findAllMallCountryBrandByPara(m))) {
                    return ResultEntity.getFailResult("品牌国家id" + mallCountryId + "下面品牌ID" + brandId + "已存在");
                }
            }
            Preconditions.checkArgument(id >= 0);
            mallCountryBrandService.saveOrUpdate(para);
            return ResultEntity.getSuccessResult();
        } catch (Exception e) {
            log.error("更新商城国家下品牌失败", e);
            return ResultEntity.getFailResult("更新商城国家下品牌失败");
        }
    }

    @RequestMapping("/updateDisplay")
    @ResponseBody
    @ControllerLog(description = "批量更新商城国家品牌显示状态")
    public ResultEntity updateDisplay(HttpServletRequest req) {
        List<Integer> idList = CgiUtil.getSplitToIntegerList(req, "id", "", ",");
        int isDisplay = CgiUtil.getValue(req, "isDisplay", -1);

        Map<String, Object> para = new HashMap<>();
        para.put("idList", idList);
        if (isDisplay != -1) {
            para.put("isDisplay", isDisplay);
        }
        try {
            mallCountryBrandService.saveOrUpdate(para);
            return ResultEntity.getSuccessResult();
        } catch (Exception e) {
            log.error("更新大牌汇失败", e);
            return ResultEntity.getFailResult("更新大牌汇失败");
        }
    }

    @RequestMapping("/delete")
    @ResponseBody
    @ControllerLog(description = "删除商城国家下品牌")
    public ResultEntity delete(HttpServletRequest req) {
        List<Integer> ids = CgiUtil.getSplitToIntegerList(req, "id", "", ",");
        try {
            mallCountryBrandService.delete(ids);
            return ResultEntity.getSuccessResult();
        } catch (Exception e) {
            log.error("删除商城国家下品牌失败" + ids, e);
            return ResultEntity.getFailResult("删除商城国家下品牌失败");
        }
    }

    public void checkBrand(int brandId, int mallCountryId, int mallCountryBrandId) throws Exception {
        BrandEntity brand = brandService.findBrandById(brandId);
        Preconditions.checkArgument(brand != null, "品牌id：" + brandId + "尚未创建");
        Preconditions.checkArgument(brand.getIsAvailable() == 1, "品牌id：" + brandId + "不可用");
        //检查品牌国家和商城国家是否匹配
        Map map = new HashMap();
        map.put("id", mallCountryId);
        List<Map<String, Object>> mallCountry = mallCountryDao.findAllMallCountryByPara(map);
        Preconditions.checkArgument((((Long) mallCountry.get(0).get("saleFlagId")).intValue()) == brand.getStateId(), "品牌国家不匹配 请先修改");

        Map m = new HashMap();  //检查国家下品牌是否已存在
        m.put("mallCountryId", mallCountryId);
        m.put("brandId", brandId);
        List<Map<String, Object>> counrtyBrands = mallCountryBrandService.findAllMallCountryBrandByPara(m);
        if (CollectionUtils.isNotEmpty(counrtyBrands)) {
            if (((Long) (counrtyBrands.get(0).get("id"))).intValue() != mallCountryBrandId) {
                throw new IllegalArgumentException("品牌国家id" + mallCountryId + "下面品牌ID" + brandId + "已存在");
            }
        }
    }


}
