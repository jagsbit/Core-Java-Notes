package intermediate;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class CountOccuranceOfEachWordInString {
    static void main() {
        String str="I am a developer and a tester";
        Map<String,Long> map=Arrays.stream(str.split("\\s+"))
                .collect(Collectors.groupingBy(s->s,Collectors.counting()));
        System.out.println(map);
    }
}
