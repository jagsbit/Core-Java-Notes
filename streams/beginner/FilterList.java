package beginner;

import java.util.List;

public class FilterList {
    static void main() {
        List<Integer> list=List.of(1,2,3,4,5,6,7,8,9,10);
        List<Integer> filteredList=list.stream().filter(x->x%2==0).toList();
        System.out.println(filteredList);
    }
}
