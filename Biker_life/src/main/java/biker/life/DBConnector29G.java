package biker.life;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DBConnector29G
{
    //--------------------------------------------------------
    private static String postUrl;
    private static String myResponse;
    static String result = null;
    private static OkHttpClient client = new OkHttpClient(); //client 端
    //---------------------------------------------------------
    // -------HOSTING-------

    static String connect_ip = "https://bklifetw.com/T29/android_connect_db_all_login.php";
    public static int httpstate;

    public static String executeQuery(ArrayList<String> query_string) {
//        OkHttpClient client = new OkHttpClient();
        postUrl=connect_ip ;
        //--------------
        String query_0 = query_string.get(0);//抓第一個值
        FormBody body = new FormBody.Builder() //FORMBODY 我包進PHP問號後面的字
                .add("selefunc_string","query")
                .add("query_string", query_0)
                .build();
//--------------固定寫法
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)//post 會回值
                .build();
        // 使用httpResponse的方法取得http 狀態碼設定給httpstate變數
        httpstate = 0;   //設 httpcode初始值

        try (Response response = client.newCall(request).execute()) {
            httpstate=response.code();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    //-------------新增
    public static String executeInsert(ArrayList<String> query_string) {
//        OkHttpClient client = new OkHttpClient();
        postUrl=connect_ip ;
        //--------------
//        String query_0 = query_string.get(0);//tname取陣列
//        String query_1 = query_string.get(1);//tgrp取陣列
//        String query_2 = query_string.get(2);//taddr取陣列
//
//
//
//        //body指php問號後面的參數 https....//////////php?(body)
//        FormBody body = new FormBody.Builder()
//                .add("selefunc_string","insert")//等於php的值   放insert
//                .add("name", query_0)
//                .add("grp", query_1)
//                .add("address", query_2)
//                .build();
        String query_0 = query_string.get(0);
        String query_1 = query_string.get(1);
        String query_2 = query_string.get(2);
        String query_3 = query_string.get(3);
        String query_4 = query_string.get(4);


        //body指php問號後面的參數 https....//////////php?(body)
        FormBody body = new FormBody.Builder()
                .add("selefunc_string","insert")//等於php的值   放insert
                .add("g_id", query_0)
                .add("g_name", query_1)
                .add("g_email", query_2)
                .add("g_img", query_3)
                .add("g_permission", query_4)
                .build();
//--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        // 使用httpResponse的方法取得http 狀態碼設定給httpstate變數
        httpstate = 0;   //設 httpcode初始值
        try (Response response = client.newCall(request).execute()) { //=執行HTTP://.........all.php.... 命令
            httpstate=response.code();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }

    //---更新資料--------------------------------------------------------------
    public static String executeUpdate(ArrayList<String> query_string) {
//        OkHttpClient client = new OkHttpClient();
        postUrl=connect_ip ;
        //--------------
        String id =query_string.get(0);
        String query_0 = query_string.get(1);
        String query_1 = query_string.get(2);
        String query_2 = query_string.get(3);
        String query_3 = query_string.get(4);

        FormBody body = new FormBody.Builder()
                .add("selefunc_string","update")
                .add("id", id)
                .add("g_id", query_0)
                .add("g_name", query_1)
                .add("g_email", query_2)
                .add("g_img", query_3)
                .build();
//--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        // 使用httpResponse的方法取得http 狀態碼設定給httpstate變數
        httpstate = 0;   //設 httpcode初始值
        try (Response response = client.newCall(request).execute()) {
            httpstate=response.code();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static String Update_A106(String u_id,String A106) {
//        OkHttpClient client = new OkHttpClient();
        postUrl=connect_ip ;
        //--------------
        String id ="71";


        FormBody body = new FormBody.Builder()
                .add("selefunc_string","update_A106")
                .add("id", u_id)
                .add("A106", A106)
                .build();
//--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        // 使用httpResponse的方法取得http 狀態碼設定給httpstate變數
        httpstate = 0;   //設 httpcode初始值
        try (Response response = client.newCall(request).execute()) {
            httpstate=response.code();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    //---刪除資料--------------------------------------------------------------
    public static String executeDelet(ArrayList<String> query_string) {
//        OkHttpClient client = new OkHttpClient();
        postUrl=connect_ip ;
        //--------------
        String query_0 = query_string.get(0);

        FormBody body = new FormBody.Builder()
                .add("selefunc_string","delete")
                .add("id", query_0)
                .build();
//--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        // 使用httpResponse的方法取得http 狀態碼設定給httpstate變數
        httpstate = 0;   //設 httpcode初始值
        try (Response response = client.newCall(request).execute()) {
            httpstate=response.code();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
