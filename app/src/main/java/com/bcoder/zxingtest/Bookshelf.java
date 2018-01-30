package com.bcoder.zxingtest;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.R.attr.id;

/**
 * Created by 杜宾 on 2017/12/24.
 */

public class Bookshelf {
    private int bookshelfid = 0;
    private int line;
    private int column;
    private int maxline = 10;
    private int maxcolumn;
    private String name = "";
    private Book book[][];
    public Bookshelf(int id,int maxcolumn,int maxline){
        this.maxcolumn = maxcolumn;
        this.maxline = maxline;
        this.bookshelfid = id;
        book = new Book[maxline][maxcolumn];
        for(int i=0;i<maxline;i++){
            for(int j=0;j<maxcolumn;j++){
                book[i][j] = new Book();
            }
        }
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }
    public void getid(String hostip,String account){
        String temp="";
        try {
            temp = readParse("http://" + hostip + ":3000/get/bookshelf/id/"+name+"/"+account+"/");
            JSONObject jsondata = new JSONObject(temp);
            bookshelfid =Integer.parseInt(jsondata.getString("id"));
        }catch (Exception e){}
    }

    public boolean ifexistence(Book b){
        for(int i=0;i<maxline;i++){
            for(int j=0;j<maxcolumn;j++){
                if(book[i][j].checkbook()) {
                    if (book[i][j].ifsame(b)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Book find(int id){
        for(int i=0;i<maxline;i++){
            for(int j=0;j<maxcolumn;j++){
                if(book[i][j].checkbook()) {
                    if (book[i][j].getId()== id) {
                        return book[i][j];
                    }
                }
            }
        }
        return null;
    }

    public int getBookshelfid() {
        return bookshelfid;
    }

    public Bookshelf(int id){
        bookshelfid = id;
        this.maxcolumn = 5;
        this.maxline = 5;
        book = new Book[maxline][maxcolumn];
        for(int i=0;i<maxline;i++){
            for(int j=0;j<maxcolumn;j++){
                book[i][j] = new Book();
            }
        }
    }

    public  void setMaxline(int maxline){
        this.maxline = maxline;
    }

    public  void setMaxcolumn(int maxcolumn){
        this.maxcolumn = maxcolumn;
    }
    public boolean addbook(final Book newbook , final String hostip , final String account){
        for(int i=0;i<maxline;i++){
            for(int j=0;j<maxcolumn;j++){
                if(!book[i][j].checkbook()){
                    book[i][j] = new Book(newbook.getBook_json());
                    book[i][j].setCover(newbook.getCover());
                    book[i][j].setBookshelfline(i);
                    book[i][j].setBookshelfcolumn(j);
                    book[i][j].setBookshelfid(bookshelfid);
                    book[i][j].setId(newbook.getId());
                    line = i;
                    column = j;
                    new Thread() {
                        public void run() {
                            try {
                                String temp = readParse("http://"+hostip+":3000/upload/book/"+newbook.getId()+"/"+bookshelfid+"/"+ account+"/"+ column +"/"+line);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        ;
                    }.start();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean addbook(final Book newbook ,final int line,final String hostip , final String account){
        for(int j=0;j<maxcolumn;j++){
            if(!book[line][j].checkbook()){
                book[line][j] = new Book(newbook.getBook_json());
                book[line][j].setCover(newbook.getCover());
                book[line][j].setBookshelfline(line);
                book[line][j].setBookshelfcolumn(j);
                book[line][j].setBookshelfid(bookshelfid);
                book[line][j].setId(newbook.getId());
                column = j;
                new Thread() {
                    public void run() {
                        try {
                            String temp = readParse("http://"+hostip+":3000/upload/book/"+newbook.getId()+"/"+bookshelfid+"/"+ account+"/"+ column +"/"+line);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    ;
                }.start();
                return true;
            }
        }
        return false;
    }

    public boolean addbook(final Book newbook,int line,int column){
        if(line<maxline && column <maxcolumn){
            book[line][column] = newbook;
            book[line][column].setBookshelfline(line);
            book[line][column].setBookshelfcolumn(column);
            book[line][column].setBookshelfid(bookshelfid);
            return true;
        }

        return false;
    }

    public boolean deletebook(String hostip,int line,int column){
        String temp="";
        try {
            temp = readParse("http://" + hostip + ":3000/delete/book/" + book[line][column].getId() + "/" + bookshelfid+"/");
        }catch (Exception e){

        }
        if(temp.equals("true")) {
            book[line][column] = new Book();
            return true;
        }else return false;
    }

    public int getLine(){
        return maxline;
    }

    public int getColumn(){
        return maxcolumn;
    }

    public Book getbook(int line,int column){
        return book[line][column];
    }

    public boolean update(String hostip,String account){
        String temp="";
        try {
            temp= readParse("http://" + hostip + ":3000/upload/bookshelf/"+bookshelfid+"/"+name+"/"+maxline+ "/" + maxcolumn + "/"+account+"/" );
        }catch (Exception e){}
        if(temp.equals("true")){
            return true;
        }else
            return false;

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
