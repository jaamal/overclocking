package dataContracts;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BusinessObject implements IBusinessObject
{
    @Id
    private String id;
    private long lastPersistMillis;

    @Override
    public void setId(String id)
    {
        this.id = id;
    }
    
    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public long getLastPersistMillis()
    {
        return lastPersistMillis;
    }

    @Override
    public void setLastPersistMillis(long lastPersistMillis)
    {
        this.lastPersistMillis = lastPersistMillis;
    }
}
