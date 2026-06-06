package beginner;

import java.util.*;

/**
   Input - swiss
   output -w
 */
public class FirstNonRepeating {
    public static void main() {
         String str="swiss";

         String[] arr=str.split("");

        Map<String,Integer> map=new LinkedHashMap<>();
        for(String s:arr) map.put(s,map.getOrDefault(s,0)+1);

        String result=map.entrySet().stream()
                .filter(entry->entry.getValue()==1)
                .map(Map.Entry::getKey)
                .findFirst()
                .get();
        System.out.println(result);


    }
}
