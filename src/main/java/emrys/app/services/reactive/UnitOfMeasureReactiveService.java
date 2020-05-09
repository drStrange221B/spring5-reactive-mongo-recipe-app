package emrys.app.services.reactive;

import emrys.app.commands.UnitOfMeasureCommand;
import reactor.core.publisher.Flux;

public interface UnitOfMeasureReactiveService {
    Flux<UnitOfMeasureCommand> listAllReactiveUoms();
}
