package com.bcoder.zxingtest;

import android.graphics.Bitmap;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 杜宾 on 2017/12/24.
 */

public class Book {
    private JSONObject book_json;
    private Bitmap cover;
    private int id;
    private boolean ifuse =false;
    private int bookshelfid=999999 ;
    private int bookshelfline =999999;
    private int bookshelfcolumn =999999;
    private String bookshelfname = "";
    public Book(String jsonstring){
        try {
            book_json = new JSONObject(jsonstring);
        }catch (Exception e){
            e.printStackTrace();
        }
        ifuse = true;
    }
    public Book(JSONObject jsonstring){
        try {
            book_json = jsonstring;
        }catch (Exception e){
            e.printStackTrace();
        }
        ifuse = true;
    }

    public String getBookshelfname() {
        return bookshelfname;
    }

    public void setBookshelfname(String bookshelfname) {
        this.bookshelfname = bookshelfname;
    }

    public JSONObject getBook_json() {
        return book_json;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void saveid(final String hostip){
        new Thread() {
            public void run() {
                try {
                    String isbn = book_json.getString("isbn13");
                    String temp = readParse("http://"+hostip+":3000/get/bookid/"+isbn);
                    JSONObject jsondata = new JSONObject(temp);
                    id = Integer.parseInt(jsondata.getString("id"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
        }.start();
    }

    public boolean ifsame(Book book){
        if(book.gettitle().equals(this.gettitle()) && book.get("pubdate").equals(this.get("pubdate")))
            return true;
        else
            return false;
    }

    public int getBookshelfcolumn() {
        return bookshelfcolumn;
    }

    public int getBookshelfid() {
        return bookshelfid;
    }

    public int getBookshelfline() {
        return bookshelfline;
    }

    public void setBookshelfcolumn(int bookshelfcolumn) {
        this.bookshelfcolumn = bookshelfcolumn;
    }

    public void setBookshelfid(int bookshelfid) {
        this.bookshelfid = bookshelfid;
    }

    public void setBookshelfline(int bookshelfline) {
        this.bookshelfline = bookshelfline;
    }

    public boolean checkbook(){
        return ifuse;
    }

    public Book(){
        book_json = null;
    }

    public void Setbookjson(String jsonstring){
        try {
            book_json = new JSONObject(jsonstring);
        }catch (Exception e){
            e.printStackTrace();
        }
        ifuse = true;
    }

    public String getimageurl(String hostip){
        String imageurl ="";
        String path="";
        try {
            imageurl = book_json.getString("isbn13");
            path = "http://"+hostip+":3000/download/image/"+imageurl;
        }catch (Exception e){
            e.printStackTrace();
        }
        return path;
    }

    public String gettitle(){
        String title ="";
        try {
            title = book_json.getString("title");
        }catch (Exception e){
            e.printStackTrace();
        }
        return title;
    }

    public String get(String need){
        String title ="";
        try {
            title = book_json.getString(need);
        }catch (Exception e){
            e.printStackTrace();
        }
        return title;
    }
    public void setCover(Bitmap c){
        cover =c;
    }
    public Bitmap getCover(){
        return cover;
    }

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
}
