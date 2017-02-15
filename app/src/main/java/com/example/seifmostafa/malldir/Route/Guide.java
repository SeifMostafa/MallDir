package com.example.seifmostafa.malldir.Route;

import android.content.Context;
import android.util.Log;


import com.example.seifmostafa.malldir.MainActivity;
import com.example.seifmostafa.malldir.algorithm.AlgorithmClass;
import com.example.seifmostafa.malldir.algorithm.Vertex;
import com.example.seifmostafa.malldir.data.XReader;
import com.example.seifmostafa.malldir.model.MyMapNode;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by seifmostafa on 05/09/16.
 */
enum DIRECTION{RIGHT ,LEFT,NONE};
enum DIRECTION_ON_CHANGE{X,Y};

public class Guide {
    ArrayList<MyMapNode> path;
    POINT current;
    MyMapNode Start;
    MyMapNode End;
    Context context;
    AlgorithmClass algorithmClass ;


    public Guide(POINT Start,MyMapNode end, Context c,AlgorithmClass AC) {
        this.context=c;
        this.current=Start;
        this.End=end;
        this.algorithmClass=AC;
        path = new ArrayList<>();
    }
    public Guide(MyMapNode start,MyMapNode end, Context c,AlgorithmClass AC) {
        this.context=c;
        this.Start=start;
        this.End=end;
        this.algorithmClass=AC;
        path = new ArrayList<>();
    }
    private MyMapNode TranslatePointToMyMapNode(int floorId) {
        double x=this.current.getLon();
        double y=this.current.getLat();
        // should have start , end points
        MainActivity.translator.calcXY(x,y);
        Log.i("translator",""+MainActivity.translator.X+","+MainActivity.translator.Y);
        return Nearby(new XReader().getNodesByFloor(floorId),MainActivity.translator.X,MainActivity.translator.Y);
        //return null;
    }
    public  ArrayList<MyMapNode>getPath(int floorID,boolean usingGPS) {
        MyMapNode beginning=null;
        if(usingGPS){
            try {
                beginning = TranslatePointToMyMapNode(floorID);
                Log.i("getPath","beginning"+beginning.getId());

            }catch (Exception e){
                Log.i("getPath","beginning"+"NullEx");
            }
            if(beginning==null) beginning = new XReader().getMapNode(1);
        }else{
            beginning=this.Start;
        }
        LinkedList<Vertex> p = algorithmClass.getPathById(beginning.getId(),End.getId());
        for(Vertex vertex:p)
        {
            this.path.add(algorithmClass.get(Integer.parseInt(vertex.getId())));
        }
         return path;
    }

    public MyMapNode Nearby(ArrayList<MyMapNode>floorNodes,double X_from_translate_gpsValue,double Y_from_translate_gpsValue) {
        MyMapNode NearbyNode=null ;
        double max_x=500,max_y=500;
        for(MyMapNode myMapNode:floorNodes)
        {
            if(Math.abs(myMapNode.getx()-X_from_translate_gpsValue)<=max_x &&
                    Math.abs(myMapNode.gety()-Y_from_translate_gpsValue)<=max_y){
                NearbyNode = myMapNode;
                max_x = Math.abs(myMapNode.getx()-X_from_translate_gpsValue);
                max_y = Math.abs(myMapNode.gety()-Y_from_translate_gpsValue);
            }
        }
        return NearbyNode;
    }
    //unused
    public DIRECTION ActionOnRelativePoints(MyMapNode prev,MyMapNode p1,MyMapNode p2,DIRECTION_ON_CHANGE direction_on_change) {
        if(direction_on_change == DIRECTION_ON_CHANGE.X)
        {
            if(p1.gety()!=p2.gety())
            {
                if(p1.gety()<p2.gety())
                {
                    if(prev.getx()>p1.getx())return DIRECTION.RIGHT;
                    else return DIRECTION.LEFT;
                }
                else
                {
                    if(prev.getx()>p1.getx())return DIRECTION.LEFT;
                    else return DIRECTION.RIGHT;
                }

            }else return DIRECTION.NONE;
        }else {
            if(p1.getx()!=p2.getx())
            {
                if(p1.getx()<p2.getx())
                {
                    if(prev.gety()>p1.gety())return DIRECTION.RIGHT;
                    else return DIRECTION.LEFT;
                }
                else
                {
                    if(prev.gety()>p1.gety())return DIRECTION.LEFT;
                    else return DIRECTION.RIGHT;
                }

            }else return DIRECTION.NONE;
        }
    }


}
