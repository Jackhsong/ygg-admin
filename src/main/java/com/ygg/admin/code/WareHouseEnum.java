package com.ygg.admin.code;


public enum WareHouseEnum
{
    HZWH(1,"杭州仓","",""),
    
    BirWH(2,"香港笨鸟仓","香港仓-HKG","香港标准服务-HKBDX"),

    JAPANWH(3,"日本埼玉仓", "日本埼玉仓库","日本邮政Dmail");


    private int id;
    
    private String value;
    private String displayInRetExcel; //在上传订单后得到的发货订单中显示的名称
    private String serverWay;

    private WareHouseEnum(int id, String value,String displayInRetExcel,String serverWay)
    {
        this.id = id;
        this.value = value;
        this.displayInRetExcel = displayInRetExcel;
        this.serverWay = serverWay;
    }
    
    public int getId()
    {
        return id;
    }

    public String getValue()
    {
        return this.value;
    }

    public String getDisplayInRetExcel() {
        return displayInRetExcel;
    }

    public String getServerWay() {
        return serverWay;
    }

    public static WareHouseEnum of(int id){
        for (WareHouseEnum e : WareHouseEnum.values())
        {
            if (e.id == id)
            {
                return e;
            }
        }
        return null;
    }
    
    
    public static String getValueById(int id)
    {
        for (WareHouseEnum e : WareHouseEnum.values())
        {
            if (e.id == id)
            {
                return e.getValue();
            }
        }
        return "";
    }
    
    public static int getIdByValue(String value){
        for (WareHouseEnum e : WareHouseEnum.values())
        {
            if (e.value.equals(value))
            {
                return e.getId();
            }
        }
        return 0;
    }
    
    public static int[] getSellerIdByValue(String value){
        for (WareHouseEnum e : WareHouseEnum.values())
        {
            if (e.value.equals(value))
            {
                return getSellerIdById(e.getId());
            }
        }
        return null;
    }
    
    public static int[] getSellerIdById(int id){
        for(SellerIdEnum se:SellerIdEnum.values()){
            if(se.id == id){
                return se.getSellerIds();
            }
        }
        return null;
    }
    
    
    enum SellerIdEnum{
        HZWH(1,782,776,435,392,75),
        
        BirWH(2,251,236,722,771,772,742,743),
        
        JAPWH(3,519,556,583,723);
                
        private int id;
        
        private int[] sellerIds;
        
        private SellerIdEnum(int id,int... sellerIds)
        {
            this.id = id;
            this.sellerIds = sellerIds;
        }
        
        public int getId(){
            return id;
        }

        public int[] getSellerIds()
        {
            return sellerIds;
        }
    }
}
