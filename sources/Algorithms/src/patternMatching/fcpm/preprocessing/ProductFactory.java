package patternMatching.fcpm.preprocessing;

public class ProductFactory implements IProductFactory {

    @Override
    public Product create(int firstIndex, int secondIndex, char firstLetter, char lastLetter, int cutPosition, int length) {
        return new Product(firstIndex, secondIndex, firstLetter, lastLetter, cutPosition, length);
    }

    @Override
    public Product create(char letter) {
        return new Product(letter);
    }
}
