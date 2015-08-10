package compressionservice.algorithms.lcaOnlineSlp;

import dataContracts.Product;
import productEnumerator.ProductEnumerator;

public class CompressingQueue implements ICompressingQueue {
    public CompressingQueue(ProductEnumerator slp) {
        this.slp = slp;
        queue = new CyclicQueue();
        queue.push(-1);
        nextQueue = null;
    }

    @Override
    public void insertSymbol(long symbol) {
        queue.push(symbol);
        if (queue.size() == 5) {
            if (nextQueue == null)
                nextQueue = new CompressingQueue(slp);

            if (SymbolPairs.isNeedCompressPair(queue, 1)) {
                queue.pop();
                long y1 = queue.pop();
                long y2 = queue.peek();
                long fromNumber = slp.append(new Product(y1, y2));
                nextQueue.insertSymbol(fromNumber);
            } else {
                queue.pop();
                long y1 = queue.pop();
                nextQueue.insertSymbol(y1);
                long y2 = queue.pop();
                long y3 = queue.peek();
                long fromNumber = slp.append(new Product(y2, y3));
                nextQueue.insertSymbol(fromNumber);
            }
        }
    }

    @Override
    public void postProcessingRemain() {
        if (queue.size() <= 2 && nextQueue == null)
            return;

        if (nextQueue == null)
            nextQueue = new CompressingQueue(slp);
        while (queue.size() > 2) {
            queue.pop();
            long y1 = queue.pop();
            long y2 = queue.peek();
            long fromNumber = slp.append(new Product(y1, y2));
            nextQueue.insertSymbol(fromNumber);
        }
        if (queue.size() == 2) {
            queue.pop();
            nextQueue.insertSymbol(queue.peek());
        }
        nextQueue.postProcessingRemain();
    }

    private final ProductEnumerator slp;
    private final CyclicQueue queue;
    private CompressingQueue nextQueue;

    private class CyclicQueue implements ILongArray {
        public CyclicQueue() {
            symbols = new long[LENGTH];
            head = tail = 0;
            for (int i = 0; i < LENGTH; ++i)
                symbols[i] = -1;
        }

        public void push(long symbol) {
            symbols[tail] = symbol;
            tail = (tail + 1) % LENGTH;
        }

        public long pop() {
            long result = symbols[head];
            symbols[head] = -1;
            head = (head + 1) % LENGTH;
            return result;
        }

        public long get(int index)
        {
            assert(0 <= index && index < size());
            return symbols[(head + index) % LENGTH];
        }

        public long peek() {
            return symbols[head];
        }

        public int size() {
            return (tail - head + LENGTH) % LENGTH;
        }

        private final long[] symbols;
        private int head;
        private int tail;

        private static final int LENGTH = 6;
    }
}



