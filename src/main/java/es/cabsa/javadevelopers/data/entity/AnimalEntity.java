package es.cabsa.javadevelopers.data.entity;

import java.io.Serializable;

public class AnimalEntity implements Serializable {

	private Integer id;

	private String name;

	private Integer legs;

	private Integer idFood;

	public AnimalEntity() { }

	public AnimalEntity(Integer id, String name, Integer legs, Integer idFood) {
		this.id = id;
		this.name = name;
		this.legs = legs;
		this.idFood = idFood;
	}

	public Integer getId() { return id; }

	public void setId(Integer id) { this.id = id; }

	public String getName() { return name; }

	public void setName(String name) { this.name = name; }

	public Integer getLegs() { return legs; }

	public void setLegs(Integer legs) { this.legs = legs; }

	public Integer getIdFood() { return idFood; }

	public void setIdFood(Integer idFood) { this.idFood = idFood; }
}
