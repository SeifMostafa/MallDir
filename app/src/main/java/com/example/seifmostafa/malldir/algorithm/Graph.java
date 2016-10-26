package com.example.seifmostafa.malldir.algorithm;

import android.graphics.Point;

import com.example.seifmostafa.malldir.algorithm.Edge;
import com.example.seifmostafa.malldir.algorithm.Vertex;
import com.example.seifmostafa.malldir.data_model.MyMapNode;

import java.util.ArrayList;
import java.util.List;

public class Graph {
	private final List<Vertex> vertexes;
	private final List<Edge> edges;

	public Graph() {
		vertexes = new ArrayList<>();
		edges = new ArrayList<>();
	}

	public List<Vertex> getVertexes() {
		return vertexes;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public void addVertex(Vertex node) {
		vertexes.add(node);
	}

	public void addEdge(Vertex from, Vertex to,int distance) {
		int id=edges.size()+1;
		edges.add(new Edge("" + id, from, to, distance));
	}

	public static int calculateDistance(MyMapNode from, MyMapNode to) {
		int distance = 0;
		Point start=new Point((int)from.getx(),(int)from.gety());
		Point end=new Point((int)to.getx(),(int)to.gety());
		distance=(int) Math.sqrt(Math.pow(start.x-end.x,2)+Math.pow(start.y-end.y,2));
		return distance;
	}
}