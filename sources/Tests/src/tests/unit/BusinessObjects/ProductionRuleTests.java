package tests.unit.BusinessObjects;

import junit.framework.Assert;

import org.junit.Test;

import dataContracts.Product;
import tests.unit.UnitTestBase;

public class ProductionRuleTests extends UnitTestBase {

	@Test
	public void testHashCode()
	{
		Product rule1 = new Product('a');
		Product rule2 = new Product('a');
		Product rule3 = new Product(10, 20);
		Product rule4 = new Product(10, 20);
		
		Assert.assertEquals(rule1, rule2);
		Assert.assertEquals(rule1.hashCode(), rule2.hashCode());
		
		Assert.assertEquals(rule3, rule4);
		Assert.assertEquals(rule3.hashCode(), rule4.hashCode());
	}
}
