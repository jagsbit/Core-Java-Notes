package intermediate;

import java.util.Map;
import java.util.stream.Collectors;

public class OccuranceOfEachChar {
    static void main() {
        String str="abcdabcd";
         Map<Character,Long> map=str.chars()
                 .mapToObj(c->(char)c)
                 .collect(Collectors.groupingBy(c->c,Collectors.counting()));
        System.out.println(map);


    }
}
