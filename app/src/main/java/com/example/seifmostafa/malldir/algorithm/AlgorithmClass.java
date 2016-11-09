package com.example.seifmostafa.malldir.algorithm;

import android.media.Image;
import android.util.Log;

import com.example.seifmostafa.malldir.file_model.XReader;
import com.example.seifmostafa.malldir.data_model.AccessPoint;
import com.example.seifmostafa.malldir.data_model.Floor;
import com.example.seifmostafa.malldir.data_model.MyMapNode;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

public class AlgorithmClass {
	private ArrayList<Floor> floors;
	private ArrayList<Image> floorMaps;
	private ArrayList<Graph> graphs;
	private ArrayList<ArrayList<MyMapNode>> pointlists;
	private MyMapNode source,destination;
	private Dijkstra dijkstra;
	private XReader reader;
	private int directDistance=0,nearestDistance=0,currentFloorId;
	private double startTime,endTime;
	private boolean handicapped;	

	/**
	 * 
	 */
	public AlgorithmClass() {		
		startTime=System.currentTimeMillis();
		currentFloorId=initialize();				
	}	

	/**
	 * @return the currentFloorId
	 */
	public final int getCurrentFloorId() {
		return currentFloorId;
	}

	/**
	 * @return the floors
	 */
	public final Floor getFloor(int i) {
		for (Floor floor : floors) {
			if (floor.getId()==i) {
				return floor;
			}
		}
		return null;
	}

	/**
	 * @return the handicapped
	 */
	public final boolean isHandicapped() {
		return handicapped;
	}

	/**
	 * @param handicapped the handicapped to set
	 */
	public final void setHandicapped(boolean handicapped) {
		this.handicapped = handicapped;
	}

	private int initialize() {
		reader=new XReader();
		handicapped=false;
		floors=reader.getFloors();
		floorMaps=new ArrayList<>();
		pointlists=new ArrayList<>();
		graphs=new ArrayList<>();
		for (Floor floor : floors) {
			//File file = new File("D:/MallEditor/"+floor.getImage_path());
			//Image image = ImageIO.read(file);
			//floorMaps.add(image);
			drawData(floor.getId());
		}
		endTime=System.currentTimeMillis();
		endTime-=startTime;
		//System.out.println("Initialize time = "+(endTime/1000)+" seconds");
		return reader.getCurrentFloorId();
	}

	/**
	 * @return the floorMaps
	 */
	public final ArrayList<Image> getFloorMaps() {
		return floorMaps;
	}

	private void drawData(int floorID) {
		ArrayList<MyMapNode> pointList = reader.getNodesByFloor(floorID);
		Graph graph=new Graph();
		pointlists.add(pointList);

		for (MyMapNode mapNode : pointList) {
			Vertex source=new Vertex(""+mapNode.getId(), mapNode.getName());
			graph.addVertex(source);			
			if (mapNode.getJoin().length() > 0) {
				String[] splat = mapNode.getJoin().split(",");
				for (int i = 0; i < splat.length; i++) {
					int id = Integer.parseInt(splat[i]);					
					MyMapNode mNode = get(id);
					if (mNode==null) {
						Log.e("Algorithm","Error processing Graph at floor ID: "+floorID+", between nodes with ID: "+mapNode.getId()+" & "+id);
						continue;
					}
					Vertex destination=new Vertex(""+id, mNode.getName());
					graph.addEdge(source, destination,Graph.calculateDistance(mapNode, mNode));					
				}
			}
		}
		this.graphs.add(graph);
	}

	private void setFloorId(int i) {		
	//		floor.setId(i);
			dijkstra=new Dijkstra(graphs.get(i-1));
		}

	public LinkedList<Vertex> getPathById(int fromId, int toId) {
		startTime=System.currentTimeMillis();
		this.source=reader.getMapNode(fromId);
		this.destination=reader.getMapNode(toId);
		
		if (source==null||destination==null) {
			Log.e("Algorithm","Source or Destination unknown !");
			System.exit(0);
		}
  		int fromFloorId=source.getFloorId();
		int toFloorId=destination.getFloorId();
		setFloorId(fromFloorId);
		if (fromFloorId==toFloorId) {
			return getPath(source.getId(),destination.getId());
		}
		else {			
			LinkedList<Vertex> direct=getDirectPath(source.getId(), destination.getId());
			nearestDistance=0;			
			LinkedList<Vertex> nearest=getNearestPath(source.getId(), destination.getId());			
			
			if (direct==null) {
//				JOptionPane.showMessageDialog(null, "Distance: "+nearestDistance);
				return nearest;				
			}
			else if(directDistance>nearestDistance) {
//				JOptionPane.showMessageDialog(null, "Distance: "+nearestDistance);
				return nearest;
			}
			else {
//				JOptionPane.showMessageDialog(null, "Distance: "+directDistance);
				return direct;
			}
			
		}		
	}

	public LinkedList<Vertex> getNearestItem(int startID,String type) {				
		MyMapNode start = get(startID);
		int index=pointlists.get(start.getFloorId()-1).indexOf(start);
		Vertex from = graphs.get(start.getFloorId()-1).getVertexes().get(index);
		int floorID=start.getFloorId();
		setFloorId(floorID);		
		dijkstra.execute(from);
		LinkedList<Vertex> p = getItemFromFloor(start,type);
		if (p.size()==0) {
			LinkedList<Vertex> pathUp=null,pathDown=null;			
			for (Vertex access : sortArray(getAccesses(floorID,0))) {					
				MyMapNode node = get(Integer.parseInt(access.getId()));
				AccessPoint accessPoint = reader.getAccess(node.getType(), node.getTypeId());
				
				int up=floorID+1;		int down=floorID-1;		/*int size=reader.getFloors().size();*/					
				if (accessPoint.getRange().contains(""+up)) {					
					setFloorId(up);
					pathUp = getItemFromFloor(get(node.getId()+1), type);					
				}
				if (accessPoint.getRange().contains(""+down)) {
					setFloorId(down);
					pathDown = getItemFromFloor(get(node.getId()-1), type);					
				}
				if (pathUp.size()==0&&pathDown.size()==0) {
					continue;
				}
				setFloorId(floorID);					
				LinkedList<Vertex> finalPath=getPath(Integer.parseInt(from.getId()), Integer.parseInt(access.getId()));
				if (pathUp.size()>pathDown.size()) {					
					finalPath.addAll(pathUp);
					return finalPath;
				}
				else if (pathDown.size()>pathUp.size()) {					
					finalPath.addAll(pathDown);
					return finalPath;
				}
			}
		}
		else {
			return p;
		}
		return null;
	}

	private LinkedList<Vertex> getItemFromFloor(MyMapNode start, String type) {		
		for (MyMapNode myMapNode : pointlists.get(start.getFloorId()-1)) {
			if (myMapNode.getType().equals(type)) {
				return getPath(start.getId(), myMapNode.getId());
			}					
		}
		return new LinkedList<>();
	}

	private ArrayList<Vertex> sortArray(ArrayList<Vertex> accesses) {
		int size=accesses.size();
		ArrayList<Vertex> sorted=new ArrayList<>();
		for (int i=0;i<size;i++) {
			Vertex minimum=dijkstra.getMinimum(accesses);
			accesses.remove(accesses.indexOf(minimum));
			sorted.add(minimum);
		}
		return sorted;
	}

	private ArrayList<Vertex> getAccesses(int i,int direction) {
		ArrayList<Vertex> accesses=new ArrayList<>();
		ArrayList<MyMapNode> floorAccesses=reader.getAllAccess(i);
		for (MyMapNode myMapNode : floorAccesses) {
			if (handicapped&&myMapNode.getType().equals("Escalator")) {
				continue;
			}
			if (direction>0&&myMapNode.getAccess().equals("down")) {
				continue;
			}
			else if (direction<0&&myMapNode.getAccess().equals("up")) {
				continue;
			}
			int index=pointlists.get(i-1).indexOf(get(myMapNode.getId()));
			Vertex access = graphs.get(i-1).getVertexes().get(index);
			accesses.add(access);
		}		
		return accesses;		
	}

	public MyMapNode get(int id) {		
		int floorIndex=reader.getMapNode(id).getFloorId()-1;		
//		pointList=;
//		for (int i = 0; i < pointlists.size(); i++) {
			for (MyMapNode myMapNode : pointlists.get(floorIndex)) {
				if (myMapNode.getId()==id) {
					return myMapNode;
				}
			}
//		}
		return null;
	}
	public MyMapNode get(double x,double y,int floorId) {
		int floorIndex=floorId;
		for (MyMapNode myMapNode : pointlists.get(floorIndex)) {
			if ( myMapNode.getx()==x && myMapNode.gety()==y ) { // maybe not exactly , so we can handle % or error using > <
				return myMapNode;
			}
		}
		return null;
	}
	private LinkedList<Vertex>  getDirectPath(int idFrom,int idTo){
		directDistance=0;
		LinkedList<Vertex> direct = new LinkedList<>();
		int fromFloorId=reader.getMapNode(idFrom).getFloorId();
		int toFloorId=reader.getMapNode(idTo).getFloorId();
		
		MyMapNode mapNode = get(idFrom);
		int index_from=pointlists.get(fromFloorId-1).indexOf(mapNode);
		Vertex from = graphs.get(fromFloorId-1).getVertexes().get(index_from);
					
		dijkstra.execute(from);
		ArrayList<Vertex> accesses = getAccesses(fromFloorId,toFloorId-fromFloorId);		
		accesses=sortArray(accesses);
		for (Vertex vertex : accesses) {
			if (reader.floorInRange(vertex.getName(),toFloorId)) {
				int step=Integer.parseInt(vertex.getId());
				
				LinkedList<Vertex> p;
				p=getPath(idFrom,step);
				direct.addAll(p);
				directDistance+=getDistance(p);
				
				setFloorId(toFloorId);
				step+=(toFloorId-fromFloorId);
				
				p=getPath(step,idTo);
				direct.addAll(p);
				directDistance+=getDistance(p);
				break;
			}
		}
		return direct;
	}

	private LinkedList<Vertex> getNearestPath(int idFrom,int idTo) {
		int fromFloorId=reader.getMapNode(idFrom).getFloorId();		
		int toFloorId=reader.getMapNode(idTo).getFloorId();
		
		setFloorId(fromFloorId);
		MyMapNode fromMapNode = get(idFrom);
		int index_from=pointlists.get(fromFloorId-1).indexOf(fromMapNode);
		Vertex fromVertex = graphs.get(fromFloorId-1).getVertexes().get(index_from);
								
		if (fromFloorId==toFloorId) {
			LinkedList<Vertex> p = getPath(idFrom, idTo);
			nearestDistance+=getDistance(p);
			return p;
		}
		else {
			dijkstra.execute(fromVertex);
			ArrayList<Vertex> nearestAccess = sortArray(getAccesses(fromFloorId,toFloorId-fromFloorId));			
			int j=0;
			int id=Integer.parseInt(nearestAccess.get(j).getId());			
			MyMapNode accessMapNode = reader.getMapNode(id);			
			AccessPoint accessNode=reader.getAccess(accessMapNode.getType(), accessMapNode.getTypeId());			
			int index = accessNode.getRange().lastIndexOf(",");
			int lastRange=Integer.parseInt(accessNode.getRange().substring(index+1));
			while (lastRange==get(Integer.parseInt(fromVertex.getId())).getFloorId()) {
				j++;
				id=Integer.parseInt(nearestAccess.get(j).getId());			
				accessMapNode = reader.getMapNode(id);
				accessNode=reader.getAccess(accessMapNode.getType(), accessMapNode.getTypeId());
				index = accessNode.getRange().lastIndexOf(",");
				lastRange=Integer.parseInt(accessNode.getRange().substring(index+1));
			}
			
			/*if (accessNode.getRange().substring(index+1).contains(""+get(Integer.parseInt(fromVertex.getId())).getFloorId())) {
				
			}*/
			if (accessNode.getRange().contains(""+toFloorId)) {
				LinkedList<Vertex> p = getPath(idFrom, accessMapNode.getId());
				nearestDistance+=getDistance(p);				
				p.addAll(getNearestPath(accessMapNode.getId()+(toFloorId-fromFloorId),idTo));
				return p;			
			}
			else {
				int i=accessNode.getRange().lastIndexOf(",");
				String range=accessNode.getRange().substring(i+1);
				int newFloorId = Integer.parseInt(range);
				LinkedList<Vertex> p=getPath(idFrom, accessMapNode.getId());
				nearestDistance+=getDistance(p);
				p.addAll(getNearestPath(accessMapNode.getId()+(newFloorId-fromFloorId), idTo));
				return p;
			}			
		}
	}
	
	private LinkedList<Vertex> getPath(int idFrom,int idTo) {		
		MyMapNode myMapNode = get(idFrom);
		int index=myMapNode.getFloorId()-1;
		
		int index_from=pointlists.get(index).indexOf(myMapNode);		
		Vertex from = graphs.get(index).getVertexes().get(index_from);
		dijkstra.execute(from);
		
		int index_to=pointlists.get(index).indexOf(get(idTo));
		Vertex to=graphs.get(index).getVertexes().get(index_to);		
		return dijkstra.getPath(to);
		
	}

	private int getDistance(LinkedList<Vertex> path){
		int distance=0;
		MyMapNode from,to;
		Vertex start,end;
		int i;
		
		for (i = 0; i < path.size()-1; i++) {
			start=path.get(i);end=path.get(i+1);
			from=get(Integer.parseInt(start.getId()));
			to=get(Integer.parseInt(end.getId()));
			try {
				distance+=Math.sqrt((int)(Math.pow((to.gety()-from.gety()), 2)+Math.pow((to.getx()-from.getx()), 2)));
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("error at "+i);
			}
		}		
//		System.out.println("finished at "+i);
//		System.out.println("last node was "+path.get(i));
		return distance;
	}

	@Override
	public String toString() {
		//return super.toString();
		return String.valueOf(pointlists.size());
//		for (int i =0;i<pointlists.size();i++)
//		{
//
//		}

	}
}
