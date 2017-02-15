package com.example.seifmostafa.malldir.Route;

import android.util.Log;

/**
 * Created by seifmostafa on 04/09/16.
 */
public class POINT{
    Double lon,lat,alt;
    public POINT(double l1, double l2, double a)
    {
        this.lon =l1;
        this.lat=l2;
        this.alt = a;
    }
    @Override
    public String toString() {
        return "lon: "+ String.valueOf(lat)+",lat: "+String.valueOf(lon)+",alt: "+String.valueOf(alt);
    }
    public static POINT getDiff(POINT p1 ,POINT p2)
    {
        // suppose that p1 and p2 have same alt
        return new POINT(Math.abs(p1.lat - p2.lat), Math.abs(p1.lon - p2.lon),p1.alt);

    }

    public Double getLon() {
        double d=lon;
        return d;
    }

    public Double getLat() {
        double d=lat;
        return d;
    }
    public Double getAlt()
    {
        double d=alt;
        return d;
    }
}