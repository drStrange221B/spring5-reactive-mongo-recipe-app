package emrys.app.services;

import emrys.app.commands.UnitOfMeasureCommand;
import emrys.app.domain.UnitOfMeasure;
import reactor.core.publisher.Flux;

import java.util.Set;

public interface UnitOfMeasureService {
    Set<UnitOfMeasureCommand> listAllUoms();


}
