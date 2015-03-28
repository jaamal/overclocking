package tests.unit.BusinessObjects.serialization.dataContracts;

import dataContracts.BusinessObject;

public class DataContractWithArrays extends BusinessObject
{
    private byte[] byteArray;
    private int[] intArray;
    private SimpleDataContract[] simpleDataContract;

    public byte[] getByteArray()
    {
        return byteArray;
    }

    public void setByteArray(byte[] byteArray)
    {
        this.byteArray = byteArray;
    }

    public int[] getIntArray()
    {
        return intArray;
    }

    public void setIntArray(int[] intArray)
    {
        this.intArray = intArray;
    }

    public SimpleDataContract[] getSimpleDataContract()
    {
        return simpleDataContract;
    }

    public void setSimpleDataContract(SimpleDataContract[] simpleDataContract)
    {
        this.simpleDataContract = simpleDataContract;
    }
}
