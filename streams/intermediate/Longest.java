package intermediate;

import java.util.Arrays;
/** 
Input - I am learning streams API in Java
output- learning
**/

public class Longest {

    public static void main(String[] args) {
        String str="I am learning streams API in Java";
        String ans=Arrays.stream(str.split("\\s+")).max((s1,s2)->Integer.compare(s1.length(),s2.length())).get();
        System.out.println(ans);
    }
}