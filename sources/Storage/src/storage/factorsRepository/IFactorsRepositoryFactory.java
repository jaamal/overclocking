package storage.factorsRepository;


public interface IFactorsRepositoryFactory {
    IFactorsRepository getLZRepository();
    IFactorsRepository getLZ77Repository();
}
