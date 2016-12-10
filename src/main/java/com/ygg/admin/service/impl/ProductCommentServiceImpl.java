package com.ygg.admin.service.impl;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

import com.ygg.admin.code.AccountEnum;
import com.ygg.admin.code.ProductEnum;
import com.ygg.admin.config.YggAdminProperties;
import com.ygg.admin.dao.OrderDao;
import com.ygg.admin.dao.ProductCommentDao;
import com.ygg.admin.dao.UserDao;
import com.ygg.admin.entity.OrderEntity;
import com.ygg.admin.entity.OrderProductCommentEntity;
import com.ygg.admin.entity.User;
import com.ygg.admin.service.ProductCommentService;
import com.ygg.admin.util.MathUtil;

@Service("productCommentService")
public class ProductCommentServiceImpl implements ProductCommentService
{
    private static Logger logger = Logger.getLogger(ProductCommentServiceImpl.class);
    
    @Resource
    private ProductCommentDao productCommentDao;
    
    @Resource
    private UserDao userDao;
    
    @Resource
    private OrderDao orderDao;
    
    @Override
    public Map<String, Object> jsonProductCommentInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> infoList = productCommentDao.findAllProductComment(para);
        int total = 0;
        if (infoList.size() > 0)
        {
            for (Map<String, Object> map : infoList)
            {
                int level = Integer.valueOf(map.get("level") + "").intValue();
                int productType = Integer.valueOf(map.get("productType") + "").intValue();
                StringBuffer sb = new StringBuffer("<a target='_blank' href='http://m.gegejia.com/");
                if (productType == ProductEnum.PRODUCT_TYPE.SALE.getCode())
                {
                    sb.append("item-");
                }
                else if (productType == ProductEnum.PRODUCT_TYPE.MALL.getCode())
                {
                    sb.append("mitem-");
                }
                sb.append(map.get("productId")).append(".htm'>").append(map.get("productName")).append("</a>");
                map.put("productNameUrl", sb.toString());
                map.put("levelDesc", ProductEnum.PRODUCT_COMMENT_TYPE.getDescByCode(level));
                map.put("typeDesc", ProductEnum.PRODUCT_TYPE.getDescByCode(productType));
                int imageAmount = 0;
                if (!"".equals(map.get("image1") + ""))
                {
                    imageAmount++;
                }
                if (!"".equals(map.get("image2") + ""))
                {
                    imageAmount++;
                }
                if (!"".equals(map.get("image3") + ""))
                {
                    imageAmount++;
                }
                map.put("imageAmount", imageAmount);
                String content = map.get("content") + "";
                if (!StringUtils.isEmpty(content))
                {
                    try
                    {
                        map.put("content", URLDecoder.decode(content, "UTF-8"));
                    }
                    catch (Exception e)
                    {
                        map.put("content", content);
                    }
                }
            }
            total = productCommentDao.countProductComment(para);
        }
        resultMap.put("rows", infoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public OrderProductCommentEntity findProductCommentById(int id)
        throws Exception
    {
        OrderProductCommentEntity opce = productCommentDao.findProductCommentById(id);
        if (opce != null)
        {
            if (!StringUtils.isEmpty(opce.getContent()))
            {
                try
                {
                    opce.setContent(URLDecoder.decode(opce.getContent(), "UTF-8"));
                }
                catch (Exception e)
                {
                    logger.error("商品评论Id=" + id + "内容不需要解码");
                }
            }
        }
        return opce;
    }
    
    @Override
    public Map<String, Object> jsonProductBaseCommentInfo(Map<String, Object> para)
        throws Exception
    {
        Object goodCommentPercent = para.get("goodCommentPercent");
        if (goodCommentPercent != null)
        {
            List<Integer> productBaseIdList = getProductBaseIdListByGoodCommentPercent(goodCommentPercent);
            if (productBaseIdList.size() > 0)
            {
                para.put("productBaseIdList", productBaseIdList);
            }
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> listInfo = productCommentDao.findAllProductBaseComment(para);
        
        int total = 0;
        if (listInfo.size() > 0)
        {
            Map<Integer, Map<String, Integer>> commentLevelMap = new HashMap<>();
            //查找差评
            List<Map<String, Object>> levelOne = productCommentDao.findAllProductBaseCommentLevel(ProductEnum.PRODUCT_COMMENT_TYPE.BAD.getCode());
            //查找中评
            List<Map<String, Object>> levelTwo = productCommentDao.findAllProductBaseCommentLevel(ProductEnum.PRODUCT_COMMENT_TYPE.MIDDLE.getCode());
            //查找好评
            List<Map<String, Object>> levelThree = productCommentDao.findAllProductBaseCommentLevel(ProductEnum.PRODUCT_COMMENT_TYPE.GOOD.getCode());
            //差评去重,以后可以考虑去掉（数据库已经去重）
            for (Map<String, Object> it : levelOne)
            {
                int productBaseId = Integer.valueOf(it.get("productBaseId") == null ? "0" : it.get("productBaseId") + "").intValue();
                int levelAmount = Integer.valueOf(it.get("levelAmount") == null ? "0" : it.get("levelAmount") + "").intValue();
                Map<String, Integer> levelMap = commentLevelMap.get(productBaseId);
                if (levelMap == null)
                {
                    levelMap = new HashMap<>();
                    levelMap.put("levelOne", levelAmount);
                    commentLevelMap.put(productBaseId, levelMap);
                }
                else
                {
                    if (levelMap.get("levelOne") == null)
                    {
                        levelMap.put("levelOne", levelAmount);
                    }
                    else
                    {
                        levelMap.put("levelOne", levelMap.get("levelOne") + levelAmount);
                    }
                }
            }
            //中评去重，以后可以考虑去掉（数据库已经去重）
            for (Map<String, Object> it : levelTwo)
            {
                int productBaseId = Integer.valueOf(it.get("productBaseId") == null ? "0" : it.get("productBaseId") + "").intValue();
                int levelAmount = Integer.valueOf(it.get("levelAmount") == null ? "0" : it.get("levelAmount") + "").intValue();
                Map<String, Integer> levelMap = commentLevelMap.get(productBaseId);
                if (levelMap == null)
                {
                    levelMap = new HashMap<>();
                    levelMap.put("levelTwo", levelAmount);
                    commentLevelMap.put(productBaseId, levelMap);
                }
                else
                {
                    if (levelMap.get("levelTwo") == null)
                    {
                        levelMap.put("levelTwo", levelAmount);
                    }
                    else
                    {
                        levelMap.put("levelTwo", levelMap.get("levelTwo") + levelAmount);
                    }
                }
            }
            //好评去重，以后可以考虑去掉（数据库已经去重）
            for (Map<String, Object> it : levelThree)
            {
                int productBaseId = Integer.valueOf(it.get("productBaseId") == null ? "0" : it.get("productBaseId") + "").intValue();
                int levelAmount = Integer.valueOf(it.get("levelAmount") == null ? "0" : it.get("levelAmount") + "").intValue();
                Map<String, Integer> levelMap = commentLevelMap.get(productBaseId);
                if (levelMap == null)
                {
                    levelMap = new HashMap<>();
                    levelMap.put("levelThree", levelAmount);
                    commentLevelMap.put(productBaseId, levelMap);
                }
                else
                {
                    if (levelMap.get("levelThree") == null)
                    {
                        levelMap.put("levelThree", levelAmount);
                    }
                    else
                    {
                        levelMap.put("levelThree", levelMap.get("levelThree") + levelAmount);
                    }
                }
            }
            for (Map<String, Object> map : listInfo)
            {
                int totalComment = Integer.valueOf(map.get("totalComment") + "").intValue();
                int productBaseId = Integer.valueOf(map.get("productBaseId") + "").intValue();
                Map<String, Integer> levelMap = commentLevelMap.get(productBaseId);
                if (levelMap == null)
                {
                    map.put("badComment", 0);
                    map.put("middleComment", 0);
                    map.put("goodComment", 0);
                    map.put("goodPercent", "0%");
                }
                else
                {
                    map.put("badComment", levelMap.get("levelOne") == null ? 0 : levelMap.get("levelOne"));
                    map.put("middleComment", levelMap.get("levelTwo") == null ? 0 : levelMap.get("levelTwo"));
                    map.put("goodComment", levelMap.get("levelThree") == null ? 0 : levelMap.get("levelThree"));
                    if (totalComment == 0)
                    {
                        map.put("goodPercent", 0);
                    }
                    else
                    {
                        map.put("goodPercent", MathUtil.round(levelMap.get("levelThree") == null ? 0.0d : levelMap.get("levelThree") * 1.0d / totalComment * 100, 2) + "%");
                    }
                }
            }
            total = productCommentDao.countProductBaseComment(para);
        }
        resultMap.put("rows", listInfo);
        resultMap.put("total", total);
        return resultMap;
    }
    
    private List<Integer> getProductBaseIdListByGoodCommentPercent(Object goodCommentPercent)
        throws Exception
    {
        int type = (int)goodCommentPercent;
        Set<Integer> productBaseIdSet = new HashSet<>();
        List<Map<String, Object>> goodCommentProductList = productCommentDao.findAllProductBaseCommentLevel(ProductEnum.PRODUCT_COMMENT_TYPE.GOOD.getCode());
        List<Map<String, Object>> productCommentList = productCommentDao.findAllProductBaseTotalComment();
        Map<Integer, Integer> goodCommentMap = new HashMap<>();
        for (Map<String, Object> it : goodCommentProductList)
        {
            int productBaseId = Integer.valueOf(it.get("productBaseId") == null ? "0" : it.get("productBaseId") + "").intValue();
            int levelAmount = Integer.valueOf(it.get("levelAmount") == null ? "0" : it.get("levelAmount") + "").intValue();
            goodCommentMap.put(productBaseId, levelAmount);
        }
        for (Map<String, Object> it : productCommentList)
        {
            int productBaseId = Integer.valueOf(it.get("productBaseId") == null ? "0" : it.get("productBaseId") + "").intValue();
            int totalComment = Integer.valueOf(it.get("totalComment") == null ? "0" : it.get("totalComment") + "").intValue();
            if (totalComment > 0)
            {
                Double goodComment = Double.valueOf(goodCommentMap.get(productBaseId) == null ? 0 : goodCommentMap.get(productBaseId));
                Double percent = goodComment / (totalComment * 1.0d);
                if (type == 1)//99%以上
                {
                    if (percent.compareTo(0.99d) > 0)
                    {
                        productBaseIdSet.add(productBaseId);
                    }
                }
                else if (type == 2)//95%-99%
                {
                    if (percent.compareTo(0.95d) >= 0 && percent.compareTo(0.99d) < 0)
                    {
                        productBaseIdSet.add(productBaseId);
                    }
                }
                else if (type == 3)//95%以下
                {
                    if (percent.compareTo(0.95d) < 0)
                    {
                        productBaseIdSet.add(productBaseId);
                    }
                }
            }
        }
        return new ArrayList<Integer>(productBaseIdSet);
    }
    
    @Override
    public Map<String, Object> jsonProductCommentDetailList(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        Object orderNo = para.get("orderNo");
        if (orderNo != null)
        {
            OrderEntity entity = orderDao.findOrderByNumber(orderNo.toString());
            if (entity == null)
            {
                resultMap.put("rows", new ArrayList<>());
                resultMap.put("total", 0);
                return resultMap;
            }
            para.put("orderId", entity == null ? 0 : entity.getId());
        }
        List<Map<String, Object>> infoList = productCommentDao.findAllProductCommentDetail(para);
        int total = 0;
        if (infoList.size() > 0)
        {
            List<User> userList = userDao.findUserByPara(new HashMap<String, Object>());
            Map<String, String> userIdAndNameMap = new HashMap<>();
            for (User user : userList)
            {
                userIdAndNameMap.put(user.getId() + "", user.getRealname());
            }
            
            for (Map<String, Object> map : infoList)
            {
                map.put("dealUser", userIdAndNameMap.get(map.get("dealUser") + "") == null ? "" : userIdAndNameMap.get(map.get("dealUser") + ""));
                int level = Integer.valueOf(map.get("level") + "").intValue();
                int productType = Integer.valueOf(map.get("productType") + "").intValue();
                StringBuffer sb = new StringBuffer("<a target='_blank' href='http://m.gegejia.com/");
                if (productType == ProductEnum.PRODUCT_TYPE.SALE.getCode())
                {
                    sb.append("item-");
                }
                else if (productType == ProductEnum.PRODUCT_TYPE.MALL.getCode())
                {
                    sb.append("mitem-");
                }
                sb.append(map.get("productId")).append(".htm'>").append(map.get("productName")).append("</a>");
                map.put("productNameUrl", sb.toString());
                map.put("levelDesc", ProductEnum.PRODUCT_COMMENT_TYPE.getDescByCode(level));
                map.put("typeDesc", ProductEnum.PRODUCT_TYPE.getDescByCode(productType));
                StringBuilder imageSB = new StringBuilder("");
                if (!"".equals(map.get("image1") + ""))
                {
                    imageSB.append("<img alt='' src='").append(map.get("image1")).append("' style='max-height:40px'/>");
                }
                if (!"".equals(map.get("image2") + ""))
                {
                    imageSB.append("&nbsp;<img alt='' src='").append(map.get("image2")).append("' style='max-height:40px'/>");
                }
                if (!"".equals(map.get("image3") + ""))
                {
                    imageSB.append("&nbsp;<img alt='' src='").append(map.get("image3")).append("' style='max-height:40px'/>");
                }
                if (imageSB.length() == 0)
                {
                    imageSB.append("无");
                    map.put("hasImage", 0);
                }
                else
                {
                    map.put("hasImage", 1);
                }
                map.put("image", imageSB.toString());
                map.put("isDisplayStr", ((int)map.get("isDisplay")) == 1 ? "展现" : "不展现");
                String content = map.get("content") + "";
                if (!StringUtils.isEmpty(content))
                {
                    try
                    {
                        map.put("content", URLDecoder.decode(content, "UTF-8"));
                    }
                    catch (Exception e)
                    {
                        map.put("content", content);
                    }
                }
                map.put("createTime", map.get("createTime").toString());
                if(map.get("handleTime")!= null){
                    map.put("handleTime", map.get("handleTime").toString());
                }
                map.put("isDealDesc", ((int)map.get("isDeal")) == 1 ? "是" : "否");
            }
            total = productCommentDao.countProductCommentDetail(para);
        }
        resultMap.put("rows", infoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public Map<String, Object> findProductCommentByPara(int id)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        para.put("start", 0);
        para.put("max", 1);
        List<Map<String, Object>> result = productCommentDao.findAllProductComment(para);
        if (result == null || result.size() == 0)
            return null;
        Map<String, Object> map = result.get(0);
        int level = Integer.valueOf(map.get("level") + "").intValue();
        int userType = Integer.valueOf(map.get("userType") + "").intValue();
        map.put("userType", AccountEnum.ACCOUNT_TYPE.getDescByCode(userType));
        map.put("levelDesc", ProductEnum.PRODUCT_COMMENT_TYPE.getDescByCode(level));
        map.put("salePrice", MathUtil.round(map.get("salePrice") + "", 2));
        String content = map.get("content") + "";
        if (!StringUtils.isEmpty(content))
        {
            try
            {
                map.put("content", URLDecoder.decode(content, "UTF-8"));
            }
            catch (Exception e)
            {
                map.put("content", content);
            }
        }
        return map;
    }
    
    @Override
    public int updateProductCommentDisplayStatus(Map<String, Object> para)
        throws Exception
    {
        return productCommentDao.updateProductCommentDisplayStatus(para);
    }
    
    @Override
    public int replayProductComment(Map<String, Object> para)
        throws Exception
    {
        return productCommentDao.replayProductComment(para);
    }
    
    @Override
    public int updateDealContent(int id, String dealContent)
        throws Exception
    {
        String username = SecurityUtils.getSubject().getPrincipal() + "";
        User user = userDao.findUserByUsername(username);
        
        Map<String, Object> para = new HashMap<>();
        para.put("id", id);
        para.put("dealContent", dealContent);
        para.put("isDeal", 1);
        para.put("dealUser", user == null ? Integer.parseInt(YggAdminProperties.getInstance().getPropertie("admin_account_id")) : user.getId());
        return productCommentDao.updateDealContent(para);
    }

    @Override
    public int updateProductCommentDisplayTextStatus(Map<String, Object> para)
            throws Exception
    {
        return productCommentDao.updateProductCommentDisplayTextStatus(para);
    }

    @Override
    public List<OrderProductCommentEntity> findProCommentsByIds(Map<String, Object> para)
            throws Exception
    {
        List<OrderProductCommentEntity> opceList = productCommentDao.findProCommentsByIds(para);
        return opceList;
    }

    @Override
    public String getProductBaseCommentRateById(int productId, int commentLevel) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("productBaseId", productId);
        int totalComment = productCommentDao.findProductBaseCommentByParam(params);
        params.put("level", commentLevel);
        int specificComment = productCommentDao.findProductBaseCommentByParam(params);
        return totalComment == 0 ? "0" : MathUtil.round(specificComment * 100.0 / totalComment, 2) + "%";
    }
}
