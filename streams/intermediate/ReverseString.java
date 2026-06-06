package intermediate;

import java.util.Arrays;

public class ReverseString {
    static void main() {
        String str = "abcd";
         String rev=Arrays.stream(str.split(""))
                        .reduce("",(a,b)->b+a);
         System.out.println(rev);


    }
}
