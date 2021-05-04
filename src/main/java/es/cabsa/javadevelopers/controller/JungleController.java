package es.cabsa.javadevelopers.controller;

import es.cabsa.javadevelopers.commons.ApiResponse;
import es.cabsa.javadevelopers.services.interfaces.AnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "${spring.data.rest.base-path}")
public class JungleController {

  @Autowired
  private AnimalService service;

  @GetMapping(path = "/get-all-animals-and-food", produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse getAllAnimalsAndFood() {
    return service.getAllAnimalsAndFood();
  }

  @GetMapping(path = "/get-animal-by-name-or-food", produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse findAnimalsByNameOrFood(@RequestParam String animal, @RequestParam String food) {
    if(animal.isEmpty() && food.isEmpty()) {
      return new ApiResponse(400, "Información errónea.");
    }
    return service.findAnimalsByNameOrFood(animal, food);
  }

  @PostMapping(path = "/save-food", produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse addNewFood(@RequestParam String food) {
    if(food.isEmpty()) {
      return new ApiResponse(400, "Información errónea.");
    }
    return service.addNewFood(food);
  }

}
