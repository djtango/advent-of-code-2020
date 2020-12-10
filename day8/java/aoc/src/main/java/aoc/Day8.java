package aoc;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.util.ArrayList;

public class Day8 {
    private static ArrayList<String> getInput() {
        // String[] ss = {
        //     "nop +0",
        //     "acc +1",
        //     "jmp +4",
        //     "acc +3",
        //     "jmp -3",
        //     "acc -99",
        //     "acc +1",
        //     "jmp -4",
        //     "acc +6"
        // };
        String[] ss = readFile("/tmp/aoc8").split("\n");
        ArrayList<String> input = new ArrayList();
        int i;
        for (i = 0; i < ss.length; i += 1) {
            input.add(ss[i]);
        }
        return input;
    }

    public static String readFile(String filename) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Path f = new File(filename).toPath();
            Files.copy(f, baos);
            String out = baos.toString(StandardCharsets.UTF_8.name());

            return out;
        }
        catch (IOException e) {
            return "oops";
        }
    }

    public static void main( String[] args ) {
        ArrayList<String> input = getInput();
        int i;
        for ( i = 0; i < input.size(); i +=1 ) {
            System.out.println( input.get(i) );
        }
    }
}
