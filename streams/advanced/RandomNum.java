package advanced;

import java.util.List;
import java.util.stream.Stream;

public class RandomNum {
    static void main() {
        List<Double> nums= Stream.generate(Math::random).limit(10).toList();
        System.out.println(nums);


    }
}
