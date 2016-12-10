package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

public interface SpecialGroupActivityService
{
    /**
     * 根据条件查找组合情景特卖
     * @param para
     * @return
     * @throws Exception
     */
    String findSpecialGroupActivityByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 新增组合情景
     * @param title：活动标题
     * @param isAvailable：是否可用，1是，0否
     * @return
     * @throws Exception
     */
    String saveSpecialGroup(String title, int isAvailable)
        throws Exception;
    
    /**
     * 修改组合情景
     * @param id：id
     * @param title：活动标题
     * @param isAvailable：是否可用，1是，0否
     * @return
     * @throws Exception
     */
    String updateSpecialGroup(int id, String title, int isAvailable)
        throws Exception;
    
    Map<String, Object> findSpecialGroupActivityById(int specialGroupActivityId)
        throws Exception;
    
    Map<String, Object> findSpecialGroupActivityProductByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 编辑情景模版商品
     * @param id：id
     * @param type：类型，1楼层，2更多
     * @param activityId：活动Id
     * @param layoutType：布局类型，1一张一张，2一行两张
     * @param oneType：第一张关联类型，1：商品，2：组合；3：自定义活动；4：点击不跳转
     * @param oneRemark：第一张备注
     * @param oneImageUrl：第一张图片url
     * @param oneDisplayId：第一张关联对象Id
     * @param twoType：第二张关联类型，1：商品，2：组合；3：自定义活动；4：点击不跳转
     * @param twoRemark：第二张备注
     * @param twoImageUrl：第二张图片url
     * @param twoDisplayId：第二张关联对象Id
     * @param productId：商品Id
     * @param isDisplay：是否展现，1是，0否
     * @return
     * @throws Exception
     */
    String editSpecialGroupActivityProduct(int id, int activityId, int type, int layoutType, int oneType, String oneRemark, String oneImageUrl, int oneDisplayId, int twoType,
        String twoRemark, String twoImageUrl, int twoDisplayId, int productId, int isDisplay)
        throws Exception;
    
    /**
     * 编辑情景模版商品排序值
     * @param id
     * @param sequence
     * @return
     * @throws Exception
     */
    String updateSpecialGroupActivityProductSequence(int id, int sequence)
        throws Exception;
    
    /**
     * 删除情景模版商品
     * @param id
     * @return
     * @throws Exception
     */
    String deleteSpecialGroupActivityProduct(int id)
        throws Exception;
    
    /**
     * 编辑情景模版商品展现状态
     * @param id
     * @param isDisplay
     * @return
     * @throws Exception
     */
    String updateSpecialGroupActivityProductDisplay(int id, int isDisplay)
        throws Exception;
    
    /**
     * 情景模版批量添加商品
     * @param activityId
     * @param type
     * @param isDisplay
     * @param productIdList
     * @return
     * @throws Exception
     */
    String batchAddSpecialGroupActivityProduct(int activityId, int type, int isDisplay, List<Integer> productIdList)
        throws Exception;
    
    List<Map<String, Object>> findAllSpecialGroupActivity(Map<String, Object> para)
        throws Exception;
    
}
