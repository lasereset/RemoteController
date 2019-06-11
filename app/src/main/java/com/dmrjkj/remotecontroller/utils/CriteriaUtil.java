package com.dmrjkj.remotecontroller.utils;

import android.annotation.SuppressLint;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class CriteriaUtil {
    
    public static String ge(String field, Object value) {
        if (value instanceof Date) {
        	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	String start = df.format(((Date) value).getTime());
            return MessageFormat.format("('{alias}'.{0} >= ''{1}'')", field, start);
        } else if (value instanceof String) {
            return MessageFormat.format("('{alias}'.{0} >= ''{1}'')", field, value);
        }
        // int,float etc..
        return MessageFormat.format("('{alias}'.{0} >= {1})", field, String.valueOf(value));
    }

    public static String gt(String field, Object value) {
        if (value instanceof Date) {
        	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	String start = df.format(((Date) value).getTime());
            return MessageFormat.format("('{alias}'.{0} > ''{1}'')", field, start);
        } else if (value instanceof String) {
            return MessageFormat.format("('{alias}'.{0} > ''{1}'')", field, value);
        }
        return MessageFormat.format("('{alias}'.{0} > {1})", field, String.valueOf(value));
    }

    public static String le(String field, Object value) {
        if (value instanceof Date) {
        	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	String end = df.format(((Date) value).getTime());
            return MessageFormat.format("('{alias}'.{0} <= ''{1}'')", field, end);
        } else if (value instanceof String) {
            return MessageFormat.format("('{alias}'.{0} <=''{1}'')", field, value);
        }
        return MessageFormat.format("('{alias}'.{0} <= {1})", field, String.valueOf(value));
    }

    public static String lt(String field, Object value) {
        if (value instanceof Date) {
        	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	String end = df.format(((Date) value).getTime());
            return MessageFormat.format("('{alias}'.{0} < ''{1}'')", field, end);
        } else if (value instanceof String) {
            return MessageFormat.format("('{alias}'.{0} < ''{1}'')", field, value);
        }
        return MessageFormat.format("('{alias}'.{0} < {1})", field, String.valueOf(value));
    }

    // 字段和数值不相同
    public static String ne(String field, Object value) {
        if (value instanceof String) {  
            return MessageFormat.format("('{alias}'.{0} <> ''{1}'')", field, value);
        }
        return MessageFormat.format("('{alias}'.{0} <> {1})", field, String.valueOf(value));
    }

    // 字段和数值相同
    public static String eq(String field, Object value) {
        if (value instanceof String) {
            return MessageFormat.format("('{alias}'.{0} = ''{1}'')", field, value);
        }
        return MessageFormat.format("('{alias}'.{0} = {1})", field, String.valueOf(value));
    }
    
    // 字段和数值不相同
    public static String neq(String field, Object value) {
        if (value instanceof String) {
            return MessageFormat.format("('{alias}'.{0} != ''{1}'')", field, value);
        }
        return MessageFormat.format("('{alias}'.{0} != {1})", field, String.valueOf(value));
    }
    
    

    // 字段和字符串比较
    public static String like(String field, String value) {
        return MessageFormat.format("('{alias}'.{0} like ''?{1}?'')", field, value);
    }

    // 用于日期
    public static String between(String field, Date startDate, Date endDate) {
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String start = df.format(startDate.getTime());
    	String end = df.format(endDate.getTime());
    	return MessageFormat.format("('{alias}'.{0} > ''{1}'' and  ('{alias}'.{2}) < ''{3}'')", field, start, field, end);
    }

    public static String and(String sql1, String sql2) {
        return MessageFormat.format("({0} and {1})", sql1, sql2);
    }

    public static String or(String sql1, String sql2) {
        return MessageFormat.format("({0} or {1})", sql1, sql2);
    }

    public static String or(String... sql) {
        if (sql == null || sql.length == 0) {
            return null;
        }
        
        if (sql.length == 1) {
            return sql[0];
        }
        
        StringBuilder patten = new StringBuilder("("); 
        for (int i = 0; i < sql.length; i++) {
            patten.append("{");
            patten.append(i);
            patten.append("}");
            if (i < sql.length - 1) {
                patten.append(" or ");
            }
        }
        patten.append(")");

        //Log.d("pattern:" + patten.toString());
        Object[] objs = sql;
        String result = MessageFormat.format(patten.toString(), objs);
        //Log.d("result:" + result);
        return result;
    }
    
    public static String and(String... sql) {
        if (sql == null || sql.length == 0) {
            return null;
        }
        
        if (sql.length == 1) {
            return sql[0];
        }
        
        StringBuilder patten = new StringBuilder("("); 
        for (int i = 0; i < sql.length; i++) {
            patten.append("{");
            patten.append(i);
            patten.append("}");
            if (i < sql.length - 1) {
                patten.append(" and ");
            }
        }
        patten.append(")");

        //Log.d("pattern:" + patten.toString());
        Object[] objs = sql;
        String result = MessageFormat.format(patten.toString(), objs);
        //Log.d("result:" + result);
        return result;
    }
    
    // 两个字段不相同
    public static String nef(String field1, String field2) {
        return MessageFormat.format("('{alias}'.{0} <> '{alias}'.{1})", field1, field2);
    }
    
    public static String isNull(String field) {
        return MessageFormat.format("('{alias}'.{0} is NULL)", field);
    }
    
    public static String isNotNull(String field) {
        return MessageFormat.format("('{alias}'.{0} is NOT NULL)", field);
    }
    
    public static String isNullOrIsSt(String field, Object st) {
        return or(eq(field, st), isNull(field));
    }
    
    /**
     * 排序规则
     * @param field1　按什么排序
     * @param field2　1代表升序，0代表降序
     * @return
     */
    public static String orderByRule(String field1, int field2) {
        return String.format("{\"field\" :\"%s\" , \"direction\":%s}", field1, field2);
    }

    public static void main(String[] args) {
        System.out.println(CriteriaUtil.ge("money", 20.0f));
        System.out.println(CriteriaUtil.ge("money", 20.0));
        System.out.println(CriteriaUtil.gt("money", 20.0f));
        System.out.println(CriteriaUtil.gt("money", 20.0));
        System.out.println(CriteriaUtil.eq("money", 20.0f));
        System.out.println(CriteriaUtil.eq("money", 20.0));
        System.out.println(CriteriaUtil.eq("name", "hello"));
        System.out.println(CriteriaUtil.eq("tel", "hello?"));
        System.out.println(CriteriaUtil.eq("tel", "?hello?"));
        System.out.println(CriteriaUtil.or("name like 1", "name like 1", "name like 1", "name like 1"));
        System.out.println(CriteriaUtil.between("cdate", new Date(), new Date()));
        System.out.println(CriteriaUtil.and(CriteriaUtil.between("cdate", new Date(), new Date()), CriteriaUtil.eq("customerid", 23)));

    }

}
