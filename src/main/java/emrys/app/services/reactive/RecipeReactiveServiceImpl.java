package emrys.app.services.reactive;

import emrys.app.commands.RecipeCommand;
import emrys.app.converters.RecipeCommandToRecipe;
import emrys.app.converters.RecipeToRecipeCommand;
import emrys.app.domain.Recipe;
import emrys.app.exceptions.NotFoundException;
import emrys.app.repositories.RecipeRepository;
import emrys.app.repositories.reactive.RecipeReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class RecipeReactiveServiceImpl implements RecipeReactiveService {

    @Autowired
    private RecipeReactiveRepository recipeRepository;
    @Autowired
    private  RecipeCommandToRecipe recipeCommandToRecipe;
    @Autowired
    private  RecipeToRecipeCommand recipeToRecipeCommand;


    @Override
    public Mono<Recipe> findById(String id) {
        Mono<Recipe> recipe = recipeRepository.findById(id);

//        if(!recipe.isPresent())
//        {
//            throw new NotFoundException("Recipe Not Found for ID value: " + id.toString());
//        }

        return recipe;
    }



    @Override
    public Flux<Recipe> getRecipes() {
        log.debug("I'm in the service");

//        Set<Recipe> recipeSet = new HashSet<>();

        return recipeRepository.findAll();
    }

    @Override
    @Transactional
    public Mono<RecipeCommand> findCommandById(String l) {
        return findById(l).map(recipeToRecipeCommand::convert);
    }

    @Override
    @Transactional
    public Mono<RecipeCommand> saveRecipeCommand(RecipeCommand command) {
        Recipe detachedRecipe = recipeCommandToRecipe.convert(command);


       Mono<Recipe> savedRecipe = recipeRepository.save(detachedRecipe);
//        log.debug("Saved RecipeId:" + savedRecipe.getId());
        return savedRecipe.map(recipeToRecipeCommand::convert);
    }

    @Override
    public void deleteById(String idToDelete) {
        recipeRepository.deleteById(idToDelete);
    }



}
