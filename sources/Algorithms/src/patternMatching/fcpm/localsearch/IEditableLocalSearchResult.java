package patternMatching.fcpm.localsearch;

import patternMatching.fcpm.arithmeticProgression.ArithmeticProgression;

public interface IEditableLocalSearchResult extends ILocalSearchResult {

    public static final IEditableLocalSearchResult Empty = new IEditableLocalSearchResult()
    {
        @Override
        public void add(ArithmeticProgression ap) throws IllegalStateException
        {
            throw new RuntimeException();
        }

        @Override
        public boolean isEmpty()
        {
            return true;
        }

        @Override
        public int size()
        {
            return 0;
        }

        @Override
        public ArithmeticProgression get(int index)
        {
            throw new RuntimeException();
        }
    };


    void add(ArithmeticProgression progression);
}
