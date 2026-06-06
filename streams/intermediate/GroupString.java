package intermediate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupString {
    static void main() {
        List<String> list=List.of("Ram","Hari","ansh","sky");

        Map<Integer,List<String>> map=list
                                        .stream()
                                        .collect(Collectors.groupingBy(String::length));
        System.out.println(map);

    }
}
