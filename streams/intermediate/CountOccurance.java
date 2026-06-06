package intermediate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CountOccurance {
    static void main() {
        List<String> words = List.of("apple", "banana", "apple", "orange");
        Map<String,Long> freq=words.stream()
                .collect(Collectors.groupingBy(s->s,Collectors.counting()));
        System.out.println(freq);

    }
}
