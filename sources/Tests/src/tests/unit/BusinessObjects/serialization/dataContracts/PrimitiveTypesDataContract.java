package tests.unit.BusinessObjects.serialization.dataContracts;

import dataContracts.BusinessObject;

public class PrimitiveTypesDataContract extends BusinessObject
{
    private int intField;
    private long longField;
    private char charField;
    private boolean booleanField;
    private short shortField;
    private byte byteField;

    public int getIntField()
    {
        return intField;
    }

    public long getLongField()
    {
        return longField;
    }

    public char getCharField()
    {
        return charField;
    }

    public boolean isBooleanField()
    {
        return booleanField;
    }

    public short getShortField()
    {
        return shortField;
    }

    public byte getByteField()
    {
        return byteField;
    }
}
