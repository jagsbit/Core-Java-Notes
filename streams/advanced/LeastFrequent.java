package advanced;

import java.util.List;
import java.util.stream.Collectors;

public class LeastFrequent {
    static void main() {
        List<String> words = List.of("apple", "banana", "apple", "orange", "banana", "apple");
        String leastFrequent=words.stream()
                .collect(Collectors.groupingBy(s->s,Collectors.counting()))
                .entrySet().stream()
                .min((e1,e2)->Long.compare(e1.getValue(),e2.getValue()))
                .map(e->e.getKey())
                .orElse(null);
        System.out.println(leastFrequent);
    }
}
