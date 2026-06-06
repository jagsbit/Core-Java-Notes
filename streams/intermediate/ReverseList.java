package intermediate;

import java.util.List;
import java.util.stream.Collectors;

public class ReverseList {
    static void main() {
        List<Integer> list=List.of(1,2,3,4,5,6);
        List<Integer> reversed=list.stream().collect(Collectors.toList()).reversed();
        System.out.println(reversed);
    }
}
