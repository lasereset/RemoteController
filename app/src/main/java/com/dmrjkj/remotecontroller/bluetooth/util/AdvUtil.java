package com.dmrjkj.remotecontroller.bluetooth.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinchen on 19-5-27.
 */

public class AdvUtil {

    public static Adv parse(String paramString1, String paramString2) {
        while ((paramString1.length() > 0) && (!paramString1.substring(0, 2).equals("00"))) {
            String str1 = paramString1.substring(0, 2);
            String str3 = paramString1.substring(2, Integer.parseInt(str1, 16) * 2 + 2);
            String str2 = str3.substring(0, 2);
            str3 = str3.substring(2, str3.length());
            if (paramString2.toUpperCase().equals(str2.toUpperCase())) {
                return new Adv(str1, str2, str3);
            }
            paramString1 = paramString1.substring(Integer.parseInt(str1, 16) * 2 + 2, paramString1.length());
        }
        return null;
    }

    public static List<Adv> parse(String paramString) {
        ArrayList localArrayList = new ArrayList();
        while ((paramString.length() > 0) && (!paramString.substring(0, 2).equals("00"))) {
            String str1 = paramString.substring(0, 2);
            String str2 = paramString.substring(2, Integer.parseInt(str1, 16) * 2 + 2);
            localArrayList.add(new Adv(str1, str2.substring(0, 2), str2.substring(2, str2.length())));
            paramString = paramString.substring(Integer.parseInt(str1, 16) * 2 + 2, paramString.length());
        }
        return localArrayList;
    }

    public static class Adv {
        public String data;
        public String length;
        public String type;

        public Adv() {
        }

        public Adv(String paramString1, String paramString2, String paramString3) {
            this.length = paramString1;
            this.type = paramString2;
            this.data = paramString3;
        }
    }
}
