package intermediate;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FindUnion {
    static void main() {
        List<Integer> l1=List.of(1,2,3,4,5,6);
        List<Integer> l2=List.of(2,4,1,3,6,8,9);

        Set<Integer> ans= Stream.concat(l1.stream(),l2.stream())
                .distinct()
                .collect(Collectors.toSet());
        System.out.println(ans);
    }
}
