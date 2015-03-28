package patternMatching.fcpm.arithmeticProgression;

public class APHelpers {

    //TODO refactor this
    public static ArithmeticProgression intersect(ArithmeticProgression first, ArithmeticProgression second) {
        if (first.isEmpty() || second.isEmpty())
            return ArithmeticProgression.Empty;
        int A = first.difference;
        int C = second.difference;
        int B = ((second.firstElement - first.firstElement) % C + C) % C;

        int D = gcd(A, C);
        if (B % D != 0) return ArithmeticProgression.Empty;

        A /= D;
        B /= D;
        C /= D;

        int x = (B * (euclid(A, C) % C + C)) % C;
        int potentialStart = first.firstElement + x * first.difference;
        int newDifference = first.difference / D * second.difference;

        if (potentialStart > second.lastElement() || potentialStart > first.lastElement())
            return ArithmeticProgression.Empty;

        if (potentialStart > second.firstElement) {
            int newSize = (Math.min(first.lastElement(), second.lastElement()) - potentialStart) / newDifference + 1;
            return ArithmeticProgression.create(potentialStart, newDifference, newSize);
        }
        
        int left = (second.firstElement - potentialStart + newDifference - 1) / newDifference;
        int right = (second.lastElement() - potentialStart) / newDifference;
        if (left > right)
            return ArithmeticProgression.Empty;
        
        int newStart = potentialStart + left * newDifference;
        if (newStart > first.lastElement())
            return ArithmeticProgression.Empty;
        int newSize = (Math.min(first.lastElement(), second.lastElement()) - newStart) / newDifference + 1;
        return ArithmeticProgression.create(newStart, newDifference, newSize);
    }

    private static int gcd(int a, int b) {
        while (b != 0) {
            int tmp = a % b;
            a = b;
            b = tmp;
        }
        return a;
    }

    private static int euclid(int a, int b) {
        int m = 1, n = 0, p = 0, q = 1;
        while (b != 0) {
            int m1 = m;
            int n1 = n;
            m = p;
            n = q;
            p = m1 - (a / b) * p;
            q = n1 - (a / b) * q;

            int tmp = a % b;
            a = b;
            b = tmp;
        }

        return m;
    }

}
