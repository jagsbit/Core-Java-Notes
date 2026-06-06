package intermediate;

import java.util.Map;
import java.util.stream.Collectors;

public class OccurranceOFEachVowel {
    static void main() {
        String str="hello world";
        Map<Character,Long> map=str.chars()
                .mapToObj(c->(char)c)
                .filter(OccurranceOFEachVowel::isVowel)
                .collect(Collectors.groupingBy(c->c,Collectors.counting()));
        System.out.println(map);
    }
    public static boolean isVowel(char ch){
        return ch=='a'||ch=='e'||ch=='i'||ch=='o'||ch=='u';
    }
}
