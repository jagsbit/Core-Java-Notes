package advanced;

import java.util.List;

public class SumOfEvenInNested {
    static void main() {
        List<List<Integer>> listOfLists = List.of(
                List.of(1, 2, 3),
                List.of(4, 5, 6),
                List.of(7, 8, 9)
        );
        int sum=listOfLists.stream().flatMap(list->list.stream()).filter(x->x%2!=0).reduce(0,(a,b)->a+b);
        System.out.println(sum);
    }
}
