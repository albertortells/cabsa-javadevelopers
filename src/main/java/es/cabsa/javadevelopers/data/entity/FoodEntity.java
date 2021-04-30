package es.cabsa.javadevelopers.data.entity;

import java.io.Serializable;

public class FoodEntity implements Serializable {

	private Integer id;

	private String name;

	public FoodEntity() { }

	public FoodEntity(String name) { this.name = name; }

	public FoodEntity(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer getId() { return id; }

	public void setId(Integer id) { this.id = id; }

	public String getName() { return name; }

	public void setName(String name) { this.name = name; }
}
