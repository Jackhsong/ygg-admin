package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.OrderQuestionDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.OrderQuestionEntity;
import com.ygg.admin.entity.OrderQuestionProgressEntity;
import com.ygg.admin.entity.OrderQuestionTemplateEntity;

@Repository("orderQuestionDao")
public class OrderQuestionDaoImpl extends BaseDaoImpl implements OrderQuestionDao
{
    
    @Override
    public List<Map<String, Object>> findOrderQuestionListByOrderId(int orderId)
        throws Exception
    {
        List<Map<String, Object>> reList = this.getSqlSessionRead().selectList("OrderQuestionMapper.findOrderQuestionListByOrderId", orderId);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public int countQuestionTemplate(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("OrderQuestionMapper.countQuestionTemplate", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllQuestionTemplate(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = this.getSqlSessionRead().selectList("OrderQuestionMapper.findAllQuestionTemplate", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public int saveQuestionTemplate(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().insert("OrderQuestionMapper.saveQuestionTemplate", para);
    }
    
    @Override
    public int updateOrderQuestionTemplateStatus(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("OrderQuestionMapper.updateOrderQuestionTemplateStatus", para);
    }
    
    @Override
    public int addOrderQuestion(OrderQuestionEntity question)
        throws Exception
    {
        return this.getSqlSession().insert("OrderQuestionMapper.addOrderQuestion", question);
    }
    
    @Override
    public int insertOrderQuestionImage(List<Map<String, Object>> imageList)
        throws Exception
    {
        return this.getSqlSession().insert("OrderQuestionMapper.insertOrderQuestionImage", imageList);
    }
    
    @Override
    public OrderQuestionEntity findOrderQuestionById(int id)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("OrderQuestionMapper.findOrderQuestionById", id);
    }
    
    @Override
    public int countDealingOrderQuestionByOrderId(int orderId)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("OrderQuestionMapper.countDealingOrderQuestionByOrderId", orderId);
    }
    
    @Override
    public List<String> findOrderQuestionImageListqid(int id)
        throws Exception
    {
        List<String> reList = this.getSqlSessionRead().selectList("OrderQuestionMapper.findOrderQuestionImageListqid", id);
        return reList == null ? new ArrayList<String>() : reList;
    }
    
    @Override
    public List<Map<String, Object>> findCustomerProgressListByqid(int id)
        throws Exception
    {
        List<Map<String, Object>> reList = this.getSqlSessionRead().selectList("OrderQuestionMapper.findCustomerProgressListByqid", id);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public List<Map<String, Object>> findSellerProgressListByqid(int id)
        throws Exception
    {
        List<Map<String, Object>> reList = this.getSqlSessionRead().selectList("OrderQuestionMapper.findSellerProgressListByqid", id);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public int insertCustomerQuestionProgress(OrderQuestionProgressEntity oqpe)
        throws Exception
    {
        return this.getSqlSession().insert("OrderQuestionMapper.insertCustomerQuestionProgress", oqpe);
    }
    
    @Override
    public int insertCustomerQuestionProgressImage(List<Map<String, Object>> imageList)
        throws Exception
    {
        return this.getSqlSession().insert("OrderQuestionMapper.insertCustomerQuestionProgressImage", imageList);
    }
    
    @Override
    public int insertSellerQuestionProgress(OrderQuestionProgressEntity oqpe)
        throws Exception
    {
        return this.getSqlSession().insert("OrderQuestionMapper.insertSellerQuestionProgress", oqpe);
    }
    
    @Override
    public int insertSellerQuestionProgressImage(List<Map<String, Object>> imageList)
        throws Exception
    {
        return this.getSqlSession().insert("OrderQuestionMapper.insertSellerQuestionProgressImage", imageList);
    }
    
    @Override
    public int updateOrderQuestionStatus(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("OrderQuestionMapper.updateOrderQuestionStatus", para);
    }
    
    @Override
    public int countOrderAndQuestionInfo(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("OrderQuestionMapper.countOrderAndQuestionInfo", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllOrderAndQuestionInfo(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = this.getSqlSessionRead().selectList("OrderQuestionMapper.findAllOrderAndQuestionInfo", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public List<OrderQuestionEntity> findAllOrderQuestion()
        throws Exception
    {
        List<OrderQuestionEntity> reList = this.getSqlSessionRead().selectList("OrderQuestionMapper.findAllOrderQuestion");
        return reList == null ? new ArrayList<OrderQuestionEntity>() : reList;
    }
    
    @Override
    public OrderQuestionTemplateEntity findOrderQuestionTemplateByPara(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("OrderQuestionMapper.findOrderQuestionTemplateByPara", para);
    }
    
    @Override
    public int updateQuestionTemplate(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("OrderQuestionMapper.updateQuestionTemplate", para);
    }
    
    @Override
    public List<String> findCustomerProgressImagesBycid(int cid)
        throws Exception
    {
        List<String> reList = this.getSqlSessionRead().selectList("OrderQuestionMapper.findCustomerProgressImagesBycid", cid);
        return reList == null ? new ArrayList<String>() : reList;
    }
    
    @Override
    public List<String> findSellerProgressImagesBysid(int sid)
        throws Exception
    {
        List<String> reList = this.getSqlSessionRead().selectList("OrderQuestionMapper.findSellerProgressImagesBysid", sid);
        return reList == null ? new ArrayList<String>() : reList;
    }
    
    @Override
    public List<Map<String, Object>> findOrderQuestionDescListByOrderId(List<Integer> orderIdList)
        throws Exception
    {
        return getSqlSessionRead().selectList("OrderQuestionMapper.findOrderQuestionDescListByOrderId", orderIdList);
    }
    
    @Override
    public List<Map<String, Object>> findSellerFeedbackDetailListByqid(int id)
        throws Exception
    {
        return getSqlSessionRead().selectList("OrderQuestionMapper.findSellerFeedbackDetailListByqid", id);
    }
    
    @Override
    public int saveOrderQuestionSellerFeedback(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("OrderQuestionMapper.saveOrderQuestionSellerFeedback", para);
    }
}
