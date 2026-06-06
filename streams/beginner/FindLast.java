package beginner;

import java.util.List;

public class FindLast {
    static void main() {
        List<Integer> nums=List.of(9,2,3,4,8,16,7);
        int elem=nums.stream().reduce((a,b)->b).orElse(0);
        System.out.println(elem);
    }
}
