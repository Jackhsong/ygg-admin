package com.ygg.admin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ygg.admin.dao.OverseasDelayDateDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.service.OverseasDelayRemindDateService;

@Service("delayRemindDateService")
public class OverseasDelayRemindDateServiceImpl extends BaseDaoImpl implements OverseasDelayRemindDateService
{
    @Resource
    private OverseasDelayDateDao overseasDelayDateDao;
    
    @Override
    public int delete(Map<String, Object> para)
        throws Exception
    {
        return overseasDelayDateDao.delete(para);
    }
    
    @Override
    public int saveOrUpdate(Map<String, Object> para)
        throws Exception
    {
        return overseasDelayDateDao.saveOrUpdate(para);
    }
    
    @Override
    public Map<String, Object> jsonInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> categoryInfoList = overseasDelayDateDao.findOverseasDelayDateInfo(para);
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        int total = 0;
        if (categoryInfoList.size() > 0)
        {
            for (Map<String, Object> curr : categoryInfoList)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", curr.get("id"));
                map.put("sendDate", curr.get("day"));
                map.put("beginTime", curr.get("startHour"));
                map.put("endTime", curr.get("endHour"));
                resultList.add(map);
            }
            total = overseasDelayDateDao.countOverseasDelayDateInfo(para);
        }
        resultMap.put("rows", resultList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public boolean deleteByYear(String year)
        throws Exception
    {
        int result = overseasDelayDateDao.deleteByYear(year);
        return result > 0;
    }
    
    @Override
    public Map<String, Object> importDelayDate(MultipartFile file, String year)
        throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        
        Workbook workbook = new HSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        
        int startNum = sheet.getFirstRowNum();
        int lastNum = sheet.getLastRowNum();
        if (startNum == lastNum)
        {// 可过滤第一行，因为第一行是标题
            map.put("status", 0);
            map.put("msg", "文件为空请确认！");
            return map;
        }
        
        // 删除以前的数据
        overseasDelayDateDao.deleteByYear(year);
        
        for (int i = startNum + 1; i <= lastNum; i++)
        {
            Row row = sheet.getRow(i);
            Cell cell0 = row.getCell(0);
            Cell cell1 = row.getCell(1);
            Cell cell2 = row.getCell(2);
            
            if (cell0 != null)
            {
                cell0.setCellType(Cell.CELL_TYPE_STRING);
            }
            if (cell1 != null)
            {
                cell1.setCellType(Cell.CELL_TYPE_NUMERIC);
            }
            if (cell2 != null)
            {
                cell2.setCellType(Cell.CELL_TYPE_NUMERIC);
            }
            
            String day = cell0 == null ? "" : cell0.getStringCellValue();
            int beginHour = cell1 == null ? 0 : (int)cell1.getNumericCellValue();
            int endHour = cell2 == null ? 24 : (int)cell2.getNumericCellValue();
            
            if ("".equals(day))
            {
                map.put("status", 0);
                map.put("msg", "第" + (i + 1) + "行日期不能为空");
                throw new RuntimeException("第" + (i + 1) + "行日期不能为空");
            }
            if (beginHour >= endHour)
            {
                map.put("status", 0);
                map.put("msg", "第" + (i + 1) + "结束时间不能小于开始时间");
                throw new RuntimeException("第" + (i + 1) + "结束时间不能小于开始时间");
            }
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", 0);
            para.put("day", day);
            para.put("startHour", beginHour);
            para.put("endHour", endHour);
            int resultStatus = overseasDelayDateDao.saveOrUpdate(para);
            if (resultStatus == 0)
            {
                map.put("status", 0);
                map.put("msg", "第" + (i + 1) + "导入失败");
                throw new Exception("第" + (i + 1) + "导入失败");
            }
        }
        map.put("status", 1);
        map.put("msg", "导入成功");
        return map;
    }
    
    @Override
    public boolean isExist(Map<String, Object> para)
        throws Exception
    {
        int count = overseasDelayDateDao.isExist(para);
        return count > 0;
    }
}
