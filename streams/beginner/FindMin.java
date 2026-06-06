package beginner;

import java.util.List;

public class FindMin {
    static void main() {
        List<Integer> nums=List.of(9,2,3,4,8,16,7);
        int mini=nums.stream().min(Integer::compare).orElse(0);
        System.out.println(mini);
    }
}
