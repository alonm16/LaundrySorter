package com.example.laundrysorter;

import java.util.Date;

public class History {
    private Date date;
    private int basket1;
    private int basket2;
    private int basket3;

    public History(Date date, int basket1, int basket2, int basket3) {
        this.date = date;
        this.basket1 = basket1;
        this.basket2 = basket2;
        this.basket3 = basket3;
    }

    public History(){}

    public Date getDate() {
        return date;
    }

    public int getBasket1() {
        return basket1;
    }

    public int getBasket2() {
        return basket2;
    }

    public int getBasket3() {
        return basket3;
    }
}
