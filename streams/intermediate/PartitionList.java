package intermediate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PartitionList {
    static void main() {
        List<Integer> list=List.of(1,2,3,4,5,6,8);
        Map<Boolean,List<Integer>> partitionList=list.stream()
                .collect(Collectors.partitioningBy(x->x%2==0));
        System.out.println(partitionList);
    }
}
