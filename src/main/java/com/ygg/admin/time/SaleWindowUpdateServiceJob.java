package com.ygg.admin.time;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.ygg.admin.dao.SaleWindowDao;
import com.ygg.admin.entity.SaleWindowEntity;

public class SaleWindowUpdateServiceJob
{
    Logger log = Logger.getLogger(SaleWindowUpdateServiceJob.class);
    
    @Resource(name = "saleWindowDao")
    private SaleWindowDao saleWindowDao = null;
    
    /**
     * 定时器自动执行   已经停用
     */
    public void autoWork()
    {
        // 记录开始时间
        long startAt = System.currentTimeMillis();
        String name = "定时器10点更新特卖排序值";
        log.info(name + "任务开始执行~~~");
        try
        {
            // 执行具体业务逻辑
            doExecute(true);
            // 记录结束时间
            long endAt = System.currentTimeMillis();
            log.info(name + "定时器任务执行结束。耗时：" + (endAt - startAt) + "毫秒");
        }
        catch (Exception e)
        {
            log.error(name + "定时器定时任务执行失败", e);
        }
    }
    
    /**
     * 手动执行
     */
    public void manWork()
    {
        // 记录开始时间
        long startAt = System.currentTimeMillis();
        String name = "手动更新特卖排序值";
        log.info(name + "开始执行~~~");
        try
        {
            // 执行具体业务逻辑
            int hour = DateTime.now().getHourOfDay();
            boolean beforeTen = (hour < 10) ? true : false;
            if (beforeTen)
            {
                doExecuteBeforeTen();
            }
            else
            {
                doExecuteAfterTen();
            }
            // 记录结束时间
            long endAt = System.currentTimeMillis();
            log.info(name + "执行结束。耗时：" + (endAt - startAt) + "毫秒");
        }
        catch (Exception e)
        {
            log.error(name + "执行失败", e);
        }
    }
    
    private void doExecuteBeforeTen()
        throws Exception
    {
        // 当前时间为2015-03-07 9:00:00 得到值:20150307
        // 1. 得到2015-03-07 10:00:00开始的特卖列表
        int nowIntValue = Integer.valueOf(DateTime.now().toString("yyyyMMdd"));// 20150307
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("start", 0);
        para.put("max", 1000);
        para.put("startTime", nowIntValue);
        List<SaleWindowEntity> saleWindowsWaitting = saleWindowDao.findAllSaleWindow(para);
        // 2. 已经开始并且明天10点后仍然进行的特卖 start < 20150307 - end > 20150306
        para.remove("startTime");
        para.put("status", 2);
        int compareToStart = Integer.valueOf(DateTime.now().toString("yyyyMMdd")).intValue();
        int compareToEnd = Integer.valueOf(DateTime.now().plusDays(-1).toString("yyyyMMdd")).intValue();
        para.put("compareToStart", compareToStart);
        para.put("compareToEnd", compareToEnd);
        List<SaleWindowEntity> saleWindowsStillRunning = saleWindowDao.findAllSaleWindow(para);
        Collections.sort(saleWindowsStillRunning, new Comparator<SaleWindowEntity>()
        {
            @Override
            public int compare(SaleWindowEntity entity1, SaleWindowEntity entity2)
            {
                return entity1.getNowOrder() - entity2.getNowOrder();
            }
        });
        Collections.sort(saleWindowsWaitting, new Comparator<SaleWindowEntity>()
        {
            @Override
            public int compare(SaleWindowEntity entity1, SaleWindowEntity entity2)
            {
                return entity1.getNowOrder() - entity2.getNowOrder();
            }
        });
        System.out.println("------1------");
        System.out.println(saleWindowsWaitting);
        System.out.println("------2------");
        System.out.println(saleWindowsStillRunning);
        int index = updateSaleOrder(2, 1, saleWindowsStillRunning);
        updateSaleOrder(2, index, saleWindowsWaitting);
    }
    
    private void doExecuteAfterTen()
        throws Exception
    {
        // 当前时间为2015-03-07 11:00:00 得到值:20150307
        // 1. 得到2015-03-08 10:00:00开始的特卖列表
        int nowIntValue = Integer.valueOf(DateTime.now().plusDays(1).toString("yyyyMMdd"));// 20150308
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("start", 0);
        para.put("max", 1000);
        para.put("startTime", nowIntValue);
        List<SaleWindowEntity> saleWindowsWaitting = saleWindowDao.findAllSaleWindow(para);
        log.info("得到" + nowIntValue + "开始的特卖列表");
        for (SaleWindowEntity entity : saleWindowsWaitting)
        {
            log.info(entity.getId() + ":" + entity.getName());
        }
        // 2. 已经开始并且明天10点后仍然进行的特卖 start < 20150308 - end > 20150307
        para.remove("startTime");
        para.put("status", 2);
        int compareToStart = Integer.valueOf(DateTime.now().plusDays(1).toString("yyyyMMdd")).intValue();
        int compareToEnd = Integer.valueOf(DateTime.now().toString("yyyyMMdd")).intValue();
        para.put("compareToStart", compareToStart);
        para.put("compareToEnd", compareToEnd);
        List<SaleWindowEntity> saleWindowsStillRunning = saleWindowDao.findAllSaleWindow(para);
        log.info("得到" + nowIntValue + "任然在售的特卖列表");
        for (SaleWindowEntity entity : saleWindowsStillRunning)
        {
            log.info(entity.getId() + ":" + entity.getName());
        }
        
        Collections.sort(saleWindowsStillRunning, new Comparator<SaleWindowEntity>()
        {
            @Override
            public int compare(SaleWindowEntity entity1, SaleWindowEntity entity2)
            {
                return entity1.getNowOrder() - entity2.getNowOrder();
            }
        });
        
        Collections.sort(saleWindowsWaitting, new Comparator<SaleWindowEntity>()
        {
            @Override
            public int compare(SaleWindowEntity entity1, SaleWindowEntity entity2)
            {
                return entity1.getNowOrder() - entity2.getNowOrder();
            }
        });
        
        int index = updateSaleOrder(2, 1, saleWindowsStillRunning);
        updateSaleOrder(2, index, saleWindowsWaitting);
    }
    
    private void doExecute(boolean auto)
        throws Exception
    {
        // 当前时间为2015-03-07 10:00:00 得到值:20150307
        int nowIntValue = Integer.valueOf(DateTime.now().toString("yyyyMMdd"));
        
        // 1. 得到2015-03-07 10:00:00开始的特卖列表
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("start", 0);
        para.put("max", 1000);
        para.put("startTime", nowIntValue);
        List<SaleWindowEntity> saleWindowsStarting = saleWindowDao.findAllSaleWindow(para);
        log.info("得到" + nowIntValue + "开始的特卖列表");
        for (SaleWindowEntity entity : saleWindowsStarting)
        {
            log.info(entity.getId() + ":" + entity.getName());
        }
        
        // 2. 得到2015-03-08 10:00:00开始的特卖列表
        nowIntValue = Integer.valueOf(DateTime.now().plusDays(1).toString("yyyyMMdd"));
        para.put("startTime", nowIntValue);
        List<SaleWindowEntity> saleWindowsTomorrowStarting = saleWindowDao.findAllSaleWindow(para);
        log.info("得到" + nowIntValue + "开始的特卖列表");
        for (SaleWindowEntity entity : saleWindowsStarting)
        {
            log.info(entity.getId() + ":" + entity.getName());
        }
        
        // 3.得到2015-03-09 10:00:00开始的特卖列表
        nowIntValue = Integer.valueOf(DateTime.now().plusDays(2).toString("yyyyMMdd"));
        para.put("startTime", nowIntValue);
        List<SaleWindowEntity> saleWindowsAfterTomorrowStarting = saleWindowDao.findAllSaleWindow(para);
        log.info("得到" + nowIntValue + "开始的特卖列表");
        for (SaleWindowEntity entity : saleWindowsStarting)
        {
            log.info(entity.getId() + ":" + entity.getName());
        }
        
        // 4. 已经开始并且10点后仍然进行的特卖 start < 20150306 - end > 20150305
        para.remove("startTime");
        para.put("status", 2);
        int compareToStart = Integer.valueOf(DateTime.now().toString("yyyyMMdd")).intValue();
        int compareToEnd = Integer.valueOf(DateTime.now().plusDays(-1).toString("yyyyMMdd")).intValue();
        para.put("compareToStart", compareToStart);
        para.put("compareToEnd", compareToEnd);
        List<SaleWindowEntity> saleWindowsStillRunning = saleWindowDao.findAllSaleWindow(para);
        
        /*
         * 第一步： 当前时间为10点后，设置今日正售的今日排序值。分两块排序，一块有明日排序值（说明10点前有人设置过了该值），一块没有。 有明日排序值块
         * ：1中设置了的和4的全部，按明日排序值升序。（注意：只有在第一天的情况下，可能会出现没有4的情况） 没有明日排序值块：1中没有设置的全部，先按今日升序，其次按UpdateTime升序。
         * 得到今日正售排序（从小到大分别为）：有明日排序值块 + 没有明日排序值块 。
         * 
         * 第二步： 设置2中得到的特卖的今日排序值，排序规则，按明日排序值升序。若没有明日排序值，则按UpdateTime升序。
         * 
         * 第三步： 设置3中得到的特卖的明日排序值，排序规则，按UpdateTime排序。
         * 
         * 第四步： 设置第一、第二步的特卖（也就是1和2得到的特卖）的明日排序值，排序规则，直接copy他们的今日排序值，当然要排除明日就结束的特卖。
         */
        List<SaleWindowEntity> saleWindowsStarting_haveLaterOrder = new ArrayList<SaleWindowEntity>();
        List<SaleWindowEntity> saleWindowsStarting_notHaveLaterOrder = new ArrayList<SaleWindowEntity>();
        for (SaleWindowEntity entity : saleWindowsStarting)
        {
            if (entity.getLaterOrder() > 0)
            {
                saleWindowsStarting_haveLaterOrder.add(entity);
            }
            else
            {
                saleWindowsStarting_notHaveLaterOrder.add(entity);
            }
        }
        List<SaleWindowEntity> saleWindowsRunning = new ArrayList<SaleWindowEntity>();
        saleWindowsRunning.addAll(saleWindowsStarting_haveLaterOrder);
        saleWindowsRunning.addAll(saleWindowsStillRunning);
        // 更新今日排序值
        sortList(saleWindowsRunning);
        sortList(saleWindowsStarting_notHaveLaterOrder);
        int index = updateSaleOrder(1, 1, saleWindowsRunning);
        updateSaleOrder(1, index, saleWindowsStarting_notHaveLaterOrder);
        // --------------二--------------------
        sortList(saleWindowsTomorrowStarting);
        updateSaleOrder(1, 1, saleWindowsTomorrowStarting);
        // --------------三--------------------
        sortList(saleWindowsAfterTomorrowStarting);
        updateSaleOrder(2, 1, saleWindowsAfterTomorrowStarting);
        // --------------四--------------------
        doExecuteAfterTenAuto();
        
    }
    
    private void doExecuteAfterTenAuto()
        throws Exception
    {
        // 当前时间为2015-03-06 11:00:00 得到值:20150306
        // 1. 得到2015-03-07 10:00:00开始的特卖列表
        int nowIntValue = Integer.valueOf(DateTime.now().plusDays(1).toString("yyyyMMdd"));
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("start", 0);
        para.put("max", 1000);
        para.put("startTime", nowIntValue);
        List<SaleWindowEntity> saleWindowsWaitting = saleWindowDao.findAllSaleWindow(para);
        System.out.println("-------doExecuteAfterTenAuto----saleWindowsWaitting-------------");
        System.out.println(saleWindowsWaitting);
        // 2. 已经开始并且明天10点后仍然进行的特卖 start < 20150306 - end > 20150305
        para.remove("startTime");
        para.put("status", 2);
        int compareToStart = Integer.valueOf(DateTime.now().plusDays(1).toString("yyyyMMdd")).intValue();
        int compareToEnd = Integer.valueOf(DateTime.now().toString("yyyyMMdd")).intValue();
        para.put("compareToStart", compareToStart);
        para.put("compareToEnd", compareToEnd);
        List<SaleWindowEntity> saleWindowsStillRunning = saleWindowDao.findAllSaleWindow(para);
        System.out.println("-----------doExecuteAfterTenAuto---saleWindowsStillRunning----------");
        System.out.println(saleWindowsStillRunning);
        Collections.sort(saleWindowsStillRunning, new Comparator<SaleWindowEntity>()
        {
            @Override
            public int compare(SaleWindowEntity entity1, SaleWindowEntity entity2)
            {
                return entity1.getNowOrder() - entity2.getNowOrder();
            }
        });
        Collections.sort(saleWindowsWaitting, new Comparator<SaleWindowEntity>()
        {
            @Override
            public int compare(SaleWindowEntity entity1, SaleWindowEntity entity2)
            {
                return entity1.getNowOrder() - entity2.getNowOrder();
            }
        });
        int index = updateSaleOrder(2, 1, saleWindowsStillRunning);
        updateSaleOrder(2, index, saleWindowsWaitting);
    }
    
    private int updateSaleOrder(int type, int index, List<SaleWindowEntity> saleList)
        throws Exception
    {
        int hour = DateTime.now().getHourOfDay();
        boolean beforeTen = (hour < 10) ? true : false;
        for (SaleWindowEntity entity : saleList)
        {
            Map<String, Object> p = new HashMap<String, Object>();
            p.put("id", entity.getId());
            if (type == 1)
            {// 更新nowOrder
                p.put("nowOrder", index++);
                saleWindowDao.updateNowOrder(p);
            }
            else
            {// 更新LaterOrder，得判断是否需要更新明日排序值
                int nowIntValue = Integer.valueOf(DateTime.now().toString("yyyyMMdd"));// 20150403
                if (beforeTen)
                {
                    if (entity.getEndTime() >= nowIntValue)
                    {
                        p.put("laterOrder", index++);
                        saleWindowDao.updateLaterOrder(p);
                    }
                }
                else
                {
                    if (entity.getEndTime() > nowIntValue)
                    {
                        p.put("laterOrder", index++);
                        saleWindowDao.updateLaterOrder(p);
                    }
                }
            }
        }
        return index;
    }
    
    /**
     * SaleWindow 排序，对于没有laterOrder的SaleWindow 优先按照nowOrder排序，其次按更新时间排列
     * 
     * @param saleList
     * @throws Exception
     */
    private void sortList(List<SaleWindowEntity> saleList)
        throws Exception
    {
        Collections.sort(saleList, new Comparator<SaleWindowEntity>()
        {
            @Override
            public int compare(SaleWindowEntity entity1, SaleWindowEntity entity2)
            {
                if (entity1.getLaterOrder() == entity2.getLaterOrder())
                {
                    if (entity1.getNowOrder() == entity2.getNowOrder())
                    {
                        return entity1.getUpdateTime().compareTo(entity2.getUpdateTime());
                    }
                    return entity1.getNowOrder() - entity2.getNowOrder();
                }
                else
                {
                    return entity1.getLaterOrder() - entity2.getLaterOrder();
                }
            }
        });
    }
    
}
