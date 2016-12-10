package com.ygg.admin.controller;

import com.google.common.collect.Lists;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.entity.ResultEntity;
import com.ygg.admin.service.SellerCategoryOperationManagerService;
import com.ygg.admin.util.CgiUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-5-11
 */
@Controller
@RequestMapping("/sellerCategoryOperationManager")
public class SellerCategoryOperationManagerController {

    private static Logger logger = Logger.getLogger(SellerCategoryOperationManagerController.class);

    @Resource
    private SellerCategoryOperationManagerService sellerCategoryOperationManagerService;

    @RequestMapping("/list")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView("operationManager/list");
        return mv;
    }

    @RequestMapping(value = "/jsonInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, Object> jsonChannelInfo(
            HttpServletRequest req,
            @RequestParam(value = "id", required = false, defaultValue = "-1") int id,
            @RequestParam(value = "isAvailable", required = false, defaultValue = "-1") int isAvailable,
            @RequestParam(value = "name", required = false, defaultValue = "") String name
    ) {
        try {
            Map<String, Object> para = CgiUtil.getPageParaMap(req, 50);
            if (id > 0) {
                para.put("id", id);
            }
            if (isAvailable != -1) {
                para.put("isAvailable", isAvailable);
            }
            if (StringUtils.isNotEmpty(name)) {
                para.put("name", name);
            }
            return sellerCategoryOperationManagerService.jsonInfo(para);
        } catch (Exception e) {
            logger.error("异步加载运营对接人失败", e);
            Map<String, Object> result = new HashMap<>();
            result.put("totals", 0);
            result.put("rows", new ArrayList<>());
            return result;
        }
    }

    @RequestMapping("/saveOrUpdate")
    @ResponseBody
    @ControllerLog(description = "创建编辑运营对接人")
    public ResultEntity save(
            @RequestParam(required = true, value = "categoryFirstId") int categoryFirstId,
            @RequestParam(required = true, value = "name") String name,
            @RequestParam(required = true, value = "qqNumber") String qqNumber,
            @RequestParam(required = true, value = "mobileNumber") String mobileNumber,
            @RequestParam(required = true, value = "isAvailable") int isAvailable,
            @RequestParam(required = false, value = "id", defaultValue = "0") int id
    ) {
        Map<String, Object> data = new HashMap<>();
        if (id != 0) {
            data.put("idList", Lists.newArrayList(id));
        }
        data.put("categoryFirstId", categoryFirstId);
        data.put("name", name);
        data.put("qqNumber", qqNumber);
        data.put("mobileNumber", mobileNumber);
        data.put("isAvailable", isAvailable);
        try {
            sellerCategoryOperationManagerService.saveOrUpdate(data);
            return ResultEntity.getSuccessResult();
        } catch (IllegalArgumentException ia) {
            return ResultEntity.getFailResult(ia.getMessage());
        } catch (Exception e) {
            logger.error("创建编辑运营对接人失败", e);
            return ResultEntity.getFailResult("创建编辑运营对接人失败");
        }
    }

}
