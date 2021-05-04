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

		if(animals == null || animals.isEmpty()) {
			response.setStatus(404);
			response.setMessage("Animales no encontrados.");
			return response;
		}

		for(AnimalEntity animal : animals) {
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
		ApiResponse response = new ApiResponse();
		AnimalDto dto = new AnimalDto();
		AnimalEntity animalEntity = new AnimalEntity();
		FoodEntity foodEntity = new FoodEntity();

		boolean nullEntities = false;
		int status = 404;
		String message = "Datos no encontrados.";

		if(animal.isEmpty() && !food.isEmpty()) {
			foodEntity = foodRepository.getFoodEntityByName(food);

			nullEntities = checkNullEntities(foodEntity);
			if(!nullEntities) {
				animalEntity = animalRepository.getAnimalEntityByIdFood(foodEntity.getId());

				nullEntities = checkNullEntities(animalEntity);
				if(!nullEntities) {
					status = 200;
					message = "OK";
					dto = returnDto(animalEntity, foodEntity);
				}
			}


		} else if(!animal.isEmpty() && food.isEmpty()) {
			animalEntity = animalRepository.getAnimalEntityByName(animal);

			nullEntities = checkNullEntities(animalEntity);
			if(!nullEntities) {
				foodEntity = foodRepository.getFoodEntityById(animalEntity.getIdFood());

				nullEntities = checkNullEntities(foodEntity);
				if(!nullEntities) {
					status = 200;
					message = "OK";
					dto = returnDto(animalEntity, foodEntity);
				}
			}
		} else {
			animalEntity = animalRepository.getAnimalEntityByName(animal);
			foodEntity = foodRepository.getFoodEntityByName(food);

			if(!checkNullEntities(animalEntity) || !checkNullEntities(foodEntity)) {
				status = 200;
				message = "OK";
				dto = returnDto(animalEntity, foodEntity);
			}

		}

		response.setStatus(status);
		response.setMessage(message);
		response.setData(dto);

		return response;
	}

	@Override
	public ApiResponse addNewFood(String food) {
		ApiResponse response = new ApiResponse();

		int status = 400;
		String message = "No se ha podido guardar la información.";

		FoodEntity foodEntity = new FoodEntity(food);

		FoodEntity saved = foodRepository.save(foodEntity);

		boolean nullEntities = checkNullEntities(saved);
		if(!nullEntities) {
			status = 200;
			message = "OK";
		}

		response.setStatus(status);
		response.setMessage(message);

		return response;
	}

	private boolean checkNullEntities(Object obj) {
		boolean check = false;

		if(Objects.isNull(obj)){ check = true; }

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
