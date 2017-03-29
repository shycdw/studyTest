package com.david.study.util;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * 常用工具
 * Created by DavidChen on 2017/3/14.
 */

public class Utils {
    /**
     * 解析省市json数据，通过用户输入的城市名称查找对应的code
     *
     * @return
     */
    public static String getCityCode(String cityName, Context context) {

        try {
            JSONArray array = new JSONArray(initCitysAndPros(context));
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonData = (JSONObject) array.get(i);

                JSONArray cityList = jsonData.getJSONArray("citylist");
                for (int j = 0; j < cityList.length(); j++) {
                    JSONObject cityObj = (JSONObject) cityList.get(j);
                    String city = (String) cityObj.get("cityname");

                    if (cityName.equals(city) || (cityName + "市").equals(city)) {
                        return cityObj
                                .getString("citycode");
                    }
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
        return "";
    }

    /**
     * 获取省市json信息
     *
     * @param context
     * @return
     */
    public static String initCitysAndPros(Context context) {
        try {
            // 读取省市JOSN数据的txt文本
            InputStream is = context.getAssets().open("province_and_city.txt");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String citysAndPros = new String(buffer, "utf-8");
            return citysAndPros;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
