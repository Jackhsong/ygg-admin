package com.ygg.admin.service.impl;

import com.google.common.base.Preconditions;
import com.ygg.admin.code.CustomLayoutRelationTypeEnum;
import com.ygg.admin.code.CustomLayoutStyleTypeEnum;
import com.ygg.admin.code.ImageTypeEnum;
import com.ygg.admin.dao.ProductDao;
import com.ygg.admin.dao.SpecialActivityDao;
import com.ygg.admin.entity.ProductEntity;
import com.ygg.admin.entity.SpecialActivityEntity;
import com.ygg.admin.service.ProductService;
import com.ygg.admin.service.SpecialActivityService;
import com.ygg.admin.util.ImageUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("specialActivityService")
public class SpecialActivityServiceImpl implements SpecialActivityService
{
    @Resource
    private SpecialActivityDao specialActivityDao;
    
    @Resource
    private ProductDao productDao;

    @Resource
    private ProductService productService;


    @Override
    public Map<String, Object> jsonSpecialActivityInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> infoList = specialActivityDao.findAllSpecialActivity(para);
        int total = 0;
        if (infoList.size() > 0)
        {
            for (Map<String, Object> map : infoList)
            {
                int isAvailable = Integer.valueOf(map.get("isAvailable") + "").intValue();
                
                map.put("availableDesc", isAvailable == 1 ? "可用" : "停用");
                map.put("imageURL", "<a href='" + map.get("image") + "' target='_blank'>查看图片</a>");
                map.put("url", "http://m.gegejia.com/ygg/special/activity/web/" + map.get("id"));
            }
            total = specialActivityDao.countSpecialActivity(para);
        }
        resultMap.put("rows", infoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public int saveOrUpdateSpecialActivity(SpecialActivityEntity sae)
        throws Exception
    {
        Map<String, Object> imageMap = ImageUtil.getProperty(sae.getImage());
        sae.setImageWidth(Integer.valueOf(imageMap.get("width") + ""));
        sae.setImageHeight(Integer.valueOf(imageMap.get("height") + ""));
        if (sae.getId() == 0)
        {
            return specialActivityDao.saveSpecialActivity(sae);
        }
        else
        {
            return specialActivityDao.updateSpecialActivity(sae);
        }
    }
    
    @Override
    public int updateSpecialActivityAvailableStatus(Map<String, Object> para)
        throws Exception
    {
        return specialActivityDao.updateSpecialActivityAvailableStatus(para) >= 1 ? 1 : 0;
    }
    
    @Override
    public Map<String, Object> findSpecialActivityById(int regionId)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", regionId);
        para.put("start", 0);
        para.put("max", 1);
        List<Map<String, Object>> infoList = specialActivityDao.findAllSpecialActivity(para);
        if (infoList == null || infoList.size() == 0)
            return null;
        return infoList.get(0);
    }
    
    @Override
    public Map<String, Object> jsonActivityLayoutInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> infoList = specialActivityDao.findAllSpecialActivityLayout(para);
        int total = 0;
        if (infoList.size() > 0)
        {
            for (Map<String, Object> map : infoList)
            {
                int isDisplay = Integer.valueOf(map.get("isDisplay") + "").intValue();
                map.put("displayDesc", isDisplay == 1 ? "展现" : "不展现");
            }
            total = specialActivityDao.countSpecialActivityLayout(para);
        }
        resultMap.put("rows", infoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public int updateSpecialActivityLayoutDisplayStatus(Map<String, Object> para)
        throws Exception
    {
        return specialActivityDao.updateSpecialActivityLayoutDisplayStatus(para) >= 1 ? 1 : 0;
    }
    
    @Override
    public int saveOrUpdateSpecialActivityLayout(Map<String, Object> para)
        throws Exception
    {
        int id = Integer.valueOf(para.get("id") + "").intValue();
        int saId = Integer.valueOf(para.get("saId") + "").intValue();
        if (id == 0)
        {
            //新增
            int sequence = specialActivityDao.findMaxSpecialActivityLayoutSequenceByActivityId(saId);
            para.put("sequence", sequence);
            return specialActivityDao.saveSpecialActivityLayout(para);
        }
        else
        {
            //修改
            return specialActivityDao.updateSpecialActivityLayout(para);
        }
    }
    
    @Override
    public int updateSpecialActivityLayoutSequence(Map<String, Object> para)
        throws Exception
    {
        return specialActivityDao.updateSpecialActivityLayoutSequence(para);
    }
    
    @Override
    public Map<String, Object> findSpecialActivityLayout(int layoutId)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("start", 0);
        para.put("max", 1);
        para.put("id", layoutId);
        List<Map<String, Object>> infoList = specialActivityDao.findAllSpecialActivityLayout(para);
        if (infoList == null || infoList.size() == 0)
            return null;
        return infoList.get(0);
    }
    
    @Override
    public Map<String, Object> jsonSpecialActivityLayoutProductInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> infoList = specialActivityDao.findSpecialActivityLayouProduct(para);
        int total = 0;
        if (infoList.size() > 0)
        {
            for (Map<String, Object> map : infoList)
            {
                int isDisplay = Integer.valueOf(map.get("isDisplay") + "").intValue();
                int displayStyle = Integer.valueOf(map.get("displayType") + "").intValue();
                map.put("displayDesc", isDisplay == 1 ? "展现" : "不展现");
                map.put("layout", displayStyle == 1 ? "单张" : "左右");
                if (displayStyle == CustomLayoutStyleTypeEnum.LAYOUT_STYLE_OF_SINGLE.ordinal())
                {
                    map.put("singleDesc", map.get("oneDesc"));
                    map.put("leftDesc", "-");
                    map.put("rightDesc", "-");
                }
                else if (displayStyle == CustomLayoutStyleTypeEnum.LAYOUT_STYLE_OF_DOUBLE.ordinal())
                {
                    map.put("singleDesc", "-");
                    map.put("leftDesc", map.get("oneDesc"));
                    map.put("rightDesc", map.get("twoDesc"));
                }
            }
            total = specialActivityDao.countSpecialActivityLayouProduct(para);
        }
        resultMap.put("rows", infoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public int updateSpecialActivityLayoutProductSequence(Map<String, Object> para)
        throws Exception
    {
        return specialActivityDao.updateSpecialActivityLayoutProduct(para);
    }
    
    @Override
    public int updateSpecialActivityLayoutProductDisplayStatus(Map<String, Object> para)
        throws Exception
    {
        return specialActivityDao.updateSpecialActivityLayoutProduct(para);
    }
    
    @Override
    public Map<String, Object> findSpecialActivityLayoutProduct(int layoutProductId)
        throws Exception
    {
        return specialActivityDao.findSpecialActivityLayoutProduct(layoutProductId);
    }
    
    @Override
    public int saveOrUpdateSpecialActivityLayoutProduct(Map<String, Object> para)
        throws Exception
    {
        int id = (int)para.get("id");
        int displayType = Integer.valueOf(para.get("displayType") + "").intValue();
        int layoutId = Integer.valueOf(para.get("layoutId") + "").intValue();
        int oneType = Integer.valueOf(para.get("oneType") + "").intValue();
        int twoType = Integer.valueOf(para.get("twoType") + "").intValue();
        //左右布局且为单品时，图片未上传，则取第一张
        if (displayType == CustomLayoutStyleTypeEnum.LAYOUT_STYLE_OF_DOUBLE.ordinal())
        {
            String oneImage = para.get("oneImage") + "";
            String twoImage = para.get("twoImage") + "";
            if (oneType == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_SALEPRODUCT.ordinal() && StringUtils.isEmpty(oneImage))
            {
                ProductEntity product = productDao.findProductByID(Integer.valueOf(para.get("oneDisplayId") + ""), null);
                oneImage = product.getImage1();
            }
            
            int twoDisplayId = (int)para.get("twoDisplayId");
            if (twoType == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_SALEPRODUCT.ordinal() && StringUtils.isEmpty(twoImage) && twoDisplayId > 0)
            {
                ProductEntity product = productDao.findProductByID(Integer.valueOf(para.get("twoDisplayId") + ""), null);
                twoImage = product.getImage1();
            }
            para.put("oneImage", adjustImageSize(oneImage, ImageUtil.getSuffix(ImageTypeEnum.newlistproduct.ordinal())));
            if (!twoImage.isEmpty())
            {
                para.put("twoImage", adjustImageSize(twoImage, ImageUtil.getSuffix(ImageTypeEnum.newlistproduct.ordinal())));
            }
        }
        
        String oneImage = para.get("oneImage").toString();
        Map<String, Object> oneImageMap = ImageUtil.getProperty(oneImage);
        para.put("oneWidth", oneImageMap.get("width"));
        para.put("oneHeight", oneImageMap.get("height"));
        if (displayType == CustomLayoutStyleTypeEnum.LAYOUT_STYLE_OF_SINGLE.ordinal())
        {
            para.put("twoWidth", 0);
            para.put("twoHeight", 0);
        }
        else if (displayType == CustomLayoutStyleTypeEnum.LAYOUT_STYLE_OF_DOUBLE.ordinal())
        {
            String twoImage = para.get("twoImage").toString();
            if (!twoImage.isEmpty())
            {
                Map<String, Object> twoImageMap = ImageUtil.getProperty(twoImage);
                para.put("twoWidth", twoImageMap.get("width"));
                para.put("twoHeight", twoImageMap.get("height"));
            }
            else
            {
                para.put("twoWidth", 0);
                para.put("twoHeight", 0);
            }
        }
        if (id == 0)
        {
            //新增
            
            int sequence = specialActivityDao.findMaxSpecialActivityLayoutProductSequenceByActivityId(layoutId);
            para.put("sequence", sequence);
            return specialActivityDao.insertSpecialActivityLayoutProduct(para);
            
        }
        else
        {
            //修改
            return specialActivityDao.updateSpecialActivityLayoutProduct(para);
        }
        
    }
    
    @Override
    public List<Map<String, Object>> findSpecialActivity()
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("isAvailable", 1);
        return specialActivityDao.findAllSpecialActivity(para);
    }

    @Override
    //没有上线功能 暂时保留
    public int saveByQuickAdd(List<List<Integer>> idsPartition, int layoutId) throws Exception {
        int addCount = 0;
        for(List<Integer> partition : idsPartition){
            Map<String, Object> para = new HashMap<>();
            ProductEntity product1 = productService.findProductById(partition.get(0));
            ProductEntity product2 = null;
            Preconditions.checkArgument(product1 != null, "商品id：" + partition.get(0) + "不存在");
            if (partition.size() > 1) {
                product2 = productService.findProductById(partition.get(1));
                Preconditions.checkArgument(product2 != null, "商品id：" + partition.get(1) + "不存在");
            }
            para.put("layoutId", layoutId);
            para.put("displayType", 2);
            para.put("isDisplay", 0);

            para.put("oneDesc", "");
            para.put("oneType", 1);
            para.put("oneDisplayId", partition.get(0));
            para.put("oneImage",
                    adjustImageSize(product1.getImage1(), ImageUtil.getSuffix(ImageTypeEnum.newlistproduct.ordinal())));
            Map<String, Object> oneImageMap = ImageUtil.getProperty(product1.getImage1());
            para.put("oneWidth", oneImageMap.get("width"));
            para.put("oneHeight", oneImageMap.get("height"));

            if (product2 != null) {
                para.put("twoDesc", "");
                para.put("twoType", 1);
                para.put("twoDisplayId", partition.get(0));
                para.put("twoImage",
                        adjustImageSize(product2.getImage1(), ImageUtil.getSuffix(ImageTypeEnum.newlistproduct.ordinal())));
                Map<String, Object> twoImageMap = ImageUtil.getProperty(product2.getImage1());
                para.put("twoWidth", twoImageMap.get("width"));
                para.put("twoHeight", twoImageMap.get("height"));
            } else {
                para.put("twoDesc", "");
                para.put("twoImage", "");
                para.put("twoType", 0);
                para.put("twoDisplayId", 0);
                para.put("twoWidth", 0);
                para.put("twoHeight", 0);
            }
            int sequence = specialActivityDao.findMaxSpecialActivityLayoutProductSequenceByActivityId(layoutId);
            para.put("sequence", sequence);
            addCount += specialActivityDao.insertSpecialActivityLayoutProduct(para);
        }
        return addCount;
    }

    private String adjustImageSize(String imageUrl, String postfix)
    {
        if (imageUrl == null || "".equals(imageUrl))
        {
            return "";
        }
        if (imageUrl.indexOf(ImageUtil.getPrefix()) > 0 && !imageUrl.endsWith(postfix))
        {
            imageUrl = imageUrl.substring(0, imageUrl.indexOf(ImageUtil.getPrefix()));
        }
        return (imageUrl.indexOf(ImageUtil.getPrefix()) > 0) ? imageUrl : (imageUrl + ImageUtil.getPrefix() + postfix);
    }
}
