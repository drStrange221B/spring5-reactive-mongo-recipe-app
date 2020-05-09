package emrys.app.services;

import emrys.app.commands.IngredientCommand;
import emrys.app.commands.RecipeCommand;
import emrys.app.converters.IngredientCommandToIngredient;
import emrys.app.converters.IngredientToIngredientCommand;
import emrys.app.domain.Ingredient;
import emrys.app.domain.Recipe;
import emrys.app.repositories.RecipeRepository;
import emrys.app.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService  {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @Autowired
    private IngredientCommandToIngredient ingredientCommandToIngredient;

    @Autowired
    private IngredientToIngredientCommand ingredientToIngredientCommand;

    @Override
    public IngredientCommand findByRecipeIdAndId(String recipeId, String id) {

        RecipeCommand recipe = recipeService.findCommandById(recipeId);
        Optional<IngredientCommand> ingredientCommand= (recipe.getIngredients()).stream().filter(i->i.getId()==id).findFirst();

        if(!ingredientCommand.isPresent())
        {
            //throw an exception
        }
        return ingredientCommand.get();
    }

    @Override
    public Set<IngredientCommand> listsOfIngredientCommnad() {

        return ingredientService.listsOfIngredientCommnad();
    }

    @Override
    public IngredientCommand saveIngerdientCommand(IngredientCommand command) {

        Optional<Recipe> recipeOptional=recipeRepository.findById(command.getRecipeId());

        if(!recipeOptional.isPresent())
        {
            log.error("Recipe not found for id: " + command.getRecipeId());
            return new IngredientCommand();
        }else
        {
            Recipe recipe=recipeOptional.get();

            Optional<Ingredient> ingredientOptional=(recipe.getIngredients()).stream().
                    filter((x)->x.getId().equals(command.getId()))
                    .findFirst();

            if(ingredientOptional.isPresent()) {
                Ingredient ingredientFound = ingredientOptional.get();
                ingredientFound.setAmount(command.getAmount());
                ingredientFound.setDescription(command.getDescription());
                ingredientFound.setUom(unitOfMeasureRepository.findById(command.getUom().getId())
                .orElseThrow(()->new RuntimeException("UOM NOT FOUND")));
            }
            else
            {
                Ingredient ingredient=ingredientCommandToIngredient.convert(command);
//                ingredient.setRecipe(recipe);
                recipe.addIngredient(ingredient);
            }

            Recipe savedRecipe = recipeRepository.save(recipe);

            Optional<Ingredient> savedIngredientOptional=savedRecipe.getIngredients().stream()
                    .filter(recipeIngredients->recipeIngredients.getId().equals(command.getId()))
                    .findFirst();

            if(!savedIngredientOptional.isPresent()) {

                savedIngredientOptional = savedRecipe.getIngredients().stream()
                        .filter(recipeIngredients -> recipeIngredients.getDescription().equals(command.getDescription()))
                        .filter(recipeIngredients -> recipeIngredients.getAmount().equals(command.getAmount()))
                        .filter(recipeIngredients -> recipeIngredients.getUom().getId().equals(command.getUom().getId()))
                        .findFirst();
            }

            return ingredientToIngredientCommand.convert(savedIngredientOptional.get());
        }


    }

    @Override
    public void deleteById(String recipeId, String id) {
        log.debug("Deleting ingredient: " + recipeId + ":" + id);

        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if(recipeOptional.isPresent())
        {
            Recipe recipe = recipeOptional.get();

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
                recipeRepository.save(recipe);
            }
            else
            {
                log.error("inggredient with Id: " + id + " Not found");
            }
        }
        else{
            log.error("recipe with id: " + recipeId + " Not Found !");
        }
    }
}
