package TestPackage.qqbs;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.ygg.admin.dao.qqbs.QqbsAccountDao;
import com.ygg.admin.entity.base.AbstractPageableEntity;
import com.ygg.admin.entity.qqbs.QqbsAccountEntity;
import com.ygg.admin.service.qqbs.user.impl.QqbsUserService;

/**
 * @author wuhy
 * @date 创建时间：2016年5月16日 下午4:46:11
 */
public class QqbsUserServiceTest
{
    
    private static QqbsUserService qqbsUserService;
    
    @BeforeClass
    public static void beforeTest()
    {
        qqbsUserService = new QqbsUserService();
        qqbsUserService.setQqbsAcountDao(new QqbsAccountDao()
        {
            
            @Override
            public int updateAccounSpread(Map<String, Object> para)
            {
                return 0;
            }
            
            @Override
            public List<QqbsAccountEntity> findAccountsByQqbsUserQueryCriteria(QqbsAccountEntity qqbsUserQueryCriteria)
            {
                List<QqbsAccountEntity> mockQueryResult = new ArrayList<QqbsAccountEntity>();
                if (qqbsUserQueryCriteria.getAccountId() == AbstractPageableEntity.NOT_EXIST_ACCOUNT_ID)
                {
                    mockQueryResult.add(new QqbsAccountEntity());
                    mockQueryResult.add(new QqbsAccountEntity());
                    
                }
                else
                {
                    mockQueryResult.add(new QqbsAccountEntity());
                }
                return mockQueryResult;
                
            }
            
            @Override
            public QqbsAccountEntity findAccountByAccountId(int accountId)
            {
                return null;
            }
            
            @Override
            public int countQqbsAccountByQqbsUserQueryCriteria(QqbsAccountEntity qqbsUserQueryCriteria)
            {
                if (qqbsUserQueryCriteria.getAccountId() == AbstractPageableEntity.NOT_EXIST_ACCOUNT_ID)
                {
                    return 2;
                }
                return 1;
            }
            
            @Override
            public int addPersistentQRCodeToAccount(QqbsAccountEntity qqbsAccountEntity)
            {
                return 0;
                
            }
        });
    }
    
    @Test
    public void testEmptyAccountIdAndEmptyNickName()
    {
        QqbsAccountEntity qqbsUserQueryCriteria = new QqbsAccountEntity();
        Map<String, Object> resultMap = qqbsUserService.findQqbsUsersAndCount(qqbsUserQueryCriteria);
        assertEquals(resultMap.get("rows"), null);
        assertEquals(resultMap.get("total"), 0);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testEmptyAccountIdAndNotEmptyNickName()
    {
        QqbsAccountEntity qqbsUserQueryCriteria = new QqbsAccountEntity();
        qqbsUserQueryCriteria.setNickName("Just");
        Map<String, Object> resultMap = qqbsUserService.findQqbsUsersAndCount(qqbsUserQueryCriteria);
        assertEquals(((List<QqbsAccountEntity>)resultMap.get("rows")).size(), 2);
        assertEquals(resultMap.get("total"), 2);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testNotEmptyAccountIdAndEmptyNickName()
    {
        QqbsAccountEntity qqbsUserQueryCriteria = new QqbsAccountEntity();
        qqbsUserQueryCriteria.setAccountId(100);
        Map<String, Object> resultMap = qqbsUserService.findQqbsUsersAndCount(qqbsUserQueryCriteria);
        assertEquals(((List<QqbsAccountEntity>)resultMap.get("rows")).size(), 1);
        assertEquals(resultMap.get("total"), 1);
    }
}
