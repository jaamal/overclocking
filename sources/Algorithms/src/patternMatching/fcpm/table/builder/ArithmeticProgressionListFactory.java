package patternMatching.fcpm.table.builder;

public class ArithmeticProgressionListFactory implements IArithmeticProgressionListFactory {
    @Override
    public IArithmeticProgressionList create(int capacity) {
        return new ArithmeticProgressionList(capacity);
    }
}
