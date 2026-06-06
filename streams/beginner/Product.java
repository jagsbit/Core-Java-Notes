package beginner;

import java.util.List;

public class Product {
    static void main() {
        List<Integer> list=List.of(1,2,4,5,6,7);
        int result=list.stream().reduce(1,(a,b)->a*b);
        System.out.println(result);
    }
}
