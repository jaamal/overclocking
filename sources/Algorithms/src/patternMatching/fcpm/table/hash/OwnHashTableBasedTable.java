package patternMatching.fcpm.table.hash;

import patternMatching.fcpm.arithmeticProgression.ArithmeticProgression;
import patternMatching.fcpm.table.IAPTable;

    public final class OwnHashTableBasedTable implements IAPTable
    {
        private static final int MAXIMUM_CAPACITY = 1 << 30;
        private static float loadFactor = 0.75f;


        public final static class Entry
        {
            Entry next;
            final int patternIndex;
            final int textIndex;
            final ArithmeticProgression progression;
            final int hashCode;

            public Entry(int hashCode, int patternIndex, int textIndex, ArithmeticProgression progression, Entry next)
            {
                this.hashCode = hashCode;
                this.patternIndex = patternIndex;
                this.textIndex = textIndex;
                this.progression = progression;
                this.next = next;
            }
        }

        private static int hashCode(int patternIndex, int textIndex)
        {
            int h = 0;
            h ^= 31 * patternIndex + textIndex;
            // This function ensures that hashCodes that differ only by
            // constant multiples at each bit position have a bounded
            // number of collisions (approximately 8 at default load factor).
            h ^= (h >>> 20) ^ (h >>> 12);
            return h ^ (h >>> 7) ^ (h >>> 4);
        }

        private static int indexFor(int hashCode, int length)
        {
            return hashCode & (length - 1);
        }


        private final int patternSize;
        private final int textSize;
        private Entry[] table;
        private int size;
        private int threshold;


        public OwnHashTableBasedTable(int patternSize, int textSize)
        {
            this(patternSize, textSize, 16);
        }

        public OwnHashTableBasedTable(int patternSize, int textSize, int capacity)
        {
            this.patternSize = patternSize;
            this.textSize = textSize;
            this.table = new Entry[capacity];
            this.size = 0;
            this.threshold = (int) Math.min(capacity * loadFactor, MAXIMUM_CAPACITY + 1);
        }

        @Override
        public ArithmeticProgression get(int patternIndex, int textIndex)
        {
            int hashCode = hashCode(patternIndex, textIndex);
            int index = indexFor(hashCode, table.length);
            Entry entry = table[index];
            while (entry != null)
            {
                if (entry.hashCode == hashCode && entry.patternIndex == patternIndex && entry.textIndex == entry.textIndex)
                    return entry.progression;
                entry = entry.next;
            }
            return ArithmeticProgression.Empty;
        }

        @Override
        public void set(int patternIndex, int textIndex, ArithmeticProgression progression)
        {
            if (progression.isEmpty())
                return;
            int hashCode = hashCode(patternIndex, textIndex);
            int index = indexFor(hashCode, table.length);
            for (Entry e = table[index]; e != null; e = e.next)
            {
                if (e.hashCode == hashCode && e.patternIndex == patternIndex && e.textIndex == textIndex)
                {
                    throw new RuntimeException("Changing set value is prohibited");
                }
            }

            addEntry(hashCode, index, patternIndex, textIndex, progression);

        }

        private void addEntry(int hashCode, int index, int patternIndex, int textIndex, ArithmeticProgression progression)
        {
            if ((size >= threshold) && (null != table[index]))
            {
                resize(2 * table.length);
                hashCode = hashCode(patternIndex, textIndex);
                index = indexFor(hashCode, table.length);
            }

            createEntry(hashCode, index, patternIndex, textIndex, progression);
        }


        private void createEntry(int hash, int bucketIndex, int patternIndex, int textIndex, ArithmeticProgression progression)
        {
            Entry entry = table[bucketIndex];
            table[bucketIndex] = new Entry(hash, patternIndex, textIndex, progression, entry);
            size++;
        }


        private void resize(int newCapacity)
        {
            Entry[] oldTable = table;
            int oldCapacity = oldTable.length;
            if (oldCapacity == MAXIMUM_CAPACITY)
            {
                threshold = Integer.MAX_VALUE;
                return;
            }

            Entry[] newTable = new Entry[newCapacity];
            transfer(newTable);
            table = newTable;
            threshold = (int) Math.min(newCapacity * loadFactor, MAXIMUM_CAPACITY + 1);
        }


        private void transfer(Entry[] newTable)
        {
            int newCapacity = newTable.length;
            for (Entry e : table)
            {
                while (null != e)
                {
                    Entry next = e.next;
                    int index = indexFor(e.hashCode, newCapacity);
                    e.next = newTable[index];
                    newTable[index] = e;
                    e = next;
                }
            }
        }


        @Override
        public int patternSize()
        {
            return patternSize;
        }

        @Override
        public int textSize()
        {
            return textSize;
        }
    }
