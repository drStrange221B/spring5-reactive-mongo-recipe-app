package emrys.app.services;

import emrys.app.domain.Recipe;
import emrys.app.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final RecipeRepository recipeRepository;

    public ImageServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    @Transactional
    public void saveImageFile(String id, MultipartFile file)  {
        try{
            Recipe recipe = (recipeRepository.findById(id)).orElseThrow(()->new RuntimeException("recipe not found"));

            Byte[] byteObjects = new Byte[file.getBytes().length];

            int i=0;

            for(byte b: file.getBytes())
            {
                byteObjects[i++]=b;
            }

            recipe.setImage(byteObjects);

            recipeRepository.save(recipe);
        }catch (IOException e)
        {
            log.error("Error occurred", e);

            e.printStackTrace();


        }

    }
}
