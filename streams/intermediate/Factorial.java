package intermediate;

import java.util.stream.Stream;

public class Factorial {
    static void main() {
        int num=5;
        int fact=Stream.iterate(1,(x)->x+1)
                .limit(num)
                .reduce(1,(a,b)->a*b);

        System.out.println(fact);

    }
}
