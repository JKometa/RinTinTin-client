package com.example.TinTin;

/**
 * Created with IntelliJ IDEA.
 * User: pawel
 * Date: 19.05.13
 * Time: 21:44
 * To change this template use File | Settings | File Templates.
 */
public class Komentarz {
    int id;
    int user_id;
    int res_id;
    String text;
    String date;

    Komentarz(int user, int res, String text, String date)
    {
        this.date=date;
        this.res_id = res;
        this.user_id = user;
        this.text=text;
    }

    void setId(int id)
    {
        this.id=id;
    }
}
