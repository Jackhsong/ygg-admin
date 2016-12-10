package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.QueryImageDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;

@Repository("queryImageDao")
public class QueryImageDaoImpl extends BaseDaoImpl implements QueryImageDao
{
    
    @Override
    public List<String> findActivitiesCommonImage(Map<String, Object> para)
        throws Exception
    {
        List<String> result = getSqlSessionRead().selectList("QueryImageMapper.findActivitiesCommonImage", para);
        return result == null ? new ArrayList<String>() : result;
    }
    
    @Override
    public List<String> findBannerWindowImage(Map<String, Object> para)
        throws Exception
    {
        
        List<String> result = getSqlSessionRead().selectList("QueryImageMapper.findBannerWindowImage", para);
        return result == null ? new ArrayList<String>() : result;
    }
    
    @Override
    public List<String> findBrandImage(Map<String, Object> para)
        throws Exception
    {
        List<String> result = getSqlSessionRead().selectList("QueryImageMapper.findBrandImage", para);
        return result == null ? new ArrayList<String>() : result;
    }
    
    @Override
    public List<String> findGegeImageActivitiesImage(Map<String, Object> para)
        throws Exception
    {
        List<String> result = getSqlSessionRead().selectList("QueryImageMapper.findGegeImageActivitiesImage", para);
        return result == null ? new ArrayList<String>() : result;
    }
    
    @Override
    public List<String> findGegeImageProductImage(Map<String, Object> para)
        throws Exception
    {
        List<String> result = getSqlSessionRead().selectList("QueryImageMapper.findGegeImageProductImage", para);
        return result == null ? new ArrayList<String>() : result;
    }
    
    @Override
    public List<String> findOrderProductImage(Map<String, Object> para)
        throws Exception
    {
        List<String> result = getSqlSessionRead().selectList("QueryImageMapper.findOrderProductImage", para);
        return result == null ? new ArrayList<String>() : result;
    }
    
    @Override
    public Set<String> findOrderProductRefundImage(Map<String, Object> para)
        throws Exception
    {
        Set<String> resultSet = new HashSet<String>();
        List<Map<String, String>> resultMap = getSqlSessionRead().selectList("QueryImageMapper.findOrderProductRefundImage", para);
        if (resultMap != null)
        {
            for (Map<String, String> map : resultMap)
            {
                String image1 = map.get("image1");
                String image2 = map.get("image2");
                String image3 = map.get("image3");
                
                if (StringUtils.isNotEmpty(image1) && image1.startsWith("http://yangege.b0.upaiyun.com"))
                {
                    resultSet.add(image1);
                }
                if (StringUtils.isNotEmpty(image2) && image2.startsWith("http://yangege.b0.upaiyun.com"))
                {
                    resultSet.add(image2);
                }
                if (StringUtils.isNotEmpty(image3) && image3.startsWith("http://yangege.b0.upaiyun.com"))
                {
                    resultSet.add(image3);
                }
            }
        }
        return resultSet;
    }
    
    @Override
    public Set<String> findProductImage(Map<String, Object> para)
        throws Exception
    {
        Set<String> resultSet = new HashSet<String>();
        List<Map<String, String>> resultMap = getSqlSessionRead().selectList("QueryImageMapper.findProductImage", para);
        if (resultMap != null)
        {
            for (Map<String, String> map : resultMap)
            {
                String image1 = map.get("image1");
                String image2 = map.get("image2");
                String image3 = map.get("image3");
                String image4 = map.get("image4");
                String image5 = map.get("image5");
                
                if (StringUtils.isNotEmpty(image1) && image1.startsWith("http://yangege.b0.upaiyun.com"))
                {
                    resultSet.add(image1);
                }
                if (StringUtils.isNotEmpty(image2) && image2.startsWith("http://yangege.b0.upaiyun.com"))
                {
                    resultSet.add(image2);
                }
                if (StringUtils.isNotEmpty(image3) && image3.startsWith("http://yangege.b0.upaiyun.com"))
                {
                    resultSet.add(image3);
                }
                if (StringUtils.isNotEmpty(image4) && image4.startsWith("http://yangege.b0.upaiyun.com"))
                {
                    resultSet.add(image4);
                }
                if (StringUtils.isNotEmpty(image5) && image5.startsWith("http://yangege.b0.upaiyun.com"))
                {
                    resultSet.add(image5);
                }
            }
        }
        return resultSet;
    }
    
    @Override
    public Set<String> findProductCommonImage(Map<String, Object> para)
        throws Exception
    {
        Set<String> resultSet = new HashSet<String>();
        List<Map<String, String>> resultMap = getSqlSessionRead().selectList("QueryImageMapper.findProductCommonImage", para);
        if (resultMap != null)
        {
            for (Map<String, String> map : resultMap)
            {
                String image1 = map.get("medium_image");
                String image2 = map.get("small_image");
                
                if (StringUtils.isNotEmpty(image1) && image1.startsWith("http://yangege.b0.upaiyun.com"))
                {
                    resultSet.add(image1);
                }
                if (StringUtils.isNotEmpty(image2) && image2.startsWith("http://yangege.b0.upaiyun.com"))
                {
                    resultSet.add(image2);
                }
            }
        }
        return resultSet;
    }
    
    @Override
    public List<String> findProductMobileDetailImage(Map<String, Object> para)
        throws Exception
    {
        List<String> result = getSqlSessionRead().selectList("QueryImageMapper.findProductMobileDetailImage", para);
        return result == null ? new ArrayList<String>() : result;
    }
    
    @Override
    public List<String> findSaleTagImage(Map<String, Object> para)
        throws Exception
    {
        List<String> result = getSqlSessionRead().selectList("QueryImageMapper.findSaleTagImage", para);
        return result == null ? new ArrayList<String>() : result;
    }
    
    @Override
    public List<String> findSaleWindowImage(Map<String, Object> para)
        throws Exception
    {
        List<String> result = getSqlSessionRead().selectList("QueryImageMapper.findSaleWindowImage", para);
        return result == null ? new ArrayList<String>() : result;
    }
    
    @Override
    public Set<String> findProductBaseImage(Map<String, Object> para)
        throws Exception
    {
        Set<String> resultSet = new HashSet<String>();
        List<Map<String, String>> resultMap = getSqlSessionRead().selectList("QueryImageMapper.findProductBaseImage", para);
        if (resultMap != null)
        {
            for (Map<String, String> map : resultMap)
            {
                String image1 = map.get("image1");
                String image2 = map.get("image2");
                String image3 = map.get("image3");
                String image4 = map.get("image4");
                String image5 = map.get("image5");
                
                if (StringUtils.isNotEmpty(image1) && image1.startsWith("http://yangege.b0.upaiyun.com"))
                {
                    resultSet.add(image1);
                }
                if (StringUtils.isNotEmpty(image2) && image2.startsWith("http://yangege.b0.upaiyun.com"))
                {
                    resultSet.add(image2);
                }
                if (StringUtils.isNotEmpty(image3) && image3.startsWith("http://yangege.b0.upaiyun.com"))
                {
                    resultSet.add(image3);
                }
                if (StringUtils.isNotEmpty(image4) && image4.startsWith("http://yangege.b0.upaiyun.com"))
                {
                    resultSet.add(image4);
                }
                if (StringUtils.isNotEmpty(image5) && image5.startsWith("http://yangege.b0.upaiyun.com"))
                {
                    resultSet.add(image5);
                }
            }
        }
        return resultSet;
    }
    
    @Override
    public List<String> findProductBaseMobileDetailImage(Map<String, Object> para)
        throws Exception
    {
        
        List<String> result = getSqlSessionRead().selectList("QueryImageMapper.findProductBaseMobileDetailImage", para);
        return result == null ? new ArrayList<String>() : result;
    }
    
    @Override
    public List<String> findAccountImage(Map<String, Object> para)
        throws Exception
    {
        List<String> result = getSqlSessionRead().selectList("QueryImageMapper.findAccountImage", para);
        return result == null ? new ArrayList<String>() : result;
    }
}
