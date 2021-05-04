package es.cabsa.javadevelopers.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "food", schema = "jungle")
public class FoodEntity implements Serializable {

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	private Integer id;

	@Column(name = "name", nullable = false, updatable = false)
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
