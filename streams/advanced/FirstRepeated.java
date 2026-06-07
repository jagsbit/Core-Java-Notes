package advanced;

import java.util.LinkedHashMap;
import java.util.stream.Collectors;

public class FirstRepeated {
    static void main() {
        String str="abccddeefff";
        char ch=str.chars()
                .mapToObj(c->(char)c)
                .collect(Collectors.groupingBy(c->c, LinkedHashMap::new,Collectors.counting()))
                .entrySet().stream()
                .filter(e->e.getValue()>1)
                .map(e->e.getKey())
                .findFirst()
                .orElse(null);

        System.out.println(ch);

    }
}
