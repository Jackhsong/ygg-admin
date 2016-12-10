package com.ygg.admin.controller;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.entity.ProductEntity;
import com.ygg.admin.service.NotSendMsgProductService;
import com.ygg.admin.service.ProductService;

@Controller
@RequestMapping("/notSendMsgProduct")
public class NotSendMsgProductController
{
    private static final Logger logger = Logger.getLogger(NotSendMsgProductController.class);
    
    @Resource
    private ProductService productService;
    
    @Resource
    private NotSendMsgProductService notSendMsgService;
    
    @RequestMapping("/list")
    public ModelAndView productList()
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("sms/productList");
        return mv;
    }
    
    @RequestMapping(value = "/jsonProductInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonInfo(HttpServletRequest request, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows)
        throws Exception
    {
        
        Map<String, Object> para = new HashMap<String, Object>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        List<Map<String, Object>> notSendMsgProducts = notSendMsgService.queryAllProductId();
        Map<String, Object> resultMap = null;
        List<String> idList = new ArrayList<String>();
        if (notSendMsgProducts != null && notSendMsgProducts.size() > 0)
        {
            for (Map<String, Object> map : notSendMsgProducts)
            {
                idList.add(map.get("productId") + "");
            }
            para.put("idList", idList);
            resultMap = productService.jsonProductForManage(para);
        }
        else
        {
            resultMap = new HashMap<String, Object>();
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/getProductInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getProductInfo(@RequestParam(value = "productId", required = true) int id)
        throws Exception
    {
        ProductEntity product = productService.findProductById(id);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (product != null)
        {
            resultMap.put("status", 1);
            resultMap.put("msg", product.getName());
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "请检查商品Id是否正确");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/addProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "不发短息商品管理-新增不发短信商品")
    public String addProduct(@RequestParam(value = "productId", required = true) String productId)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            if ("".equals(productId) || productId == null)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "请输入商品Id");
                return JSON.toJSONString(resultMap);
            }
            boolean isExist = notSendMsgService.checkIsExist(productId);
            if (isExist)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "该商品已经存在");
                return JSON.toJSONString(resultMap);
            }
            para.put("productId", productId);
            int result = notSendMsgService.add(para);
            if (result == 1)
            {
                resultMap.put("status", 1);
                resultMap.put("msg", "保存成功");
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "保存失败");
            }
            
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "不发短息商品管理-删除不发短信商品")
    public String delete(@RequestParam(value = "id", required = false, defaultValue = "-1") int productId,
        @RequestParam(value = "ids", required = false, defaultValue = "") String ids)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Integer> idList = new ArrayList<Integer>();
        try
        {
            if (!"".equals(ids))
            {
                String[] arr = ids.split(",");
                for (String cur : arr)
                {
                    idList.add(Integer.valueOf(cur));
                }
                para.put("ids", idList);
            }
            if (productId != -1)
            {
                para.put("productId", productId);
            }
            int result = notSendMsgService.delete(para);
            if (result > 0)
            {
                resultMap.put("status", 1);
                resultMap.put("msg", "删除成功");
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "删除失败");
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "删除失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/exportAllProductIds")
    @ControllerLog(description = "不发短息商品管理-导出不发短信商品")
    public void exportAllProductIds(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        List<Map<String, Object>> notSendMsgProducts = notSendMsgService.queryAllProductId();
        response.setContentType("application/vnd.ms-excel");
        String codedFileName = "用户列表";
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        try
        {
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            // 产生工作簿对象
            workbook = new HSSFWorkbook();
            // 产生工作表对象
            HSSFSheet sheet = workbook.createSheet();
            String[] str = {"商品ID"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            int index = 1;
            for (Map<String, Object> it : notSendMsgProducts)
            {
                Row r = sheet.createRow(index++);
                r.createCell(0).setCellValue(it.get("productId") + "");
            }
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            response.setHeader("content-disposition", "");
            response.setContentType("text/html");
            if (fOut == null)
            {
                fOut = response.getOutputStream();
            }
            String errorStr = "<script>alert('系统出错');window.history.back();</script>";
            fOut.write(errorStr.getBytes());
            fOut.flush();
        }
        finally
        {
            if (fOut != null)
            {
                try
                {
                    fOut.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            if (workbook != null)
            {
                try
                {
                    workbook.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
