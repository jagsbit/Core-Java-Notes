package advanced;

import java.util.List;
import java.util.stream.IntStream;

public class LongestPallindrom {
    static void main() {
        List<String> list=List.of("why","aba","ada","madam");
        String ans=list.stream()
                .filter(s-> IntStream.range(0,s.length()/2).allMatch(i->s.charAt(i)==s.charAt(s.length()-1-i)))
                .sorted((s1,s2)->s1.length()-s2.length())
                        .findFirst().orElse(null);
        System.out.println(ans);

    }
}
