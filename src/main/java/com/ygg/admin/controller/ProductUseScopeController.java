package com.ygg.admin.controller;

import com.google.common.collect.Lists;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.dao.ProductUseScopeDao;
import com.ygg.admin.entity.ResultEntity;
import com.ygg.admin.service.ProductUseScopeService;
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
 * @since 16-5-7
 */
@RequestMapping("/productUseScope")
@Controller
public class ProductUseScopeController {

    private Logger log = Logger.getLogger(ProductUseScopeController.class);

    @Resource
    private ProductUseScopeService productUseScopeService;

    @Resource
    private ProductUseScopeDao productUseScopeDao;

    @RequestMapping("/list")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView("/productUseScope/list");
        return mv;
    }

    @ResponseBody
    @RequestMapping("/jsonListInfo")
    public Map<String, Object> jsonListInfo(
            @RequestParam(value = "isAvailable", defaultValue = "-1") int isAvailable,
            HttpServletRequest req
    ) {
        try {
            Map<String, Object> para = CgiUtil.getPageParaMap(req, 50);
            if (isAvailable != -1) {
                para.put("isAvailable", isAvailable);
            }
            return productUseScopeService.jsonListInfo(para);
        } catch (Exception e) {
            log.error(e);
            return new HashMap<>();
        }
    }

    @RequestMapping("/saveOrUpdate")
    @ResponseBody
    @ControllerLog(description = "创建编辑商品使用范围")
    public ResultEntity saveOrUpdate(
            @RequestParam(required = true, value = "name", defaultValue = "") String name,
            @RequestParam(required = true, value = "isAvailable", defaultValue = "-1") int isAvailable,
            @RequestParam(required = false, value = "id", defaultValue = "0") int id
    ) {
        Map<String, Object> data = new HashMap<>();
        if (id != 0) {
            data.put("idList", Lists.newArrayList(id));
        }
        if (StringUtils.isNotEmpty(name)) {
            data.put("name", name);
        }
        if (isAvailable != -1) {
            data.put("isAvailable", isAvailable);
        }
        try {
            productUseScopeService.saveOrUpdate(data);
            return ResultEntity.getSuccessResult();
        } catch (Exception e) {
            log.error("创建编辑商品使用范围error", e);
            return ResultEntity.getFailResult(e.getMessage());
        }
    }

    @RequestMapping("/delete")
    @ResponseBody
    @ControllerLog(description = "删除品牌描述")
    public ResultEntity delete(HttpServletRequest req) {
        List<Integer> ids = CgiUtil.getSplitToIntegerList(req, "id", "", ",");
        try {
            productUseScopeService.delete(ids);
            return ResultEntity.getSuccessResult();
        } catch (Exception e) {
            log.error("删除品牌描述失败" + ids, e);
            return ResultEntity.getFailResult("删除品牌描述失败");
        }
    }

    @RequestMapping("/jsonproductUseScope")
    @ResponseBody
    public List<Map<String, Object>> jsonproductUseScope(
            @RequestParam(value = "includeAll", defaultValue = "0") int includeAll,
            @RequestParam(value = "isAvailable", defaultValue = "1") int isAvailable
    ) {
        try {
            List<Map<String, Object>> codeList = new ArrayList<>();
            Map<String, Object> qMap = new HashMap();
            if (isAvailable != -1) {
                qMap.put("isAvailable", isAvailable);
            }
            List<Map<String, Object>> scopes = productUseScopeDao.findProductUseScope(qMap);
            for (Map<String, Object> scope : scopes) {
                Map<String, Object> line = new HashMap();
                line.put("text", scope.get("name"));
                line.put("code", scope.get("id"));
                codeList.add(line);
            }
            if (includeAll == 1) {
                codeList.add(0, new HashMap<String, Object>() {
                    {
                        put("code", "-1");
                        put("text", "--全部--");
                    }
                });
            }
            return codeList;
        } catch (Exception e) {
            log.error("获取商品适用范围列表失败", e);
            return new ArrayList<>();
        }
    }
}
