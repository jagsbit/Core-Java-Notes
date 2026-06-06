package intermediate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupList {
    static void main() {
        List<Person> people = List.of(
                new Person("Alice", 25),
                new Person("Bob", 30),
                new Person("Charlie", 25)
        );
        Map<Integer,List<Person>> group=people.stream().collect(Collectors.groupingBy(person -> person.age));
        System.out.println(group);
    }
}
class Person {
    String name;
    int age;
    // Constructor, getters, and setters
    Person(String name,int age){
        this.name=name;
        this.age=age;
    }
    public String toString(){
        return name+" : "+age;
    }
}
