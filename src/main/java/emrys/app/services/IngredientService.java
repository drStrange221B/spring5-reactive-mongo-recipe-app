package emrys.app.services;

import emrys.app.commands.IngredientCommand;

import java.util.Set;

public interface IngredientService {

    IngredientCommand findByRecipeIdAndId(String recipeId, String id);

    Set<IngredientCommand> listsOfIngredientCommnad();

    public IngredientCommand saveIngerdientCommand(IngredientCommand command);


    void deleteById(String recipeId, String id);
}
