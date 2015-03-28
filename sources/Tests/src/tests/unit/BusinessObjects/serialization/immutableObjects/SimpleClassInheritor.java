package tests.unit.BusinessObjects.serialization.immutableObjects;


public class SimpleClassInheritor extends SimpleClass
{
    public final String element;

    public SimpleClassInheritor(
            String firstField,
            String secondField,
            String element)
    {
        super(firstField, secondField);
        this.element = element;
    }
}
