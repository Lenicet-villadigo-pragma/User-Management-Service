package reactivechallenge.pragma.mapper;

public interface IGenericMapper<M, E> {

    E fromModel(M model);

    M toModel(E entity);
}
