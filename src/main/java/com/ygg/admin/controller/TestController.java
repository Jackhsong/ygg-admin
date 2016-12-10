package com.ygg.admin.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ygg.admin.excel.WorksheetTest;
import com.ygg.admin.util.ProductUtil;

@Controller
@RequestMapping("/test")
public class TestController
{
    
    @RequestMapping("/testExcel")
    @ResponseBody
    public String testExcel()
        throws Exception
    {
        output();
        return "ok";
    }
    
    public void output()
        throws FileNotFoundException
    {
        long startTimne = System.currentTimeMillis();
        StringTemplateGroup stGroup = new StringTemplateGroup("stringTemplate");
        //写入excel文件头部信息
        //        StringTemplate head = stGroup.getInstanceOf("com/ygg/admin/excel/head");
        StringTemplate head = stGroup.getInstanceOf("head");
        File file = new File("D:/output(打开提示文件格式与扩展名不符，选是即可).xls");
        PrintWriter writer = new PrintWriter(new BufferedOutputStream(new FileOutputStream(file)));
        writer.print(head.toString());
        writer.flush();
        
        int sheets = 1;
        //excel单表最大行数是65535
        int maxRowNum = 10000;
        for (int i = 0; i < sheets; i++)
        {
            StringTemplate body = stGroup.getInstanceOf("body");
            WorksheetTest worksheet = new WorksheetTest();
            worksheet.setSheet("1");
            worksheet.setColumnNum(62);
            worksheet.setRowNum(maxRowNum);
            List<com.ygg.admin.excel.Row> rows = new ArrayList<>();
            for (int j = 0; j < maxRowNum; j++)
            {
                com.ygg.admin.excel.Row row = new com.ygg.admin.excel.Row();
                //                row.setOrderType(ofv.getOrderType());//
                //                row.setNumber(ofv.getNumber());//
                //                row.setHbNumber(ofv.getHbNumber());//
                //                row.setStatus(ofv.getStatus());//
                //                row.setSettlement(ofv.getSettlement());//
                //                row.setSettlementTime(ofv.getSettlementTime());
                //                row.setPayChannel(ofv.getPayChannel());//
                //                row.setCreateTime(ofv.getCreateTime());//
                //                row.setPayTime(ofv.getPayTime());//
                //                row.setSendTime(ofv.getSendTime());//
                //                row.setSellerName(ofv.getSellerName());//
                //                row.setSendAddress(ofv.getSendAddress());//
                //                row.setReceiveFullName(ofv.getReceiveFullName());//
                //                row.setIdCard(ofv.getIdCard());//
                //                row.setReceiveAddress(ofv.getReceiveAddress());//
                //                row.setProvince(ofv.getProvince());//
                //                row.setCity(ofv.getCity());//
                //                row.setDistrict(ofv.getDistrict());//
                //                row.setDetailAddress(ofv.getDetailAddress());//
                //                row.setMobileNumber(ofv.getMobileNumber());//
                //                row.setType(liv.getType());//
                //                row.setProductId(liv.getProductId());//
                //                row.setCode(liv.getCode());//
                //                row.setProductName(liv.getName());//
                //                row.setCount(liv.getCount());//
                //                row.setSalePrice(liv.getSalePrice());
                //                row.setSalePriceMulCount(liv.getSalePriceMulCount());
                //                row.setFreightMoney(ofv.getFreightMoney());
                //                row.setTotalPrice(ofv.getTotalPrice());//
                //                row.setRealPrice(ofv.getRealPrice());//
                //                row.setAccountPointPrice(liv.getAccountPointPrice());//
                //                row.setCouponPrice(liv.getCouponPrice());//
                //                row.setAdjustPrice(liv.getAdjustPrice());//
                //                row.setSinglePayPrice(liv.getSinglePayPrice());
                //                row.setCost(liv.getCost());
                //                row.setSingleGross(liv.getSingleGross());//
                //                row.setTotalSinglePayPrice(liv.getTotalSinglePayPrice());//
                //                row.setTotalCost(liv.getTotalCost());//
                //                row.setTotalGross(liv.getTotalGross());//
                //                row.setBuyerRemark(ofv.getBuyerRemark());
                //                row.setSellerRemark(ofv.getSellerRemark());
                //                row.setSendTime(ofv.getSendTime());//
                //                row.setLogisticsChannel(ofv.getLogisticsChannel());
                //                row.setLogisticsNumber(ofv.getLogisticsNumber());
                //                row.setUserType(ofv.getUserType());
                //                row.setProductName(ofv.getName());//
                //                row.setPostageSettlementStatus(ofv.getPostageSettlementStatus());
                //                row.setPostageConfirmDate(ofv.getPostageConfirmDate());
                //                row.setPostage(ofv.getPostage());
                //                row.setRefundId(liv.getRefundId() == 0 ? "" : liv.getRefundId() + "");//
                //                row.setRefundType(liv.getRefundType());
                //                row.setRefundCount(liv.getRefundCount() == 0 ? "" : liv.getRefundCount() + "");//
                //                row.setRefundPrice(liv.getRefundPrice());
                //                row.setSellerRefundPrice(liv.getSellerRefundPrice());
                //                row.setGegeRefundPrice(liv.getGegeRefundPrice());
                //                row.setRefundStatus(liv.getRefundStatus());
                //                row.setMoneyStatus(liv.getMoneyStatus());
                //                row.setReceiveGoodsStatus(liv.getReceiveGoodsStatus());
                //                row.setRefundSettlementStatus(liv.getRefundSettlementStatus());
                //                row.setSettlementComfirmDate(liv.getSettlementComfirmDate());
                //                row.setResponsibilityPosition(liv.getResponsibilityPosition());
                //                row.setResponsibilityMoney(liv.getResponsibilityMoney());
                row.setOrderType("getOrderType");//
                row.setNumber("getNumber");//
                row.setHbNumber("getHbNumber");//
                row.setStatus("getStatus");//
                row.setSettlement("getSettlement");//
                row.setSettlementTime("getSettlementTime");
                row.setPayChannel("getPayChannel");//
                row.setCreateTime("getCreateTime");//
                row.setPayTime("getPayTime");//
                row.setSendTime("getSendTime");//
                row.setSellerName("getSellerName");//
                row.setSendAddress("getSendAddress");//
                row.setReceiveFullName("getReceiveFullName");//
                row.setIdCard("getIdCard");//
                row.setReceiveAddress("getReceiveAddress");//
                row.setProvince("getProvince");//
                row.setCity("getCity");//
                row.setDistrict("getDistrict");//
                row.setDetailAddress("getDetailAddress");//
                row.setMobileNumber("getMobileNumber");//
                row.setType("getType");//
                row.setProductId("getProductId");//
                row.setCode("getCode");//
                row.setProductName("getName");//
                row.setCount("getCount");//
                row.setSalePrice("getSalePrice");
                row.setSalePriceMulCount("getSalePriceMulCount");
                row.setFreightMoney("getFreightMoney");
                row.setTotalPrice("getTotalPrice");//
                row.setRealPrice("getRealPrice");//
                row.setAccountPointPrice("getAccountPointPrice");//
                row.setCouponPrice("getCouponPrice");//
                row.setAdjustPrice("getAdjustPrice");//
                row.setSinglePayPrice("getSinglePayPrice");
                row.setCost("getCost");
                row.setSingleGross("getSingleGross");//
                row.setTotalSinglePayPrice("getTotalSinglePayPrice");//
                row.setTotalCost("getTotalCost");//
                row.setTotalGross("getTotalGross");//
                row.setBuyerRemark("getBuyerRemark");
                row.setSellerRemark("getSellerRemark");
                row.setSendTime("getSendTime");//
                row.setLogisticsChannel("getLogisticsChannel");
                row.setLogisticsNumber("getLogisticsNumber");
                row.setUserType("getUserType");
                row.setProductName("getName");//
                row.setPostageSettlementStatus("getPostageSettlementStatus");
                row.setPostageConfirmDate("getPostageConfirmDate");
                row.setPostage("getPostage");
                row.setRefundId("getRefundId");//
                row.setRefundType("getRefundType");
                row.setRefundCount("getRefundCount");//
                row.setRefundPrice("getRefundPrice");
                row.setSellerRefundPrice("getSellerRefundPrice");
                row.setGegeRefundPrice("getGegeRefundPrice");
                row.setRefundStatus("getRefundStatus");
                row.setMoneyStatus("getMoneyStatus");
                row.setReceiveGoodsStatus("getReceiveGoodsStatus");
                row.setRefundSettlementStatus("getRefundSettlementStatus");
                row.setSettlementComfirmDate("getSettlementComfirmDate");
                row.setResponsibilityPosition("getResponsibilityPosition");
                row.setResponsibilityMoney("getResponsibilityMoney");
                rows.add(row);
            }
            worksheet.setRows(rows);
            body.setAttribute("worksheet", worksheet);
            writer.print(body.toString());
            writer.flush();
            rows.clear();
            rows = null;
            worksheet = null;
            body = null;
            Runtime.getRuntime().gc();
        }
        //写入excel文件尾部
        writer.print("</Workbook>");
        writer.flush();
        writer.close();
        System.out.println("生成excel文件完成");
        long endTime = System.currentTimeMillis();
        System.out.println("用时=" + ((endTime - startTimne) / 1000) + "秒");
    }
    
    //    Logger log = Logger.getLogger(TestController.class);
    //
    //    private CacheServiceIF cache = CacheManager.getClient();
    //
    //    @Resource
    //    private AreaService areaService;
    //
    //    @RequestMapping("/setCode")
    //    public ModelAndView setCode(HttpServletRequest request)
    //        throws Exception
    //    {
    //        ModelAndView mv = new ModelAndView("test/zipCode");
    //        return mv;
    //    }
    //
    //    @RequestMapping(value = "/setZipCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    //    @ResponseBody
    //    public String setZipCode(HttpServletRequest request, @RequestParam("zipFile") MultipartFile file)
    //        throws Exception
    //    {
    //        try
    //        {
    //            Map<String, Object> map = new HashMap<String, Object>();
    //            Workbook workbook = new HSSFWorkbook(file.getInputStream());
    //            Sheet sheet = workbook.getSheetAt(0);
    //            int startNum = sheet.getFirstRowNum();
    //            int lastNum = sheet.getLastRowNum();
    //            if (startNum == lastNum)
    //            {// 可过滤第一行，因为第一行是标题
    //                map.put("status", 0);
    //                map.put("msg", "文件为空请确认！");
    //                return JSON.toJSONString(map);
    //            }
    //            List<Map<String, Object>> infoList = new ArrayList<Map<String, Object>>();
    //            for (int i = startNum + 1; i <= lastNum; i++)
    //            {
    //                Row row = sheet.getRow(i);
    //                Cell cell0 = row.getCell(0);
    //                Cell cell1 = row.getCell(1);
    //                Cell cell2 = row.getCell(2);
    //                if (cell0 != null)
    //                {
    //                    cell0.setCellType(Cell.CELL_TYPE_STRING);
    //                }
    //                if (cell1 != null)
    //                {
    //                    cell1.setCellType(Cell.CELL_TYPE_STRING);
    //                }
    //                if (cell2 != null)
    //                {
    //                    cell2.setCellType(Cell.CELL_TYPE_STRING);
    //                }
    //                String s1 = cell0 == null ? "" : cell0.getStringCellValue();
    //                String s2 = cell1 == null ? "" : cell1.getStringCellValue();
    //                String s3 = cell2 == null ? "" : cell2.getStringCellValue();
    //                Map<String, Object> cm = new HashMap<String, Object>();
    //                cm.put("city", s1);
    //                cm.put("zipCode", s3);
    //                infoList.add(cm);
    //            }
    //            areaService.setCityZipCode(infoList);
    //            Map<String, Object> result = new HashMap<String, Object>();
    //            result.put("status", 1);
    //            result.put("msg", "ok");
    //            return JSON.toJSONString(result);
    //        }
    //        catch (Exception e)
    //        {
    //            log.error(e.getMessage(), e);
    //            Map<String, Object> result = new HashMap<String, Object>();
    //            result.put("status", 0);
    //            result.put("msg", "wrong");
    //            return JSON.toJSONString(result);
    //        }
    //    }
    //
    //    @RequestMapping(value = "/birdex", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    //    @ResponseBody
    //    public String birdex(HttpServletRequest request)
    //        throws Exception
    //    {
    //        System.out.println("birdex...");
    //        // System.out.println(request.getParameter("logistics_interface"));
    //        // System.out.println(request.getParameter("version"));
    //        Enumeration<?> temp = request.getParameterNames();
    //        if (null != temp)
    //        {
    //            while (temp.hasMoreElements())
    //            {
    //                String en = (String)temp.nextElement();
    //                String value = request.getParameter(en);
    //                System.out.println("name : " + en + "    value : " + value);
    //            }
    //        }
    //        System.out.println("temp is null");
    //        Map<String, Object> responses = new HashMap<String, Object>();
    //        Map<String, Object> responseItems = new HashMap<String, Object>();
    //        Map<String, Object> response = new HashMap<String, Object>();
    //        response.put("success", true);
    //        response.put("reason", "");
    //        response.put("reasonDesc", "");
    //        responseItems.put("response", response);
    //        responses.put("responseItems", responseItems);
    //        return JSON.toJSONString(responses);
    //    }
    //
    //    @RequestMapping("/test")
    //    public ModelAndView toForm(HttpServletRequest request)
    //        throws Exception
    //    {
    //        Subject s = login("classpath:shiro-realm.ini", "lh", "654321");
    //        if (s != null)
    //        {
    //            System.out.println("登陆状态:");
    //            System.out.println(s.isAuthenticated());
    //            System.out.println("next-role:");
    //            System.out.println(s.hasRole("money"));
    //            System.out.println("next-permitted-delete:");
    //            System.out.println(s.isPermitted("user:delete"));
    //            System.out.println("next-permitted-update:");
    //            System.out.println(s.isPermitted("user:update"));
    //        }
    //        else
    //        {
    //            System.out.println("登陆失败");
    //        }
    //
    //        ModelAndView mv = new ModelAndView();
    //        return mv;
    //    }
    //
    //    public Subject login(String source, String name, String pwd)
    //    {
    //        // 1、获取SecurityManager工厂，此处使用Ini配置文件初始化SecurityManager
    //        Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory(source);
    //
    //        // 2、得到SecurityManager实例 并绑定给SecurityUtils
    //        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
    //        SecurityUtils.setSecurityManager(securityManager);
    //
    //        // 3、得到Subject及创建用户名/密码身份验证Token（即用户身份/凭证）
    //        Subject subject = SecurityUtils.getSubject();
    //        UsernamePasswordToken token = new UsernamePasswordToken(name, pwd.toCharArray());
    //        // 4、登录，即身份验证
    //        try
    //        {
    //            subject.login(token);
    //            return subject;
    //        }
    //        catch (Exception e)
    //        {
    //            return null;
    //        }
    //
    //    }
    //
//    @RequestMapping("/ckeditor")
//    public ModelAndView ckeditor(HttpServletRequest request)
//        throws Exception
//    {
//        ModelAndView mv = new ModelAndView("test/ckeditor");
//        //        cache.set("zyb", System.currentTimeMillis(), 30);
//        return mv;
//    }
    
    //
    //    @RequestMapping("/getCache")
    //    @ResponseBody
    //    public String getCache(HttpServletRequest request)
    //        throws Exception
    //    {
    //        System.out.println("nowC:" + cache.get("zyb"));
    //        return "ok";
    //    }
    
    @RequestMapping("/jsonTest")
    public String jsonTest()
        throws Exception
    {
        return "test/test2";
    }
    
    @RequestMapping(value = "/testjson", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String testjson(@RequestBody String body)
    {
        System.out.println(body);
        JSONObject jsonObject = JSON.parseObject(body);
        System.out.println(jsonObject.toJSONString());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("msg", body);
        return JSON.toJSONString(map);
    }
    
    @RequestMapping(value = "/test10", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String test10()
    {
        //        List<Integer> list1 = ProductUtil.findAllDisplayAndAvailableCustomRegion();
        //        List<Integer> list2 = ProductUtil.findAllDisplayAndAvailableActivitiesCommon();
        //        List<Integer> list3 = ProductUtil.findAllDisplayAndAvailablePage();
        //        List<Integer> list4 = ProductUtil.findAllDisplayAndAvailableCustomActivity();
        //        List<Integer> list5 = ProductUtil.findAllDisplayAndAvailableSpecialActivity();
        //        List<Integer> list6 = ProductUtil.findAllDisplayAndAvailableSimplifyActivity();
        //        List<Integer> list7 = ProductUtil.findAllDisplayAndAvailableSpecialActivityNew();
        //        List<Integer> list8 = ProductUtil.findAllDisplayAndAvailableBannerWindow();
        //        List<Integer> list9 = ProductUtil.findAllDisplayAndAvailableCustomCenter();
        //        List<Integer> list10 = ProductUtil.findAllDisplayAndAvailableCustomGegeRecommend();
        //        List<Integer> list11 = ProductUtil.findAllDisplayAndAvailableSaleWindow();
        //        List<Integer> list12 = ProductUtil.findAllDisplayAndAvailableCustomNavigation();
        List<Integer> list13 = ProductUtil.findAllDisplayAndAvailableProductId();
        return JSON.toJSONString(list13);
    }
}
