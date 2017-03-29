package com.david.study.util;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * 城市相关工具类
 * Created by DavidChen on 2016/2/3.
 */
public class CityUtils {

    public final static String SPECIAL = "北京,上海,天津,重庆,香港,澳门";

    /**
     * 把全国的省市区的信息以json的格式保存，解析完成后赋值为null
     */
    private static JSONObject mJsonObj;

    /**
     * 所有省
     */
    private static String[] mProvincesData;

    /**
     * 从assert文件夹中读取省市区的json文件，然后转化为json对象
     */
    private static void initJsonData(Context context)
    {
        try
        {
            StringBuffer sb = new StringBuffer();
            InputStream is = context.getAssets().open("city.json");
            int len = -1;
            byte[] buf = new byte[1024];
            while ((len = is.read(buf)) != -1)
            {
                sb.append(new String(buf, 0, len, "utf-8"));
            }
            is.close();
            mJsonObj = new JSONObject(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有省份的名字
     * @param context context
     * @return 所有省份的名字
     */
    public static String[] getProvinces(Context context) {
        initJsonData(context);
        try {
            JSONArray jsonArray = mJsonObj.getJSONArray("citylist");
            mProvincesData = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonP = jsonArray.getJSONObject(i);// 每个省的json对象
                String province = jsonP.getString("p");// 省名字
                mProvincesData[i] = province;
            }
            mJsonObj = null;
            return mProvincesData;
        } catch (JSONException e) {
            e.printStackTrace();
            mJsonObj = null;
            return null;
        }
    }

    /**
     * 获取指定省份的所有市
     * @param context context
     * @param province 省份名
     * @return 所有市
     */
    public static String[] getCities(Context context, String province) {
        initJsonData(context);
        try {
            JSONArray jsonArray = mJsonObj.getJSONArray("citylist");
            mProvincesData = new String[jsonArray.length()];
            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonP = jsonArray.getJSONObject(i);// 每个省的json对象
                String tmpProvince = jsonP.getString("p");// 省名字
                if (tmpProvince.equals(province)) {
                    JSONArray jsonCs;
                    try
                    {
                        /**
                         * Throws JSONException if the mapping doesn't exist or is
                         * not a JSONArray.
                         */
                        jsonCs = jsonP.getJSONArray("c");
                    } catch (Exception e1)
                    {
                        continue;
                    }
                    String[] mCitiesData = new String[jsonCs.length()];
                    for (int j = 0; j < jsonCs.length(); j++)
                    {
                        JSONObject jsonCity = jsonCs.getJSONObject(j);
                        String city = jsonCity.getString("n");// 市名字
                        mCitiesData[j] = city;
                    }
                    mJsonObj = null;
                    return mCitiesData;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mJsonObj = null;
        return null;
    }

    /**
     * 根据指定的省、市名称返回该市所有区县的名称
     * @param context context
     * @param province 省份
     * @param city 市
     * @return 所有区县
     */
    public static String[] getAreas(Context context, String province, String city) {
        initJsonData(context);
        try
        {
            JSONArray jsonArray = mJsonObj.getJSONArray("citylist");
            mProvincesData = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonP = jsonArray.getJSONObject(i);// 每个省的json对象
                String tmpProvince = jsonP.getString("p");// 省名字

                mProvincesData[i] = province;
                if (tmpProvince.equals(province)) {

                    JSONArray jsonCs;
                    try {
                        /**
                         * Throws JSONException if the mapping doesn't exist or is
                         * not a JSONArray.
                         */
                        jsonCs = jsonP.getJSONArray("c");
                    } catch (Exception e1) {
                        continue;
                    }
                    for (int j = 0; j < jsonCs.length(); j++) {
                        JSONObject jsonCity = jsonCs.getJSONObject(j);
                        String tmpCity = jsonCity.getString("n");// 市名字
                        if (tmpCity.equals(city)) {
                            JSONArray jsonAreas;
                            try {
                                /**
                                 * Throws JSONException if the mapping doesn't exist or
                                 * is not a JSONArray.
                                 */
                                jsonAreas = jsonCity.getJSONArray("a");
                            } catch (Exception e) {
                                continue;
                            }

                            String[] mAreasData = new String[jsonAreas.length()];// 当前市的所有区
                            for (int k = 0; k < jsonAreas.length(); k++) {
                                String area = jsonAreas.getJSONObject(k).getString("s");// 区域的名称
                                mAreasData[k] = area;
                            }
                            mJsonObj = null;
                            return mAreasData;
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mJsonObj = null;
        return null;
    }


    /**
     * 解析整个Json对象，完成后释放Json对象的内存
     */
    public static String[] initData(Context context, Map<String, String[]> mCitiesDataMap)
    {
        initJsonData(context);
        try
        {
            JSONArray jsonArray = mJsonObj.getJSONArray("citylist");
            mProvincesData = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonP = jsonArray.getJSONObject(i);// 每个省的json对象
                String province = jsonP.getString("p");// 省名字

                mProvincesData[i] = province;

                JSONArray jsonCs;
                try
                {
                    /**
                     * Throws JSONException if the mapping doesn't exist or is
                     * not a JSONArray.
                     */
                    jsonCs = jsonP.getJSONArray("c");
                } catch (Exception e1)
                {
                    continue;
                }
                String[] mCitiesData = new String[jsonCs.length()];
                for (int j = 0; j < jsonCs.length(); j++)
                {
                    JSONObject jsonCity = jsonCs.getJSONObject(j);
                    String city = jsonCity.getString("n");// 市名字
                    mCitiesData[j] = city;
                }

                mCitiesDataMap.put(province, mCitiesData);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mJsonObj = null;
        return mProvincesData;
    }

    /**
     * 解析整个Json对象，完成后释放Json对象的内存
     */
    private static String[] initData(Context context, Map<String, String[]> mCitiesDataMap, Map<String, String[]> mAreasDataMap)
    {
        initJsonData(context);
        try
        {
            JSONArray jsonArray = mJsonObj.getJSONArray("citylist");
            mProvincesData = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonP = jsonArray.getJSONObject(i);// 每个省的json对象
                String province = jsonP.getString("p");// 省名字

                mProvincesData[i] = province;

                JSONArray jsonCs;
                try
                {
                    /**
                     * Throws JSONException if the mapping doesn't exist or is
                     * not a JSONArray.
                     */
                    jsonCs = jsonP.getJSONArray("c");
                } catch (Exception e1)
                {
                    continue;
                }
                String[] mCitiesData = new String[jsonCs.length()];
                for (int j = 0; j < jsonCs.length(); j++)
                {
                    JSONObject jsonCity = jsonCs.getJSONObject(j);
                    String city = jsonCity.getString("n");// 市名字
                    mCitiesData[j] = city;
                    JSONArray jsonAreas;
                    try
                    {
                        /**
                         * Throws JSONException if the mapping doesn't exist or
                         * is not a JSONArray.
                         */
                        jsonAreas = jsonCity.getJSONArray("a");
                    } catch (Exception e)
                    {
                        continue;
                    }

                    String[] mAreasData = new String[jsonAreas.length()];// 当前市的所有区
                    for (int k = 0; k < jsonAreas.length(); k++)
                    {
                        String area = jsonAreas.getJSONObject(k).getString("s");// 区域的名称
                        mAreasData[k] = area;
                    }
                    mAreasDataMap.put(city, mAreasData);
                }

                mCitiesDataMap.put(province, mCitiesData);
            }

        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        mJsonObj = null;
        return mProvincesData;
    }
}