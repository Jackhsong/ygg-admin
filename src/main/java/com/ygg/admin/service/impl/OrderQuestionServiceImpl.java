package com.ygg.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.code.OrderEnum;
import com.ygg.admin.dao.OrderDao;
import com.ygg.admin.dao.OrderQuestionDao;
import com.ygg.admin.dao.UserDao;
import com.ygg.admin.entity.OrderQuestionEntity;
import com.ygg.admin.entity.OrderQuestionProgressEntity;
import com.ygg.admin.entity.OrderQuestionTemplateEntity;
import com.ygg.admin.entity.User;
import com.ygg.admin.service.OrderQuestionService;
import com.ygg.admin.util.CommonConstant;
import com.ygg.admin.util.DateTimeUtil;
import com.ygg.admin.util.MathUtil;
import com.ygg.admin.util.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("orderQuestionService")
public class OrderQuestionServiceImpl implements OrderQuestionService
{
    @Resource
    private OrderQuestionDao orderQuestionDao;
    
    @Resource
    private UserDao userDao;
    
    @Resource
    private OrderDao orderDao;
    
    @Override
    public String findOrderQuestionInfoStr(int orderId)
        throws Exception
    {
        List<Map<String, Object>> infoList = orderQuestionDao.findOrderQuestionListByOrderId(orderId);
        DateTime nowTime = DateTime.now();
        for (Map<String, Object> map : infoList)
        {
            int leftMinute = 0;
            int id = Integer.parseInt(map.get("id") + "");
            int customerStatus = (int)map.get("customerStatus");
            int sellerStatus = (int)map.get("sellerStatus");
            int limitType = (int)map.get("timeLimitType");
            DateTime createTime = DateTimeUtil.string2DateTime(map.get("createTime") + "", "yyyy-MM-dd HH:mm:ss.SSS");
            DateTime endTime = null;
            if (limitType == 1)
            {
                endTime = createTime.plusHours(1);
            }
            else if (limitType == 2)
            {
                endTime = createTime.plusHours(2);
            }
            else if (limitType == 3)
            {
                endTime = DateTimeUtil.string2DateTime(map.get("customTime") + "", "yyyy-MM-dd HH:mm:ss.SSS");
            }
            //已完结(只有当顾客状态和商家状态同时完结的时候才是已完结)
            if (customerStatus == 2 && sellerStatus == 2)
            {
                DateTime customerFinishTime = DateTimeUtil.string2DateTime(map.get("customerFinishTime") + "", "yyyy-MM-dd HH:mm:ss.SSS");
                DateTime sellerFinishTime = DateTimeUtil.string2DateTime(map.get("sellerFinishTime") + "", "yyyy-MM-dd HH:mm:ss.SSS");
                if (customerFinishTime.isBefore(sellerFinishTime))
                {
                    leftMinute = Minutes.minutesBetween(sellerFinishTime, endTime).getMinutes();
                }
                else
                {
                    leftMinute = Minutes.minutesBetween(customerFinishTime, endTime).getMinutes();
                }
            }
            else
            {
                leftMinute = Minutes.minutesBetween(nowTime, endTime).getMinutes();
            }
            int createUser = Integer.parseInt(map.get("createUser") + "");
            User user = userDao.findUserById(createUser);
            map.put("createUser", user == null ? "匿名用户" : user.getRealname());
            map.put("customerStatusStr", customerStatus == 1 ? "进行中" : "已完结");
            map.put("sellerStatusStr", sellerStatus == 1 ? "进行中" : "已完结");
            map.put("leftTime", leftMinute <= 0 ? "已超时" : "剩" + MathUtil.round(leftMinute * 1.0 / 60, 1) + "小时");
            
            //顾客问题处理进度
            /*StringBuilder sbCustomerDesc = new StringBuilder();
            sbCustomerDesc.append(createTime.toString("yyyy-MM-dd HH:mm:ss.S"))
                .append(" ")
                .append(user == null ? "匿名用户" : user.getRealname())
                .append(" ")
                .append(map.get("questionDesc") + "")
                .append("<br/>");
            ;
            List<Map<String, Object>> cProgressList = orderQuestionDao.findCustomerProgressListByqid(id);
            for (Map<String, Object> it : cProgressList)
            {
                String cpCreateTime = ((Timestamp)it.get("createTime")).toString();
                int cpCreateUserId = Integer.parseInt(it.get("createUser") + "");
                User cpCreateUser = userDao.findUserById(cpCreateUserId);
                sbCustomerDesc.append(cpCreateTime)
                    .append(" ")
                    .append(cpCreateUser == null ? "匿名用户" : cpCreateUser.getRealname())
                    .append(" ")
                    .append(it.get("content") + "")
                    .append("<br/>");
            }
            map.put("customerDealDetail", sbCustomerDesc.toString());*/
            
            //商家问题处理进度
            /*StringBuilder sbSellerDesc = new StringBuilder();
            List<Map<String, Object>> sProgressList = orderQuestionDao.findSellerProgressListByqid(id);
            for (Map<String, Object> it : sProgressList)
            {
                String spCreateTime = ((Timestamp)it.get("createTime")).toString();
                int spCreateUserId = Integer.parseInt(it.get("createUser") + "");
                User spCreateUser = userDao.findUserById(spCreateUserId);
                sbSellerDesc.append(spCreateTime)
                    .append(" ")
                    .append(spCreateUser == null ? "匿名用户" : spCreateUser.getRealname())
                    .append(" ")
                    .append(it.get("content") + "")
                    .append("<br/>");
            }
            map.put("sellerDealDetail", sbSellerDesc.toString());*/
        }
        return JSON.toJSONString(infoList);
    }
    
    @Override
    public Map<String, Object> jsonQuestionTemplateInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> infoList = orderQuestionDao.findAllQuestionTemplate(para);
        int total = 0;
        if (infoList.size() > 0)
        {
            for (Map<String, Object> map : infoList)
            {
                map.put("status", ((int)map.get("isAvailable")) == 1 ? "可用" : "不可用");
                map.put("limitHour", map.get("limitHour") + "");
                map.put("limitHourStr", map.get("limitHour") + "小时");
            }
            total = orderQuestionDao.countQuestionTemplate(para);
        }
        resultMap.put("rows", infoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public int saveQuestionTemplate(int id, String name, String limitHour)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("name", name);
        para.put("limitHour", limitHour);
        if (id == 0)
        {
            return orderQuestionDao.saveQuestionTemplate(para);
        }
        else
        {
            para.put("id", id);
            return orderQuestionDao.updateQuestionTemplate(para);
        }
    }
    
    @Override
    public int updateOrderQuestionTemplateStatus(Map<String, Object> para)
        throws Exception
    {
        return orderQuestionDao.updateOrderQuestionTemplateStatus(para);
    }
    
    @Override
    public int addOrderQuestion(OrderQuestionEntity question, String[] imageArray)
        throws Exception
    {
        int result = orderQuestionDao.addOrderQuestion(question);
        if (result == 1)
        {
            if (imageArray.length > 0)
            {
                List<Map<String, Object>> imageList = new ArrayList<Map<String, Object>>();
                for (String image : imageArray)
                {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("questionId", question.getId());
                    map.put("image", image);
                    imageList.add(map);
                }
                orderQuestionDao.insertOrderQuestionImage(imageList);
            }
        }
        return result;
    }
    
    @Override
    public Map<String, Object> findOrderQuestionDetailInfo(int id)
        throws Exception
    {
        Map<String, Object> resultMap = null;
        OrderQuestionEntity question = orderQuestionDao.findOrderQuestionById(id);
        if (question == null)
            return resultMap;
        
        resultMap = new HashMap<String, Object>();
        String receiveMobile = orderDao.findReceiveMobileNumberByOrderId(question.getOrderId() + "");
        int count = orderQuestionDao.countDealingOrderQuestionByOrderId(question.getOrderId());
        User createUser = userDao.findUserById(question.getCreateUser());
        DateTime ceateTime = DateTimeUtil.string2DateTime(question.getCreateTime(), "yyyy-MM-dd HH:mm:ss.SSS");
        resultMap.put("isPush", question.getIsPush());
        resultMap.put("id", question.getId() + "");
        resultMap.put("orderId", question.getOrderId() + "");
        resultMap.put("count", count + "");
        resultMap.put("customerStatus", question.getCustomerStatus() == 1 ? "进行中" : "已完结");
        resultMap.put("sellerStatus", question.getSellerStatus() == 1 ? "进行中" : "已完结");
        resultMap.put("createTime", question.getCreateTime());
        resultMap.put("createUser", createUser == null ? "未知" : createUser.getRealname());
        resultMap.put("questionType", question.getTemplateName());
        resultMap.put("receiveMobile", StringUtils.isEmpty(receiveMobile) ? "" : receiveMobile);
        if (question.getTimeLimitType() != 3)
        {
            resultMap.put("timeLimit", question.getTimeLimitType() + "小时");
        }
        else
        {
            resultMap.put("timeLimit", question.getCustomTime());
        }
        List<String> imageList = orderQuestionDao.findOrderQuestionImageListqid(id);
        resultMap.put("imageList", imageList);
        resultMap.put("questionDesc", question.getQuestionDesc());
        
        //顾客问题处理进度
        List<Map<String, Object>> cProgressList = orderQuestionDao.findCustomerProgressListByqid(id);
        for (Map<String, Object> it : cProgressList)
        {
            DateTime cpCreateTime = DateTimeUtil.string2DateTime(((Timestamp)it.get("createTime")).toString(), "yyyy-MM-dd HH:mm:ss.SSS");
            int cid = Integer.parseInt(it.get("id") + "");
            int cpCreateUserId = Integer.parseInt(it.get("createUser") + "");
            int timeBetween = Minutes.minutesBetween(ceateTime, cpCreateTime).getMinutes();
            int status = Integer.parseInt(it.get("status") + "");
            User cpCreateUser = userDao.findUserById(cpCreateUserId);
            String operation = (cpCreateUser == null ? "匿名用户" : cpCreateUser.getRealname()) + MathUtil.round(timeBetween * 1.0 / 60, 1) + "小时后" + (status == 1 ? "更新问题" : "完结问题");
            it.put("createTime", ((Timestamp)it.get("createTime")).toString());
            it.put("operation", operation);
            List<String> cimageList = orderQuestionDao.findCustomerProgressImagesBycid(cid);
            it.put("images", cimageList);
        }
        resultMap.put("cProgressList", cProgressList);
        
        //商家问题处理进度
        List<Map<String, Object>> sProgressList = orderQuestionDao.findSellerProgressListByqid(id);
        for (Map<String, Object> it : sProgressList)
        {
            DateTime spCreateTime = DateTimeUtil.string2DateTime(((Timestamp)it.get("createTime")).toString(), "yyyy-MM-dd HH:mm:ss.SSS");
            int sid = Integer.parseInt(it.get("id") + "");
            int spCreateUserId = Integer.parseInt(it.get("createUser") + "");
            int timeBetween = Minutes.minutesBetween(ceateTime, spCreateTime).getMinutes();
            int status = Integer.parseInt(it.get("status") + "");
            User spCreateUser = userDao.findUserById(spCreateUserId);
            String operation =
                (spCreateUser == null ? "匿名用户" : spCreateUser.getRealname()) + MathUtil.round(timeBetween * 1.0 / 60, 1) + "小时后" + (status == 1 ? "更新对接情况" : "完结对接情况");
            it.put("createTime", ((Timestamp)it.get("createTime")).toString());
            it.put("operation", operation);
            List<String> cimageList = orderQuestionDao.findSellerProgressImagesBysid(sid);
            it.put("images", cimageList);
        }
        resultMap.put("sProgressList", sProgressList);
        
        //商家反馈记录
        if (question.getIsPush() == CommonConstant.COMMON_YES)
        {
            List<Map<String, Object>> feedBackList = orderQuestionDao.findSellerFeedbackDetailListByqid(id);
            for (Map<String, Object> it : feedBackList)
            {
                it.put("createTime", ((Timestamp)it.get("createTime")).toString());
            }
            resultMap.put("feedBackList", feedBackList);
        }
        return resultMap;
    }
    
    @Override
    public int updateOrderQuestionProgress(Map<String, Object> para, String[] imageArray)
        throws Exception
    {
        int dealType = (int)para.get("dealType");
        int status = (int)para.get("status");
        int questionId = (int)para.get("questionId");
        String content = para.get("content").toString();
        int createUser = (int)para.get("createUser");
        
        OrderQuestionProgressEntity oqpe = new OrderQuestionProgressEntity();
        oqpe.setQuestionId(questionId);
        oqpe.setContent(content);
        oqpe.setStatus(status);
        oqpe.setCreateUser(createUser);
        
        List<Map<String, Object>> imageList = new ArrayList<Map<String, Object>>();
        if (imageArray.length > 0 && StringUtils.isNotEmpty(imageArray[0]))
        {
            oqpe.setHasImage(1);
            for (String image : imageArray)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("image", image);
                imageList.add(map);
            }
        }
        else
        {
            para.put("hasImage", 0);
        }
        if (dealType == 1)
        {
            //更新顾客问题处理进度
            orderQuestionDao.insertCustomerQuestionProgress(oqpe);
            for (Map<String, Object> it : imageList)
            {
                it.put("progressId", oqpe.getId());
            }
            if (imageList.size() > 0)
            {
                //插入顾客问题处理图片
                orderQuestionDao.insertCustomerQuestionProgressImage(imageList);
            }
            if (status == 2)
            {
                para.put("cf", 1);
            }
        }
        else if (dealType == 2)
        {
            //更新商家对接问题处理进度
            orderQuestionDao.insertSellerQuestionProgress(oqpe);
            for (Map<String, Object> it : imageList)
            {
                it.put("progressId", oqpe.getId());
            }
            if (imageList.size() > 0)
            {
                //更新商家对接问题处理进度
                orderQuestionDao.insertSellerQuestionProgressImage(imageList);
            }
            if (status == 2)
            {
                para.put("sf", 1);
            }
        }
        //更新订单问题处理状态
        return orderQuestionDao.updateOrderQuestionStatus(para);
    }
    
    @Override
    public Map<String, Object> jsonQuestionListInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> infoList = orderQuestionDao.findAllOrderAndQuestionInfo(para);
        int total = 0;
        DateTime nowTime = DateTime.now();
        if (infoList.size() > 0)
        {
            for (Map<String, Object> map : infoList)
            {
                int leftMinute = 0;
                int customerStatus = (int)map.get("customerStatus");
                int sellerStatus = (int)map.get("sellerStatus");
                int limitType = (int)map.get("timeLimitType");
                DateTime createTime = DateTimeUtil.string2DateTime(map.get("createTime") + "", "yyyy-MM-dd HH:mm:ss.SSS");
                DateTime endTime = null;
                if (limitType == 1)
                {
                    endTime = createTime.plusHours(1);
                }
                else if (limitType == 2)
                {
                    endTime = createTime.plusHours(2);
                }
                else if (limitType == 3)
                {
                    endTime = DateTimeUtil.string2DateTime(map.get("customTime") + "", "yyyy-MM-dd HH:mm:ss.SSS");
                }
                //已完结(只有当顾客状态和商家状态同时完结的时候才是已完结)
                if (customerStatus == 2 && sellerStatus == 2)
                {
                    DateTime customerFinishTime = DateTimeUtil.string2DateTime(map.get("customerFinishTime") + "", "yyyy-MM-dd HH:mm:ss.SSS");
                    DateTime sellerFinishTime = DateTimeUtil.string2DateTime(map.get("sellerFinishTime") + "", "yyyy-MM-dd HH:mm:ss.SSS");
                    if (customerFinishTime.isBefore(sellerFinishTime))
                    {
                        leftMinute = Minutes.minutesBetween(sellerFinishTime, endTime).getMinutes();
                    }
                    else
                    {
                        leftMinute = Minutes.minutesBetween(customerFinishTime, endTime).getMinutes();
                    }
                }
                else
                {
                    leftMinute = Minutes.minutesBetween(nowTime, endTime).getMinutes();
                }
                int createUser = Integer.parseInt(map.get("createUser") + "");
                User user = userDao.findUserById(createUser);
                map.put("createUser", user == null ? "匿名用户" : user.getRealname());
                map.put("customerStatusStr", customerStatus == 1 ? "进行中" : "已完结");
                map.put("sellerStatusStr", sellerStatus == 1 ? "进行中" : "已完结");
                map.put("leftTime", leftMinute <= 0 ? "已超时" : "剩" + MathUtil.round(leftMinute * 1.0 / 60, 1) + "小时");
                map.put("oStatusDescripton", OrderEnum.ORDER_STATUS.getDescByCode(Integer.parseInt(map.get("oStatus") + "")));
                map.put("payTime", map.get("payTime") == null ? "" : ((Timestamp)map.get("payTime")).toString());
                map.put("sendTime", map.get("sendTime") == null ? "" : ((Timestamp)map.get("sendTime")).toString());
                map.put("createTime", map.get("createTime") == null ? "" : ((Timestamp)map.get("createTime")).toString());
                map.put("orderType", map.containsKey("orderType") ? OrderEnum.ORDER_TYPE.getDescByCode((Integer) map.get("orderType")) : "");
            }
            total = orderQuestionDao.countOrderAndQuestionInfo(para);
        }
        resultMap.put("rows", infoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public OrderQuestionTemplateEntity findOrderQuestionTemplateById(int templateId)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", templateId);
        para.put("isAvailable", 1);
        return orderQuestionDao.findOrderQuestionTemplateByPara(para);
    }
    
    @Override
    public OrderQuestionTemplateEntity findOrderQuestionTemplateByName(String name)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("name", name);
        return orderQuestionDao.findOrderQuestionTemplateByPara(para);
    }
    
    @Override
    public String updateOrderQuestionMark(int id, int isMark)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("questionId", id);
        para.put("isMark", isMark);
        if (orderQuestionDao.updateOrderQuestionStatus(para) > 0)
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "保存成功");
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public String updateOrderQuestionPushStatus(int id)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        DateTime nowTime = DateTime.now();
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("questionId", id);
        para.put("isPush", 1);
        para.put("pushTime", nowTime.toString("yyyy-MM-dd HH:mm:ss"));
        para.put("sellerFeedbackEndTime", nowTime.plusDays(1).withHourOfDay(18).withMinuteOfHour(0).withSecondOfMinute(0).toString("yyyy-MM-dd HH:mm:ss"));
        if (orderQuestionDao.updateOrderQuestionStatus(para) > 0)
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "保存成功");
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }
}
