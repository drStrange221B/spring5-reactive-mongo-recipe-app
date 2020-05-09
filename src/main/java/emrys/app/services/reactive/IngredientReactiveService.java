package emrys.app.services.reactive;

import emrys.app.commands.IngredientCommand;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IngredientReactiveService {

    Mono<IngredientCommand> findByRecipeIdAndId(String recipeId, String id);

    Flux<IngredientCommand> listsOfIngredientCommnad();

     Mono<IngredientCommand> saveIngerdientCommand(IngredientCommand command);


    Mono<Void> deleteById(String recipeId, String id);
}
