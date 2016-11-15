package com.example.seifmostafa.malldir;

/**
 * Created by seifmostafa on 15/11/16.
 */
public class Nearby {
    private double x,y,MapNode_x,MapNode_y,factor;

    public Nearby(double x, double y, double factor) {
        this.x = x;
        this.y = y;
        this.factor = factor;
    }

    public double getMapNode_x() {
        return MapNode_x;
    }

    public double getMapNode_y() {
        return MapNode_y;
    }
    public void calculate(){
        // solve two equations or whatever to get MapNode ..
        MapNode_x=x;
        MapNode_y=y;
    }
}
