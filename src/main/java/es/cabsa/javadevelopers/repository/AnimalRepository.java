package es.cabsa.javadevelopers.repository;

import es.cabsa.javadevelopers.data.entity.AnimalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface AnimalRepository extends JpaRepository<AnimalEntity, Integer> {

	ArrayList<AnimalEntity> getAllAnimals();

	AnimalEntity getAnimalEntityByIdFood(Integer id);

	AnimalEntity getAnimalEntityByName(String name);
}
