package intermediate;

import java.util.Map;
import java.util.stream.Collectors;

public class OccuranceOfEachDigit {
    static void main() {
        String str="hello 123 i am the 10";
        Map<Character,Long> map=
                str.chars().mapToObj(c->(char)c)
                        .filter(Character::isDigit)
                        .collect(Collectors.groupingBy(c->c,Collectors.counting()));
        System.out.println(map);
    }
}
