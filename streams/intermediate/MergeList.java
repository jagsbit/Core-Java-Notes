package intermediate;

import java.util.List;
import java.util.stream.Stream;

public class MergeList {
    static void main() {
        List<Integer> list1 = List.of(1, 2, 3);
        List<Integer> list2 = List.of(4, 5, 6);

        List<Integer> combined= Stream.concat(list1.stream(),list2.stream()).toList();
        System.out.println(combined);
    }
}
