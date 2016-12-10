package com.ygg.admin.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ygg.admin.dao.PageCustomDao;
import com.ygg.admin.entity.PageCustomEntity;
import com.ygg.admin.service.PageCustomService;

@Service("pageCustomService")
public class PageCustomServiceImpl implements PageCustomService
{
    
    @Resource
    PageCustomDao pageCustomDao = null;
    
    @Override
    public List<PageCustomEntity> findAllPageCustomForProduct()
        throws Exception
    {
        return pageCustomDao.findAllPageCustomForProduct();
    }
    
    @Override
    public PageCustomEntity findPageCustomById(int id)
        throws Exception
    {
        return pageCustomDao.findPageCustomByid(id);
    }
    
    @Override
    public int update(PageCustomEntity para)
        throws Exception
    {
        Map map = new HashMap();
        map.put("id", para.getId());
        map.put("name", para.getName());
        map.put("desc", para.getDesc());
        map.put("pcUrl", para.getPcUrl());
        map.put("mobileUrl", para.getMobileUrl());
        map.put("fileName", para.getFileName());
        map.put("isAvailable", para.getIsAvailable());
        map.put("mobileDetail", para.getMobileDetail());
        map.put("pcDetail", para.getPcDetail());
        return pageCustomDao.update(map);
    }
    
    @Override
    public int countPageCustom(Map<String, Object> para)
        throws Exception
    {
        return pageCustomDao.countPageCustom(para);
    }
    
    @Override
    public List<PageCustomEntity> findPageCustom(Map<String, Object> para)
        throws Exception
    {
        return pageCustomDao.findAllPageCustomByPara(para);
    }
    
    @Override
    public int write(PageCustomEntity entity)
        throws Exception
    {
        // 写入静态文件
        String mobileDir = "/alidata/server/image/mobile/web";
        String pcDir = "/alidata/server/image/mobile/app";
        //        String pchome = "/alidata/server/pchome";
        
        writeFile(mobileDir, entity.getFileName() + ".html", entity.getMobileDetail());
        writeFile(pcDir, entity.getFileName() + ".html", entity.getPcDetail());
        //        writeFile(pchome, entity.getFileName() + ".html", entity.getMobileDetail());
        return 1;
    }
    
    private void writeFile(String dir, String fileName, String text)
        throws Exception
    {
        File file = new File(dir, fileName);
        if (file.exists())
        {
            file.delete();
        }
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF8"));
        bw.write(text);
        bw.close();
        fos.close();
    }
    
    @Override
    public int save(PageCustomEntity entity)
        throws Exception
    {
        return pageCustomDao.save(entity);
    }
    
}
