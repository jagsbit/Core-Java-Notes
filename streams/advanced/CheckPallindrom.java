package advanced;

import java.util.stream.IntStream;

public class CheckPallindrom {
    static void main() {
        String str="katak";
        int n=str.length();
        boolean ans=IntStream.range(0,n)
                              .allMatch(i->str.charAt(i)==str.charAt(n-1-i));
        System.out.println(ans);

    }
}
