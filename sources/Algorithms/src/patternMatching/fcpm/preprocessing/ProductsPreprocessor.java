package patternMatching.fcpm.preprocessing;

public class ProductsPreprocessor implements IProductsPreprocessor {
    private IProductFactory productFactory;

    public ProductsPreprocessor(IProductFactory productFactory) {
        this.productFactory = productFactory;
    }

    @Override
    public Product[] execute(dataContracts.Product[] products) {
        Product[] processedProducts = new Product[products.length];

        for (int i = 0; i < products.length; ++i) {
        	dataContracts.Product product = products[i];
            if (product.isTerminal)
                processedProducts[i] = createForTerminal(product);
            else {
                Product first = processedProducts[(int) product.first];
                Product second = processedProducts[(int) product.second];
                processedProducts[i] = createForNonTerminal((int) product.first, (int) product.second, first, second);
            }
        }
        return processedProducts;
    }

    private Product createForNonTerminal(int firstIndex, int lastIndex, Product first, Product second) {
        int firstLength = first.Length;
        int secondLength = second.Length;
        return productFactory.create(firstIndex, lastIndex, first.FirstLetter, second.LastLetter, firstLength, firstLength + secondLength);
    }

    private Product createForTerminal(dataContracts.Product product) {
        return productFactory.create(product.symbol);
    }
}
