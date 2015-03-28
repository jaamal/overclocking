package tests.unit.BusinessObjects.serialization.immutableObjects;

import dataContracts.BusinessObject;

public class ClassWithCleverConstructor extends BusinessObject
{
    public ClassWithCleverConstructor()
    {
        this.zzz = "zzz";
        this.qxx = 25;
    }

    public final String zzz;
    public final int qxx;
}
