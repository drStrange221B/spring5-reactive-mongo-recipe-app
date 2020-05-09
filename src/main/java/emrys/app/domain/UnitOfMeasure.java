package emrys.app.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * Created by jt on 6/13/17.
 */
@Data
@Document
public class UnitOfMeasure {

    @Id
    private String id;
    private String description;
}
