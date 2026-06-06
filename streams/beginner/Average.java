package beginner;

import java.util.List;
public class Average {
    static void main() {
        List<Integer> nums=List.of(1,2,3,4,5,6,7);

        double result=nums.stream().mapToInt(Integer::intValue).average().orElse(0);
        System.out.println(result);

    }
}
