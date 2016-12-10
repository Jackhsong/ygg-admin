package TestPackage.qqbs;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ygg.admin.sdk.wenxin.WeiXinUtil;

/**
 * @author wuhy
 * @date 创建时间：2016年5月17日 下午4:00:37
 */
public class WeiXinUtilTest
{
    
    private static final int TEST_ACCOUNT_ID = 1000000;
    
    @Test
    public void testbuildPersistentQRCode()
    {
        assertEquals(WeiXinUtil.buildPersistentQRCode(TEST_ACCOUNT_ID) == null, false);
        
    }
}
