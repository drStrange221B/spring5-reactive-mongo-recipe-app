package emrys.app.controllers.reactive;

import emrys.app.commands.RecipeCommand;
import emrys.app.domain.Recipe;
import emrys.app.services.reactive.ImageReactiveService;
import emrys.app.services.reactive.RecipeReactiveService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/reactive/image")
public class ImageReactiveController {

    @Autowired
    private ImageReactiveService imageReactiveService;

    @Autowired
    private RecipeReactiveService recipeReactiveService;


    @GetMapping("recipe/{id}/image")
    public String showUploadForm(@PathVariable String id, Model model)
    {
        model.addAttribute("recipe", recipeReactiveService.findCommandById(id).block());

        return "recipe/imageuploadform";
    }
    @PostMapping("recipe/{id}/image")
    public String handleImagePost(@PathVariable String id, @RequestParam("imagefile") MultipartFile file)
    {
        imageReactiveService.saveImageFile(id,file).block();

        return "redirect:/recipe/" + id + "/show";
    }

    @GetMapping("recipe/{id}/recipeimage")
    public void renderImageFoundDB(@PathVariable String id, HttpServletResponse response) throws IOException{

        RecipeCommand recipeCommand = recipeReactiveService.findCommandById(id).block();

        if(recipeCommand.getIngredients() !=null)
        {
            byte[] bytes = new byte[recipeCommand.getImage().length];

            int i=0;
            for(Byte wrappedByte: recipeCommand.getImage()){
                bytes[i++] = wrappedByte;
            }

            response.setContentType("image/jpeg");
            InputStream is = new ByteArrayInputStream((bytes));
            IOUtils.copy(is, response.getOutputStream());
        }
    }

}
