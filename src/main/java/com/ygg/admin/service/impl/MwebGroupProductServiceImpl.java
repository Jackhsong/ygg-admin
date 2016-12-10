package com.ygg.admin.service.impl;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygg.admin.dao.MwebGroupImageDetailDao;
import com.ygg.admin.dao.MwebGroupProductCountDao;
import com.ygg.admin.dao.MwebGroupProductDao;
import com.ygg.admin.dao.MwebGroupProductInfoDao;
import com.ygg.admin.dao.ProductBaseDao;
import com.ygg.admin.dao.SellerDao;
import com.ygg.admin.entity.DetailPicAndText;
import com.ygg.admin.entity.MwebGroupImageDetailEntity;
import com.ygg.admin.entity.MwebGroupProductCountEntity;
import com.ygg.admin.entity.MwebGroupProductEntity;
import com.ygg.admin.entity.MwebGroupProductInfoEntity;
import com.ygg.admin.entity.ProductBaseEntity;
import com.ygg.admin.entity.ProductEntity;
import com.ygg.admin.entity.SellerEntity;
import com.ygg.admin.service.MwebGroupProductService;
import com.ygg.admin.util.CommonEnum.PRODUCT_TYPE;
import com.ygg.admin.util.CommonEnum.TEAM_STATUS;
import com.ygg.admin.util.DateTimeUtil;

@Service("mwebGroupProductService")
public class MwebGroupProductServiceImpl implements MwebGroupProductService
{
    
    @Resource
    private MwebGroupProductDao mwebGroupProductDao;
    
    @Resource
    private MwebGroupProductInfoDao mwebGroupProductInfoDao;
    
    @Resource
    private MwebGroupProductCountDao mwebGroupProductCountDao;
    
    @Resource
    private MwebGroupImageDetailDao mwebGroupImageDetailDao;
    
    @Resource
    private ProductBaseDao productBaseDao;
    
    @Resource
    private SellerDao sellerDao;
    
    @Override
    public JSONArray findProductAndStock(Map<String, Object> parameter)
        throws Exception
    {
        JSONArray jsonArray = new JSONArray();
        
        List<JSONObject> productList = mwebGroupProductDao.findProductAndStock(parameter);
        for (Iterator<JSONObject> it = productList.iterator(); it.hasNext();)
        {
            JSONObject jsonObject = new JSONObject();
            JSONObject j = it.next();
            int mwebGroupProductId = j.getInteger("mwebGroupProductId");
            int sellerId = j.getInteger("sellerId");
            String isOffShelves = j.getByte("isOffShelves") == 1 ? "已下架" : "出售中";
            String isAvailable = j.getByte("isAvailable") == 1 ? "可用" : "停用";
            String typeView = j.getByte("type") == 0 ? "普通团" : "千人团";
            
            SellerEntity sellerEntity = sellerDao.findSellerById(sellerId);
            String sellerName = sellerEntity.getSellerName();
            String sendAddress = sellerEntity.getSendAddress();
            jsonObject.putAll(j);
            jsonObject.put("id", mwebGroupProductId);
            jsonObject.put("isOffShelves", isOffShelves);
            jsonObject.put("isAvailable", isAvailable);
            jsonObject.put("sendAddress", sendAddress);
            jsonObject.put("typeView", typeView);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }
    
    public JSONObject findProductAndStock2(Map<String, Object> parameter)
        throws Exception
    {
        JSONObject result = new JSONObject();
        result.put("rows", findProductAndStock(parameter));
        result.put("total", mwebGroupProductDao.countProductAndStock(parameter));
        return result;
    }
    
    /**
     * 
     * @创建人: zero
     * @创建时间: 2015年11月9日 下午7:54:06
     * @描述:
     *      <p>
     *      (添加拼团商品)
     *      </p>
     * @修改人: zero
     * @修改时间: 2015年11月9日 下午7:54:06
     * @修改备注:
     *        <p>
     *        (修改备注)
     *        </p>
     * @param mwebGroupProductEntity
     * @param stock
     * @param detailPicAndText
     * @return
     * @version V1.0
     * @throws Exception
     */
    @Override
    public int addProduct(MwebGroupProductEntity mwebGroupProductEntity, int stock, DetailPicAndText detailPicAndText, int baseId)
        throws Exception
    {
        int i = 0;
        
        ProductBaseEntity productBaseEntity = productBaseDao.queryProductBaseById(baseId);
        int brand_id = productBaseEntity.getBrandId();
        int seller_id = productBaseEntity.getSellerId();
        
        String gege_say = productBaseEntity.getGegeSay();
        String code = productBaseEntity.getCode();
        String barcode = productBaseEntity.getBarcode();
        String name = productBaseEntity.getName();
        String selling_point = mwebGroupProductEntity.getSellingPoint();
        
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductBaseId(baseId);
        productEntity.setType(3);
        productEntity.setCode(code);
        productEntity.setBarcode(barcode);
        productEntity.setName(name);
        productEntity.setShortName(mwebGroupProductEntity.getShortName());
        productEntity.setSellerId(seller_id);
        productEntity.setBrandId(brand_id);
        productEntity.setDesc(gege_say);
        productEntity.setSellingPoint(selling_point);
        productEntity.setFreightTemplateId(0);
        productEntity.setPcDetail("");
        productEntity.setSellerProductName(mwebGroupProductEntity.getSellerProductName());
        
        i = mwebGroupProductDao.addProductEntity(productEntity);
        if (i > 0)
        {
            i = 0;
            mwebGroupProductEntity.setProductId(productEntity.getId());
            if (StringUtils.isEmpty(mwebGroupProductEntity.getStartTime()))
            {
                mwebGroupProductEntity.setStartTime(null);
            }
            if (StringUtils.isEmpty(mwebGroupProductEntity.getEndTime()))
            {
                mwebGroupProductEntity.setEndTime(null);
            }
            if (StringUtils.isEmpty(mwebGroupProductEntity.getStartTeamTime()))
            {
                mwebGroupProductEntity.setStartTeamTime(null);
            }
            i = mwebGroupProductDao.addProduct(mwebGroupProductEntity);
            if (i > 0)
            {
                MwebGroupProductCountEntity mwebGroupProductCountEntity = new MwebGroupProductCountEntity();
                mwebGroupProductCountEntity.setMwebGroupProductId(mwebGroupProductEntity.getId());
                
                if (mwebGroupProductEntity.getType() == PRODUCT_TYPE.ORDINARY_GOODS.getValue())
                {
                    mwebGroupProductCountEntity.setStock(stock);
                }
                // 自动开团
                else if (mwebGroupProductEntity.getType() == PRODUCT_TYPE.LARGE_GROUP.getValue())
                {
                    i = 0;
                    mwebGroupProductCountEntity.setStock(mwebGroupProductEntity.getTeamNumberLimit() * mwebGroupProductEntity.getTeamCount());
                    mwebGroupProductCountEntity.setLock(mwebGroupProductEntity.getTeamNumberLimit() * mwebGroupProductEntity.getTeamCount());
                    // 组团时间
                    DateTime dateTime = DateTimeUtil.string2DateTime(mwebGroupProductEntity.getStartTeamTime());
                    // 组团结束时间
                    DateTime teamEndTime = dateTime.plusHours(mwebGroupProductEntity.getTeamValidHour());
                    
                    for (int k = 0; k < mwebGroupProductEntity.getTeamCount(); k++)
                    {
                        MwebGroupProductInfoEntity m = new MwebGroupProductInfoEntity();
                        m.setType((byte)PRODUCT_TYPE.LARGE_GROUP.getValue());
                        m.setMwebGroupProductId(mwebGroupProductEntity.getId());
                        m.setTeamNumberLimit(mwebGroupProductEntity.getTeamNumberLimit());
                        m.setCurrentNumber(0);
                        m.setStatus((byte)TEAM_STATUS.TRANSACTION.getValue());
                        m.setCreateTime(dateTime.toString(DateTimeUtil.WEB_FORMAT));
                        m.setTeamEndTime(teamEndTime.toString(DateTimeUtil.WEB_FORMAT));
                        i += mwebGroupProductInfoDao.addGroupProductInfo(m);
                        
                        JSONObject jsonAutoTeamConfig = new JSONObject();
                        jsonAutoTeamConfig.put("shortName", mwebGroupProductEntity.getShortName());
                        jsonAutoTeamConfig.put("mwebGroupProductId", mwebGroupProductEntity.getId());
                        jsonAutoTeamConfig.put("mwebGroupProductInfoId", m.getId());
                        jsonAutoTeamConfig.put("teamValidHour", mwebGroupProductEntity.getTeamValidHour());
                        jsonAutoTeamConfig.put("teamNumberLimit", m.getTeamNumberLimit());
                        jsonAutoTeamConfig.put("startTeamTime", mwebGroupProductEntity.getStartTeamTime());
                        mwebGroupProductInfoDao.addAutoTeamConfig(jsonAutoTeamConfig);
                    }
                    if (i == mwebGroupProductEntity.getTeamCount())
                    {
                        i = 1;
                        
                    }
                    else
                    {
                        i = 0;
                    }
                    
                }
                
                // 加库存
                mwebGroupProductCountDao.addMwebGroupProductCount(mwebGroupProductCountEntity);
                
                List<String> obileDetailList = getDetailListPara(detailPicAndText);
                int j = 0;
                for (String curr : obileDetailList)
                {
                    if (StringUtils.isNotEmpty(curr))
                    {
                        InputStream is = new URL(curr).openStream();
                        BufferedImage sourceImg = ImageIO.read(is);
                        
                        int height = sourceImg.getHeight();
                        int width = sourceImg.getWidth();
                        
                        MwebGroupImageDetailEntity mwebGroupImageDetailEntity = new MwebGroupImageDetailEntity();
                        mwebGroupImageDetailEntity.setMwebGroupProductId(mwebGroupProductEntity.getId());
                        mwebGroupImageDetailEntity.setUrl(curr);
                        mwebGroupImageDetailEntity.setHeight(height);
                        mwebGroupImageDetailEntity.setWidth(width);
                        mwebGroupImageDetailEntity.setOrder(new Byte(j + ""));
                        
                        mwebGroupImageDetailDao.addMwebGroupImageDetail(mwebGroupImageDetailEntity);
                        j++;
                    }
                    
                }
            }
        }
        
        return i;
    }
    
    List<String> getDetailListPara(DetailPicAndText detailPicAndText)
    {
        byte order = 10;
        List<String> resList = new ArrayList<String>();
        resList.add(detailPicAndText.getDetail_pic_1());
        resList.add(detailPicAndText.getDetail_pic_2());
        resList.add(detailPicAndText.getDetail_pic_3());
        resList.add(detailPicAndText.getDetail_pic_4());
        resList.add(detailPicAndText.getDetail_pic_5());
        resList.add(detailPicAndText.getDetail_pic_6());
        resList.add(detailPicAndText.getDetail_pic_7());
        resList.add(detailPicAndText.getDetail_pic_8());
        resList.add(detailPicAndText.getDetail_pic_9());
        resList.add(detailPicAndText.getDetail_pic_10());
        resList.add(detailPicAndText.getDetail_pic_11());
        resList.add(detailPicAndText.getDetail_pic_12());
        resList.add(detailPicAndText.getDetail_pic_13());
        resList.add(detailPicAndText.getDetail_pic_14());
        resList.add(detailPicAndText.getDetail_pic_15());
        resList.add(detailPicAndText.getDetail_pic_16());
        resList.add(detailPicAndText.getDetail_pic_17());
        resList.add(detailPicAndText.getDetail_pic_18());
        resList.add(detailPicAndText.getDetail_pic_19());
        resList.add(detailPicAndText.getDetail_pic_20());
        resList.add(detailPicAndText.getDetail_pic_21());
        resList.add(detailPicAndText.getDetail_pic_22());
        return resList;
    }
    
    @Override
    public JSONObject getProduct(int id)
        throws Exception
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", 0);
        MwebGroupProductEntity mwebGroupProductEntity = new MwebGroupProductEntity();
        mwebGroupProductEntity.setId(id);
        List<MwebGroupProductEntity> list = mwebGroupProductDao.findProduct(mwebGroupProductEntity);
        if (list.size() == 1)
        {
            mwebGroupProductEntity = list.get(0);
            jsonObject.put("mwebGroupProductEntity", mwebGroupProductEntity);
            
            MwebGroupProductCountEntity mwebGroupProductCountEntity = new MwebGroupProductCountEntity();
            mwebGroupProductCountEntity.setMwebGroupProductId(id);
            mwebGroupProductCountEntity = mwebGroupProductCountDao.getGroupProductCount(mwebGroupProductCountEntity);
            jsonObject.put("stock", mwebGroupProductCountEntity.getStock());
            
            MwebGroupImageDetailEntity mwebGroupImageDetailEntity = new MwebGroupImageDetailEntity();
            mwebGroupImageDetailEntity.setMwebGroupProductId(id);
            List<MwebGroupImageDetailEntity> mwebGroupImageDetailEntitylList = mwebGroupImageDetailDao.findMwebGroupImageDetail(mwebGroupImageDetailEntity);
            int i = 0;
            for (Iterator<MwebGroupImageDetailEntity> it = mwebGroupImageDetailEntitylList.iterator(); it.hasNext();)
            {
                i++;
                MwebGroupImageDetailEntity groupImageDetail = it.next();
                jsonObject.put("detail_pic_" + i, groupImageDetail.getUrl());
                jsonObject.put("detail_pic_id_" + i, groupImageDetail.getId());
            }
            
            jsonObject.put("status", 1);
        }
        return jsonObject;
    }
    
    @Override
    public JSONObject updateProduct(MwebGroupProductEntity mwebGroupProductEntity)
        throws Exception
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", 0);
        jsonObject.put("msg", "系统出错");
        if (StringUtils.isEmpty(mwebGroupProductEntity.getStartTime()))
        {
            mwebGroupProductEntity.setStartTime(null);
        }
        if (StringUtils.isEmpty(mwebGroupProductEntity.getEndTime()))
        {
            mwebGroupProductEntity.setEndTime(null);
        }
        MwebGroupProductEntity mp = mwebGroupProductDao.getProduct(mwebGroupProductEntity.getId());
        int type = mp.getType();
        // 千人团商品
        if (type == PRODUCT_TYPE.LARGE_GROUP.getValue())
        {
            String oldShortName = mp.getShortName();
            int oldTeamNumberLimit = mp.getTeamNumberLimit();
            int oldTeamValidHour = mp.getTeamValidHour();
            int oldTeamCount = mp.getTeamCount();
            String oldStartTeamTime = mp.getStartTeamTime().replace(".0", "");
            
            String shortName = mwebGroupProductEntity.getShortName();
            int teamNumberLimit = mwebGroupProductEntity.getTeamNumberLimit();
            int teamValidHour = mwebGroupProductEntity.getTeamValidHour();
            int teamCount = mwebGroupProductEntity.getTeamCount();
            String startTeamTime = mwebGroupProductEntity.getStartTeamTime().replace(".0", "");
            DateTime dt1 = DateTimeUtil.string2DateTime(oldStartTeamTime);
            
            if (oldTeamNumberLimit != teamNumberLimit || oldTeamValidHour != teamValidHour || oldTeamCount != teamCount || !oldStartTeamTime.equals(startTeamTime))
            {
                if (dt1.isBeforeNow())
                {
                    
                    jsonObject.put("msg", "该商品已经开团,开团时间、参团人数、成团时间不能被修改!");
                }
                else
                {
                    if (mwebGroupProductDao.updateProduct(mwebGroupProductEntity) > 0)
                    {
                        // 修改所有的自动团
                        List<MwebGroupProductInfoEntity> list = mwebGroupProductInfoDao.findAutoGroupProductInfoByMwebGroupProductId(mwebGroupProductEntity.getId());
                        for (Iterator<MwebGroupProductInfoEntity> it = list.iterator(); it.hasNext();)
                        {
                            MwebGroupProductInfoEntity team = it.next();
                            team.setTeamNumberLimit(teamNumberLimit);
                            team.setCreateTime(startTeamTime);
                            // 组团时间
                            DateTime dateTime = DateTimeUtil.string2DateTime(startTeamTime);
                            // 组团结束时间
                            DateTime teamEndTime = dateTime.plusHours(teamValidHour);
                            team.setTeamEndTime(teamEndTime.toString(DateTimeUtil.WEB_FORMAT));
                            // 修改自动团信息表
                            if (mwebGroupProductInfoDao.updateGroupProductInfo(team) > 0)
                            {
                                JSONObject autoTeam = mwebGroupProductInfoDao.findAutoTeamListByMwebGroupProductInfoId(team.getId());
                                
                                JSONObject upAutoTeam = new JSONObject();
                                upAutoTeam.put("id", autoTeam.getIntValue("id"));
                                upAutoTeam.put("teamValidHour", teamValidHour);
                                upAutoTeam.put("teamNumberLimit", teamNumberLimit);
                                upAutoTeam.put("startTeamTime", startTeamTime);
                                upAutoTeam.put("shortName", shortName);
                                mwebGroupProductInfoDao.updateAutoTeamConfig(upAutoTeam);
                            }
                        }
                        jsonObject.put("status", 1);
                        jsonObject.put("msg", "成功");
                    }
                    
                }
            }
            else
            {
                if (mwebGroupProductDao.updateProduct(mwebGroupProductEntity) > 0)
                {
                    
                    if (!oldShortName.equals(shortName))
                    {
                        // 修改所有的自动团
                        List<MwebGroupProductInfoEntity> list = mwebGroupProductInfoDao.findAutoGroupProductInfoByMwebGroupProductId(mwebGroupProductEntity.getId());
                        for (Iterator<MwebGroupProductInfoEntity> it = list.iterator(); it.hasNext();)
                        {
                            MwebGroupProductInfoEntity team = it.next();
                            JSONObject autoTeam = mwebGroupProductInfoDao.findAutoTeamListByMwebGroupProductInfoId(team.getId());
                            
                            JSONObject upAutoTeam = new JSONObject();
                            upAutoTeam.put("id", autoTeam.getIntValue("id"));
                            upAutoTeam.put("shortName", shortName);
                            mwebGroupProductInfoDao.updateAutoTeamConfig(upAutoTeam);
                        }
                        
                        ProductEntity productEntity = new ProductEntity();
                        productEntity.setId(mp.getProductId());
                        productEntity.setShortName(shortName);
                        mwebGroupProductDao.updateProductEntity(productEntity);
                    }
                    jsonObject.put("status", 1);
                    jsonObject.put("msg", "成功");
                }
            }
        }
        else if (type == PRODUCT_TYPE.ORDINARY_GOODS.getValue())
        {
            if (mwebGroupProductDao.updateProduct(mwebGroupProductEntity) > 0)
            {
                String oldShortName = mp.getShortName();
                String shortName = mwebGroupProductEntity.getShortName();
                if (!oldShortName.equals(shortName))
                {
                    ProductEntity productEntity = new ProductEntity();
                    productEntity.setId(mp.getProductId());
                    productEntity.setShortName(shortName);
                    mwebGroupProductDao.updateProductEntity(productEntity);
                }
                
                jsonObject.put("status", 1);
                jsonObject.put("msg", "成功");
            }
        }
        
        return jsonObject;
    }
    
    @Override
    public int forSale(Map<String, Object> para)
        throws Exception
    {
        return mwebGroupProductDao.forSale(para);
    }
    
    @Override
    public int updateOrder(Map<String, Object> para)
        throws Exception
    {
        // TODO Auto-generated method stub
        return mwebGroupProductDao.updateOrder(para);
    }
    
    @Override
    public JSONObject findProductAndStockForTeam(Map<String, Object> parameter)
        throws Exception
    {
        List<JSONObject> productList = mwebGroupProductDao.findProductAndStockForTeam(parameter);
        int total = mwebGroupProductDao.countProductAndStockForTeam(parameter);
        JSONObject result = new JSONObject();
        result.put("rows", productList);
        result.put("total", total);
        return result;
    }
    
    @Override
    public JSONObject findProductAndStockForTeamById(Map<String, Object> parameter)
        throws Exception
    {
        List<JSONObject> productList = mwebGroupProductDao.findProductAndStockForTeam(parameter);
        if (productList != null && productList.size() > 0)
        {
            
            productList.get(0).put("startTime", DateTimeUtil.timestampStringToWebString(productList.get(0).getString("startTime")));
            productList.get(0).put("endTime", DateTimeUtil.timestampStringToWebString(productList.get(0).getString("endTime")));
            return productList.get(0);
        }
        
        return new JSONObject();
    }
    
    @Override
    public int updateProductForTeam(Map<String, Object> parameter)
        throws Exception
    {
        
        return mwebGroupProductDao.updateProductForTeam(parameter);
    }
    
    @Override
    public MwebGroupProductEntity getProductById(int id)
        throws Exception
    {
        // TODO Auto-generated method stub
        return mwebGroupProductDao.getProduct(id);
    }
    
}
