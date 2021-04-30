package es.cabsa.javadevelopers.services.interfaces;

import es.cabsa.javadevelopers.commons.ApiResponse;

public interface AnimalService {
	ApiResponse getAllAnimalsAndFood();

	ApiResponse findAnimalsByNameOrFood(String animal, String food);

	ApiResponse addNewFood(String food);
}
