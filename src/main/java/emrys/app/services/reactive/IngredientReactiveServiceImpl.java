package emrys.app.services.reactive;

import emrys.app.commands.IngredientCommand;
import emrys.app.commands.RecipeCommand;
import emrys.app.converters.IngredientCommandToIngredient;
import emrys.app.converters.IngredientToIngredientCommand;
import emrys.app.domain.Ingredient;
import emrys.app.domain.Recipe;
import emrys.app.repositories.reactive.RecipeReactiveRepository;
import emrys.app.repositories.reactive.UnitOfMeasureReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@Slf4j
public class IngredientReactiveServiceImpl implements IngredientReactiveService {

    @Autowired
    private RecipeReactiveService recipeService;

    @Autowired
    private RecipeReactiveRepository recipeRepository;

    @Autowired
    private IngredientReactiveService ingredientService;

    @Autowired
    private UnitOfMeasureReactiveRepository unitOfMeasureRepository;

    @Autowired
    private IngredientCommandToIngredient ingredientCommandToIngredient;

    @Autowired
    private IngredientToIngredientCommand ingredientToIngredientCommand;

    @Override
    public Mono<IngredientCommand> findByRecipeIdAndId(String recipeId, String id) {

//        Mono<RecipeCommand> recipe = recipeService.findCommandById(recipeId);
//        RecipeCommand recipeCommand =   recipe.block();
//        recipeCommand.getIngredients().forEach(
//                i-> System.out.println(i.getId() +"=" + id + "       value = "+ i.getId().equalsIgnoreCase(id))
//        );
//        Optional<IngredientCommand> ingredientCommand= (recipe.block().getIngredients()).stream().filter(i->i.getId().equalsIgnoreCase(id)).findFirst();
//
//
//        System.out.println("####################### " + ingredientCommand.isPresent());
//        System.out.println("####################### " + ingredientCommand.get());
//
//
//
//        if(!ingredientCommand.isPresent())
//        {
//            //throw an exception
//        }
//        IngredientCommand ingredientCommand1 = ingredientCommand.get();
//        ingredientCommand1.setRecipeId(recipeId);
//

        return recipeService.findCommandById(recipeId).map(
                recipe->recipe.getIngredients()
                        .stream().
                        filter(ingredient ->ingredient.getId().trim().equalsIgnoreCase(id.trim()))
                        .findFirst())
                        .filter(Optional::isPresent)
                .map(ingredient-> {
                            ingredient.get().setRecipeId();
                                    return ingredient.get();
                                }

                        );
        );
    }

    @Override
    public Flux<IngredientCommand> listsOfIngredientCommnad() {

        return ingredientService.listsOfIngredientCommnad();
    }

    @Override
    public Mono<IngredientCommand> saveIngerdientCommand(IngredientCommand command) {

        Mono<Recipe> recipeOptional=recipeRepository.findById(command.getRecipeId());

        if(recipeOptional.block()==null)
        {
            log.error("Recipe not found for id: " + command.getRecipeId());
            return Mono.just( new IngredientCommand());
        }else
        {
            Recipe recipe=recipeOptional.block();

            Optional<Ingredient> ingredientOptional=(recipe.getIngredients()).stream().
                    filter((x)->x.getId().equals(command.getId()))
                    .findFirst();

            if(ingredientOptional.isPresent()) {
                Ingredient ingredientFound = ingredientOptional.get();
                ingredientFound.setAmount(command.getAmount());
                ingredientFound.setDescription(command.getDescription());
                ingredientFound.setRecipeId(recipe.getId());
                ingredientFound.setUom(unitOfMeasureRepository.findById(command.getUom().getId()).block());

                if(ingredientFound.getUom()==null){
                    throw new RuntimeException("UOM NOT FOUND");
                }
            }
            else
            {
                Ingredient ingredient=ingredientCommandToIngredient.convert(command);
//                ingredient.setRecipe(recipe);
                recipe.addIngredient(ingredient);
            }

            Mono<Recipe> savedRecipe = recipeRepository.save(recipe);

            Optional<Ingredient> savedIngredientOptional=savedRecipe.block().getIngredients().stream()
                    .filter(recipeIngredients->recipeIngredients.getId().equals(command.getId()))
                    .findFirst();

            if(!savedIngredientOptional.isPresent()) {

                savedIngredientOptional = savedRecipe.block().getIngredients().stream()
                        .filter(recipeIngredients -> recipeIngredients.getDescription().equals(command.getDescription()))
                        .filter(recipeIngredients -> recipeIngredients.getAmount().equals(command.getAmount()))
                        .filter(recipeIngredients -> recipeIngredients.getUom().getId().equals(command.getUom().getId()))
                        .findFirst();
            }

            return Mono.just(ingredientToIngredientCommand.convert(savedIngredientOptional.get()));
        }


    }

    @Override
    public Mono<Void> deleteById(String recipeId, String id) {
        log.debug("Deleting ingredient: " + recipeId + ":" + id);

        Mono<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if(recipeOptional.block() !=null)
        {
            Recipe recipe = recipeOptional.block();

            log.debug("recipe found");

            Optional<Ingredient> ingredientOptional = recipe.getIngredients()
                    .stream().filter(ingredient -> ingredient.getId().equals(id))
                    .findFirst();

            if(ingredientOptional.isPresent())
            {
                log.debug("Ingredient with Id: " + id + "found");

                Ingredient ingredient = ingredientOptional.get();

                log.info("size of ingredient before removal: " + recipe.getIngredients().size());

                recipe.getIngredients().stream().forEach(System.out::println);
//                ingredient.setRecipe(null);
                recipe.getIngredients().remove(ingredient);

                log.info("size of ingredient  after removal: " + recipe.getIngredients().size());
                Mono<Recipe> recipeMono = recipeRepository.save(recipe);

            }
            else
            {
                log.error("inggredient with Id: " + id + " Not found");
            }
        }
        else{
            log.error("recipe with id: " + recipeId + " Not Found !");
        }
        return Mono.empty();
    }
}
