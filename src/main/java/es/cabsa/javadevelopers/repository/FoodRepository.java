package es.cabsa.javadevelopers.repository;

import es.cabsa.javadevelopers.data.entity.FoodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<FoodEntity, Integer> {

	FoodEntity getFoodEntityById(Integer id);

	FoodEntity getFoodEntityByName(String name);
}
