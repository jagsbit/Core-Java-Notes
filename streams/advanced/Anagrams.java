package advanced;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Anagrams {
    static void main() {
        List<String> words=List.of("listen", "silent", "enlist", "google", "inlets");
        String target="silent";
        char[] chrs=target.toCharArray();
        Arrays.sort(chrs);
        String sorted_target=new String(chrs);

        List<String> ans= words.stream().filter(s->{
                  char [] temp=s.toCharArray();
                  Arrays.sort(temp);
                  String str=new String(temp);
                  return str.equals(sorted_target);
        })
        .toList();
        System.out.println(ans);
    }
}
