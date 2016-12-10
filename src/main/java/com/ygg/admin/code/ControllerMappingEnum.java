package com.ygg.admin.code;

public enum ControllerMappingEnum
{
    account("AccountController"),
    
    simplify("ActivitySimplifyController"),
    
    admin("AdminController"),
    
    analyze("AnalyzeController"),
    
    banner("BannerController"),
    
    birdex("BirdexController"),
    
    brand("BrandController"),
    
    cache("CacheController"),
    
    category("CategoryController"),
    
    couponCode("CouponCodeController"),
    
    coupon("CouponController"),
    
    customActivities("CustomActivitiesController"),
    
    customRegion("CustomRegionController"),
    
    finance("FinanceController"),
    
    game("GameActivityController"),
    
    gate("GateActivityController"),
    
    image("GegeImageController"),
    
    group("GroupBuyController"),
    
    index("IndexSettingController"),
    
    lotteryActivity("LotteryActivityController"),
    
    mallWindow("MallWindowController"),
    
    menu("MenuController"),
    
    notSendMsgProduct("NotSendMsgProductController"),
    
    order("OrderController"),
    
    orderManual("OrderManualController"),
    
    orderQuestion("OrderQuestionController"),
    
    delayDate("OverseasDelayRemindDateController"),
    
    overseasOrder("OverseasOrderController"),
    
    pageCustom("PageCustomController"),
    
    partner("PartnerController"),
    
    postage("PostageController"),
    
    productBase("ProductBaseController"),
    
    productBlacklist("ProductBlacklistController"),
    
    comment("ProductCommentController"),
    
    product("ProductController"),
    
    productUseScope("ProductUseScopeController"),
    
    refund("RefundController"),
    
    sale("SaleController"),
    
    flag("SaleFlagController"),
    
    saleTag("SaleTagController"),
    
    indexSale("SaleWindowController"),
    
    search("SearchController"),
    
    seller("SellerController"),
    
    signin("SignInController"),
    
    sms("SmsController"),
    
    special("SpecialActivityController"),
    
    spreaChannel("SpreadChannelController"),
    
    sysConfig("SystemConfigController"),
    
    log("SystemLogController"),

    fortuneWheel("FortuneWheelController"),
    
    user("UserController"),
    
    tools("ToolsController"),
    
    promotion("PromotionActivityController"),
    
    productCheck("ProductCheckController"),
    
    wechatGroup("WeChatGroupController"),
    
    sellerDeliverArea("SellerDeliverAreaController"),
    
    page("PageController"),
    
    customNavigation("CustomNavigationController"),
    
    customFunction("CustomFunctionController"),
    
    customCenter("CustomCenterController"),
    
    purchase("PurchaseContorller"),
    
    activityManjian("ActivityManjianController"),
    
    customGGRecommend("CustomGGRecommendController"),
    
    wechatGroupData("WeChatGroupDataController"),
    
    member("MemberController"),
    
    gegetuan("GegetuanController"),
    
    edbOrder("EdbOrderController"),
    
    thirdProduct("ThirdPartyProductController"),
    
    timeoutOrder("TimeoutOrderController"),
    
    customerProblem("CustomerProblemController"),
    
    qqbsBanner("QqbsBannerController"),
    
    qqbsSale("QqbsSaleController"),
    
    sellerTip("SellerTipController"),
    
    globalSale("GlobalSaleController"),
    
    logisticsTimeout("LogisticsTimeoutController"),
    
    customerStatistics("CustomerStatisticsController"),
    
    activityOptionalPart("ActivityOptionalPartController"),
    
    specialGroup("SpecialGroupActivityController"),
    
    mwebGroupBanner("MwebGroupBannerController"),
    
    refundReason("RefundReasonController"),
    
    welfareGroup("WelfareGroupController"),
    
    brandRecommend("BrandRecommendController"),
    
    cateGoryManager("CateGoryManagerController"),
    
    categoryBanner("CategoryBannerController"),
    
    categoryClassification("CategoryClassificationController"),
    
    categoryGGrecommend("CategoryGGrecommendController"),
    
    categoryRecommend("CategoryRecommendController"),
    
    categoryRegion("CategoryRegionController"),
    
    categorySale("CategorySaleController"),
    
    customRegion24("CustomRegion24Controller"),
    
    customGGRecommend24("CustomGGRecommend24Controller"),
    
    sellerBlacklist("SellerBlacklistController"),
    
    indexNavigation("IndexNavigationController"),
    
    specialActivityModel("SpecialActivityModelController"),
    
    sellerCategoryOperationManager("SellerCategoryOperationManagerController"),
    
    /** 大牌汇 */
    hotBrand("BrandHotController"),
    
    /** 商城国家 */
    mallCountry("MallCountryController"),
    
    /** 商城国家下品牌 */
    mallCountryBrand("MallCountryBrandController"),
    
    /** 渠道管理控制器 */
    channel("ChannelController"), channelOrder("ChannelOrderController"),
    
    /** 格格推荐 */
    recommend("RecommendController"),
    
    /** 采购商品库存 */
    purchaseStoring("PurchaseStoringController"),
    
    /** 第三方订单 */
    thirdPartyOrder("ThirdPartyOrderController"),
    
    /** 热卖品牌 */
    saleBrand("SaleBrandController"),
    
    /** 热卖单品 */
    saleSingle("SaleSingleController"),
    
    /** 左岸城堡
数据统计 */
    qqbsDataAnalyze("QqbsAnalyzeDataController"),
    
    /** 左岸城堡
 */
    qqbsCash("QqbsCashController"),
    /** 左岸城堡
用户关系 */
    qqbsError("QqbsErrorController"),
    /**
     * 左岸城堡
用户查询
     */
    qqbsUser("QqbsUserController"),
    /**
     * 左岸城堡
永久二维码管理
     */
    qRcode("QqbsQRCodeController"),
    /** 第三方平台管理 商品管理 */
    channelProManage("ChannelProductManageController"),
    
    /** 第三方平台管理 渠道统计 */
    channelStatistic("ChannelStatisticController"),
    
    /** 第三方平台管理 商品统计-数量/金额 */
    channelProStatistic("ChannelProStatisticController"),
    /** 用户关系管理 */
    crm("CrmController"),
    /** 左岸城堡优惠券 */
    mwebGroupCoupon("MwebGroupCouponController"),
    /** 左岸城堡优惠码 */
    mwebGroupCouponCode("MwebGroupCouponCodeController"),
    
    /** 左岸城堡
品牌馆管理 */
    qqbsBrandCategory("QqbsBrandCategoryController"),
    
    /** 左岸城堡
品牌管理 */
    qqbsBrand("QqbsBrandController"),
    
    qqbsOrder("QqbsOrderController"),
    
    /** 燕网提现管理 */
    ywCash("YwCashController"),
    /** 行动派升级审核管理 */
    qqbsUpgradeCheck("QqbsUpgradeCheckController"),
    /** 左岸城堡管理 */
    mwebGroupGame("MwebGroupGameActivityController"),
    
    /** 燕网数据统计 */
    ywDataAnalyze("YwAnalyzeDataController"),
    /** 左岸城堡团长免单 */
    mwebGroupTeamHeadFreeOrder("MwebGroupTeamHeadFreeOrderController"),
    
    /** 推送管理 */
    push("PushController"),
    
    /** 燕网用户关系修正 */
    ywError("YwErrorController"),
    
    /** 燕网Banner管理 */
    ywBanner("YwBannerController"),
    
    /** 燕网特卖管理 */
    ywSale("YwSaleController"),
    
    /** 燕网品牌馆管理 */
    ywBrandCategory("YwBrandCategoryController"),
    
    /** 燕网品牌管理 */
    ywBrand("YwBrandController"),

    /** 订单商品退款原因 */
    orderProductRefundReason("OrderProductRefundReasonController"),
    
    /**左岸城堡
总奖励*/
    qqbsAllRewThr("QqbsAllRewardThrController"),
    
    /**新手总奖励*/
    qqbsNewGuyRewThr("QqbsNewGuyRewardThrController"),
    /**商务中心白名单管理    */
    qqbsWhiteList("QqbsWhiteListController");
    
    
    final String controllerName;
    
    ControllerMappingEnum(String controllerName)
    {
        this.controllerName = controllerName;
    }
    
    public String getControllerName()
    {
        return controllerName;
    }
    
    public static String getControllerNameByName(String name)
    {
        for (ControllerMappingEnum e : ControllerMappingEnum.values())
        {
            if (e.name().equals(name))
            {
                return e.controllerName;
            }
        }
        return null;
    }
}
