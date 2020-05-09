package emrys.app.controllers.reactive;

import emrys.app.commands.IngredientCommand;
import emrys.app.commands.UnitOfMeasureCommand;
import emrys.app.services.reactive.IngredientReactiveService;
import emrys.app.services.reactive.RecipeReactiveService;
import emrys.app.services.reactive.UnitOfMeasureReactiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Controller
@Slf4j
@RequestMapping("/reactive/ingredient")
public class IngredientReactiveController {

    @Autowired
    private RecipeReactiveService recipeService;

    @Autowired
    private IngredientReactiveService ingredientService;

    @Autowired
    private UnitOfMeasureReactiveService unitOfMeasureService;


    @GetMapping("/recipe/{id}/ingredients")
    public String getIngredient(@PathVariable String id, Model model) {
        model.addAttribute("recipe", recipeService.findCommandById(id).block());

        return "recipe/ingredient/list";
    }

    @GetMapping("/recipe/{recipeId}/ingredient/{id}/show")
    public String showRecipeIngredient(@PathVariable String recipeId,
                                       @PathVariable String id, Model model) {
       IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndId(recipeId, id).block();
        ingredientCommand.setRecipeId(recipeId);
        model.addAttribute("ingredient",ingredientCommand );;


        return "recipe/ingredient/show";
    }

    @GetMapping("/recipe/{recipeId}/ingredient/new")
    public String createNewIngredient(@PathVariable String recipeId, Model model)
    {

        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(recipeId);
        ingredientCommand.setUom(new UnitOfMeasureCommand());

        model.addAttribute("ingredient", ingredientCommand);
        model.addAttribute("uomList", unitOfMeasureService.listAllReactiveUoms().collectList().block());

        return "recipe/ingredient/ingredientform";

    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/{id}/update")
    public String updateRecipeIngredient(@PathVariable String recipeId, @PathVariable String id,
                                         Model model) {
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndId(recipeId, id).block());

        model.addAttribute("uomList", unitOfMeasureService.listAllReactiveUoms().collectList().block());
        return "recipe/ingredient/ingredientform";
    }

    @PostMapping("/recipe/{recipeId}/ingredient")
    public String saveOrUpdate(@ModelAttribute IngredientCommand command) {
        Mono<IngredientCommand> saveCommand = ingredientService.saveIngerdientCommand(command);

        IngredientCommand ingredientCommand = saveCommand.block();

        return "redirect:/reactive/ingredient/recipe/" + saveCommand.block().getRecipeId() + "/ingredient/" + saveCommand.block().getId() + "/show";
    }

    @GetMapping("/recipe/{recipeId}/ingredient/{id}/delete")
    public String deleteIngredient(@PathVariable String recipeId, @PathVariable String id,
                                   Model model)
    {
        log.debug("deleting ingredient id: "  + id);

        ingredientService.deleteById(recipeId,id).block();

        return "redirect:/reactive/ingredient/recipe/" + recipeId + "/ingredients";
    }

}
