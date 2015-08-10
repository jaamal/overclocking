package SLPs;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import commons.utils.TimeCounter;
import dataContracts.Product;
import productEnumerator.ProductEnumerator;
import tree.ITree;
import tree.ITreeNode;

import java.time.Duration;
import java.util.HashMap;

public class SLPExtractor implements ISLPExtractor {
    private Logger log = LogManager.getLogger(SLPExtractor.class);
    
    @Override
    public <TNode extends ITreeNode> ProductEnumerator getSLP(ITree<TNode> tree) {
        ProductEnumerator slp = new ProductEnumerator();
        if (tree.isEmpty())
            return slp;
        HashMap<Long, Long> used = new HashMap<Long, Long>();
        log.info(String.format("Count of nodes approximate %d", tree.getRoot().getNumber()));
        TimeCounter timeCounter = TimeCounter.start();
        dfs(tree, used, slp);
        Duration duration = timeCounter.finish();
        log.info(String.format("Total time of SLP extracting is %dms", duration.toMillis()));
        return slp;
    }

    private static <TNode extends ITreeNode> long dfs(ITree<TNode> tree, HashMap<Long, Long> used, ProductEnumerator slp) {
        long rootNumber = tree.getRoot().getNumber();
        Long mapValue = used.get(rootNumber);
        if (mapValue != null)
            return mapValue;
        ITree<TNode> leftSubTree = tree.getLeftSubTree();
        ITree<TNode> rightSubTree = tree.getRightSubTree();
        Product product;
        boolean isTerminal = leftSubTree.isEmpty() && rightSubTree.isEmpty();
        if (isTerminal)
            product = new Product((char) tree.getRoot().getValue());
        else
            product = new Product(dfs(leftSubTree, used, slp), dfs(rightSubTree, used, slp));
        long fromNumber = slp.append(product);
        used.put(rootNumber, fromNumber);
        return fromNumber;
    }
}
