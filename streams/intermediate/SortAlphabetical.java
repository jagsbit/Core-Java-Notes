package intermediate;

import java.util.List;

public class SortAlphabetical {
    static void main() {
        List<String> list=List.of("Nitish","Anshu","Jagannath","Hamza");
        List<String> sortedList=list.stream().sorted((s1,s2)->s1.length()-s2.length()).toList();
        System.out.println(sortedList);
    }
}
