# Solución

Explicación de cómo se ha resuelto la prueba técnica descrita en el [README](README.md).

## Proceso de resolución

1. **Implementación de los tres endpoints pedidos** sobre el esqueleto de Spring Boot facilitado, siguiendo la separación por capas ya iniciada (Controller → Service → Repository → Entity) y añadiendo un DTO para no exponer el modelo persistido tal cual.
2. **Revisión de estructura y limpieza del código**, ya que el enunciado indica que se valora más que la funcionalidad en sí:
   - Se simplificó `AnimalServiceImpl.findAnimalsByNameOrFood`, que tenía tres ramas casi idénticas (búsqueda por animal, por comida, o por ambos) con el mismo patrón de "buscar → comprobar null → construir DTO" repetido. Se sustituyó por una única lógica de ~15 líneas basada en dos flags (`hasAnimal`, `hasFood`). Como efecto colateral positivo, se corrigió un `NullPointerException` latente: al buscar por nombre y comida a la vez, si uno de los dos no existía en base de datos, el código original intentaba construir el DTO igualmente y lanzaba una excepción en vez de devolver un `404`.
   - Se añadió `@GeneratedValue(strategy = GenerationType.IDENTITY)` en `AnimalEntity.id`, que no lo tenía pese a que la columna `animal.id` es `auto_increment` en `jungle.sql` y su entidad hermana `FoodEntity` sí generaba el id de esa forma. Se alinean ambas entidades al mismo criterio.
3. **Batería de tests unitarios** sobre `AnimalServiceImpl` una vez cerrada la lógica, para verificar de forma automática que el comportamiento tras la limpieza seguía siendo correcto (ver [Tests](#tests)).

## Tecnologías

- Java 8
- Spring Boot 2.1.2 (Web, Data JPA, Data REST, JDBC)
- MySQL (mysql-connector-java)
- Maven
- Empaquetado `war`

## Estructura del proyecto

```
src/main/java/es/cabsa/javadevelopers
├── Application.java                     # Arranque de la aplicación
├── commons/
│   └── ApiResponse.java                 # Envoltorio común de respuesta (status, message, data)
├── controller/
│   └── JungleController.java            # Endpoints REST
├── data/
│   ├── dto/AnimalDto.java               # DTO expuesto en las respuestas (name, legs, food)
│   └── entity/
│       ├── AnimalEntity.java            # Entidad JPA "animal"
│       └── FoodEntity.java              # Entidad JPA "food"
├── repository/
│   ├── AnimalRepository.java
│   └── FoodRepository.java
└── services/
    ├── interfaces/AnimalService.java
    └── AnimalServiceImpl.java

src/main/resources
├── application.properties               # Configuración de servidor, BBDD y Spring Session
└── jungle.sql                           # Script de creación de esquema y datos de ejemplo
```

La arquitectura sigue una separación clásica por capas: **Controller → Service → Repository → Entity**, con un DTO (`AnimalDto`) para desacoplar la respuesta expuesta al cliente del modelo persistido.

## Modelo de datos

Dos tablas relacionadas mediante clave foránea:

- `jungle.food (id, name)`
- `jungle.animal (id, name, legs, food)` — `food` referencia a `food.id`

El script `src/main/resources/jungle.sql` crea el esquema `jungle` y precarga datos de ejemplo (conejo, oso, ciervo, serpiente, cocodrilo, gallina, araña y sus respectivas comidas).

## Requisitos previos

- JDK 8
- Maven
- MySQL en `localhost:3306`

## Configuración y arranque

1. Ejecutar el script `src/main/resources/jungle.sql` contra el servidor MySQL para crear el esquema `jungle` y los datos iniciales.
2. Revisar las credenciales de conexión en `src/main/resources/application.properties` (por defecto `root` / `root`) y ajustarlas si fuera necesario.
3. Arrancar la aplicación:

   ```bash
   mvn spring-boot:run
   ```

La aplicación levanta en el puerto **8090** y todos los endpoints cuelgan del path base **`/jungle`**.

> Nota: `spring.jpa.hibernate.ddl-auto=create` está activo, por lo que Hibernate recreará las tablas mapeadas al arrancar. El script `jungle.sql` sirve como referencia del esquema y para recargar los datos de ejemplo tras cada arranque si se desea partir de un estado limpio.

## Endpoints

Todas las respuestas siguen el mismo formato (`ApiResponse`):

```json
{
  "status": 200,
  "message": "OK",
  "data": { }
}
```

### Listar todos los animales y su comida

```
GET /jungle/get-all-animals-and-food
```

```bash
curl http://localhost:8090/jungle/get-all-animals-and-food
```

Respuesta de ejemplo:

```json
{
  "status": 200,
  "message": "OK",
  "data": [
    { "name": "Rabbit", "legs": 4, "food": "carrot" },
    { "name": "Bear", "legs": 4, "food": "honey" }
  ]
}
```

### Buscar animales por nombre y/o comida

```
GET /jungle/get-animal-by-name-or-food?animal={nombre}&food={comida}
```

Ambos parámetros son obligatorios en la petición, pero se puede dejar uno de los dos vacío (`animal=` o `food=`) para buscar únicamente por el otro criterio. Si se envían ambos vacíos, la API devuelve un `400`.

```bash
# Buscar por nombre de animal
curl "http://localhost:8090/jungle/get-animal-by-name-or-food?animal=Bear&food="

# Buscar por tipo de comida
curl "http://localhost:8090/jungle/get-animal-by-name-or-food?animal=&food=honey"
```

### Dar de alta un nuevo tipo de comida

```
POST /jungle/save-food?food={nombre}
```

```bash
curl -X POST "http://localhost:8090/jungle/save-food?food=fruit"
```

## Decisiones de diseño

- **`ApiResponse` como envoltorio único**: uniformiza el contrato de salida de todos los endpoints (código de estado propio, mensaje descriptivo y payload), independientemente del resultado.
- **DTO independiente de la entidad**: `AnimalDto` solo expone `name`, `legs` y `food` (el nombre de la comida, no su id), evitando acoplar la respuesta al modelo relacional interno.
- **Capa de servicio con interfaz** (`AnimalService` / `AnimalServiceImpl`): permite testear y sustituir la implementación sin tocar el controlador.
- **Lógica de búsqueda unificada**: en vez de tres bloques duplicados según qué parámetro llega vacío, `findAnimalsByNameOrFood` resuelve primero cada entidad de forma independiente y solo completa la que falta cuando el otro parámetro venía vacío, devolviendo `404` de forma uniforme si al final falta cualquiera de las dos.

## Tests

Una vez cerrada la implementación y la limpieza del código, se añadió `src/test/java/es/cabsa/javadevelopers/services/AnimalServiceImplTest.java` con tests unitarios (JUnit 5 + Mockito) sobre `AnimalServiceImpl`, mockeando `AnimalRepository` y `FoodRepository` para no depender de una base de datos real. El objetivo era comprobar que, tras simplificar la lógica de búsqueda, el comportamiento seguía siendo correcto en todos los casos relevantes:

- `getAllAnimalsAndFood`: devuelve el listado mapeado a `AnimalDto` cuando hay animales, y `404` cuando el repositorio no devuelve ninguno.
- `findAnimalsByNameOrFood`: búsqueda solo por nombre de animal, solo por comida, y por ambos a la vez; `404` cuando el animal buscado no existe; `404` cuando la comida buscada no existe; `404` cuando ambos parámetros llegan vacíos (sin llegar a consultar los repositorios); y un caso específico de regresión que reproduce el `NullPointerException` corregido durante la limpieza, verificando que ahora devuelve `404` en lugar de lanzar una excepción.
- `addNewFood`: alta correcta (`200`) y fallo al guardar (`400`).

Para ejecutarlos:

```bash
mvn test
```

## Posibles mejoras (fuera del alcance de la prueba)

- Validación de parámetros con Bean Validation en lugar de comprobaciones manuales de `isEmpty()`.
- Manejo de excepciones centralizado (`@ControllerAdvice`) en vez de códigos de estado embebidos en `ApiResponse`.
- Uso de `ResponseEntity` para reflejar el código HTTP real en la respuesta, además del campo `status` interno.
- Ampliar la cobertura de tests al controlador (`@WebMvcTest`) y a un test de integración con base de datos embebida.
