package es.cabsa.javadevelopers.services;

import es.cabsa.javadevelopers.commons.ApiResponse;
import es.cabsa.javadevelopers.data.dto.AnimalDto;
import es.cabsa.javadevelopers.data.entity.AnimalEntity;
import es.cabsa.javadevelopers.data.entity.FoodEntity;
import es.cabsa.javadevelopers.repository.AnimalRepository;
import es.cabsa.javadevelopers.repository.FoodRepository;
import es.cabsa.javadevelopers.services.interfaces.AnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Service
public class AnimalServiceImpl implements AnimalService {

	@Autowired
	private AnimalRepository animalRepository;

	@Autowired
	private FoodRepository foodRepository;

	@Override
	public ApiResponse getAllAnimalsAndFood() {
		ApiResponse response = new ApiResponse();
		ArrayList<AnimalDto> animalList = new ArrayList<>();

		ArrayList<AnimalEntity> animals = animalRepository.findAll();

		if (animals == null || animals.isEmpty()) {
			response.setStatus(404);
			response.setMessage("Animales no encontrados.");
			return response;
		}

		for (AnimalEntity animal : animals) {
			FoodEntity food = foodRepository.getFoodEntityById(animal.getIdFood());

			AnimalDto dto = new AnimalDto();
			dto.setName(animal.getName());
			dto.setLegs(animal.getLegs());
			dto.setFood(food.getName());

			animalList.add(dto);
		}

		response.setStatus(200);
		response.setMessage("OK");
		response.setData(animalList);

		return response;
	}

	@Override
	public ApiResponse findAnimalsByNameOrFood(String animal, String food) {
		boolean hasAnimal = !animal.isEmpty();
		boolean hasFood = !food.isEmpty();

		AnimalEntity animalEntity = hasAnimal ? animalRepository.getAnimalEntityByName(animal) : null;
		FoodEntity foodEntity = hasFood ? foodRepository.getFoodEntityByName(food) : null;

		if (!hasAnimal && foodEntity != null) {
			animalEntity = animalRepository.getAnimalEntityByIdFood(foodEntity.getId());
		} else if (!hasFood && animalEntity != null) {
			foodEntity = foodRepository.getFoodEntityById(animalEntity.getIdFood());
		}

		if (animalEntity == null || foodEntity == null) {
			return new ApiResponse(404, "Datos no encontrados.");
		}

		return new ApiResponse(200, "OK", returnDto(animalEntity, foodEntity));
	}

	@Override
	public ApiResponse addNewFood(String food) {
		ApiResponse response = new ApiResponse();

		int status = 400;
		String message = "No se ha podido guardar la información.";

		FoodEntity foodEntity = new FoodEntity();
		foodEntity.setName(food);

		FoodEntity saved = foodRepository.save(foodEntity);

		boolean nullEntities = checkNullEntities(saved);
		if (!nullEntities) {
			status = 200;
			message = "OK";
		}

		response.setStatus(status);
		response.setMessage(message);

		return response;
	}

	private boolean checkNullEntities(Object obj) {
		boolean check = false;

		if (Objects.isNull(obj)) {
			check = true;
		}

		return check;
	}

	private AnimalDto returnDto(AnimalEntity animal, FoodEntity food) {
		AnimalDto dto = new AnimalDto();

		dto.setName(animal.getName());
		dto.setLegs(animal.getLegs());
		dto.setFood(food.getName());

		return dto;
	}
}
