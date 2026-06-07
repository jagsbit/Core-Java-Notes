package advanced;

import java.util.List;

public class FindFirst {
    static void main() {
        List<List<Integer>> listOfLists = List.of(
                List.of(1, 2, 3),
                List.of(4, 5, 6),
                List.of(7, 8, 9),
                List.of(1,8,9)
        );

        List<List<Integer>> ans=listOfLists.stream().filter(list->list.get(0)==1).toList();
        System.out.println(ans);
    }
}
