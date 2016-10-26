package com.example.seifmostafa.malldir.data_model;

import android.widget.ImageView;

/**
 * @author Admin
 *
 */
public class MyMapNode  {
//	private static String filePath = MapEditor.filePath;
	private int id, floorId, typeId;
	private double xx, yy;
	private String name, type, access, join;
	private ImageView icon;

	public MyMapNode(int id, String name, int floorId, String type, int typeId, String access, double x, double y, String join) {
		this.id = id;
		if (name.length() > 1) {
			this.name = name;
		} else {
			this.name = type + "" + id;
		}
		this.floorId = floorId;
		this.type = type;
		this.typeId = typeId;
		this.access = access;
		this.xx = x;
		this.yy = y;
		this.join = join;
		/*if (type.length() > 0) {
			icon = new ImageIcon(this.getClass().getResource("/Icons/" + type+ ".png"));			
			icon.setImage(icon.getImage().getScaledInstance((3*icon.getIconWidth())/4,(3*icon.getIconHeight()/4), Image.SCALE_SMOOTH));
			setIcon(icon);
		}*/
	}

	/**
	 * @param id
	 *            ,x,y,floorId,Join to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	public final void setX(double x) {
		this.xx = x;
	}

	public final void setY(double y) {
		this.yy = y;
	}

	public void setFloorId(int floorId) {
		this.floorId = floorId;
	}

	public final void setJoin(String join) {
		this.join = join;
	}

	/**
	 * @return params
	 */
	public final double getx() {
		return xx;
	}

	public final double gety() {
		return yy;
	}

	public final String getJoin() {
		return join;
	}

	public final int getId() {
		return id;
	}

	public final int getFloorId() {
		return floorId;
	}

	public final int getTypeId() {
		return typeId;
	}

	/**
	 * @return the access
	 */
	public final String getAccess() {
		return access;
	}


	public final String getName() {
		return name;
	}

	public final String getType() {
		return type;
	}

	public ImageView getIcon() {
		return icon;
	}

	/**
	 * @param icon
	 *            the icon to set
	 */
	public void setIcon(ImageView icon) {
		this.icon = icon;
	}
}
