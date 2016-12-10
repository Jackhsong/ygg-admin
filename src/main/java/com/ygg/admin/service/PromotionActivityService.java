package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

/**
 * 促销活动 服务
 */
public interface PromotionActivityService
{
    /**
     * 查询新情景专场信息
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> findSpecialActivityNewByPara(Map<String, Object> para)
        throws Exception;
    
    Map<String, Object> findSpecialActivityNewById(int id)
        throws Exception;
    
    List<Map<String,Object>> findSpecialActivityNewByPara()
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
    Map<String, Object> findSpecialActivityNewProductByPara(Map<String, Object> para)
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
    
    /**
     * 新情景批量添加商品
     * @param activityId
     * @param type
     * @param isDisplay
     * @param productIdList
     * @return
     * @throws Exception
     */
    String batchAddPromotionActivityProduct(int activityId, int type, int isDisplay, List<Integer> productIdList)
        throws Exception;

    int deleteSpecialActivityNewProductById(int id)
            throws Exception;
}
