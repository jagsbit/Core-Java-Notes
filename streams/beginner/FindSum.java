package beginner;

import java.util.List;

public class FindSum {
    static void main() {
        List<Integer> nums=List.of(1,2,3,4,5,6,7);

        int result=nums.stream().reduce(0,(x,y)->x+y);
        System.out.println(result);

        int result1=nums.stream().mapToInt(Integer::intValue).sum();

        System.out.println(result1);

        int result2=nums.stream().mapToInt((x)->(Integer)x).sum();
        System.out.println(result2);

    }
}
