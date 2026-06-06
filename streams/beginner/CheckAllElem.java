package beginner;

import java.util.List;

public class CheckAllElem {
    static void main() {
        List<Integer> nums=List.of(2,4,6,8,10);

        boolean allEven=nums.stream().allMatch(x->x%2==0);
        System.out.println(allEven);
    }
}
