package emrys.app.services.reactive;

import emrys.app.commands.RecipeCommand;
import emrys.app.domain.Recipe;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

public interface RecipeReactiveService {

    Flux<Recipe> getRecipes();

    Mono<Recipe> findById(String id);

    Mono<RecipeCommand> findCommandById(String l);

    Mono<RecipeCommand> saveRecipeCommand(RecipeCommand command);

    void deleteById(String idToDelete);
}
