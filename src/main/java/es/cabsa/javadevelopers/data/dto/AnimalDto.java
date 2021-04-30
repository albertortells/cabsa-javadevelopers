package es.cabsa.javadevelopers.data.dto;

public class AnimalDto {

	private String name;
	private Integer legs;
	private String food;

	public AnimalDto() { }

	public AnimalDto(String name, Integer legs, String food) {
		this.name = name;
		this.legs = legs;
		this.food = food;
	}

	public String getName() { return name; }

	public void setName(String name) { this.name = name; }

	public Integer getLegs() { return legs; }

	public void setLegs(Integer legs) { this.legs = legs; }

	public String getFood() { return food; }

	public void setFood(String food) { this.food = food; }
}
