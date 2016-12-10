package com.ygg.admin.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Splitter;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.config.YggAdminProperties;
import com.ygg.admin.util.CommonConstant;
import com.ygg.admin.util.FileUtil;
import com.ygg.admin.util.ZipCompressorByAnt;
import com.ygg.admin.util.ZipUtil;
import com.ygg.common.services.image.ImgYunManager;
import com.ygg.common.services.image.OSSImgYunServiceIF;
import com.ygg.common.services.image.UpImgYunServiceIF;
import com.ygg.common.utils.CommonConst;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/pic")
public class PictureController
{
    Logger logger = Logger.getLogger(PictureController.class);
    
    // 线程池维护线程的最少数量
    private final static int COREPOOLSIZE = 1;
    
    // 线程池允许线程的最大数量
    private final static int MAXIMUMPOOLSIZE = 1;
    
    // 当线程数大于核心时，此为终止前多余的空闲线程等待新任务的最长时间,单位秒
    private final static int KEEPALIVETIME = 5;
    
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(COREPOOLSIZE, MAXIMUMPOOLSIZE, KEEPALIVETIME, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(),
        new ThreadPoolExecutor.CallerRunsPolicy());
    
    private OSSImgYunServiceIF ossImgYunService = (OSSImgYunServiceIF)ImgYunManager.getClient(CommonConst.IMG_ALIYUN);
    
    private UpImgYunServiceIF upImgYunService = (UpImgYunServiceIF)ImgYunManager.getClient(CommonConst.IMG_UPYUN);
    
    Random random = new Random();
    
    /**
     * 批量上传图片
     *
     * @param request
     * @return
     */
    @RequestMapping("/toBatch")
    public ModelAndView toBatch(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("common/batchUploadForm");
        return mv;
    }
    
    /**
     * 
     * @param file
     * @param request
     * @param limitSize：是否需要验证文件大小，1是，0否
     * @param width：图片宽(px)
     * @param height：图片高(px)
     * @return
     */
    @RequestMapping(value = "/fileUpLoad", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "图片管理-上传图片")
    public String fileUpLoad(
            @RequestParam("picFile") MultipartFile file, HttpServletRequest request,//
        @RequestParam(value = "limitSize", required = false, defaultValue = "1") int limitSize,
        @RequestParam(value = "needWidth", required = false, defaultValue = "0") int width,//限制宽度
        @RequestParam(value = "needHeight", required = false, defaultValue = "0") int height,//限制高度
        @RequestParam(value = "heightList", required = false, defaultValue = "") String heightList, //限制宽度2
        @RequestParam(value = "widthList", required = false, defaultValue = "") String widthList //限制高度2
    )
    {
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            if (file.isEmpty())
            {
                result.put("status", 0);
                result.put("msg", "请选择文件后上传");
                return JSON.toJSONString(result);
            }
            if (file.getSize() > 409600 && limitSize == 1)
            {
                result.put("status", 0);
                result.put("msg", "图片不得大于400kb");
                return JSON.toJSONString(result);
            }
            List<Integer> heights = new ArrayList();
            if(org.apache.commons.lang.StringUtils.isNotEmpty(heightList)) {
                List<String>  heightStrings = Splitter.on(",").splitToList(heightList);
                for(String s : heightStrings) {
                    heights.add(Integer.valueOf(s));
                }
            }
            List<Integer> widths = new ArrayList();
            if(org.apache.commons.lang.StringUtils.isNotEmpty(widthList)) {
                List<String>  widthStrings = Splitter.on(",").splitToList(widthList);
                for(String s : widthStrings) {
                    widths.add(Integer.valueOf(s));
                }
            }

            //限制图片宽高
            if (width > 0)
            {
                BufferedImage img = ImageIO.read(file.getInputStream());
                int currWidth = img.getWidth();
                if (currWidth != width)
                {
                    result.put("status", 0);
                    result.put("msg", "图片尺寸不符合要求，要求宽度：" + width + "，实际宽度：" + currWidth + "。");
                    return JSON.toJSONString(result);
                }
                
            }
            if (height > 0)
            {
                BufferedImage img = ImageIO.read(file.getInputStream());
                int currHeight = img.getHeight();
                if (currHeight != height)
                {
                    result.put("status", 0);
                    result.put("msg", "图片尺寸不符合要求，要求高度：" + height + "，实际高度：" + currHeight + "。");
                    return JSON.toJSONString(result);
                }
            }
            if (CollectionUtils.isNotEmpty(widths))
            {
                BufferedImage img = ImageIO.read(file.getInputStream());
                int currWidth = img.getWidth();
                if (!widths.contains(currWidth))
                {
                    result.put("status", 0);
                    result.put("msg", "图片尺寸不符合要求，要求宽度：" + widths + "，实际宽度：" + currWidth + "。");
                    return JSON.toJSONString(result);
                }
            }
            if (CollectionUtils.isNotEmpty(heights))
            {
                BufferedImage img = ImageIO.read(file.getInputStream());
                int currHeight = img.getHeight();
                if (!heights.contains(currHeight))
                {
                    result.put("status", 0);
                    result.put("msg", "图片尺寸不符合要求，要求高度：" + heights + "，实际高度：" + currHeight + "。");
                    return JSON.toJSONString(result);
                }
            }
            String directory = "";//获取图片目录
            directory = getDirectory(request);//获取图片目录
            byte[] bt = file.getBytes();
            String fileName = file.getOriginalFilename();
            logger.debug("上传图片原fileName：" + fileName);
            int pointIndex = fileName.lastIndexOf(".");
            String fileExt = fileName.substring(pointIndex);
            String id = Long.toHexString(Long.valueOf(random.nextInt(10) + "" + System.currentTimeMillis() + "" + random.nextInt(10)));
            fileName = directory + id + fileExt.toLowerCase();
            logger.debug("上传图片新fileName：" + fileName);
            result = toUP(bt, fileName);
        }
        catch (Exception e)
        {
            logger.error("上传图片失败", e);
            result.put("status", 0);
            result.put("msg", "文件上传失败");
        }
        return JSON.toJSONString(result);
    }
    
    private Map<String, Object> toUP(byte[] bt, String fileName)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        Map resultMap = null;
        if ("youpai".equals(CommonConstant.imageUseAliOrYoupai))
        {
            resultMap = upImgYunService.uploadFile(bt, fileName);
        }
        else if ("ali".equals(CommonConstant.imageUseAliOrYoupai))
        {
            resultMap = ossImgYunService.uploadFile(bt, fileName);
        }
        logger.debug("上传图片返回状态：" + resultMap);
        if (resultMap.get("status").equals("success"))
        {
            result.put("status", 1);
            String newUrlString = resultMap.get("url") + "";
            result.put("url", newUrlString);
        }
        else
        {
            result.put("status", 0);
            result.put("msg", "文件上传失败");
        }
        
        //做备份处理，如果上传失败也不能影响正常业务逻辑
        try
        {
            executor.execute(new BackUpToAliyun(bt, fileName));
            //                    executor.shutdown();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            logger.info("备份失败");
        }
        return result;
    }
    
    /**
     * 批量上传图片
     * 
     * @param file
     * @param request
     * @return
     */
    @RequestMapping(value = "/batchFileUpLoad", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "图片管理-批量上传图片")
    public String batchFileUpLoad(@RequestParam("Filedata") MultipartFile file, HttpServletRequest request)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            if (file.isEmpty())
            {
                result.put("status", 0);
                result.put("msg", "请选择文件后上传");
            }
            else if (file.getSize() > 409600)
            {
                result.put("status", 0);
                result.put("msg", "图片不得大于400kb");
            }
            else
            {
                byte[] bt = file.getBytes();
                String fileName = file.getOriginalFilename();
                String oldName = fileName;
                logger.info("上传图片原fileName：" + fileName);
                int pointIndex = fileName.lastIndexOf(".");
                String fileExt = fileName.substring(pointIndex);
                String id = Long.toHexString(Long.valueOf(random.nextInt(10) + "" + System.currentTimeMillis() + "" + random.nextInt(10)));
                fileName = id + fileExt.toLowerCase();
                logger.info("上传图片新fileName：" + fileName);
                
                Map resultMap = null;
                if ("youpai".equals(CommonConstant.imageUseAliOrYoupai))
                {
                    resultMap = upImgYunService.uploadFile(bt, fileName);
                }
                else if ("ali".equals(CommonConstant.imageUseAliOrYoupai))
                {
                    resultMap = ossImgYunService.uploadFile(bt, fileName);
                }
                logger.info("上传图片返回状态：" + resultMap.get("status"));
                if (resultMap.get("status").equals("success"))
                {
                    result.put("status", 1);
                    String newUrlString = resultMap.get("url") + "";
                    result.put("url", newUrlString);
                    result.put("msg", "成功");
                    result.put("originalName", oldName);
                }
                else
                {
                    result.put("status", 0);
                    result.put("msg", "失败");
                    result.put("originalName", oldName);
                }
                
                //做备份处理，如果上传失败也不能影响正常业务逻辑
                try
                {
                    executor.execute(new BackUpToAliyun(bt, fileName));
                }
                catch (Exception e)
                {
                    logger.error(e.getMessage(), e);
                    logger.info("备份失败");
                }
                
            }
        }
        catch (Exception e)
        {
            logger.error("上传图片失败", e);
            result.put("status", 0);
            result.put("msg", "文件上传失败");
        }
        return JSON.toJSONString(result);
    }
    
    class BackUpToAliyun extends Thread
    {
        byte[] bt;
        
        String fileName;
        
        public BackUpToAliyun(byte[] bt, String fileName)
        {
            this.bt = bt;
            this.fileName = fileName;
        }
        
        @Override
        public void run()
        {
            
            try
            {
                Map<String, String> statusMap = null;
                if ("youpai".equals(CommonConstant.imageUseAliOrYoupai))
                {
                    statusMap = ossImgYunService.uploadFile(bt, fileName);
                }
                else if ("ali".equals(CommonConstant.imageUseAliOrYoupai))
                {
                    statusMap = upImgYunService.uploadFile(bt, fileName);
                }
                logger.info("上传状态：" + statusMap.get("status"));
                logger.info("上传地址：" + statusMap.get("url"));
            }
            catch (Exception e)
            {
                logger.error(e.getMessage(), e);
                logger.info("备份失败");
            }
        }
    }
    
    private String getDirectory(HttpServletRequest request)
    {
        String directoryName = "";
        String referer = request.getHeader("Referer");
        if (referer.indexOf("/banner/") > -1 || referer.indexOf("/banner/") > -1)
        {
            directoryName = "activity/banner/";//首页轮播图
        }
        else if (referer.indexOf("/indexSale/") > -1 || referer.indexOf("/indexSale/") > -1)
        {
            directoryName = "activity/saleWindow/";//首页特卖位
        }
        else if (referer.indexOf("/sale/") > -1 || referer.indexOf("/sale/") > -1)
        {
            directoryName = "activity/activitiesCommon/";//特卖专场
        }
        else if (referer.indexOf("/brand/") > -1 || referer.indexOf("/brand/") > -1)
        {
            directoryName = "brand/";//品牌
        }
        else if (referer.indexOf("/image/") > -1 || referer.indexOf("/image/") > -1)
        {
            if (referer.indexOf("type=sale") > -1)
            {
                directoryName = "gegeImage/activity/"; //特卖格格说头像
            }
            else if (referer.indexOf("type=product") > -1)
            {
                directoryName = "gegeImage/product/"; //商品格格说头像
            }
        }
        else if (referer.indexOf("/refund/") > -1)
        {
            directoryName = "orderrefund/applyImages/"; //退款退货 图片
        }
        else if (referer.indexOf("/product") > -1 || referer.indexOf("/product") > -1)
        {
            directoryName = "product/"; //商品资源图
        }
        else if (referer.indexOf("/saleTag/") > -1 || referer.indexOf("/saleTag/") > -1)
        {
            directoryName = "saleTag/"; //特卖标签
        }
        else if (referer.indexOf("/pic/toBatch") > -1)
        {
            directoryName = "batch/"; //批量图片上传
        }
        return directoryName;
    }
    
    @RequestMapping(value = "/upZip", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "图片管理-上传压缩图片")
    public String upZip(HttpServletRequest request, //
        @RequestParam(value = "imageZipFile", required = false) MultipartFile imageZipFile)
        throws Exception
    {
        try
        {
            String zipFileName = imageZipFile.getOriginalFilename();
            if (!zipFileName.endsWith(".zip"))
            {
                Map<String, Object> result = new HashMap<String, Object>();
                result.put("status", 0);
                result.put("msg", "只支持zip格式的压缩文件");
                return JSON.toJSONString(result);
            }
            File fileDir = new File(YggAdminProperties.getInstance().getPropertie("image_upload_dir"));
            String newFileName = "pic" + System.currentTimeMillis() + new Random().nextInt(10000);
            File fileOrgin = new File(fileDir, newFileName + zipFileName.substring(zipFileName.lastIndexOf(".zip")));
            File fileDest = new File(fileDir, newFileName);
            imageZipFile.transferTo(fileOrgin);
            //检查压缩包是否正确
            Map<String, Object> readResult = ZipUtil.readImageZipFile(fileOrgin);
            int status = Integer.parseInt(readResult.get("status") + "");
            if (status == 0)
            {
                return JSON.toJSONString(readResult);
            }
            //图片批量上传
            fileOrgin.createNewFile();
            fileDest.mkdir();
            ZipCompressorByAnt.unZip(fileOrgin, fileDest);
            File[] files = fileDest.listFiles();
            Map<String, Object> returnResult = new HashMap<String, Object>();
            status = 1;
            String msg = "";
            Map<String, String> fileUrlMap = new HashMap<String, String>();
            for (File f : files)
            {
                String fileName = f.getName();
                int pointIndex = fileName.lastIndexOf(".");
                String fileExt = fileName.substring(pointIndex);
                String id = Long.toHexString(Long.valueOf(random.nextInt(10) + "" + System.currentTimeMillis() + "" + random.nextInt(10)));
                String directory = getDirectory(request);
                String nName = directory + id + fileExt.toLowerCase();
                Map<String, Object> toUpResult = toUP(FileUtil.getBytesFromFile(f), nName);
                status = Integer.parseInt(toUpResult.get("status") + "");
                if (status == 0)
                {
                    msg = "上传出错,请稍后重试。";
                    break;
                }
                else
                {
                    Integer fileNameWithoutPostfix = Integer.parseInt(fileName.substring(0, pointIndex));
                    if (fileNameWithoutPostfix < 8)
                    {
                        fileUrlMap.put("pic_" + fileNameWithoutPostfix, toUpResult.get("url") + "");
                    }
                    else
                    {
                        fileUrlMap.put("detail_pic_" + (fileNameWithoutPostfix - 7), toUpResult.get("url") + "");
                    }
                }
            }
            //删除临时文件
            FileUtil.deleteFile(fileOrgin.getAbsolutePath());
            FileUtil.deleteFile(fileDest.getAbsolutePath());
            
            returnResult.put("status", status);
            returnResult.put("fileUrlMap", fileUrlMap);
            returnResult.put("msg", "".equals(msg) ? "上传成功" : msg);
            return JSON.toJSONString(returnResult);
        }
        catch (Exception e)
        {
            logger.error("上传图片出错", e);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", 1);
            map.put("msg", "上传失败，请检查压缩文件，压缩文件里内不能有文件夹，文件名称不能有中文");
            return JSON.toJSONString(map);
        }
    }
}
