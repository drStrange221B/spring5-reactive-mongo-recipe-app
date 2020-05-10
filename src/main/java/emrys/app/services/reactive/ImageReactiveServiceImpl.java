package emrys.app.services.reactive;

import emrys.app.domain.Recipe;
import emrys.app.repositories.reactive.RecipeReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Repository
public class ImageReactiveServiceImpl implements ImageReactiveService {

    @Autowired
    private RecipeReactiveRepository recipeReactiveRepository;


    @Override
    public Mono<Void> saveImageFile(String id, MultipartFile file) {

     Mono<Recipe> recipeMono =  recipeReactiveRepository.findById(id)
               .map(recipe-> {
//                   Byte[] byteObject = new Byte[0];
                   try{
                     Byte[]  byteObject = new Byte[file.getBytes().length];
                       int i = 0;

                       for(byte b:file.getBytes()){
                           byteObject[i++]=b;

                       }

                       recipe.setImage(byteObject);
                       return recipe;


                   }catch(IOException e)
                   {
                       e.printStackTrace();
                       throw new RuntimeException(e);
                   }
               });

       recipeReactiveRepository.save(recipeMono.block()).block();

       return Mono.empty();


    }
}
