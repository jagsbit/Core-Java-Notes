package beginner;

import java.util.List;

public class FindFirst {
    static void main() {
        List<Integer> nums=List.of(9,2,3,4,8,16,7);
        int elem=nums.stream().findFirst().orElse(-1);
        System.out.println(elem);

    }
}
