package intermediate;

import java.util.List;

public class LongestString {
    static void main() {
        List<String> list=List.of("hamza","Anshuman","Vaibav","Venu");
        String longest=list.stream().sorted((s1,s2)->s2.length()-s1.length())
                .findFirst()
                .orElse("default");
        System.out.println(longest);
    }
}
