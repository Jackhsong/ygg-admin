package com.ygg.admin.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Splitter;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.code.SellerEnum;
import com.ygg.admin.dao.SellerDao;
import com.ygg.admin.entity.*;
import com.ygg.admin.service.*;
import com.ygg.admin.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/seller")
public class SellerController
{
    
    Logger log = Logger.getLogger(SellerController.class);
    
    // 信号量
    final Semaphore semp = new Semaphore(1);
    
    @Resource(name = "sellerService")
    SellerService sellerService = null;
    
    @Resource(name = "saleWindowServcie")
    private SaleWindowServcie saleWindowServcie;
    
    @Resource
    private AreaService areaService;
    
    @Resource
    private SellerExpandService sellerExpandService;
    
    @Resource
    private BrandService brandService;
    
    @Resource
    private UserService userService;
    
    @Resource
    private FinanceSerivce financeSerivce;
    
    @Resource
    private SellerDao sellerDao;
    /**
     * 转发到商家添加页面
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/add")
    public ModelAndView toAdd(HttpServletRequest request)
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            mv.setViewName("seller/update_new");
            SellerEntity seller = new SellerEntity();

            mv.addObject("areasList", new ArrayList<RelationSellerDeliverAreaEntity>());
            mv.addObject("seller", seller);
            mv.addObject("sellerMaster", new SellerMasterEntity());
            mv.addObject("sellerExpand", new SellerExpandEntity());
            
            List<ProvinceEntity> provinceList = areaService.allProvince();
            mv.addObject("provinceList", provinceList);
            
            Map<String, Object> para = new HashMap<String, Object>();
            List<CityEntity> cityList = areaService.findAllCity(para);
            mv.addObject("cityList", cityList);
            
            List<DistrictEntity> districtList = areaService.findAllDistrict(para);
            mv.addObject("districtList", districtList);
            
            //财务结算图片去掉
            /*List<SellerFinanceSettlementPictureEntity> pictureList = sellerService.findSellerFinancePictureBysid(seller.getId());
            mv.addObject("mobileDetails", pictureList);*/
            
            List<SellerOnlineStoreAddressEntity> storeList = sellerService.findSellerOnlineStoreBysid(seller.getId());
            mv.addObject("storeList", storeList);
            
            List<BrandEntity> brandList = brandService.findBrandIsAvailable();
            mv.addObject("brandList", brandList);
            
            List<BrandEntity> sellerBrandList = sellerService.findSellerBrandBysid(seller.getId());
            mv.addObject("sellerBrandList", JSON.toJSONString(sellerBrandList));
            
            List<User> userList = userService.findUserByRole("运营");
            mv.addObject("auditUserList", userList);

            mv.addObject("sellerCategoryList", JSON.toJSONString(new ArrayList<>()));
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    /**
     * 保存商家信息
     * @param request
     * @param sellerId：商家Id
     * @param expandId：商家扩展信息Id
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "商家管理-新增/修改商家信息")
    public String save(HttpServletRequest request,// 
        @RequestParam(value = "sellerId", required = false, defaultValue = "0") int sellerId,//
        @RequestParam(value = "expandId", required = false, defaultValue = "0") int expandId)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            String username = SecurityUtils.getSubject().getPrincipal() + "";
            User user = userService.findByUsername(username);
            
            Map<String, Object> para = new HashMap<String, Object>();
            
            SellerEntity seller = new SellerEntity();
            CommonUtil.wrapParamter2Entity(seller, request);
            seller.setId(sellerId);
            seller.setCreateUser(user.getId());
            seller.setUpdateUser(user.getId());
            //只有当运费结算选择满X元包邮，才有邮费，否则邮费为0
            if (seller.getFreightType() != 2)
            {
                seller.setFreightMoney(0);
            }
            if (seller.getKuaidi().contains("other"))
            {
                seller.setKuaidi(seller.getKuaidi().replaceAll("other", seller.getOtherKuaidi()));
            }
            para.put("seller", seller);
            
            SellerExpandEntity sellerExpand = new SellerExpandEntity();
            CommonUtil.wrapParamter2Entity(sellerExpand, request);
            sellerExpand.setId(expandId);
            sellerExpand.setCreateUser(user.getId());
            sellerExpand.setUpdateUser(user.getId());
            if (seller.getIsOwner() == CommonConstant.COMMON_YES)
            {
                //使用商家后台，则有用户名跟密码
                sellerExpand.setPassword(CommonUtil.strToMD5(sellerExpand.getUsername() + sellerExpand.getPassword()));
            }
            //只有结算方式为扣点时，扣点才有值，否则扣点为0
            if (sellerExpand.getProposalSubmitType() == 1)
            {
                sellerExpand.setProposalDeduction(0);
            }
            para.put("sellerExpand", sellerExpand);
            
            // 假期发货信息必须满足：1、holidayTips、holidayStartTime、holidayEndTime全部为空；2、holidayTips、holidayStartTime、holidayEndTime全部不为空
            if (!((StringUtils.isEmpty(seller.getHolidayTips()) && StringUtils.isEmpty(seller.getHolidayStartTime()) && StringUtils.isEmpty(seller.getHolidayEndTime())) || (StringUtils.isNotEmpty(seller.getHolidayTips())
                && StringUtils.isNotEmpty(seller.getHolidayStartTime()) && StringUtils.isNotEmpty(seller.getHolidayEndTime()))))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "请填写完整的假期发货信息");
                return JSON.toJSONString(resultMap);
            }
            
            if (StringUtils.isEmpty(seller.getHolidayStartTime()))
            {
                seller.setHolidayStartTime("0000-00-00 00:00:00");
            }
            if (StringUtils.isEmpty(seller.getHolidayEndTime()))
            {
                seller.setHolidayEndTime("0000-00-00 00:00:00");
            }
            
            int resultStatus = sellerService.saveOrUpdate(para);
            if (resultStatus == 1)
            {
                resultMap.put("status", resultStatus);
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "保存失败");
            }
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "服务器发生异常，请刷新后重试。");
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 检查商家名称是否存在
     * @param request
     * @param realSellerName
     * @param sellerId
     * @return
     */
    @RequestMapping(value = "/checkName", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String checkName(HttpServletRequest request, String realSellerName, int sellerId)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        try
        {
            SellerEntity seller = sellerService.findSellerByRealSellerName(realSellerName);
            if (seller == null)
            {
                map.put("status", 0);
            }
            else
            {
                if (seller.getId() != sellerId)
                {
                    map.put("status", 1);
                    map.put("msg", "已经存在该商家，是否继续添加?");
                }
            }
        }
        catch (Exception e)
        {
            map.put("status", 1);
            map.put("msg", "已经存在该商家，是否继续添加?");
            log.error("验证商家[" + realSellerName + "]是否存在出错", e);
        }
        return JSON.toJSONString(map);
    }
    
    /**
     * 商家管理列表
     * 
     * @param request
     * @return
     */
    @RequestMapping("/list/{isAvailable}")
    public ModelAndView list(HttpServletRequest request, @PathVariable("isAvailable") int isAvailable)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.addObject("listType", isAvailable == 1 ? "可用商家" : "停用商家");
        mv.addObject("isAvailable", isAvailable);
        mv.setViewName("seller/list");
        return mv;
    }
    
    /**
     * 商家管理列表
     * 
     * @param request
     * @return
     */
    @RequestMapping("/listStop")
    public ModelAndView listStop(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("seller/listStop");
        return mv;
    }
    
    /**
     * 异步获取商家信息
     * 
     * @param request
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "/jsonInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonInfo(HttpServletRequest request, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "30") int rows, @RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "name", required = false, defaultValue = "") String name, @RequestParam(value = "realName", required = false, defaultValue = "") String realName,
        @RequestParam(value = "isOwner", required = false, defaultValue = "-1") int isOwner,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "1") byte isAvailable,
        @RequestParam(value = "categoryId", required = false, defaultValue = "") String categoryIds,
        @RequestParam(value = "responsibilityPerson", required = false, defaultValue = "") String responsibilityPerson,
        @RequestParam(value = "createTimeStart", required = false, defaultValue = "") String createTimeStart,
        @RequestParam(value = "createTimeEnd", required = false, defaultValue = "") String createTimeEnd,
        @RequestParam(value = "depositStatus", required = false, defaultValue = "-1") int depositStatus
    )
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        if (id != 0)
        {
            para.put("id", id);
        }
        if (!"".equals(name))
        {
            para.put("sellerName", "%" + name + "%");
        }
        if (!"".equals(realName))
        {
            para.put("realSellerName", "%" + realName + "%");
        }
        if (isOwner != -1)
        {
            para.put("isOwner", isOwner);
        }
        if (StringUtils.isNotEmpty(categoryIds)) {
            List<String> categoryIdList = Splitter.on(",").splitToList(categoryIds);
            para.put("categoryIdList", categoryIdList);
        }
        if (StringUtils.isNotEmpty(responsibilityPerson)) {
           para.put("responsibilityPerson", responsibilityPerson);
        }
        if (StringUtils.isNotEmpty(createTimeStart)) {
            para.put("createTimeStart", createTimeStart);
        }
        if (StringUtils.isNotEmpty(createTimeEnd)) {
            para.put("createTimeEnd", createTimeEnd);
        }
        if( depositStatus != -1) {
            para.put("depositStatus", depositStatus);
        }
        para.put("isAvailable", isAvailable);
        String jsonInfoString = sellerService.jsonSellerInfo(para);
        return jsonInfoString;
    }
    
    /**
     * 跳转至商家编辑页面
     * 
     * @param request
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/edit/{id}")
    public ModelAndView edit(HttpServletRequest request, @PathVariable("id") int id)
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            mv.setViewName("seller/update_new");
            SellerEntity seller = sellerService.findSellerById(id);// 商家信息
            SellerExpandEntity sellerExpand = sellerExpandService.findSellerExpandBysid(id);//商家从帐号信息
            if (seller == null)
            {
                mv.setViewName("forward:/error/404");
                return mv;
            }
            if (sellerExpand == null)
            {
                sellerExpand = new SellerExpandEntity();
            }

            // 发货类型为：香港（身份证照片）暂时不用
            // if (seller.getSellerType() == SellerEnum.SellerTypeEnum.HONG_KONG.getCode() &&
            // seller.getIsNeedIdCardImage() == 1)
            // {
            // seller.setSellerType((byte)SellerEnum.SellerTypeEnum.HONG_KONG_1.getCode());
            // }
            
            mv.addObject("seller", seller);
            mv.addObject("sellerExpand", sellerExpand);
            
            List<User> userList = userService.findUserByRole("运营");
            mv.addObject("auditUserList", userList);
            
            //财务结算图片，暂时不用
            List<SellerFinanceSettlementPictureEntity> pictureList = sellerService.findSellerFinancePictureBysid(id);
            mv.addObject("mobileDetails", pictureList);
            
            //商家店铺网址
            List<SellerOnlineStoreAddressEntity> storeList = sellerService.findSellerOnlineStoreBysid(id);
            mv.addObject("storeList", storeList);
            
            //收货人省份信息
            List<ProvinceEntity> provinceList = areaService.allProvince();
            mv.addObject("provinceList", provinceList);
            
            Map<String, Object> para = new HashMap<String, Object>();
            if (sellerExpand.getReceiveProvinceCode() != 0)
            {
                para.put("provincId", sellerExpand.getReceiveProvinceCode());
            }
            List<CityEntity> cityList = areaService.findAllCity(para);
            mv.addObject("cityList", cityList);
            
            if (sellerExpand.getReceiveCityCode() != 0)
            {
                para.remove("provincId");
                para.put("cityId", sellerExpand.getReceiveCityCode());
            }
            List<DistrictEntity> districtList = areaService.findAllDistrict(para);
            mv.addObject("districtList", districtList);
            
            List<BrandEntity> brandList = brandService.findBrandIsAvailable();
            mv.addObject("brandList", brandList);

            //  商家分类
            List<Map<String, Object> > categories = sellerDao.findSellerCategoryByRelation(id);
            mv.addObject("sellerCategoryList", JSON.toJSONString(categories == null ? new ArrayList() : categories));
            
            List<BrandEntity> sellerBrandList = sellerService.findSellerBrandBysid(id);
            mv.addObject("sellerBrandList", JSON.toJSONString(sellerBrandList));
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    /**
     * 得到商家信息以json格式返回
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jsonSellerCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonSellerCode(HttpServletRequest request, //
        @RequestParam(value = "isAvailable", required = false, defaultValue = "-1") int isAvailable, //
        @RequestParam(value = "isBirdex", required = false, defaultValue = "0") int isBirdex, //
        @RequestParam(value = "q", required = false, defaultValue = "") String sellerName, //
        @RequestParam(value = "isEdbSeller", required = false, defaultValue = "0") int isEdbSeller, // 是否只展示E店宝商家
        @RequestParam(value = "id", required = false, defaultValue = "0") int id, //
        @RequestParam(value = "isOwner", required = false, defaultValue = "-1") int isOwner)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (isAvailable != -1)
        {
            para.put("isAvailable", isAvailable);
        }
        if (isBirdex != 0)
        {
            para.put("isBirdex", isBirdex);
        }
        if (!"".equals(sellerName))
        {
            para.put("realSellerName", "%" + sellerName + "%");
        }
        if (isOwner != -1)
        {
            para.put("isOwner", isOwner);
        }
        if (isEdbSeller == 1)
        {
            para.put("isEdbSeller", 1);
        }
        List<SellerEntity> sellerList = sellerService.findAllSeller(para);
        List<Map<String, String>> codeList = new ArrayList<Map<String, String>>();
        Map<String, String> mapAll = new HashMap<String, String>();
        if (isAvailable == -1)
        {
            mapAll.put("code", "-1");
            mapAll.put("text", "全部");
            codeList.add(mapAll);
        }
        for (SellerEntity entity : sellerList)
        {
            Map<String, String> map = new HashMap<String, String>();
            map.put("code", entity.getId() + "");
            String p = entity.getIsAvailable() == 1 ? "" : "(已停用)";
            map.put("text", p + entity.getRealSellerName() + "(" + entity.getSendAddress() + "-" + entity.getWarehouse() + ")");
            if (entity.getId() == id)
            {
                map.put("selected", "true");
            }
            codeList.add(map);
        }
        return JSON.toJSONString(codeList);
    }
    
    @RequestMapping(value = "/jsonSellerType", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonSellerType(HttpServletRequest request, @RequestParam(value = "code", required = false, defaultValue = "-1") int code)
        throws Exception
    {
        List<Map<String, Object>> codeList = new ArrayList<Map<String, Object>>();
        for (SellerEnum.SellerTypeEnum it : SellerEnum.SellerTypeEnum.values())
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("code", it.getCode());
            // 发货类型为：香港（身份证照片）暂时不用，不需要区分香港（仅身份证号）和香港（身份证照片）
            /*
             * if (it.getCode() == SellerTypeEnum.HONG_KONG.getCode()) { map.put("text", it.getDescription() +
             * "(仅身份证号)"); }
             * 
             * else if (it.getCode() == SellerTypeEnum.HONG_KONG_1.getCode()) { map.put("text", it.getDescription() +
             * "(身份证照片)"); } else { map.put("text", it.getDescription()); }
             */
            map.put("text", it.getDesc());
            if (code != -1 && it.getCode() == code)
            {
                map.put("selected", true);
            }
            codeList.add(map);
        }
        codeList.add(0, new HashMap<String, Object>()
        {
            {
                put("code", "0");
                put("text", "--请选择--");
            }
        });
        return JSON.toJSONString(codeList);
    }
    
    /**
     * 批量修改招商负责人
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/batchEditResponsibilityPerson", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "商家管理-批量修改招商负责人")
    public String batchEditTip(HttpServletRequest request, @RequestParam(value = "person", required = false, defaultValue = "") String person,
        @RequestParam(value = "ids", required = true) String ids)
        throws Exception
    {
        if ("".equals(person))
        {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", "负责人为空");
            return JSON.toJSONString(resultMap);
        }
        Map<String, Object> para = new HashMap<String, Object>();
        List<Integer> idList = new ArrayList<Integer>();
        if (ids.indexOf(",") > 0)
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
        para.put("person", person);
        para.put("idList", idList);
        try
        {
            
            int result = sellerService.batchUpdateSeller(para);
            
            Map<String, Object> resultMap = new HashMap<String, Object>();
            if (result > 0)
            {
                resultMap.put("status", 1);
                resultMap.put("msg", "成功保存" + result + "条");
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "保存失败");
            }
            return JSON.toJSONString(resultMap);
        }
        catch (Exception e)
        {
            log.error("批量修改商家负责人出错！", e);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
            return JSON.toJSONString(resultMap);
        }
    }
    
    /**
     * 商家结算周期列表
     * 
     * @return
     */
    @RequestMapping("/period")
    public String settlementPeriodList()
    {
        return "seller/period_list";
    }
    
    /**
     * 商家结算周期数据
     * 
     * @param request
     * @param page
     * @param rows
     * @param saleWindowId
     * @param saleWindowType
     * @param saleWindowName
     * @param sellerId
     * @return
     */
    @RequestMapping(value = "/jsonSellerPeriodList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonSellerPeriodList(HttpServletRequest request, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows,
        @RequestParam(value = "saleWindowId", required = false, defaultValue = "0") int saleWindowId,
        @RequestParam(value = "saleWindowType", required = false, defaultValue = "-1") int saleWindowType,
        @RequestParam(value = "saleWindowName", required = false, defaultValue = "") String saleWindowName,
        @RequestParam(value = "sellerId", required = false, defaultValue = "0") int sellerId,
        @RequestParam(value = "isDealWith", required = false, defaultValue = "0") int isDealWith)
    {
        Map<String, Object> para = null;
        Map<String, Object> result = null;
        try
        {
            para = new HashMap<String, Object>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            if (saleWindowId != 0)
            {
                para.put("saleWindowId", saleWindowId);
            }
            if (saleWindowType != -1)
            {
                para.put("saleWindowType", saleWindowType);
            }
            if (!"".equals(saleWindowName))
            {
                para.put("saleWindowName", "%" + saleWindowName + "%");
            }
            if (sellerId != 0)
            {
                para.put("sellerId", sellerId);
            }
            para.put("isDealWith", isDealWith);
            result = sellerService.findAllSaleWindowForSellerPeriod(para);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
        return JSON.toJSONString(result);
    }
    
    @RequestMapping(value = "/dealWithSaleWindow", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String dealWithSaleWindow(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "ids", required = false, defaultValue = "") String ids, @RequestParam(value = "dealWith", required = true) int isDealWith)
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            List<Integer> idList = new ArrayList<Integer>();
            if (id != 0)
            {
                idList.add(Integer.valueOf(id));
            }
            if (!"".equals(ids))
            {
                if (ids.indexOf(",") > 0)
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
            }
            para.put("idList", idList);
            para.put("isDealWith", isDealWith);
            int status = saleWindowServcie.hideSaleWindow(para);
            if (status > 0)
            {
                result.put("status", 1);
            }
            else
            {
                result.put("status", 0);
                result.put("msg", "操作失败");
            }
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
        return JSON.toJSONString(result);
    }
    
    /**
     * 商家实时毛利统计
     */
    @RequestMapping("/sellerGrossCalculation")
    public ModelAndView sellerGrossCalculation(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("seller/sellerGrossCalculation");
        DateTime.now().withTimeAtStartOfDay();
        mv.addObject("startTime", DateTime.now().withTimeAtStartOfDay().plusDays(1 - DateTime.now().withTimeAtStartOfDay().getDayOfMonth()).toString("yyyy-MM-dd HH:mm:ss"));
        mv.addObject("endTime", DateTime.now().withTimeAtStartOfDay().plusDays(1).toString("yyyy-MM-dd HH:mm:ss"));
        return mv;
    }
    
    /**
     * 异步获取 商家实时毛利统计 数据
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jsonSellerGrossCalculation", produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "数据统计-异步获取商家实时毛利统计数据")
    public String jsonSellerGrossCalculation(@RequestParam(value = "startTime", required = false, defaultValue = "") String startTime, //
        @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime, //
        @RequestParam(value = "search", required = false, defaultValue = "1") int search)
        throws Exception
    {
        try
        {
            if (search == 0)
            {
                return JSON.toJSONString(new ArrayList());
            }
            Map<String, Object> para = new HashMap<>();
            para.put("startTimeBegin", startTime);
            para.put("startTimeEnd", endTime);
            Map<String, Object> result = financeSerivce.findSellerGrossCalculation(para);
            List<Map<String, Object>> rows = (List<Map<String, Object>>)result.get("rows");
            return JSON.toJSONString(rows);
        }
        catch (Exception e)
        {
            log.error("异步获取 商家实时毛利统计 数据 失败！", e);
            List<Map<String, Object>> rows = new ArrayList<>();
            return JSON.toJSONString(rows);
        }
    }
    
    /**
     * 导出 商家实时毛利统计 数据
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/exportSellerGrossCalculation", produces = "application/json;charset=UTF-8")
    @ControllerLog(description = "商家管理-导出商家实时毛利统计")
    public void exportSellerGrossCalculation(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "startTime", required = false, defaultValue = "") String startTime, //
        @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime, //
        @RequestParam(value = "sellerId", required = true) int sellerId)
        throws Exception
    {
        OutputStream fOut = null;
        Workbook workbook = null;
        try
        {
            Map<String, Object> para = new HashMap<>();
            para.put("startTimeBegin", startTime);
            para.put("startTimeEnd", endTime);
            para.put("sellerId", sellerId);
            Map<String, Object> result = financeSerivce.findSellerGrossCalculationDetail(para);
            List<Map<String, Object>> rows = (List<Map<String, Object>>)result.get("rows");
            String[] title =
                {"订单编号", "订单状态", "付款日期", "商品ID", "商品名称", "件数", "单价", "总价", "运费", "订单总价", "订单实付", "应付商家", "模拟运费金额", "订单净毛利", "订单净毛利率", "分摊总价", "总供货价", "分摊模拟运费", "商品毛利", "商品毛利率",
                    "分摊积分优惠", "分摊优惠券优惠", "分摊客服调价", "商品实付", "商品净毛利", "商品净毛利率"};
            workbook = POIUtil.createXSSFWorkbookTemplate(title);
            Sheet sheet = workbook.getSheetAt(0);
            int rIndex = 1;
            for (Map<String, Object> it : rows)
            {
                int cellIndex = 0;
                Row r = sheet.createRow(rIndex++);
                r.createCell(cellIndex++).setCellValue(it.get("number") + "");
                r.createCell(cellIndex++).setCellValue(it.get("status") + "");
                r.createCell(cellIndex++).setCellValue(it.get("payTime") + "");
                r.createCell(cellIndex++).setCellValue(it.get("productId") + "");
                r.createCell(cellIndex++).setCellValue(it.get("productName") + "");
                r.createCell(cellIndex++).setCellValue(it.get("productCount") + "");
                r.createCell(cellIndex++).setCellValue(it.get("productSalePrice") + "");
                r.createCell(cellIndex++).setCellValue(it.get("productTotalSalePrice") + "");
                r.createCell(cellIndex++).setCellValue(it.get("orderFreightMoney") + "");
                r.createCell(cellIndex++).setCellValue(it.get("orderTotalPrice") + "");
                r.createCell(cellIndex++).setCellValue(it.get("orderPayPrice") + "");
                r.createCell(cellIndex++).setCellValue(it.get("sellerPrice") + "");
                r.createCell(cellIndex++).setCellValue(it.get("freight") + "");
                r.createCell(cellIndex++).setCellValue(it.get("gross") + "");
                r.createCell(cellIndex++).setCellValue(it.get("grossRate") + "");
                r.createCell(cellIndex++).setCellValue(it.get("coeTotalPrice") + "");
                r.createCell(cellIndex++).setCellValue(it.get("sellerPrice") + "");
                r.createCell(cellIndex++).setCellValue(it.get("coeFreight") + "");
                r.createCell(cellIndex++).setCellValue(it.get("opGross") + "");
                r.createCell(cellIndex++).setCellValue(it.get("opGrossRate") + "");
                r.createCell(cellIndex++).setCellValue(it.get("pointProportion") + "");
                r.createCell(cellIndex++).setCellValue(it.get("couponProportion") + "");
                r.createCell(cellIndex++).setCellValue(it.get("adjustProportion") + "");
                r.createCell(cellIndex++).setCellValue(it.get("productRealPrice") + "");
                r.createCell(cellIndex++).setCellValue(it.get("productGross") + "");
                r.createCell(cellIndex++).setCellValue(it.get("productGrossRate") + "");
            }
            String fileName = "商家实时毛利统计详细情况";
            response.setContentType("application/vnd.ms-excel");
            // 进行转码，使其支持中文文件名
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xlsx");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            fOut.close();
        }
        catch (Exception e)
        {
            log.error("导出出错", e);
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            response.setHeader("content-disposition", "");
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
    
    /**
     * 导出时间段全部商家明细
     */
    @RequestMapping(value = "/exportAllSellerGrossCalculation")
    @ControllerLog(description = "数据统计-实时毛利-导出时间段全部商家明细")
    public void exportAllSellerGrossCalculation(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "startTime", required = false, defaultValue = "") String startTime, //
        @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime)
        throws Exception
    {
        FileInputStream downFile = null;
        OutputStream servletOutPutStream = null;
        String errorMessage = "系统出错或无导出数据";
        try
        {
            if (semp.availablePermits() < 1)
            {
                errorMessage = "已有人在做导出操作，请稍后。";
                throw new RuntimeException();
            }
            // 获取许可
            semp.acquire();
            
            Date b = CommonUtil.string2Date(startTime, "yyyy-MM-dd HH:mm:ss");
            Date e = CommonUtil.string2Date(endTime, "yyyy-MM-dd HH:mm:ss");
            if ((e.getTime() - b.getTime()) / 1000 > (32 * 24 * 60 * 60))
            {
                errorMessage = "时间跨度最大限制一个月，请缩小范围！";
                throw new RuntimeException(errorMessage);
            }
            
            Map<String, Object> para = new HashMap<>();
            para.put("startTimeBegin", startTime);
            para.put("startTimeEnd", endTime);
            String result = financeSerivce.exportAllSellerGrossCalculationDetail(para);
            String dowName = "商家毛利实时统计压缩包";
            String zipName = result + ".zip";
            ZipCompressorByAnt zca = new ZipCompressorByAnt(zipName);
            zca.compressExe(result);
            downFile = new FileInputStream(zipName);
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setContentType("application/x-msdownload;charset=utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(dowName + ".zip", "utf-8"));
            servletOutPutStream = response.getOutputStream();
            // 设置缓冲区为1024个字节，即1KB
            byte bytes[] = new byte[1024];
            int len = 0;
            // 读取数据。返回值为读入缓冲区的字节总数,如果到达文件末尾，则返回-1
            while ((len = downFile.read(bytes)) != -1)
            {
                // 将指定 byte数组中从下标 0 开始的 len个字节写入此文件输出流,(即读了多少就写入多少)
                servletOutPutStream.write(bytes, 0, len);
            }
            FileUtil.deleteFile(result);
            FileUtil.deleteFile(zipName);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            response.setHeader("Content-disposition", "");
            String errorStr = "<script>alert('" + errorMessage + "');window.history.back();</script>";
            if (servletOutPutStream == null)
            {
                servletOutPutStream = response.getOutputStream();
            }
            servletOutPutStream.write(errorStr.getBytes());
            servletOutPutStream.close();
        }
        finally
        {
            // 访问完后，释放
            semp.release();
            if (servletOutPutStream != null)
            {
                servletOutPutStream.close();
            }
            if (downFile != null)
            {
                downFile.close();
            }
        }
    }
    
    /**
     * 合并商家列表
     * @return
     */
    @RequestMapping("/mergeSellerList")
    public ModelAndView mergeSellerList()
    {
        return new ModelAndView("seller/mergeSellerList");
    }
    
    /**
     * 异步加载合并商家列表
     * @param page
     * @param rows
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/mergeSellerJsonInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String mergeSellerJsonInfo(//
        @RequestParam(value = "page", required = false, defaultValue = "1") int page,//
        @RequestParam(value = "rows", required = false, defaultValue = "30") int rows)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            resultMap = sellerService.mergeSellerJsonInfo(para);
        }
        catch (Exception e)
        {
            log.error("异步加载合并商家列表出错了", e);
            resultMap.put("total", 0);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 合并商家
     * @param request
     * @return
     */
    @RequestMapping(value = "/mergeSeller", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String mergeSeller(HttpServletRequest request)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String masterId = request.getParameter("masterId");
        String[] slaveId = request.getParameterValues("slaveId");
        try
        {
            if (StringUtils.isEmpty(masterId) || slaveId == null || slaveId.length == 0)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "主商家ID和关联商家ID必填");
                return JSON.toJSONString(resultMap);
            }
            Set<String> slaveIdSet = new HashSet<String>(Arrays.asList(slaveId));
            Iterator<String> iterator = slaveIdSet.iterator();
            while (iterator.hasNext())
            {
                String slave = iterator.next();
                if (StringUtils.isEmpty(slave) || "0".equals(slave))
                {
                    iterator.remove();
                }
            }
            if (slaveIdSet.size() == 0)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "主商家ID和关联商家ID必填");
                return JSON.toJSONString(resultMap);
            }
            
            if (slaveIdSet.contains(masterId))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "商家Id=" + masterId + "不能同时作为主帐号和从帐号");
                return JSON.toJSONString(resultMap);
            }
            
            String username = SecurityUtils.getSubject().getPrincipal() + "";
            User user = userService.findByUsername(username);
            resultMap = sellerService.mergeSeller(masterId, new ArrayList<String>(slaveIdSet), user.getId());
        }
        catch (Exception e)
        {
            log.error("合并商家出错,主帐号：" + masterId + ",从帐号：" + Arrays.toString(slaveId), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "合并商家出错，请稍后再试");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/findSellerById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String findSellerById(@RequestParam(value = "sellerId", required = true) int sellerId)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            SellerEntity seller = sellerService.findSellerById(sellerId);
            if (seller == null)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "Id=" + sellerId + "的商家不存在");
            }
            else
            {
                resultMap.put("status", 1);
                resultMap.put("msg", "商家名称：" + seller.getRealSellerName() + "；发货地：" + seller.getSendAddress());
            }
        }
        catch (Exception e)
        {
            log.error("查找商家信息出错,sellerId=" + sellerId, e);
            resultMap.put("status", 0);
            resultMap.put("msg", "服务器忙，请稍后再试");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/editMergeSeller", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String editMergeSeller(@RequestParam(value = "masterId", required = true) int masterId)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<SellerMasterAndSlaveEntity> slaveList = new ArrayList<>();
        try
        {
            slaveList = sellerService.findSellerSlaveListByMasterId(masterId);
            resultMap.put("masterId", masterId);
            resultMap.put("slaveList", slaveList);
        }
        catch (Exception e)
        {
            log.error("编辑合并商家出错了,主商家ID=" + masterId, e);
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "updatePassword", produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "商家管理-修改商家密码")
    public String updatePWD(//
        @RequestParam(value = "sellerId", required = false, defaultValue = "0") int id,//
        @RequestParam(value = "pwd", required = false, defaultValue = "") String pwd,//
        @RequestParam(value = "pwd1", required = false, defaultValue = "") String _pwd)
        throws Exception
    {
        
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            SellerEntity seller = sellerService.findSellerById(id);
            SellerExpandEntity sellerExpand = sellerExpandService.findSellerExpandBysid(id);
            if (seller == null || sellerExpand == null)
            {
                result.put("status", 0);
                result.put("msg", "商家不存在");
                return JSON.toJSONString(result);
            }
            
            if (seller.getIsOwner() == CommonConstant.COMMON_NO)
            {
                result.put("status", 0);
                result.put("msg", "该商家不使用商家后台，无需密码");
                return JSON.toJSONString(result);
            }
            if (!pwd.equals(_pwd))
            {
                result.put("status", 0);
                result.put("msg", "两次输入的密码不一致");
                return JSON.toJSONString(result);
            }
            if (!Pattern.matches("^\\w{6,16}$", pwd))
            {
                result.put("status", 0);
                result.put("msg", "密码只允许6-16位的数字和26个英文字符的组合");
                return JSON.toJSONString(result);
            }
            
            sellerExpand.setPassword(CommonUtil.strToMD5(sellerExpand.getUsername() + pwd));
            int status = sellerService.updatePassword(sellerExpand);
            result.put("status", status > 0 ? 1 : status);
            result.put("msg", status > 0 ? "修改成功" : "修改失败");
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            log.error("修改商家密码失败", e);
            result.put("status", 0);
            result.put("msg", "修改失败");
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 导出 商家
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/exportSeller", produces = "application/json;charset=UTF-8")
    @ControllerLog(description = "商家管理-导出商家")
    public void exportSeller(HttpServletRequest request, HttpServletResponse response, //
        @RequestParam(value = "sellerId", required = false, defaultValue = "0") int sellerId, //
        @RequestParam(value = "realSellerName", required = false, defaultValue = "") String realSellerName, //
        @RequestParam(value = "sellerName", required = false, defaultValue = "") String sellerName,//
        @RequestParam(value = "isOwner", required = false, defaultValue = "-1") int isOwner,//
        @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable,
        @RequestParam(value = "categoryId", required = false, defaultValue = "") String categoryIds,
        @RequestParam(value = "responsibilityPerson", required = false, defaultValue = "") String responsibilityPerson,
        @RequestParam(value = "createTimeStart", required = false, defaultValue = "") String createTimeStart,
        @RequestParam(value = "createTimeEnd", required = false, defaultValue = "") String createTimeEnd
    )
        throws Exception
    {
        OutputStream fOut = null;
        Workbook workbook = null;
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (sellerId != 0)
            {
                para.put("id", sellerId);
            }
            if (!"".equals(sellerName))
            {
                para.put("sellerName", "%" + sellerName + "%");
            }
            if (!"".equals(realSellerName))
            {
                para.put("realSellerName", "%" + realSellerName + "%");
            }
            para.put("isAvailable", isAvailable);
            if (isOwner != -1)
            {
                para.put("isOwner", isOwner);
            }
            if (StringUtils.isNotEmpty(categoryIds)) {
                List<String> categoryIdList = Splitter.on(",").splitToList(categoryIds);
                para.put("categoryIdList", categoryIdList);
            }
            if (StringUtils.isNotEmpty(responsibilityPerson)) {
                para.put("responsibilityPerson", responsibilityPerson);
            }
            if (StringUtils.isNotEmpty(createTimeStart)) {
                para.put("createTimeStart", createTimeStart);
            }
            if (StringUtils.isNotEmpty(createTimeEnd)) {
                para.put("createTimeEnd", createTimeEnd);
            }
            /**
             * 序号，页面展示名称，真实商家名称，招商负责人，公司名称，发货类型，发货地， 发货联系人（姓名及号码、QQ），发货时效说明，周末是否发货

             */
            List<SellerEntity> sellerList = sellerService.findAllSeller(para);
            String[] title =
                {"ID", "页面展示名称", "真实商家名称", "招商负责人", "审核负责人", "公司名称", "发货类型", "发货地", "发货联系人姓名", "发货联系人手机", "发货联系人QQ", "发货联系人邮箱", "售后联系人姓名", "售后联系人手机", "售后联系人QQ", "售后联系人邮箱",
                    "运营联系人姓名", "运营联系人手机", "运营联系人QQ", "运营联系人邮箱", "结算联系人姓名", "结算联系人手机", "结算联系人QQ", "结算联系人邮箱", "发货时效说明", "周末是否发货", "是否使用商家后台"};
            workbook = POIUtil.createXSSFWorkbookTemplate(title);
            Sheet sheet = workbook.getSheetAt(0);
            int rIndex = 1;
            for (SellerEntity seller : sellerList)
            {
                int cellIndex = 0;
                Row r = sheet.createRow(rIndex++);
                r.createCell(cellIndex++).setCellValue(seller.getId() + "");
                r.createCell(cellIndex++).setCellValue(seller.getSellerName());
                r.createCell(cellIndex++).setCellValue(seller.getRealSellerName());
                r.createCell(cellIndex++).setCellValue(seller.getResponsibilityPerson());
                SellerExpandEntity see = sellerExpandService.findSellerExpandBysid(seller.getId());
                r.createCell(cellIndex++).setCellValue(see == null ? "" : userService.findUserById(see.getAuditUserId()) == null ? ""
                    : userService.findUserById(see.getAuditUserId()).getRealname());
                r.createCell(cellIndex++).setCellValue(seller.getCompanyName());
                r.createCell(cellIndex++).setCellValue(SellerEnum.SellerTypeEnum.getDescByCode(seller.getSellerType()));
                r.createCell(cellIndex++).setCellValue(seller.getSendAddress());
                r.createCell(cellIndex++).setCellValue(seller.getFhContactPerson());
                r.createCell(cellIndex++).setCellValue(seller.getFhContactMobile());
                r.createCell(cellIndex++).setCellValue(seller.getFhqq());
                r.createCell(cellIndex++).setCellValue(seller.getFhEmail());
                r.createCell(cellIndex++).setCellValue(seller.getShContactPerson());
                r.createCell(cellIndex++).setCellValue(seller.getShContactMobile());
                r.createCell(cellIndex++).setCellValue(seller.getShqq());
                r.createCell(cellIndex++).setCellValue(seller.getShEmail());
                r.createCell(cellIndex++).setCellValue(seller.getYyContactPerson());
                r.createCell(cellIndex++).setCellValue(seller.getYyContactMobile());
                r.createCell(cellIndex++).setCellValue(seller.getYyqq());
                r.createCell(cellIndex++).setCellValue(seller.getYyEmail());
                r.createCell(cellIndex++).setCellValue(seller.getJsContactPerson());
                r.createCell(cellIndex++).setCellValue(seller.getJsContactMobile());
                r.createCell(cellIndex++).setCellValue(seller.getJsqq());
                r.createCell(cellIndex++).setCellValue(seller.getJsEmail());
                r.createCell(cellIndex++).setCellValue(SellerEnum.SellerSendTimeTypeEnum.getDescByCode(seller.getSendTimeType()));
                r.createCell(cellIndex++).setCellValue(SellerEnum.WeekendSendTypeEnum.getDescByCode(seller.getIsSendWeekend()));
                r.createCell(cellIndex++).setCellValue(seller.getIsOwner() == 1 ? "是" : "否");
            }
            String fileName = "商家（" + (isAvailable == 1 ? "可用" : "停用") + "）";
            response.setContentType("application/vnd.ms-excel");
            // 进行转码，使其支持中文文件名
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xlsx");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            fOut.close();
        }
        catch (Exception e)
        {
            log.error("导出商家出错", e);
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            response.setHeader("content-disposition", "");
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
    
    /**
     * 得到商家信息以json格式返回
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getSellerCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getSellerCode(HttpServletRequest request, //
        @RequestParam(value = "isAvailable", required = false, defaultValue = "-1") int isAvailable, //
        @RequestParam(value = "isBirdex", required = false, defaultValue = "0") int isBirdex, //
        @RequestParam(value = "q", required = false, defaultValue = "") String sellerName, //
        @RequestParam(value = "isEdbSeller", required = false, defaultValue = "0") int isEdbSeller, // 是否只展示E店宝商家
        @RequestParam(value = "id", required = false, defaultValue = "0") int id, //
        @RequestParam(value = "isOwner", required = false, defaultValue = "-1") int isOwner)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (isAvailable != -1)
        {
            para.put("isAvailable", isAvailable);
        }
        if (isBirdex != 0)
        {
            para.put("isBirdex", isBirdex);
        }
        if (!"".equals(sellerName))
        {
            para.put("realSellerName", "%" + sellerName + "%");
        }
        if (isOwner != -1)
        {
            para.put("isOwner", isOwner);
        }
        if (isEdbSeller == 1)
        {
            para.put("isEdbSeller", 1);
        }
        List<SellerEntity> sellerList = sellerService.findAllSeller(para);
        List<Map<String, String>> codeList = new ArrayList<Map<String, String>>();
        Map<String, String> mapAll = new HashMap<String, String>();
        if (isAvailable == -1)
        {
            mapAll.put("code", "-1");
            mapAll.put("text", "全部");
            codeList.add(mapAll);
        }
        for (SellerEntity entity : sellerList)
        {
            Map<String, String> map = new HashMap<String, String>();
            map.put("code", entity.getId() + "");
            map.put("text", entity.getRealSellerName());
            if (entity.getId() == id)
            {
                map.put("selected", "true");
            }
            codeList.add(map);
        }
        return JSON.toJSONString(codeList);
    }
    
    @RequestMapping(value = "updateAvailableStatus", produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "商家管理-修改商家可用状态")
    public String updateAvailableStatus(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "0") int isAvailable)
    {
        
        try
        {
            return sellerService.updateAvailableStatus(id, isAvailable);
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<String, Object>();
            log.error("修改商家可用状态失败", e);
            result.put("status", 0);
            result.put("msg", "修改失败");
            return JSON.toJSONString(result);
        }
    }
    
    @RequestMapping(value = "updateEcount")
    @ResponseBody
    @ControllerLog(description = "商家管理-解锁")
    public Object updateEcount(@RequestParam(value = "id", required = false, defaultValue = "0") int id)
    {
        
        try
        {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 1);
            result.put("data", sellerExpandService.updateEcount(id));
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<String, Object>();
            log.error("修改商家可用状态失败", e);
            result.put("status", 0);
            result.put("msg", "修改失败");
            return JSON.toJSONString(result);
        }
    }
}
