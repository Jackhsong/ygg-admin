package TestPackage;

import com.google.common.collect.Sets;
import com.ygg.admin.util.DateTimeUtil;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.common.joda.time.DateTime;
import org.junit.Test;

import java.util.Set;

/**
 * @author lorabit
 * @since 16-5-9
 */
public class CommonTest {

    @Test
    public void t1() {
        DateTime time = new DateTime(2016, 05, 10, 00, 05, 00);
        String startTime = time.minusHours(1).toString("yyyy-MM-dd HH:00:00");
        String endTime = time.toString("yyyy-MM-dd HH:00:00");
        Integer day = Integer.valueOf(time.toString("yyyyMMddHH"));
        System.out.println(startTime);
        System.out.println(endTime);
        System.out.println(day);

        String s = "2016-05-09 12:00:00";
        System.out.println(DateTimeUtil.string2DateTime(s).toString("yyyyMMddHH"));

        DateTime t = new DateTime(2015, 02, 07, 00, 00, 00);
        System.out.println(t.toString("yyyy-MM-dd HH:00:00"));
        System.out.println(StringUtils.isEmpty((String)null));
    }

    @Test
    public void t2() {
        Set<Integer> categoryIdSet = Sets.newLinkedHashSet(null);

    }

}
