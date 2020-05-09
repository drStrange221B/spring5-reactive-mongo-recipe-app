package emrys.app.services;

import emrys.app.commands.UnitOfMeasureCommand;
import emrys.app.converters.UnitOfMeasureToUnitOfMeasureCommand;
import emrys.app.domain.UnitOfMeasure;
import emrys.app.repositories.UnitOfMeasureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

    @Autowired
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @Autowired
    private UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;

    @Override
    public Set<UnitOfMeasureCommand> listAllUoms() {

        /*

         Iterable<UnitOfMeasure> all = unitOfMeasureRepository.findAll();

        Set<UnitOfMeasureCommand> setUnitOfMeasureCommand=new HashSet<>();
        all.forEach((x)->setUnitOfMeasureCommand.add(unitOfMeasureToUnitOfMeasureCommand.convert(x)));

        return  setUnitOfMeasureCommand;

         */
        return StreamSupport.stream(unitOfMeasureRepository.findAll().spliterator(),false).map(unitOfMeasureToUnitOfMeasureCommand::convert)
                .collect(Collectors.toSet());

    }
}
