package tests.unit.Algorithms.patternMatching;

import dataContracts.Product;

public class AbrakadabraHelpers {

    private static Product product(char chr) {
        return new Product(chr);
    }

    private static Product product(int f, int s) {
        return new Product(f, s);
    }

    public final static Product[] a = new Product[]{
            product('a')
    };

    public final static Product[] c = new Product[]{
            product('c')
    };

    public final static Product[] ba = new Product[]{
            product('b'),
            product('a'),
            product(0, 1)
    };

    public final static Product[] a1ba = new Product[]{
            product('a'),
            product('b'),
            product(1, 0),
            product(0, 2)
    };


    public final static Product[] ccc = new Product[]{
            product('c'),
            product(0, 0),
            product(1, 0)
    };

    public final static Product[] cc = new Product[]{
            product('c'),
            product(0, 0),
    };

    public final static Product[] abaccc = new Product[]{
            product('a'), //0 a
            product('b'), //1 b
            product('c'), //2 c
            product(1, 0), //3 ba
            product(0, 3), //4 a1ba
            product(2, 2), //5 cc
            product(5, 2), //6 cc_c
            product(4, 6)  //7 abaccc
    };

    public final static Product[] abrakadabra = new Product[]{
            product('a'), //0
            product('b'), //1
            product('r'), //2
            product('k'), //3
            product('d'), //4
            product(0, 1), //5 ab
            product(2, 0), //6 ra
            product(3, 0), //7 ka
            product(7, 4), //8 ka_d
            product(5, 6), //9 ab_ra
            product(9, 8), //10 abra_kad
            product(10, 9)  //11 abra_kad_abra
    };

    public final static Product[] abra = new Product[]{
            product('a'),  //0 a
            product('b'),  //1 b
            product('r'),  //2 r
            product(0, 1), //3 ab
            product(2, 0), //4 ra
            product(3, 4)  //5 ab_ra
    };

    public final static Product[] abr = new Product[]{
            product('a'),
            product('b'),
            product('r'),
            product(0, 1),
            product(3, 2)
    };


    public final static Product[] ab1a = new Product[]{
            product('a'), //0 a
            product('b'), //1 b
            product(0, 1),//2 ab
            product(2, 0) //3
    };

    public final static Product[] aa = new Product[]{
            product('a'),
            product(0, 0)
    };

    public final static Product[] aaa1aa = new Product[]{
            product('a'), //0 a
            product(0, 0),//1 aa
            product(1, 0), //2 aaa
            product(2, 1)
    };


    public final static Product[] a1aa = new Product[]{
            product('a'),
            product(0, 0),
            product(0, 1)
    };


}
