package dataContracts;

public interface IBusinessObject
{
    String getId();

    void setId(String id);

    long getLastPersistMillis();

    void setLastPersistMillis(long lastPersistMillis);
}
