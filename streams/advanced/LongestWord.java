package advanced;


import java.util.Arrays;

public class LongestWord {
    static void main() {
        String sen="I am a developer";

        String ans= Arrays.stream(sen.split("\\s+"))
                .sorted((s1,s2)->s2.length()-s1.length())
                .findFirst()
                .orElse(null);
        System.out.println(ans);
    }
}
