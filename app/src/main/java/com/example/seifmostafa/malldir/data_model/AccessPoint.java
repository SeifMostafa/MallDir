package com.example.seifmostafa.malldir.data_model;

public class AccessPoint {
	int id;
	String name, range, type;

	public AccessPoint(int id, String name, String range, String type) {
		super();
		this.id = id;
		this.name = name;
		this.range = range;
		this.type = type;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the range
	 */
	public String getRange() {
		return range;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
}
