package reactivechallenge.pragma.spi;

import reactivechallenge.pragma.model.BootcampExternalModel;
import reactor.core.publisher.Flux;

import java.util.List;

public interface IBootcampServicePort {
 Flux<BootcampExternalModel> getBootcampsByIds (List<Long> bootcampIds);
}
