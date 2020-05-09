package emrys.app.controllers;

import emrys.app.domain.Recipe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TestController {

    @GetMapping("/test")
    public ModelAndView  getVew(Model model)
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("test");

        Recipe recipe = new Recipe();
        recipe.setDescription("This for theDescription !");
        recipe.setCookTime(1234);

        Recipe recipe1 = new Recipe();
        recipe1.setDescription("This for theDescription !");
        recipe1.setCookTime(1234);

        Recipe recipe2 = new Recipe();
        recipe2.setDescription("This for theDescription !");
        recipe2.setCookTime(1234);

        ArrayList<Recipe> recipeList = new ArrayList<>();

        recipeList.add(recipe);
        recipeList.add(recipe1);
        recipeList.add(recipe2);

        model.addAttribute("recipeList", recipeList);


        modelAndView.addObject("recipeList", recipeList);


        return  modelAndView;
    }
}
