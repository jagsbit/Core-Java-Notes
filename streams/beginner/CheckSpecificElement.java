package beginner;

import java.util.List;

public class CheckSpecificElement {
    static void main() {
        List<Integer> nums=List.of(9,2,3,4,8,16,7);

        boolean ifPresent=nums.stream().anyMatch((x)->x==10);
        System.out.println(ifPresent);
    }
}
