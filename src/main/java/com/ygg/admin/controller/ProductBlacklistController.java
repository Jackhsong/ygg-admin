package com.ygg.admin.controller;

import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.entity.ProductEntity;
import com.ygg.admin.service.ProductBlacklistService;
import com.ygg.admin.service.ProductService;
import com.ygg.admin.util.POIUtil;

/**
 * 商品黑名单
 * @author zhangyb
 *
 */
@Controller
@RequestMapping("/productBlacklist")
public class ProductBlacklistController
{
    private static final Logger logger = Logger.getLogger(ProductBlacklistController.class);
    
    @Resource
    private ProductService productService;
    
    @Resource
    private ProductBlacklistService productBlacklistService;
    
    @RequestMapping("/list")
    public ModelAndView productList(@RequestParam(value = "type", required = true) byte type //黑名单类型
    )
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.addObject("type", type);
        mv.setViewName("product/blackList");
        return mv;
    }
    
    @RequestMapping(value = "/jsonProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonProduct(HttpServletRequest request,// 
        @RequestParam(value = "page", required = false, defaultValue = "1") int page,//
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows,//
        @RequestParam(value = "type", required = true) byte type //黑名单类型
    )
        throws Exception
    {
        
        Map<String, Object> para = new HashMap<String, Object>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("type", type);
        List<Map<String, Object>> blacklistList = productBlacklistService.findAllProduct(para);
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        Map<String, Object> resultMap = null;
        List<String> idList = new ArrayList<String>();
        if (blacklistList.size() > 0)
        {
            for (Map<String, Object> map : blacklistList)
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
    @ControllerLog(description = "商品黑名单管理-新增商品")
    public String addProduct(@RequestParam(value = "productId", required = true) String productId,// 
        @RequestParam(value = "fileType", required = true) byte fileType, //导入类型   1:根据productId  ； 2:根据file
        @RequestParam(value = "productFile", required = false) MultipartFile file,//
        @RequestParam(value = "type", required = true) byte type //黑名单类型
    )
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (fileType == 1)
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("productId", productId);
            para.put("type", type);
            try
            {
                if ("".equals(productId) || productId == null)
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "请输入商品Id");
                    return JSON.toJSONString(resultMap);
                }
                boolean isExist = productBlacklistService.exist(para);
                if (isExist)
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "该商品已经存在");
                    return JSON.toJSONString(resultMap);
                }
                int result = productBlacklistService.add(para);
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
        }
        else
        {
            try
            {
                int rightNum = 0;
                int wrongNum = 0;
                int existsNum = 0;
                StringBuilder sbWrong = new StringBuilder();
                StringBuilder sbExists = new StringBuilder();
                Map<String, Object> fileMap = POIUtil.getSheetDataAtIndex(file.getInputStream(), 0, true);
                List<Map<String, Object>> rowList = (List<Map<String, Object>>)fileMap.get("data");
                for (Map<String, Object> it : rowList)
                {
                    int cuId = 0;
                    try
                    {
                        cuId = Integer.parseInt(it.get("cell0") == null ? "0" : it.get("cell0") + "");
                    }
                    catch (Exception e)
                    {
                        logger.error("导入商品黑名单是解析数据出错，跳过" + e.getMessage(), e);
                    }
                    if (cuId != 0)
                    {
                        Map<String, Object> para = new HashMap<String, Object>();
                        para.put("productId", cuId);
                        para.put("type", type);
                        boolean isExist = productBlacklistService.exist(para);
                        if (isExist)
                        {
                            existsNum++;
                            sbExists.append(cuId + ",");
                        }
                        else
                        {
                            int result = productBlacklistService.add(para);
                            if (result == 1)
                            {
                                rightNum++;
                            }
                            else
                            {
                                wrongNum++;
                                sbWrong.append(cuId + ",");
                            }
                        }
                    }
                }
                resultMap.put("status", 1);
                String msg =
                    (rightNum > 0 ? "成功保存" + rightNum + "条。" : "") + (wrongNum > 0 ? "失败" + wrongNum + "条，商品IDs：" + sbWrong.toString() : "")
                        + (existsNum > 0 ? "重复导入" + existsNum + "条，商品IDs：" + sbExists.toString() : "");
                resultMap.put("msg", "".equals(msg) ? "保存失败" : msg);
            }
            catch (Exception e)
            {
                logger.error(e.getMessage(), e);
                resultMap.put("status", 0);
                resultMap.put("msg", "保存失败，请检查商品ID是否正确");
            }
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "商品黑名单管理-删除商品")
    public String delete(@RequestParam(value = "ids", required = false, defaultValue = "") String ids,//
        @RequestParam(value = "type", required = true) byte type //黑名单类型
    )
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("type", type);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Integer> idList = new ArrayList<Integer>();
        try
        {
            if (ids.contains(","))
            {
                String[] arr = ids.split(",");
                for (String cur : arr)
                {
                    idList.add(Integer.valueOf(cur));
                }
            }
            else
            {
                idList.add(Integer.valueOf(ids));
            }
            para.put("idList", idList);
            int result = productBlacklistService.delete(para);
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
    
    /**
     * 下载用户推送模板
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/downloadImportFileTemplate")
    public void downloadImportFileTemplate(HttpServletRequest request, HttpServletResponse response)
    {
        response.setContentType("application/vnd.ms-excel");
        String codedFileName = "批量导入商品模板";
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
            String[] str = null;
            str = new String[] {"商品Id"};
            Row row = sheet.createRow(0);
            Cell cell = row.createCell(0);
            cell.setCellValue(str[0]);
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        finally
        {
            if (fOut != null)
            {
                try
                {
                    fOut.close();
                }
                catch (IOException e)
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
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * 修正不发积分的订单数据
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/reduceOrderProductBlacklist", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String reduceOrderProductBlacklist()
        throws Exception
    {
        //        Map<String, Object> result = productBlacklistService.reduceOrderProductBlacklist();
        //        return JSON.toJSONString(result);
        return null;
    }
}
