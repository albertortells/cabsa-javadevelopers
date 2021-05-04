package es.cabsa.javadevelopers.data.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "animal", schema = "jungle")
public class AnimalEntity implements Serializable {

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "legs")
	private Integer legs;

	@Column(name = "food")
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
