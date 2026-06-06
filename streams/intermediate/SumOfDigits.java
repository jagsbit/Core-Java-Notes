package intermediate;

import java.util.Arrays;


public class SumOfDigits {
    static void main() {
        int num=123456;
        String nums=String.valueOf(num);
        String [] arr=nums.split("");
        int sum= Arrays.stream(arr)
                .map(s->Integer.valueOf(s))
                .reduce(0,Integer::sum);
        System.out.println(sum);

        int sum1=String.valueOf(num).chars()
                .map(ch->ch-'0')
                .sum();
        System.out.println(sum1);



    }
}
