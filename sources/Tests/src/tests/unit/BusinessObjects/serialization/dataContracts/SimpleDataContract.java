package tests.unit.BusinessObjects.serialization.dataContracts;

import dataContracts.BusinessObject;

public class SimpleDataContract extends BusinessObject
{
    private String stringField;

    public String getStringField()
    {
        return stringField;
    }

    public void setStringField(String stringField)
    {
        this.stringField = stringField;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleDataContract that = (SimpleDataContract) o;

        if (stringField != null ? !stringField.equals(that.stringField) : that.stringField != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        return stringField != null ? stringField.hashCode() : 0;
    }
}
