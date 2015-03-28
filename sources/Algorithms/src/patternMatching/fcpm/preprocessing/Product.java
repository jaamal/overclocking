package patternMatching.fcpm.preprocessing;

public class Product {
    public final int FirstProduct;

    public final int SecondProduct;

    public final char FirstLetter;

    public final char LastLetter;

    public final int CutPosition;

    public final int Length;

    public final boolean IsTerminal;

    public Product(int firstProduct, int secondProduct, char firstLetter, char lastLetter, int cutPosition, int length) {
        FirstProduct = firstProduct;
        SecondProduct = secondProduct;
        FirstLetter = firstLetter;
        LastLetter = lastLetter;
        CutPosition = cutPosition;
        Length = length;
        IsTerminal = false;
    }

    public Product(char letter) {
        FirstLetter = LastLetter = letter;
        Length = 1;
        CutPosition = -1;
        FirstProduct = SecondProduct = -1;
        IsTerminal = true;
    }

    public Product(Product product) {
        FirstProduct = product.FirstProduct;
        SecondProduct = product.SecondProduct;
        FirstLetter = product.FirstLetter;
        LastLetter = product.LastLetter;
        CutPosition = product.CutPosition;
        Length = product.Length;
        IsTerminal = product.IsTerminal;

    }

    @Override
    public String toString() {
        if (IsTerminal)
            return "Terminal " + FirstLetter;
        return "NonTerminal {" +
                "FirstProduct=" + FirstProduct +
                ", SecondProduct=" + SecondProduct +
                ", FirstLetter=" + FirstLetter +
                ", LastLetter=" + LastLetter +
                ", CutPosition=" + CutPosition +
                ", Length=" + Length +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (CutPosition != product.CutPosition) return false;
        if (FirstLetter != product.FirstLetter) return false;
        if (FirstProduct != product.FirstProduct) return false;
        if (IsTerminal != product.IsTerminal) return false;
        if (LastLetter != product.LastLetter) return false;
        if (Length != product.Length) return false;
        if (SecondProduct != product.SecondProduct) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = FirstProduct;
        result = 31 * result + SecondProduct;
        result = 31 * result + (int) FirstLetter;
        result = 31 * result + (int) LastLetter;
        result = 31 * result + CutPosition;
        result = 31 * result + Length;
        result = 31 * result + (IsTerminal ? 1 : 0);
        return result;
    }
}