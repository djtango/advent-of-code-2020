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
        public int checkpoint;
        public String instruction;
        public ArrayList<Integer> cmds;
        public ArrayList<String> input;
        public State() {
            this.instruction = "";
            this.arg = 0;
            this.acc = 0;
            this.cmd = 0;
            this.checkpoint = -1;
            this.cmds = new ArrayList<Integer>();
            this.input = new ArrayList<String>();
        }

        public void setInput(ArrayList<String> input) {
            this.input = input;
        }

        public int getInputSize() {
            return this.input.size();
        }

        public String[] getLine(int n) {
            return this.input.get(n).split(" ");
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

        public int getCheckpoint() {
            return this.checkpoint;
        }

        public void setCheckpoint(int checkpoint) {
            this.checkpoint = checkpoint;
        }
    }

    public State s;
    private static ArrayList<String> getInput() {
        String[] ss = readFile("/tmp/aoc8").split("\n");
        // String[] ss = {"nop +4", "acc +2", "jmp -1", "jump -2", "acc +10"};
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
        System.out.println("rewind");
        ArrayList<Integer> cmds = this.s.getCmds();
        System.out.println(cmds.toString());
        cmds.remove(cmds.size() - 1);
        System.out.println(cmds.toString());
        // parseLine(s.getLine(cmds.get(cmds.size() - 1)));
        switch (this.s.getInstruction()) {
            case "jmp": this.unJmp(this.s.getArg()); break;
            case "acc": this.unAcc(this.s.getArg()); break;
            case "nop": this.unNop(this.s.getArg()); break;
        }
    }

    private void parseLine(String[] line) {
        this.s.setInstruction(line[0]);
        this.s.setArg(Integer.parseInt(line[1]));
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
        Day8 d = new Day8();
        d.s = new State();
        d.s.setInput(getInput());
        int i = 0;
        int cmd = 0;
        d.s.setCmd(cmd);
        while (cmd < d.s.getInputSize() && i < 1000000) {

            System.out.println("##################### START #################");
            System.out.println(String.format("i %1$s", i));
            System.out.println(String.format("cmds %1$s", d.s.getCmds().toString()));
            System.out.println(String.format("cmd %1$s", cmd));
            System.out.println(String.format("line %1$s %2$s", d.s.getInstruction(), d.s.getArg()));
            System.out.println(String.format("checkpoint %1$s", d.s.getCheckpoint()));
            System.out.println(String.format("acc %1$s", d.s.getAcc()));
            System.out.println("##################### END #################");

            if (! d.isCycle()) {
                d.parseLine(d.s.getLine(cmd));
                d.runInstruction();
                i += 1;
                cmd = d.s.getCmd();
            } else {
                System.out.println("cycle");
                // flip fails, rewind until last flip
                // undoflip
                // rewind until next jmp or nop
                // flip
                // go
                int j;
                if (d.s.getCheckpoint() != -1) {
                    for (j = d.s.getCmds().size() - 1; j > d.s.getCheckpoint(); j -= 1) {
                        d.rewind();
                        cmd = d.s.getCmd();
                        d.parseLine(d.s.getLine(cmd));
                    }
                    d.flipCommand();
                    d.s.setCheckpoint(-1);
                }
                while (d.s.getInstruction() == "acc") {
                    d.rewind();
                    cmd = d.s.getCmd();
                    d.parseLine(d.s.getLine(cmd));
                }
                // boundary here - rewinds then flip or flip then rewind
                d.flipCommand();
                d.s.setCheckpoint(d.s.getCmds().size() - 1);
                d.runInstruction();
                cmd = d.s.getCmd();
                i += 1;
            }
        }
        System.out.println(d.s.getAcc());
    }
}
