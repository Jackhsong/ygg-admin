package com.ygg.admin.service;

import java.util.Map;

public interface CustomerProblemService
{
    
    String jsonCustomerProblemInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 新增客服问题
     * @param question：问题
     * @param answer：答案
     * @param sequence：排序值
     * @param isDisplay：是否展现，1展现，0不展现
     * @return
     * @throws Exception
     */
    String saveCustomerProblem(String question, String answer, int sequence, int isDisplay)
        throws Exception;
    
    /**
     * 更新客服问题
     * @param id：id
     * @param question：问题
     * @param answer：答案
     * @param sequence：排序值
     * @param isDisplay：是否展现
     * @return
     * @throws Exception
     */
    String updateCustomerProblem(int id, String question, String answer, int sequence, int isDisplay)
        throws Exception;
    
    /**
     * 修改客服问题展现状态
     * @param id：id
     * @param isDisplay：1展现，0隐藏
     * @return
     * @throws Exception
     */
    String updateCustomerProblemStatus(int id, int isDisplay)
        throws Exception;
    
}
