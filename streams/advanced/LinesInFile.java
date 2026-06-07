package advanced;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LinesInFile {
    static void main() throws IOException {
        Path path= Paths.get("sample.txt");
        long lineCount= Files.lines(path).count();
        System.out.println(lineCount);

    }
}
