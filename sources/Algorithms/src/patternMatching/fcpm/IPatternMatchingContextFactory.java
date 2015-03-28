package patternMatching.fcpm;

import patternMatching.fcpm.preprocessing.Product;

public interface IPatternMatchingContextFactory
{
    IPatternMatchingContext create(Product[] patternSlp, Product[] textSlp);
}
