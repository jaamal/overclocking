package patternMatching.fcpm.preprocessing;

public interface IProductFactory {
    Product create(int firstIndex, int secondIndex, char firstLetter, char lastLetter, int cutPosition, int length);

    Product create(char letter);
}
