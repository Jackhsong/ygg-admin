package com.ygg.admin.time;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ygg.admin.cache.CacheManager;
import com.ygg.admin.cache.CacheServiceIF;
import com.ygg.admin.config.YggAdminProperties;
import com.ygg.admin.dao.QueryImageDao;
import com.ygg.admin.time.base.AbstractJobService;
import com.ygg.admin.util.ImageUtil;
import com.ygg.common.services.image.ImgYunManager;
import com.ygg.common.services.image.OSSImgYunServiceIF;
import com.ygg.common.services.image.UpImgYunServiceIF;
import com.ygg.common.utils.CommonConst;

public class BackUpImageToAliyunJob extends AbstractJobService
{
    //    private int ROWS = 400;
    
    //    private int PAGE = 1;
    
    private static int isExecuted = 0;
    
    private Logger logger = Logger.getLogger(BackUpImageToAliyunJob.class);
    
    @Resource
    private QueryImageDao queryImageDao;
    
    private OSSImgYunServiceIF ossImgYunService = (OSSImgYunServiceIF)ImgYunManager.getClient(CommonConst.IMG_ALIYUN);
    
    private UpImgYunServiceIF upImgYunService = (UpImgYunServiceIF)ImgYunManager.getClient(CommonConst.IMG_UPYUN);
    
    private CacheServiceIF cache = CacheManager.getClient();
    
    /**
     * 查询account表
     * @param para
     * @return
     * @throws Exception
     */
    private Set<String> getImageFromTableAccount(Map<String, Object> para)
        throws Exception
    {
        Set<String> resultSet = new HashSet<String>();
        List<String> imageList = queryImageDao.findAccountImage(para);
        for (String image : imageList)
        {
            if (StringUtils.isNotEmpty(image))
            {
                resultSet.add(image);
            }
        }
        return resultSet;
    }
    
    /**
     * 查询activities_common表
     * 
     * @param rows
     * @param page
     * @return
     * @throws Exception
     */
    private Set<String> getImageFromTableActivitiesCommon(Map<String, Object> para)
        throws Exception
    {
        Set<String> resultSet = new HashSet<String>();
        List<String> imageList = queryImageDao.findActivitiesCommonImage(para);
        for (String image : imageList)
        {
            if (StringUtils.isNotEmpty(image))
            {
                resultSet.add(image);
            }
        }
        return resultSet;
    }
    
    /**
     * 查询banner_window表
     * 
     * @param para
     * @return
     * @throws Exception
     */
    private Set<String> getImageFromTableBannerWindow(Map<String, Object> para)
        throws Exception
    {
        Set<String> resultSet = new HashSet<String>();
        List<String> imageList = queryImageDao.findBannerWindowImage(para);
        for (String image : imageList)
        {
            if (StringUtils.isNotEmpty(image))
            {
                resultSet.add(image);
            }
        }
        return resultSet;
    }
    
    /**
     * 查询brand表
     * 
     * @param para
     * @return
     * @throws Exception
     */
    private Set<String> getImageFromTableBrand(Map<String, Object> para)
        throws Exception
    {
        Set<String> resultSet = new HashSet<String>();
        List<String> imageList = queryImageDao.findBrandImage(para);
        for (String image : imageList)
        {
            if (StringUtils.isNotEmpty(image))
            {
                resultSet.add(image);
            }
        }
        return resultSet;
    }
    
    /**
     * 查询gege_image_activities表
     * 
     * @param para
     * @return
     * @throws Exception
     */
    private Set<String> getImageFromTableGegeImageActivities(Map<String, Object> para)
        throws Exception
    {
        Set<String> resultSet = new HashSet<String>();
        List<String> imageList = queryImageDao.findGegeImageActivitiesImage(para);
        for (String image : imageList)
        {
            if (StringUtils.isNotEmpty(image))
            {
                resultSet.add(image);
            }
        }
        return resultSet;
    }
    
    /**
     * 查询gege_image_product表
     * 
     * @param para
     * @return
     * @throws Exception
     */
    private Set<String> getImageFromTableGegeImageProduct(Map<String, Object> para)
        throws Exception
    {
        Set<String> resultSet = new HashSet<String>();
        List<String> imageList = queryImageDao.findGegeImageProductImage(para);
        for (String image : imageList)
        {
            if (StringUtils.isNotEmpty(image))
            {
                resultSet.add(image);
            }
        }
        return resultSet;
    }
    
    /**
     * 查询order_product表
     * 
     * @param para
     * @return
     * @throws Exception
     */
    private Set<String> getImageFromTableOrderProduct(Map<String, Object> para)
        throws Exception
    {
        Set<String> resultSet = new HashSet<String>();
        List<String> imageList = queryImageDao.findOrderProductImage(para);
        for (String image : imageList)
        {
            if (StringUtils.isNotEmpty(image))
            {
                resultSet.add(image);
            }
        }
        return resultSet;
    }
    
    /**
     * 查询order_product_refund表
     * 
     * @param para
     * @return
     * @throws Exception
     */
    private Set<String> getImageFromTableOrderProductRefund(Map<String, Object> para)
        throws Exception
    {
        return queryImageDao.findOrderProductRefundImage(para);
    }
    
    /**
     * 查询product_base表
     * 
     * @param para
     * @return
     * @throws Exception
     */
    private Set<String> getImageFromTableProductBase(Map<String, Object> para)
        throws Exception
    {
        return queryImageDao.findProductBaseImage(para);
    }
    
    /**
     * 查询product表
     * 
     * @param para
     * @return
     * @throws Exception
     */
    private Set<String> getImageFromTableProduct(Map<String, Object> para)
        throws Exception
    {
        return queryImageDao.findProductImage(para);
    }
    
    /**
     * 查询product_common表
     * 
     * @param para
     * @return
     * @throws Exception
     */
    private Set<String> getImageFromTableProductCommon(Map<String, Object> para)
        throws Exception
    {
        return queryImageDao.findProductCommonImage(para);
    }
    
    /**
     * 查询product_mobile_detail表
     * 
     * @param para
     * @return
     * @throws Exception
     */
    private Set<String> getImageFromTableProductMobileDetail(Map<String, Object> para)
        throws Exception
    {
        Set<String> resultSet = new HashSet<String>();
        List<String> imageList = queryImageDao.findProductMobileDetailImage(para);
        for (String image : imageList)
        {
            if (StringUtils.isNotEmpty(image))
            {
                resultSet.add(image);
            }
        }
        return resultSet;
    }
    
    /**
     * 查询product_base_mobile_detail表
     * 
     * @param para
     * @return
     * @throws Exception
     */
    private Set<String> getImageFromTableProductBaseMobileDetail(Map<String, Object> para)
        throws Exception
    {
        Set<String> resultSet = new HashSet<String>();
        List<String> imageList = queryImageDao.findProductBaseMobileDetailImage(para);
        for (String image : imageList)
        {
            if (StringUtils.isNotEmpty(image))
            {
                resultSet.add(image);
            }
        }
        return resultSet;
    }
    
    /**
     * 查询sale_tag表
     * 
     * @param para
     * @return
     * @throws Exception
     */
    private Set<String> getImageFromTableSaleTag(Map<String, Object> para)
        throws Exception
    {
        Set<String> resultSet = new HashSet<String>();
        List<String> imageList = queryImageDao.findSaleTagImage(para);
        for (String image : imageList)
        {
            if (StringUtils.isNotEmpty(image))
            {
                resultSet.add(image);
            }
        }
        return resultSet;
    }
    
    /**
     * 查询sale_window表
     * 
     * @param para
     * @return
     * @throws Exception
     */
    private Set<String> getImageFromTableSaleWindow(Map<String, Object> para)
        throws Exception
    {
        Set<String> resultSet = new HashSet<String>();
        List<String> imageList = queryImageDao.findSaleWindowImage(para);
        for (String image : imageList)
        {
            if (StringUtils.isNotEmpty(image))
            {
                resultSet.add(image);
            }
        }
        return resultSet;
    }
    
    // 根据url获取文件名  http://yangege.b0.upaiyun.com/saleTag/680044bc4ecf.jpg
    private String getFileNameFromUrl(String url)
    {
        String name = "";
        //        int index = url.lastIndexOf("/");
        int index = url.indexOf(".com/");
        if (index > 0)
        {
            name = url.substring(index + 5);
        }
        if (name.indexOf(ImageUtil.getPrefix()) > 0)
        {
            name = name.substring(0, name.indexOf(ImageUtil.getPrefix()));
        }
        return name;
    }
    
    // 从url下载文件
    private File downloadFileFromUrl(String url, String dir)
    {
        File file = null;
        logger.info("==========================================");
        try
        {
            logger.info("***********开始从又拍云下载图片***********");
            URL httpurl = new URL(url);
            logger.info("***********又拍云图片地址：" + url + "***********");
            String fileName = getFileNameFromUrl(url);
            File newDir = new File(dir);
            newDir.mkdir();
            if (StringUtils.isNotEmpty(fileName))
            {
                logger.info("***********又拍云图片名称：" + fileName + "***********");
                file = new File(newDir, fileName);
                FileUtils.copyURLToFile(httpurl, file);
                logger.info("***********从又拍云下载图片成功***********");
            }
        }
        catch (Exception e)
        {
            logger.info("***********从又拍云下载图片失败***********");
            e.printStackTrace();
        }
        logger.info("==========================================");
        return file;
    }
    
    @Override
    public String getDescription()
    {
        return "定时任务：备份又拍云图片到阿里云";
    }
    
    @Override
    public void doExecute()
        throws Exception
    {
        try
        {
            if (isExecuted > 0)
            {
                logger.info("***********上次没执行完，这次又开始执行，退出重新执行***********");
                return;
            }
            isExecuted = 1;
            Set<String> imageURLSet = new HashSet<String>();
            Map<String, Object> para = new HashMap<String, Object>();
            //临时接口，这里key也就这样写了额。
            Integer ROWS = 400;
            Integer PAGE = Integer.parseInt(cache.get("admin_prod_backUpImageToAliyunJob_page") == null ? "1" : cache.get("admin_prod_backUpImageToAliyunJob_page") + "");
            logger.info("***********开始查询第" + PAGE + "页数据***********");
            para.put("start", ROWS * (PAGE - 1));
            para.put("max", ROWS);
            
            imageURLSet.addAll(getImageFromTableAccount(para));
            
            imageURLSet.addAll(getImageFromTableActivitiesCommon(para));
            
            imageURLSet.addAll(getImageFromTableBannerWindow(para));
            
            imageURLSet.addAll(getImageFromTableBrand(para));
            
            imageURLSet.addAll(getImageFromTableGegeImageActivities(para));
            
            imageURLSet.addAll(getImageFromTableGegeImageProduct(para));
            
            imageURLSet.addAll(getImageFromTableOrderProduct(para));
            
            imageURLSet.addAll(getImageFromTableOrderProductRefund(para));
            
            imageURLSet.addAll(getImageFromTableProduct(para));
            
            // imageURLSet.addAll(getImageFromTableProductBase(para));
            
            imageURLSet.addAll(getImageFromTableProductCommon(para));
            
            imageURLSet.addAll(getImageFromTableProductMobileDetail(para));
            
            // imageURLSet.addAll(getImageFromTableProductBaseMobileDetail(para));
            
            imageURLSet.addAll(getImageFromTableSaleTag(para));
            
            imageURLSet.addAll(getImageFromTableSaleWindow(para));
            
            for (String url : imageURLSet)
            {
                File file = downloadFileFromUrl(url, YggAdminProperties.getInstance().getPropertie("order_zip_download_dir"));
                String fileName = getFileNameFromUrl(url);
                if (file != null && StringUtils.isNotEmpty(fileName))
                {
                    logger.info("++++++++++++++++++++++++++++++++++++++++++");
                    logger.info("***********开始上传到阿里云***********");
                    Map<String, String> result = ossImgYunService.uploadFile(file, fileName);
                    if (file.exists())
                    {
                        file.delete();
                        logger.info("***********上传完成，删除文件***********");
                    }
                    if (result != null)
                    {
                        logger.info("***********上传到阿里云的文件：" + fileName + "***********");
                        logger.info("***********上传到阿里云的状态：" + result.get("status") + "***********");
                        logger.info("***********阿里云文件的地址：" + result.get("url") + "***********");
                    }
                }
            }
            if (imageURLSet.size() == 0)
            {
                logger.info("***********备份完成***********");
            }
            PAGE++;
            cache.set("admin_prod_backUpImageToAliyunJob_page", PAGE, 3 * 24 * 60 * 60);
            isExecuted = 0;
        }
        catch (Exception e)
        {
            logger.info("***********上传到阿里云失败***********");
            logger.error(e.getMessage(), e);
        }
        logger.info("++++++++++++++++++++++++++++++++++++++++++");
    }
}
