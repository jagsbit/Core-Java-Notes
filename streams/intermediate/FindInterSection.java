package intermediate;

import java.util.List;

public class FindInterSection {
    static void main() {
        List<Integer> l1=List.of(1,2,3,4,5,6);
        List<Integer> l2=List.of(2,4,1,3,6,8,9);

        List<Integer> ans=l1.stream()
                .filter(l2::contains)
                .toList();
        System.out.println(ans);
    }
}
