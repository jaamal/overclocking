package patternMatching.fcpm.arithmeticProgression;

public class ArithmeticProgression {
    public static final ArithmeticProgression Empty = new ArithmeticProgression(0, 0, 0) {
        @Override
        public ArithmeticProgression truncate(int begin, int end) {
            return this;
        }

        @Override
        public ArithmeticProgression shift(int offset) {
            return this;
        }

        @Override
        public int length() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public int elementAt(int index) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean contains(int element) {
            return false;
        }

        @Override
        public ArithmeticProgression intersect(ArithmeticProgression that) {
            return this;
        }

        @Override
        public ArithmeticProgression safeMerge(ArithmeticProgression that) {
            return that;
        }

        @Override
        public ArithmeticProgression merge(ArithmeticProgression that) {
            return that;
        }

        @Override
        public int lastElement() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return "[empty]";
        }
    };


    public static ArithmeticProgression create(int firstElement, int difference, int elementsCount) {
        if (difference <= 0)
            throw new IllegalArgumentException("difference is less than zero");
        if (elementsCount < 0)
            throw new IllegalArgumentException("elementsCount is less than zero");
        if (elementsCount == 0)
            return Empty;
        if (elementsCount == 1)
            return create(firstElement);
        return new ArithmeticProgression(firstElement, difference, elementsCount);
    }

    public static ArithmeticProgression create(int element) {
        return new ArithmeticProgression(element, 1, 1);
    }

    public final int firstElement;
    public final int difference;
    public final int elementsCount;

    private ArithmeticProgression(int firstElement, int difference, int elementsCount) {
        this.firstElement = firstElement;
        this.difference = difference;
        this.elementsCount = elementsCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArithmeticProgression ap = (ArithmeticProgression) o;

        if (firstElement != ap.firstElement) return false;
        if (difference != ap.difference) return false;
        if (elementsCount != ap.elementsCount) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = firstElement;
        result = 31 * result + difference;
        result = 31 * result + elementsCount;
        return result;
    }

    public int lastElement() {
        return elementAt(elementsCount - 1);
    }

    public ArithmeticProgression safeMerge(ArithmeticProgression that) {
        if (that.isEmpty())
            return this;
        int lastElement = lastElement();
        if (lastElement > that.firstElement)
            return null;
        if (elementsCount == 1 && that.elementsCount == 1) {
            if (firstElement == that.firstElement)
                return this;
            return create(firstElement, that.firstElement - firstElement, 2);
        }
        if (elementsCount == 1) {
            if (firstElement == that.firstElement)
                return that;
            else if (firstElement == that.firstElement - that.difference)
                return create(firstElement, that.difference, that.elementsCount + 1);
            return null;
        }
        if (that.elementsCount == 1) {
            if (lastElement == that.firstElement)
                return this;
            else if (that.firstElement == lastElement + difference)
                return create(firstElement, difference, elementsCount + 1);
            return null;
        }
        int endsDifferent = that.firstElement - lastElement;
        if ((endsDifferent == 0 || endsDifferent == difference) && difference == that.difference)
            return create(firstElement, difference, (that.lastElement() - firstElement) / difference + 1);
        return null;
    }


    public ArithmeticProgression merge(ArithmeticProgression that) {
        ArithmeticProgression mergeResult = safeMerge(that);
        if (mergeResult == null)
            throw new IllegalArgumentException(String.format("Cannot merge %s with %s", this, that));
        return mergeResult;
    }

    public ArithmeticProgression intersect(ArithmeticProgression that) {
        return APHelpers.intersect(this, that);
    }

    public boolean contains(int element) {
        if (element < firstElement || element > lastElement())
            return false;
        return (element - firstElement) % difference == 0;
    }

    public int elementAt(int index) {
        if (index < 0 || index >= elementsCount)
            throw new IllegalArgumentException("index");
        return firstElement + index * difference;
    }

    public boolean isEmpty() {
        return false;
    }

    public int length() {
        return (elementsCount - 1) * difference + 1;
    }

    public ArithmeticProgression shift(int offset) {
        if (offset == 0)
            return this;
        return create(firstElement + offset, difference, elementsCount);
    }

    public ArithmeticProgression truncate(int begin, int end) {
        if (begin > end)
            throw new IllegalArgumentException(String.format("Can not truncate %s on [%s, %s]", this, begin, end));
        int lastElement = lastElement();
        if (firstElement > end || lastElement < begin)
            return Empty;
        if (firstElement >= begin && lastElement <= end)
            return this;
        int truncatedFirstElementIndex = 0;
        int truncatedLastElementIndex = elementsCount - 1;
        if (firstElement < begin)
            truncatedFirstElementIndex = (begin - firstElement - 1) / difference + 1;
        if (lastElement > end)
            truncatedLastElementIndex = (end - firstElement) / difference;
        int truncatedElementsCount = truncatedLastElementIndex - truncatedFirstElementIndex + 1;
        return create(firstElement + truncatedFirstElementIndex * difference, difference, truncatedElementsCount);
    }

    @Override
    public String toString() {
        return "[" + firstElement + ", " + difference + ", " + elementsCount + ']';
    }
}
