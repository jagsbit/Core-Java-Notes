package advanced;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MostFrequent {
    static void main() {
        List<String> words = List.of("apple", "banana", "apple", "orange", "banana", "apple");
        String most=words.stream()
                .collect(Collectors.groupingBy(s->s,Collectors.counting()))
                        .entrySet().stream()
                        .max((e1,e2)->Long.compare(e1.getValue(),e2.getValue()))
                                .map(e->e.getKey())
                                        .orElse(null);
        System.out.println(most);
    }
}
