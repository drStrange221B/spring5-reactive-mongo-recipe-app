package emrys.app.repositories.reactive;

import emrys.app.domain.Ingredient;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientReactiveRepository extends ReactiveMongoRepository<Ingredient, String> {
}
