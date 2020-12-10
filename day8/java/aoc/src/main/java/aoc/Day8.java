package aoc;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.util.ArrayList;

public class Day8 {
    private static class State{
        public int acc;
        public int cmd;
        public ArrayList<Integer> cmds;
        public State() {
            this.acc = 0;
            this.cmd = 0;
            this.cmds = new ArrayList();
        }

        public State setAcc(int n) {
            this.acc = n;
            return this;
        }

        public State setCmd(int n) {
            this.cmd = n;
            this.cmds.add(n);
            return this;
        }

        public int getAcc() {
            return this.acc;
        }

        public ArrayList<Integer> getCmds() {
            return this.cmds;
        }

        public int getCmd() {
            return this.cmd;
        }
    }

    public State s;
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

    private void jmp(int i) {
        this.s.setCmd(this.s.getCmd() + i);
    }

    private void acc(int i) {
        this.s.setAcc(this.s.getAcc() + i);
        this.s.setCmd(this.s.getCmd() + 1);
    }

    private void nop(int i) {
        this.s.setCmd(this.s.getCmd() + 1);
    }

    private boolean isCycle() {
        ArrayList<Integer> cmds = this.s.getCmds();
        int cmd = this.s.getCmd();
        System.out.println( cmd );
        System.out.println( cmds.indexOf(cmd));
        System.out.println( cmds.lastIndexOf(cmd));
        return cmds.lastIndexOf(cmd) != (cmds.indexOf(cmd));
    }

    public static void main( String[] args ) {
        System.out.println("foo");
        Day8 d = new Day8();
        d.s = new State();
        ArrayList<String> input = getInput();
        int i = 0;
        int cmd = 0;
        int arg = 0;
        while (! d.isCycle() && i < 1000) {
            String[] line = input.get(cmd).split(" ");
            String instruction = line[0];
            arg = Integer.parseInt(line[1]);
            switch (instruction) {
                case "jmp": d.jmp(arg); break;
                case "acc": d.acc(arg); break;
                case "nop": d.nop(arg); break;
            }
            System.out.println( cmd );
            System.out.println( input.get(cmd) );
            i += 1;
            cmd = d.s.getCmd();
        }
        System.out.println(d.s.getAcc());
    }
}
