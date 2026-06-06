package beginner;

import java.util.List;

public class MaxElement {
    static void main() {
        List<Integer> nums=List.of(9,2,3,4,8,16,7);

        int maxElement=nums.stream().max(Integer::compare).get();
        System.out.println(maxElement);
    }
}
