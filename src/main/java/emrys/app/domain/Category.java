package emrys.app.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

/**
 * Created by jt on 6/13/17.
 */
@Data
@EqualsAndHashCode(exclude = {"recipes"})
@Document
public class Category {

    @Id
    private String id;
    private String description;
    private Set<Recipe> recipes;

}
