package emrys.app.services.reactive;

import emrys.app.commands.UnitOfMeasureCommand;
import emrys.app.converters.UnitOfMeasureToUnitOfMeasureCommand;
import emrys.app.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class UnitOfMeasureReactiveServiceImpl implements UnitOfMeasureReactiveService {

    @Autowired
    private final UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

    @Autowired
    private final UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;

    public UnitOfMeasureReactiveServiceImpl(UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository,
                                            UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand) {
        this.unitOfMeasureReactiveRepository = unitOfMeasureReactiveRepository;
        this.unitOfMeasureToUnitOfMeasureCommand = unitOfMeasureToUnitOfMeasureCommand;
    }

    @Override
    public Flux<UnitOfMeasureCommand> listAllReactiveUoms() {
        return unitOfMeasureReactiveRepository.findAll().map(unitOfMeasureToUnitOfMeasureCommand::convert);
    }
}
