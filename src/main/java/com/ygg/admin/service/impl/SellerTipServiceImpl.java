package com.ygg.admin.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ygg.admin.dao.SellerTipDao;
import com.ygg.admin.service.SellerTipService;

@Service("sellerTipService")
public class SellerTipServiceImpl implements SellerTipService
{
    
    @Resource
    private SellerTipDao sellerTipDao;
    
    @Override
    public int saveOrUpdate(Map<String, Object> param)
        throws Exception
    {
        Object id = param.get("id");
        // 验证title是否重复
        Map<String, Object> p = new HashMap<String, Object>();
        p.put("title", param.get("title"));
        p.put("id", id);
        int count = sellerTipDao.checkTitle(p);
        if (count > 0)
            throw new RuntimeException("标题重复了");
        
        // 验证提示有效
        // 如果本次提交的状态是开启状态，那么不应该存在其他的状态是开启状态
        // 如果本次提交的状态是关闭状态，就可以不用验证，
        int status = Integer.valueOf(param.get("status").toString());
        if (status == 1)
        {
            p.remove("title");
            count = sellerTipDao.checkStatus(p);
            if(count > 0)
                throw new RuntimeException("只能用一条公告是开启状态");
        }
        
        // 保存或更新操作
        if (id == null)
        {
            count = sellerTipDao.save(param);
        }
        else
        {
            count = sellerTipDao.update(param);
        }
        return count;
    }
    
    @Override
    public Map<String, Object> findById(int id)
        throws Exception
    {
        return sellerTipDao.findById(id);
    }
    
    @Override
    public Map<String, Object> findListInfo(Map<String, Object> param)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", sellerTipDao.countList(param));
        result.put("rows", sellerTipDao.findListInfo(param));
        return result;
    }
    
}
