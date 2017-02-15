package com.example.seifmostafa.malldir.Route;

/**
 * Created by azizax on 14/02/17.
 */

public class Translator {
    double x1,y1,x2,y2; // use x1,y1 as origin`
    double X1,Y1,X2,Y2; // use X1,Y1 as origin
    public double factor_x,factor_y; // 1 in mall scale
    public  double X=0,Y=0;


    public Translator(double x1, double y1, double x2, double y2, double x12, double y12, double x22, double y22) {
        super();
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        X1 = x12;
        Y1 = y12;
        X2 = x22;
        Y2 = y22;
        calcFactor();
    }
    private void calcFactor(){
        double diff_x=0,diff_y=0,diff_X=0,diff_Y=0;
        diff_x=Math.abs(x1-x2);
        diff_y=Math.abs(y1-y2);
        diff_X=Math.abs(X1-X2);
        diff_Y=Math.abs(Y1-Y2);
        this.factor_x=(double)(diff_X / diff_x);
        this.factor_y=(double)(diff_Y / diff_y);
    }
    public void calcXY(double x,double y){
        double diff_x=Math.abs(x1-x);
        double diff_y=Math.abs(y1-y);

        double diff_X=factor_x*diff_x;
        double diff_Y=factor_y*diff_y;
        // switch case for x,y > <
        if (x1 - x >= 0) {
            X=Math.abs(X1-diff_X);
            Y=Math.abs(Y1-diff_Y);
        }else {
            X=Math.abs(X1+diff_X);
            Y=Math.abs(Y1+diff_Y);
        }

    }
}