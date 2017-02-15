/**
 * 
 */
package com.example.seifmostafa.malldir.model;

/**
 * @author Admin
 *
 */
public class Floor {

	private int id;
	private String name;
	private String image_path;
	private int xFactor, yFactor;

	public Floor() {
		super();
	}

	public Floor(int id, String name, String image_path, int x, int y) {
		super();
		this.id = id;
		this.name = name;
		this.image_path = image_path;
		this.xFactor = x;
		this.yFactor = y;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public int getxFactor() {
		return xFactor;
	}

	public int getyFactor() {
		return yFactor;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage_path() {
		// return "/" + image_path.replace('\\', '/');
		return image_path;
	}

	public void setImage_path(String image_path) {
		this.image_path = image_path;
	}

	public void setxFactor(int xFactor) {
		this.xFactor = xFactor;
	}

	public void setyFactor(int yFactor) {
		this.yFactor = yFactor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName();
	}

}
