package avlTree.slpBuilders;

import dataContracts.Product;

public class InvalidProductionRuleException extends RuntimeException {

	private static final long serialVersionUID = 7944255276724963426L;

	public InvalidProductionRuleException(long fromNumber, Product product)
	{
		super(String.format("Invalid ProductionRule: fromNumber = %d, first = %d, second = %d", 
			fromNumber, product.first, product.second));
	}
}
