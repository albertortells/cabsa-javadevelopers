package es.cabsa.javadevelopers.services;

import es.cabsa.javadevelopers.commons.ApiResponse;
import es.cabsa.javadevelopers.data.dto.AnimalDto;
import es.cabsa.javadevelopers.data.entity.AnimalEntity;
import es.cabsa.javadevelopers.data.entity.FoodEntity;
import es.cabsa.javadevelopers.repository.AnimalRepository;
import es.cabsa.javadevelopers.repository.FoodRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnimalServiceImplTest {

	private AnimalRepository animalRepository;
	private FoodRepository foodRepository;
	private AnimalServiceImpl service;

	@BeforeEach
	void setUp() throws Exception {
		animalRepository = mock(AnimalRepository.class);
		foodRepository = mock(FoodRepository.class);
		service = new AnimalServiceImpl();

		setField(service, "animalRepository", animalRepository);
		setField(service, "foodRepository", foodRepository);
	}

	private static void setField(Object target, String fieldName, Object value) throws Exception {
		Field field = target.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(target, value);
	}

	private static AnimalEntity bear() {
		return new AnimalEntity(1, "Bear", 4, 2);
	}

	private static FoodEntity honey() {
		return new FoodEntity(2, "honey");
	}

	@Test
	void getAllAnimalsAndFood_returnsMappedList_whenAnimalsExist() {
		when(animalRepository.findAll()).thenReturn(new ArrayList<>(Collections.singletonList(bear())));
		when(foodRepository.getFoodEntityById(2)).thenReturn(honey());

		ApiResponse response = service.getAllAnimalsAndFood();

		assertEquals(200, response.getStatus());
		assertEquals("OK", response.getMessage());

		@SuppressWarnings("unchecked")
		ArrayList<AnimalDto> data = (ArrayList<AnimalDto>) response.getData();
		assertEquals(1, data.size());
		assertEquals("Bear", data.get(0).getName());
		assertEquals(Integer.valueOf(4), data.get(0).getLegs());
		assertEquals("honey", data.get(0).getFood());
	}

	@Test
	void getAllAnimalsAndFood_returns404_whenNoAnimals() {
		when(animalRepository.findAll()).thenReturn(new ArrayList<>());

		ApiResponse response = service.getAllAnimalsAndFood();

		assertEquals(404, response.getStatus());
		assertEquals("Animales no encontrados.", response.getMessage());
	}

	@Test
	void findAnimalsByNameOrFood_findsByAnimalName() {
		when(animalRepository.getAnimalEntityByName("Bear")).thenReturn(bear());
		when(foodRepository.getFoodEntityById(2)).thenReturn(honey());

		ApiResponse response = service.findAnimalsByNameOrFood("Bear", "");

		assertEquals(200, response.getStatus());
		AnimalDto dto = (AnimalDto) response.getData();
		assertEquals("Bear", dto.getName());
		assertEquals("honey", dto.getFood());
		verify(foodRepository, never()).getFoodEntityByName(anyString());
	}

	@Test
	void findAnimalsByNameOrFood_findsByFoodName() {
		when(foodRepository.getFoodEntityByName("honey")).thenReturn(honey());
		when(animalRepository.getAnimalEntityByIdFood(2)).thenReturn(bear());

		ApiResponse response = service.findAnimalsByNameOrFood("", "honey");

		assertEquals(200, response.getStatus());
		AnimalDto dto = (AnimalDto) response.getData();
		assertEquals("Bear", dto.getName());
		assertEquals("honey", dto.getFood());
		verify(animalRepository, never()).getAnimalEntityByName(anyString());
	}

	@Test
	void findAnimalsByNameOrFood_findsByBothNameAndFood() {
		when(animalRepository.getAnimalEntityByName("Bear")).thenReturn(bear());
		when(foodRepository.getFoodEntityByName("honey")).thenReturn(honey());

		ApiResponse response = service.findAnimalsByNameOrFood("Bear", "honey");

		assertEquals(200, response.getStatus());
		AnimalDto dto = (AnimalDto) response.getData();
		assertEquals("Bear", dto.getName());
		assertEquals("honey", dto.getFood());
	}

	@Test
	void findAnimalsByNameOrFood_returns404_whenAnimalNameNotFound() {
		when(animalRepository.getAnimalEntityByName("Unicorn")).thenReturn(null);

		ApiResponse response = service.findAnimalsByNameOrFood("Unicorn", "");

		assertEquals(404, response.getStatus());
		assertEquals("Datos no encontrados.", response.getMessage());
		assertNull(response.getData());
	}

	@Test
	void findAnimalsByNameOrFood_returns404_whenFoodNameNotFound() {
		when(foodRepository.getFoodEntityByName("plastic")).thenReturn(null);

		ApiResponse response = service.findAnimalsByNameOrFood("", "plastic");

		assertEquals(404, response.getStatus());
		verify(animalRepository, never()).getAnimalEntityByIdFood(anyInt());
	}

	@Test
	void findAnimalsByNameOrFood_returns404_whenBothParamsEmpty() {
		ApiResponse response = service.findAnimalsByNameOrFood("", "");

		assertEquals(404, response.getStatus());
		verifyZeroInteractions(animalRepository, foodRepository);
	}

	@Test
	void findAnimalsByNameOrFood_returns404_insteadOfThrowing_whenOnlyOneOfBothMatches() {
		when(animalRepository.getAnimalEntityByName("Unicorn")).thenReturn(null);
		when(foodRepository.getFoodEntityByName("honey")).thenReturn(honey());

		ApiResponse response = service.findAnimalsByNameOrFood("Unicorn", "honey");

		assertEquals(404, response.getStatus());
	}

	@Test
	void addNewFood_returns200_whenSaved() {
		FoodEntity saved = new FoodEntity(8, "fruit");
		when(foodRepository.save(any(FoodEntity.class))).thenReturn(saved);

		ApiResponse response = service.addNewFood("fruit");

		assertEquals(200, response.getStatus());
		assertEquals("OK", response.getMessage());
	}

	@Test
	void addNewFood_returns400_whenSaveFails() {
		when(foodRepository.save(any(FoodEntity.class))).thenReturn(null);

		ApiResponse response = service.addNewFood("fruit");

		assertEquals(400, response.getStatus());
		assertEquals("No se ha podido guardar la información.", response.getMessage());
	}
}
