package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

/**
 * 促销活动 dao
 */
public interface PromotionActivityDao
{
    /**
     * 查询新情景专场
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findSpecialActivityNewByPara(Map<String, Object> para)
        throws Exception;
    
    int countSpecialActivityNewByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 保存新情景专场
     * @param para
     * @return
     * @throws Exception
     */
    int saveSpecialActivityNew(Map<String, Object> para)
        throws Exception;
    
    /**
     * 更新新情景专场
     * @param para
     * @return
     * @throws Exception
     */
    int updateSpecialActivityNew(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查询新情景专场商品
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findSpecialActivityNewProductByPara(Map<String, Object> para)
        throws Exception;
    
    int countSpecialActivityNewProductByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 更新新情景专场商品
     * @param para
     * @return
     * @throws Exception
     */
    int updateSpecialActivityNewProduct(Map<String, Object> para)
        throws Exception;
    
    /**
     * 保存新情景专场商品
     * @param para
     * @return
     * @throws Exception
     */
    int saveSpecialActivityNewProduct(Map<String, Object> para)
        throws Exception;
    
    Map<String, Object> findSpecialActivityNewByid(int id)
        throws Exception;
    
    List<Integer> findSpecialActivityNewProductByid(int id)
        throws Exception;

    int deleteSpecialActivityNewProductById(int id)
        throws Exception;
}
