package advanced;

import java.util.List;

public class FlatternList {
    static void main() {
        List<List<Integer>> listOfLists = List.of(
                List.of(1, 2, 3),
                List.of(4, 5, 6),
                List.of(7, 8, 9)
        );

        List<Integer> flattenList=listOfLists.stream().flatMap(list->list.stream()).toList();
        System.out.println(flattenList);

    }
}
