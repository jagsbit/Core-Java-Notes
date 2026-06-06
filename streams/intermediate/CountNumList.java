package intermediate;

import java.util.List;

public class CountNumList{
    static void main() {
        List<Integer> nums=List.of(9,2,3,4,8,16,7);

        long count=nums.stream().count();

        System.out.println(count);

    }
}
