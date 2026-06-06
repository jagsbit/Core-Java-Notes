package intermediate;

import java.util.List;

public class RemoveDup {
    static void main() {
        List<Integer> nums=List.of(1,1,2,2,3,3,4,5,5,6);
        List<Integer> unique=nums.stream().distinct().toList();
        System.out.println(unique);
    }
}
