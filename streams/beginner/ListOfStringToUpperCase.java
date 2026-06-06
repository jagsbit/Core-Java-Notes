package beginner;

import java.util.List;

public class ListOfStringToUpperCase {
    static void main() {
        List<String> list=List.of("anshu","jagannath","hamza","rehman");

        List<String> upperCaseList=list.stream().map(String::toUpperCase).toList();
        System.out.println(upperCaseList);
    }
}
