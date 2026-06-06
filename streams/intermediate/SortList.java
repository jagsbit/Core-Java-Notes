package intermediate;

import java.util.List;

public class SortList {
    static void main() {
        List<Integer> nums=List.of(9,2,3,4,8,16,7);

        List<Integer> sortList=nums.stream().sorted((x,y)->y-x).toList();
        System.out.println(sortList);
    }
}
