package com.bcoder.zxingtest;

/**
 * Created by 杜宾 on 2017/12/24.
 */

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class global_variable extends Application{
    public Book test_book;
    public ArrayList<Bookshelf> bookshelfs = new ArrayList<Bookshelf>();
    public int needcheck;
    public int needcheckindex;
    public Book Becheck;
    public String Becheck_bookshelfname;
    public String hostip = "192.168.0.52";
    public String accout;
    public String shareISBN = "";
    public int searchtype = 0;
    public String searchkey ="";
    public String results ="";
    public int backinterface = 0;
    public Book Bemove;
    public static String readParse(String urlPath) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        URL url = new URL(urlPath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        InputStream inStream = conn.getInputStream();
        while ((len = inStream.read(data)) != -1) {
            outStream.write(data, 0, len);
        }
        inStream.close();
        return new String(outStream.toByteArray());//通过out.Stream.toByteArray获取到写的数据
    }
    public static Bitmap getBitmap(String path) throws IOException {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == 200){
            InputStream inputStream = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }
        return null;
    }


}
