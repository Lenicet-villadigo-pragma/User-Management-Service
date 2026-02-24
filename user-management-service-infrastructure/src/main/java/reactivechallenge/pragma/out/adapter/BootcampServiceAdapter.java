package reactivechallenge.pragma.out.adapter;

import reactivechallenge.pragma.model.BootcampExternalModel;
import reactivechallenge.pragma.spi.IBootcampServicePort;
import reactor.core.publisher.Flux;

import java.util.List;

public class BootcampServiceAdapter implements IBootcampServicePort {
    @Override
    public Flux<BootcampExternalModel> getBootcampsByIds(List<Long> bootcampIds) {
        return null;
    }
}
