package reactivechallenge.pragma.mapper;

import org.springframework.stereotype.Component;
import reactivechallenge.pragma.model.UserBootcampModel;
import reactivechallenge.pragma.out.entity.UserBootcampEntity;

@Component
public class UserBootcampMapper implements IGenericMapper<UserBootcampModel, UserBootcampEntity> {

    @Override
    public UserBootcampEntity fromModel(UserBootcampModel model) {
        if (model == null) {
            return null;
        }
        return new UserBootcampEntity(
                model.id(),
                model.userId(),
                model.bootcampId(),
                model.statusSubscription(),
                model.subscribedOn(),
                model.unsubscribedOn()
        );
    }

    @Override
    public UserBootcampModel toModel(UserBootcampEntity entity) {
        if (entity == null) {
            return null;
        }
        return new UserBootcampModel(
                entity.id(),
                entity.userId(),
                entity.bootcampId(),
                entity.statusSubscription(),
                entity.subscribedOn(),
                entity.unsubscribedOn()
        );
    }
}
