package intermediate;

import java.util.List;

public class SecondLarget {
    static void main() {
        List<Integer> nums=List.of(1,2,3,4,5,6,7);

        int secondLarge=nums.stream().sorted((x,y)->y-x)
                .skip(1)
                .findFirst().orElse(0);
        System.out.println(secondLarge);

    }
}
