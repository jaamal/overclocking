package SLPs;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import tree.ITree;
import tree.ITreeNode;
import avlTree.slpBuilders.ConcurrentSLPBuilder;
import avlTree.slpBuilders.IParallelExecutor;
import avlTree.slpBuilders.ISLPBuilder;
import avlTree.slpBuilders.ParallelExecutor;

import commons.utils.TimeCounter;

import dataContracts.Product;

public class ConcurrentSLPExtractor implements ISLPExtractor {
    private final int threadCount;

    public ConcurrentSLPExtractor(int threadCount) {
        this.threadCount = threadCount;
    }

    @Override
    public <TNode extends ITreeNode> ISLPBuilder getSLP(ITree<TNode> tree) {
        if (tree.isEmpty())
            return new ConcurrentSLPBuilder();

        ISLPBuilder slp = new ConcurrentSLPBuilder();

        TimeCounter dfsTimeCounter = TimeCounter.start();
        HashMap<Long, ITree<TNode>> forParallel = new HashMap<>();
        IParallelExecutor parallelDfs = new ParallelExecutor(threadCount);
        ConcurrentHashMap<Long, Long> oldNumberToNewNumber = new ConcurrentHashMap<>();
        dfs(tree, forParallel, 1, parallelDfs, slp, oldNumberToNewNumber);
        System.out.println(String.format("dfs: %dms", dfsTimeCounter.getMillis()));

        dfsTimeCounter = TimeCounter.start();
        parallelDfs.await();
        System.out.println(String.format("parallelDfs: %dms", dfsTimeCounter.getMillis()));

        dfsTimeCounter = TimeCounter.start();
        dfs2(tree, oldNumberToNewNumber, slp);
        System.out.println(String.format("dfs2: %dms", dfsTimeCounter.getMillis()));

        return slp;
    }

    private <TNode extends ITreeNode> void dfs(ITree<TNode> tree,
                                               HashMap<Long, ITree<TNode>> forParallel,
                                               int count,
                                               IParallelExecutor parallelDfs,
                                               ISLPBuilder slpBuilder,
                                               ConcurrentHashMap<Long, Long> oldNumberToNewNumber) {
        long rootNumber = tree.getRoot().getNumber();
        if (count >= 40 * threadCount) {
            ITree<TNode> mapValue = forParallel.get(rootNumber);
            if (mapValue == null) {
                forParallel.put(rootNumber, tree);
                parallelDfs.append(new SubTreeWalker<>(slpBuilder, oldNumberToNewNumber, tree));
            }
            return;
        }

        ITree<TNode> leftSubTree = tree.getLeftSubTree();
        ITree<TNode> rightSubTree = tree.getRightSubTree();
        boolean isTerminal = leftSubTree.isEmpty() && rightSubTree.isEmpty();
        if (!isTerminal) {
            dfs(leftSubTree, forParallel, count * 2, parallelDfs, slpBuilder, oldNumberToNewNumber);
            dfs(rightSubTree, forParallel, count * 2, parallelDfs, slpBuilder, oldNumberToNewNumber);
        }
    }

    private <TNode extends ITreeNode> Long dfs2(ITree<TNode> tree,
                                                final ConcurrentHashMap<Long, Long> oldNumberToNewNumber,
                                                final ISLPBuilder slpBuilder) {
        TNode rootNode = tree.getRoot();
        long rootNumber = rootNode.getNumber();
        Long newNumber = oldNumberToNewNumber.get(rootNumber);
        if (newNumber != null)
            return newNumber;

        ITree<TNode> leftSubTree = tree.getLeftSubTree();
        ITree<TNode> rightSubTree = tree.getRightSubTree();
        Product product;
        if (leftSubTree.isEmpty() && rightSubTree.isEmpty())
            product = new Product((char) rootNode.getValue());
        else {
            Long leftNewNumber = dfs2(leftSubTree, oldNumberToNewNumber, slpBuilder);
            Long rightNewNumber = dfs2(rightSubTree, oldNumberToNewNumber, slpBuilder);
            product = new Product(leftNewNumber, rightNewNumber);
        }
        newNumber = slpBuilder.append(product);
        oldNumberToNewNumber.put(rootNumber, newNumber);
        return newNumber;
    }

    private class SubTreeWalker<TNode extends ITreeNode> implements Runnable {
        private final ISLPBuilder slpBuilder;
        private final ConcurrentHashMap<Long, Long> oldNumberToNewNumber;
        private final ITree<TNode> root;

        public SubTreeWalker(ISLPBuilder slpBuilder,
                             ConcurrentHashMap<Long, Long> oldNumberToNewNumber,
                             ITree<TNode> root) {
            this.slpBuilder = slpBuilder;
            this.oldNumberToNewNumber = oldNumberToNewNumber;
            this.root = root;
        }

        public void run() {
            dfs(root);
        }

        private Long dfs(ITree<TNode> tree) {
            TNode rootNode = tree.getRoot();
            long number = rootNode.getNumber();
            Long newNumber = oldNumberToNewNumber.get(number);
            if (newNumber != null)
                return newNumber;

            ITree<TNode> leftSubTree = tree.getLeftSubTree();
            ITree<TNode> rightSubTree = tree.getRightSubTree();
            Product product;
            if (leftSubTree.isEmpty() && rightSubTree.isEmpty())
                product = new Product((char) rootNode.getValue());
            else {
                Long leftNewNumber = dfs(leftSubTree);
                Long rightNewNumber = dfs(rightSubTree);
                product = new Product(leftNewNumber, rightNewNumber);
            }
            newNumber = slpBuilder.append(product);
            oldNumberToNewNumber.put(number, newNumber);
            return newNumber;
        }
    }
}
