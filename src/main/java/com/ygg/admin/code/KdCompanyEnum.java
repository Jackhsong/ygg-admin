package com.ygg.admin.code;

import org.apache.commons.lang.StringUtils;

/**
 * 快递枚举
 */
public enum KdCompanyEnum
{
    /** 中天万运 */
    ZHONGTIANWANYUN("中天万运", 1, "zhongtianwanyun", "", ""),
    
    /** 速尔快递 */
    SUER("速尔快递", 3, "Suer", "速尔", ""),
    
    /** 尚橙 */
    SHANGCHENG("尚橙", 1, "shangcheng", "", ""),
    
    /** 广东邮政 */
    GUANGDONGYOUZHENGWULIU("广东邮政", 1, "guangdongyouzhengwuliu", "", ""),
    
    /** 新蛋奥硕 */
    NEWEGGOZZO("新蛋奥硕", 1, "neweggozzo", "", ""),
    
    /** 盛辉物流 */
    SHENGHUIWULIU("盛辉物流", 1, "shenghuiwuliu", "", ""),
    
    /** Ontrac */
    ONTRAC("Ontrac", 1, "ontrac", "", ""),
    
    /** 长宇物流 */
    CHANGYUWULIU("长宇物流", 1, "changyuwuliu", "", ""),
    
    /** 七天连锁 */
    SEVENDAYS("七天连锁", 1, "sevendays", "", ""),
    
    /** 飞康达 */
    FEIKANGDA("飞康达", 1, "feikangda", "", ""),
    
    /** 明亮物流 */
    MINGLIANGWULIU("明亮物流", 1, "mingliangwuliu", "", ""),
    
    /** 包裹/平邮 */
    YOUZHENGGUONEI("包裹/平邮(邮政国内小包)", 3, "youzhengguonei", "youzhengguonei", ""),
    
    /** 国通快递 */
    GUOTONGKUAIDI("国通快递", 3, "guotongkuaidi", "guotongkuaidi", ""),
    
    /** 万家物流 */
    WANJIAWULIU("万家物流", 1, "wanjiawuliu", "", ""),
    
    /** 澳大利亚邮政-英文 */
    AUSPOST("澳大利亚邮政-英文", 1, "auspost", "", ""),
    
    /** 文捷航空 */
    WENJIESUDI("文捷航空", 1, "wenjiesudi", "", ""),
    
    /** 加拿大邮政-英文版 */
    CANPOST("加拿大邮政-英文版", 1, "canpost", "", ""),
    
    /** 全晨快递 */
    QUANCHENKUAIDI("全晨快递", 1, "quanchenkuaidi", "", ""),
    
    /** 加拿大邮政-法文版 */
    CANPOSTFR("加拿大邮政-法文版", 1, "canpostfr", "", ""),
    
    /** 佳怡物流 */
    JIAYIWULIU("佳怡物流", 1, "jiayiwuliu", "", ""),
    
    /** UPS-en */
    UPSEN("UPS-en", 1, "upsen", "", ""),
    
    /** 快捷速递 */
    KUAIJIESUDI("快捷速递", 3, "kuaijiesudi", "kuaijiesudi", ""),
    
    /** TNT-en */
    TNTEN("TNT-en", 1, "tnten", "", ""),
    
    /** D速快递 */
    DSUKUAIDI("D速快递", 1, "dsukuaidi", "", ""),
    
    /** DHL-en */
    DHLEN("DHL-en", 1, "dhlen", "", ""),
    
    /** 港中能达 */
    GANZHONGNENGDA("港中能达", 1, "ganzhongnengda", "", ""),
    
    /** 顺丰-英文版 */
    SHUNFENGEN("顺丰-英文版", 1, "shunfengen", "", ""),
    
    /** 越丰物流 */
    YUEFENGWULIU("越丰物流", 1, "yuefengwuliu", "", ""),
    
    /** 共速达 */
    GONGSUDA("共速达", 1, "gongsuda", "", ""),
    
    /** 急先达 */
    JIXIANDA("急先达", 1, "jixianda", "", ""),
    
    /** 跨越速递 */
    KUAYUE("跨越速递", 1, "kuayue", "", ""),
    
    /** 百福东方 */
    BAIFUDONGFANG("百福东方", 1, "baifudongfang", "", ""),
    
    /** 全际通 */
    QUANJITONG("全际通", 1, "quanjitong", "", ""),
    
    /** BHT */
    BHT("BHT", 1, "bht", "", ""),
    
    /** 盛丰物流 */
    SHENGFENGWULIU("盛丰物流", 1, "shengfengwuliu", "", ""),
    
    /** 凡客 */
    VANCL("凡客", 1, "vancl", "", ""),
    
    /** UPS */
    UPS("UPS", 1, "ups", "", ""),
    
    /** 汇强快递 */
    HUIQIANGKUAIDI("汇强快递", 3, "huiqiangkuaidi", "huiqiangkuaidi", ""),
    
    /** 一邦速递 */
    YIBANGWULIU("一邦速递", 1, "yibangwuliu", "", ""),
    
    /** 希优特 */
    XIYOUTEKUAIDI("希优特", 1, "xiyoutekuaidi", "", ""),
    
    /** 优速物流 */
    YOUSHUWULIU("优速物流", 3, "youshuwuliu", "youshuwuliu", ""),
    
    /** 昊盛物流 */
    HAOSHENGWULIU("昊盛物流", 1, "haoshengwuliu", "", ""),
    
    /** 源伟丰 */
    YUANWEIFENG("源伟丰", 1, "yuanweifeng", "", ""),
    
    /** 伍圆速递 */
    WUYUANSUDI("伍圆速递", 1, "wuyuansudi", "", ""),
    
    /** 顺丰速运 */
    SHUNFENG("顺丰速运", 3, "shunfeng", "shunfeng", ""),
    
    /** 蓝镖快递 */
    LANBIAOKUAIDI("蓝镖快递", 1, "lanbiaokuaidi", "", ""),
    
    /** 圆通速递 */
    YUANTONG("圆通速递", 3, "yuantong", "yuantong", ""),
    
    /** COE */
    COE("COE", 1, "coe", "", ""),
    
    /** 天天快递 */
    TIANTIAN("天天快递", 3, "tiantian", "tiantian", ""),
    
    /** 南京100 */
    NANJING("南京100", 1, "nanjing", "", ""),
    
    /** 宅急送 */
    ZHAIJISONG("宅急送", 3, "zhaijisong", "zhaijisong", ""),
    
    /** 恒路物流 */
    HENGLUWULIU("恒路物流", 1, "hengluwuliu", "", ""),
    
    /** 希伊艾斯 */
    CCES("希伊艾斯", 1, "cces", "", ""),
    
    /** 金大物流 */
    JINDAWULIU("金大物流", 1, "jindawuliu", "", ""),
    
    /** 彪记快递 */
    BIAOJIKUAIDI("彪记快递", 1, "biaojikuaidi", "", ""),
    
    /** 华夏龙 */
    HUAXIALONGWULIU("华夏龙", 1, "huaxialongwuliu", "", ""),
    
    /** 星晨急便 */
    XINGCHENGJIBIAN("星晨急便", 1, "xingchengjibian", "", ""),
    
    /** 运通中港 */
    YUNTONGKUAIDI("运通中港", 1, "yuntongkuaidi", "", ""),
    
    /** 亚风速递 */
    YAFENGSUDI("亚风速递", 1, "yafengsudi", "", ""),
    
    /** 佳吉快运 */
    JIAJIWULIU("佳吉快运", 1, "jiajiwuliu", "", ""),
    
    /** 全日通 */
    QUANRITONGKUAIDI("全日通", 1, "quanritongkuaidi", "", ""),
    
    /** 源安达 */
    YUANANDA("源安达", 1, "yuananda", "", ""),
    
    /** 安信达 */
    ANXINDAKUAIXI("安信达", 1, "anxindakuaixi", "", ""),
    
    /** 加运美 */
    JIAYUNMEIWULIU("加运美", 1, "jiayunmeiwuliu", "", ""),
    
    /** 凤凰快递 */
    FENGHUANGKUAIDI("凤凰快递", 1, "fenghuangkuaidi", "", ""),
    
    /** 万象物流 */
    WANXIANGWULIU("万象物流", 1, "wanxiangwuliu", "", ""),
    
    /** 配思货运 */
    PEISIHUOYUNKUAIDI("配思货运", 1, "peisihuoyunkuaidi", "", ""),
    
    /** 宏品物流 */
    HONGPINWULIU("宏品物流", 1, "hongpinwuliu", "hongpinwuliu", ""),
    
    /** 中铁物流 */
    ZTKY("中铁物流", 1, "ztky", "", ""),
    
    /** GLS */
    GLS("GLS", 1, "gls", "", ""),
    
    /** FedEx-国际 */
    FEDEX("FedEx-国际", 1, "fedex", "", ""),
    
    /** 上大物流 */
    SHANGDA("上大物流", 1, "shangda", "", ""),
    
    /** AAE-中国 */
    AAE("AAE-中国", 1, "aae", "", ""),
    
    /** 中铁快运 */
    ZHONGTIEWULIU("中铁快运", 1, "zhongtiewuliu", "", ""),
    
    /** 大田物流 */
    DATIANWULIU("大田物流", 1, "datianwuliu", "", ""),
    
    /** 原飞航 */
    YUANFEIHANGWULIU("原飞航", 1, "yuanfeihangwuliu", "", ""),
    
    /** 新邦物流 */
    XINBANGWULIU("新邦物流", 1, "xinbangwuliu", "", ""),
    
    /** 申通快递 */
    SHENTONG("申通快递", 3, "shentong", "shentong", ""),
    
    /** 如风达 */
    RUFENGDA("如风达", 1, "rufengda", "", ""),
    
    /** 海盟速递 */
    HAIMENGSUDI("海盟速递", 1, "haimengsudi", "", ""),
    
    /** 赛澳递 */
    SAIAODI("赛澳递", 3, "saiaodi", "赛澳递", ""),
    
    /** 圣安物流 */
    SHENGANWULIU("圣安物流", 1, "shenganwuliu", "", ""),
    
    /** 海红网送 */
    HAIHONGWANGSONG("海红网送", 1, "haihongwangsong", "", ""),
    
    /** 一统飞鸿 */
    YITONGFEIHONG("一统飞鸿", 1, "yitongfeihong", "", ""),
    
    /** 康力物流 */
    KANGLIWULIU("康力物流", 1, "kangliwuliu", "", ""),
    
    /** 海外环球 */
    HAIWAIHUANQIU("海外环球", 1, "", "", ""),
    
    /** 韵达快运 */
    YUNDA("韵达快运", 3, "yunda", "yunda", ""),
    
    /** 联邦快递 */
    LIANBANGKUAIDI("联邦快递", 1, "lianbangkuaidi", "", ""),
    
    /** 汇通快运 */
    HUITONGKUAIDI("汇通快运", 3, "huitongkuaidi", "huitongkuaidi", ""),
    
    /** 飞快达 */
    FEIKUAIDA("飞快达", 1, "feikuaida", "", ""),
    
    /** 鑫飞鸿 */
    XINHONGYUKUAIDI("鑫飞鸿", 1, "xinhongyukuaidi", "", ""),
    
    /** 中通速递 */
    ZHONGTONG("中通速递", 3, "zhongtong", "zhongtong", ""),
    
    /** 全一快递 */
    QUANYIKUAIDI("全一快递", 3, "quanyikuaidi", "全一", ""),
    
    /** EMS */
    EMS("EMS", 3, "ems", "EMS", ""),
    
    /** 民航快递 */
    MINGHANGKUAIDI("民航快递", 1, "minghangkuaidi", "", ""),
    
    /** 京广速递 */
    JINGUANGSUDIKUAIJIAN("京广速递", 1, "jinguangsudikuaijian", "", ""),
    
    /** TNT */
    TNT("TNT", 1, "tnt", "", ""),
    
    /** DHL */
    DHL("DHL", 1, "dhl", "", ""),
    
    /** 德邦物流 */
    DEBANGWULIU("德邦物流", 3, "debangwuliu", "debangwuliu", ""),
    
    /** 天地华宇 */
    TIANDIHUAYU("天地华宇", 1, "tiandihuayu", "", ""),
    
    /** 龙邦物流 */
    LONGBANWULIU("龙邦物流", 3, "longbanwuliu", "龙邦速递", ""),
    
    /** 信丰物流 */
    XINFENGWULIU("信丰物流", 1, "xinfengwuliu", "", ""),
    
    /** 联昊通 */
    LIANHAOWULIU("联昊通", 1, "lianhaowuliu", "", ""),
    
    /** 全峰快递 */
    QUANFENGKUAIDI("全峰快递", 3, "quanfengkuaidi", "quanfengkuaidi", ""),
    
    /** 中邮物流 */
    ZHONGYOUWULIU("中邮物流", 1, "zhongyouwuliu", "", ""),
    
    /** DHL-德国 */
    DHLDE("DHL-德国", 1, "dhlde", "", ""),
    
    /** 元智捷诚 */
    YUANZHIJIECHENG("元智捷诚", 1, "yuanzhijiecheng", "", ""),
    
    /** 通和天下 */
    TONGHETIANXIA("通和天下", 1, "tonghetianxia", "", ""),
    
    /** 国际包裹 */
    YOUZHENGGUOJI("国际包裹", 1, "youzhengguoji", "", ""),
    
    /** 郑州建华 */
    ZHENGZHOUJIANHUA("郑州建华", 1, "zhengzhoujianhua", "", ""),
    
    /** 华企快运 */
    HUAQIKUAIYUN("华企快运", 1, "huaqikuaiyun", "", ""),
    
    /** EMS-英文 */
    EMSEN("EMS-英文", 1, "emsen", "", ""),
    
    /** 城市100 */
    CITY100("城市100", 1, "city100", "", ""),
    
    /** 香港邮政 */
    HKPOST("香港邮政", 1, "hkpost", "", ""),
    
    /** 远成物流 */
    YUANCHENGWULIU("远成物流", 1, "yuanchengwuliu", "", ""),
    
    /** 邦送物流 */
    BANGSONGWULIU("邦送物流", 1, "bangsongwuliu", "", ""),
    
    /** 安捷快递 */
    ANJIEKUAIDI("安捷快递", 1, "anjiekuaidi", "", ""),
    
    /** 山西红马甲 */
    SXHONGMAJIA("山西红马甲", 1, "sxhongmajia", "", ""),
    
    /** DPEX */
    DPEX("DPEX", 1, "dpex", "", ""),
    
    /** 穗佳物流 */
    SUIJIAWULIU("穗佳物流", 1, "suijiawuliu", "", ""),
    
    /** 芝麻开门 */
    ZHIMAKAIMEN("芝麻开门", 1, "zhimakaimen", "", ""),
    
    /** 飞豹快递 */
    FEIBAOKUAIDI("飞豹快递", 1, "feibaokuaidi", "", ""),
    
    /** 递四方 */
    DISIFANG("递四方", 1, "disifang", "", ""),
    
    /** 传喜物流 */
    CHUANXIWULIU("传喜物流", 1, "chuanxiwuliu", "", ""),
    
    /** EMS-国际 */
    EMSGUOJI("EMS-国际", 1, "emsguoji", "", ""),
    
    /** 捷特快递 */
    JIETEKUAIDI("捷特快递", 1, "jietekuaidi", "", ""),
    
    /** FedEx-美国 */
    FEDEXUS("FedEx-美国", 1, "fedexus", "", ""),
    
    /** 隆浪快递 */
    LONGLANGKUAIDI("隆浪快递", 1, "longlangkuaidi", "", ""),
    
    /** 三态速递 */
    SANTAISUDI("三态速递", 1, "santaisudi", "", ""),
    
    /** 中速快递 */
    ZHONGSUKUAIDI("中速快递", 1, "zhongsukuaidi", "", ""),
    
    /** 晋越快递 */
    JINYUEKUAIDI("晋越快递", 1, "jinyuekuaidi", "", ""),
    
    /** 乐捷递 */
    LEJIEDI("乐捷递", 1, "lejiedi", "", ""),
    
    /** 忠信达 */
    ZHONGXINDA("忠信达", 1, "zhongxinda", "", ""),
    
    /** 嘉里大通 */
    JIALIDATONG("嘉里大通", 1, "jialidatong", "", ""),
    
    /** OCS */
    OCS("OCS", 1, "ocs", "", ""),
    
    /** USPS */
    USPS("USPS", 1, "usps", "", ""),
    
    /** 美国快递 */
    MEIGUOKUAIDI("美国快递", 1, "meiguokuaidi", "", ""),
    
    /** 立即送 */
    LIJISONG("立即送", 1, "lijisong", "", ""),
    
    /** 银捷速递 */
    YINJIESUDI("银捷速递", 1, "yinjiesudi", "", ""),
    
    /** 门对门 */
    MENDUIMEN("门对门", 1, "menduimen", "", ""),
    
    /** 河北建华 */
    HEBEIJIANHUA("河北建华", 1, "hebeijianhua", "", ""),
    
    /** 微特派 */
    WEITEPAI("微特派", 1, "weitepai", "", ""),
    
    /** 风行天下 */
    FENGXINGTIANXIA("风行天下", 1, "fengxingtianxia", "", ""),
    
    /** 贝海国际 */
    XLOBO("贝海国际速递", 1, "xlobo", "", ""),
    
    /** 京东快递 */
    JD("京东快递", 3, "jd", "jd", ""),
    
    /** 八达通 */
    BDATONG("八达通", 1, "bdatong", "", ""),
    
    /** 增益速递 */
    ZENGYISUDI("增益速递", 1, "zengyisudi", "", ""),
    
    /** 泛捷国际 */
    EPANEX("泛捷国际速递", 1, "epanex", "", ""),
    
    /** 高铁速递 */
    HRE("高铁速递", 1, "hre", "", ""),
    
    /** 安能物流 */
    ANNENGWULIU("安能物流", 1, "annengwuliu", "", ""),
    
    /** 城际速递*/
    CHENGJISUDI("城际速递", 1, "chengji", "", "请前往城际速递官网www.chengji-express.com/index.asp查询"),
    
    /** 百通物流 */
    BAITONGWULIU("百通物流", 0, "", "", "请前往百通物流官网http://www.buytong.cn/IndexNew.aspx?region=usa查询"),
    
    /** 香港速邮 */
    HKSUYOU("香港速邮", 0, "", "", "如有需要，请联系左岸城堡客服查询物流信息"),
    
    /**百世汇通  重复，和汇通快运一样*/
    //    BAISHIHUITONG("百世汇通", 2, "", "huitong", ""),
    
    /**e邮宝*/
    EYOUBAO("e邮宝", 2, "", "e邮宝", ""),
    
    /**思迈*/
    SIMAI("思迈", 2, "", "思迈", ""),
    
    JIUYE("九曳供应链", 1, "jiuyescm", "", "请前往九曳供应链官网http://www.ly-wsq.com/ 查询"),

    BIRDEX("笨鸟海淘", 1, "birdex", "", ""),

    JIKELL("极客冷链", 0, "", "", "请前往九曳供应链官网http://www.geeklogistics.cn/order/ 查询"),

    XINGBIDA("行必达", 0, "", "", "请前往行必达官网http://www.speeda.cn 查询"),

    ZHONGWAI("中外速运", 0, "", "", "请前往中外速运官网http://www.cnausu.com/ 查询"),

    ZHONGAO("中澳速递", 0, "", "", "请前往中澳速递官网http://www.cnausu.com/ 查询");

    /** 快递公司名称 */
    final String companyName;
    
    /** 支持的快递服务商：0：都不支持，1：快递100支持，2：快递吧支持，3：都支持*/
    final int serviceType;
    
    /** 快递100编码*/
    final String kd100Code;
    
    /** 快递吧编码*/
    final String kd8Code;
    
    /** 不支持快递detail*/
    final String queryURL;
    
    private KdCompanyEnum(String companyName, int serviceType, String kd100Code, String kd8Code, String queryURL)
    {
        this.companyName = companyName;
        this.serviceType = serviceType;
        this.kd100Code = kd100Code;
        this.kd8Code = kd8Code;
        this.queryURL = queryURL;
    }
    
    public String getCompanyName()
    {
        return companyName;
    }
    
    public String getQueryURL()
    {
        return queryURL;
    }
    
    public static String getCompanyNameByKd100Code(String kd100Code)
    {
        if (StringUtils.isEmpty(kd100Code))
        {
            return "";
        }
        for (KdCompanyEnum e : KdCompanyEnum.values())
        {
            if (e.kd100Code.equals(kd100Code))
            {
                return e.companyName;
            }
        }
        return "";
    }
    
    public int getServiceType()
    {
        return serviceType;
    }
    
    public String getKd100Code()
    {
        return kd100Code;
    }
    
    public String getKd8Code()
    {
        return kd8Code;
    }
    
    public static String getKd100CodeByCompanyName(String companyName)
    {
        if (StringUtils.isEmpty(companyName))
        {
            return "";
        }
        for (KdCompanyEnum e : KdCompanyEnum.values())
        {
            if (companyName.equals(e.companyName))
            {
                return e.kd100Code;
            }
        }
        return "";
    }
    
    public static String getKd8CodeByCompanyName(String companyName)
    {
        if (StringUtils.isEmpty(companyName))
        {
            return "";
        }
        for (KdCompanyEnum e : KdCompanyEnum.values())
        {
            if (companyName.equals(e.companyName))
            {
                return e.kd8Code;
            }
        }
        return "";
    }
    
    public static KdCompanyEnum getKdCompanyEnumByCompanyName(String companyName)
    {
        if (StringUtils.isEmpty(companyName))
        {
            return null;
        }
        for (KdCompanyEnum e : KdCompanyEnum.values())
        {
            if (companyName.equals(e.companyName))
            {
                return e;
            }
        }
        return null;
    }
    
    public static String getCompanyNameByKd8Code(String channel)
    {
        if (StringUtils.isEmpty(channel))
        {
            return null;
        }
        for (KdCompanyEnum e : KdCompanyEnum.values())
        {
            if (channel.equals(e.kd8Code))
            {
                return e.companyName;
            }
        }
        return null;
    }
    
}