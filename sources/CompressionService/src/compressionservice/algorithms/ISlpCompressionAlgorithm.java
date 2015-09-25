package compressionservice.algorithms;

import dataContracts.Product;

public interface ISlpCompressionAlgorithm extends ICompressionAlgorithm
{
    Product[] getSlp();
}
