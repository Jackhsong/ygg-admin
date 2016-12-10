package com.ygg.admin.util;

import com.google.common.base.Splitter;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-4-25
 * 获取请求参数工具类
 */
public class CgiUtil {

    public static <T> T getValue(HttpServletRequest request, String name, T defValue, boolean xssFilter) {
        Object result = null;
        if (!(defValue instanceof String) && defValue != null) {
            if (defValue instanceof Integer) {
                result = Integer.valueOf(parseInt(request.getParameter(name), ((Integer) defValue).intValue()));
            } else if (defValue instanceof Long) {
                result = Long.valueOf(parseLong(request.getParameter(name), ((Long) defValue).longValue()));
            } else if (defValue instanceof Boolean) {
                result = Boolean.valueOf(parseBoolean(request.getParameter(name), ((Boolean) defValue).booleanValue()));
            }
        } else {
            result = parseString(request.getParameter(name), (String) defValue);
            if (xssFilter) {
                result = xssFilter((String) result);
            }
        }

        return (T) result;
    }

    public static <T> T getRequiredValue(HttpServletRequest request, String name, T defType) {
        Object result = null;
        if (request.getParameter(name) == null)
            throw new IllegalArgumentException("参数" + name + "必要 但是没有传");
        if (!(defType instanceof String) && defType != null) {
            if (defType instanceof Integer) {
                result = Integer.valueOf(parseInt(request.getParameter(name), ((Integer) defType).intValue()));
            } else if (defType instanceof Long) {
                result = Long.valueOf(parseLong(request.getParameter(name), ((Long) defType).longValue()));
            } else if (defType instanceof Boolean) {
                result = Boolean.valueOf(parseBoolean(request.getParameter(name), ((Boolean) defType).booleanValue()));
            }
        } else {
            result = parseString(request.getParameter(name), (String) defType);
        }
        return (T) result;
    }

    public static <T> T getValue(HttpServletRequest request, String name, T defValue) {
        return getValue(request, name, defValue, false);
    }


    public static List<String> getSplitToStringList(HttpServletRequest request, String name, String defValue, String separator) {
        String value = getValue(request, name, defValue);
        return Splitter.on(separator).omitEmptyStrings().trimResults().splitToList(value);
    }

    public static List<Integer> getSplitToIntegerList(HttpServletRequest request, String name, String defValue, String separator) {
        List<String> list = getSplitToStringList(request, name, defValue, separator);
        List<Integer> ret = new ArrayList();
        for (String v_ : list) {
            ret.add(Integer.parseInt(v_));
        }
        return ret;
    }

    public static String xssFilter(String input) {
        if (input != null && !input.isEmpty()) {
            StringBuilder sb = new StringBuilder(input.length() + 16);

            for (int i = 0; i < input.length(); ++i) {
                char charAt = input.charAt(i);
                switch (charAt) {
                    case '\"':
                        sb.append('“');
                        break;
                    case '\'':
                        sb.append('‘');
                        break;
                    case '<':
                        sb.append('＜');
                        break;
                    case '>':
                        sb.append('＞');
                        break;
                    case '\\':
                        sb.append('＼');
                        break;
                    default:
                        sb.append(charAt);
                }
            }

            return sb.toString();
        } else {
            return input;
        }
    }


    public static int getMax(HttpServletRequest req, int defaultLimit) {
        return getValue(req, "rows", defaultLimit);
    }

    public static int getStart(HttpServletRequest req, int max) {
        int page = getValue(req, "page", 1);
        if (page == 0) page = 1;
        return (page - 1) * max;
    }

    /**
     * 获得 easyui datagrid 分页查询 map
     */
    public static Map<String, Object> getPageParaMap(HttpServletRequest req, int defaultLimit) {
        int max = getMax(req, defaultLimit);
        int start = getStart(req, max);
        Map<String, Object> para = new HashMap<>();
        para.put("start", start);
        para.put("max", max);
        return para;
    }


    public static int parseInt(String str, int defValue) {
        if (str != null && str.length() != 0) {
            try {
                return Integer.parseInt(str);
            } catch (Exception var3) {
                return defValue;
            }
        } else {
            return defValue;
        }
    }

    public static long parseLong(String str, long defValue) {
        if (str != null && str.length() != 0) {
            try {
                return Long.parseLong(str);
            } catch (Exception var4) {
                return defValue;
            }
        } else {
            return defValue;
        }
    }

    public static String parseString(Object obj, String defValue) {
        return obj == null ? defValue : obj.toString();
    }

    public static boolean parseBoolean(Object obj, boolean defValue) {
        return obj != null && obj instanceof Boolean ? ((Boolean) obj).booleanValue() : defValue;
    }


}
