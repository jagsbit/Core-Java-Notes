package intermediate;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 Input - dabcdabc
 output- dabc

*/
public class RemoveDuplicates {
    public static void main(String[] args) {
        String str="dabcdabc";
        System.out.println(str);


        // character stream not available

       /** char[] arr=str.toCharArray();

        Stream<Character> stream=Arrays.stream(arr);

      */

        String[] arr=str.split("");
        Stream<String> stream=Arrays.stream(arr);
        StringBuilder sb=new StringBuilder();
        stream.distinct().forEach((ch)->{
            sb.append(ch);
        });

        System.out.println(sb.toString());

    } 
}
