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
        public int arg;
        public String instruction;
        public ArrayList<Integer> cmds;
        public State() {
            this.instruction = "";
            this.arg = 0;
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

        public String getInstruction() {
            return this.instruction;
        }

        public int getArg() {
            return this.arg;
        }

        public void setInstruction(String instruction) {
            this.instruction = instruction;
        }

        public void setArg(int arg) {
            this.arg = arg;
        }
    }

    public State s;
    private static ArrayList<String> getInput() {
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
        System.out.println("############## cycle detected ########");
        System.out.println( cmd );
        System.out.println( cmds.indexOf(cmd));
        System.out.println( cmds.lastIndexOf(cmd));
        System.out.println("############## end cycle detected ########");
        return cmds.lastIndexOf(cmd) != (cmds.indexOf(cmd));
    }

    private void unJmp(int i) {
        this.s.setCmd(this.s.getCmd() - i);
    }

    private void unAcc(int i) {
        this.s.setAcc(this.s.getAcc() - i);
        this.s.setCmd(this.s.getCmd() - 1);
    }

    private void unNop(int i) {
        this.s.setCmd(this.s.getCmd() - 1);
    }

    private void rewind() {
        ArrayList<Integer> cmds = this.s.getCmds();
        cmds.remove(cmds.size() - 1);
        switch (this.s.getInstruction()) {
            case "jmp": this.unJmp(this.s.getArg()); break;
            case "acc": this.unAcc(this.s.getArg()); break;
            case "nop": this.unNop(this.s.getArg()); break;
        }
    }

    private void flipCommand() {
        String instruction = "";
        switch (this.s.getInstruction()) {
            case "jmp": instruction = "nop"; break;
            case "nop": instruction = "jmp"; break;
            case "acc": instruction = "acc"; break;
        }
        this.s.setInstruction(instruction);
    }

    private void runInstruction() {
        switch (this.s.getInstruction()) {
            case "jmp": this.jmp(this.s.getArg()); break;
            case "acc": this.acc(this.s.getArg()); break;
            case "nop": this.nop(this.s.getArg()); break;
        }
    }

    public static void main( String[] args ) {
        System.out.println("foo");
        Day8 d = new Day8();
        d.s = new State();
        ArrayList<String> input = getInput();
        int i = 0;
        int cmd = 0;
        while (cmd < input.size() && i < 1000000) {
            if (! d.isCycle()) {
                String[] line = input.get(cmd).split(" ");
                d.s.setInstruction(line[0]);
                d.s.setArg(Integer.parseInt(line[1]));
                d.runInstruction();
                System.out.println( cmd );
                System.out.println( input.get(cmd) );
                i += 1;
                cmd = d.s.getCmd();
            } {
                d.rewind();
                d.flipCommand();
                d.runInstruction();
            }
        }
        System.out.println(d.s.getAcc());
    }
}
