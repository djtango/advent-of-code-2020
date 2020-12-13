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
        public ArrayList<Integer> flipped;
        public State() {
            instruction = "";
            arg = 0;
            acc = 0;
            cmd = 0;
            checkpoint = -1;
            cmds = new ArrayList<Integer>();
            input = new ArrayList<String>();
            flipped = new ArrayList<Integer>();
        }

        public void setInput(ArrayList<String> input) {
            this.input = input;
        }

        public int getInputSize() {
            return input.size();
        }

        public boolean isFlipped(int i) {
            return flipped.indexOf(i) > -1;
        }

        public void setFlipped(int i) {
            flipped.add(i);
        }

        public String[] getLine(int n) {
            return this.input.get(n).split(" ");
        }

        public State setAcc(int n) {
            acc = n;
            return this;
        }

        public State setCmd(int n) {
            cmd = n;
            return this;
        }

        public int getAcc() {
            return acc;
        }

        public ArrayList<Integer> getCmds() {
            return cmds;
        }

        public int getCmd() {
            return cmd;
        }

        public String getInstruction() {
            return instruction;
        }

        public int getArg() {
            return arg;
        }

        public void setInstruction(String instruction) {
            this.instruction = instruction;
        }

        public void setArg(int arg) {
            this.arg = arg;
        }

        public int getCheckpoint() {
            return checkpoint;
        }

        public void setCheckpoint(int checkpoint) {
            this.checkpoint = checkpoint;
        }

        public int getLastCmd() {
            return cmds.get(cmds.size() - 1);
        }

        public void removeLastCmd() {
            cmds.remove(cmds.size() - 1);
        }
    }

    public State s;
    private static ArrayList<String> getInput() {
        String[] ss = readFile("/tmp/aoc8").split("\n");
        // String[] ss = readFile("/tmp/aoc82").split("\n");
        // String[] ss = {"nop +6", "acc +222", "acc +22", "acc +2", "jmp -1", "jmp -2", "acc +10"};
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
        s.setCmd(s.getCmd() + i);
    }

    private void acc(int i) {
        s.setAcc(s.getAcc() + i);
        s.setCmd(s.getCmd() + 1);
    }

    private void nop(int i) {
        s.setCmd(s.getCmd() + 1);
    }

    private boolean isCycle() {
        ArrayList<Integer> cmds = s.getCmds();
        int cmd = s.getCmd();
        return cmds.indexOf(cmd) > -1;
    }

    private void unJmp(int i) {
        s.setCmd(s.getCmd() - i);
    }

    private void unAcc(int i) {
        s.setAcc(s.getAcc() - i);
        s.setCmd(s.getCmd() - 1);
    }

    private void unNop(int i) {
        s.setCmd(s.getCmd() - 1);
    }

    private void rewind() {
        String before = s.getCmds().toString();
        parseLine(s.getLine(s.getLastCmd()));
        switch (s.getInstruction()) {
            case "jmp": unJmp(s.getArg()); break;
            case "acc": unAcc(s.getArg()); break;
            case "nop": unNop(s.getArg()); break;
        }
        s.removeLastCmd();
        String after = s.getCmds().toString();
    }

    private void parseLine(String[] line) {
        s.setInstruction(line[0]);
        s.setArg(Integer.parseInt(line[1]));
    }

    private void flipCommand() {
        String instruction = "";
        switch (s.getInstruction()) {
            case "jmp": instruction = "nop"; break;
            case "nop": instruction = "jmp"; break;
            case "acc": instruction = "acc"; break;
        }
        s.setInstruction(instruction);
        s.setFlipped(s.getCmd());
    }

    private void undoFlip() {
        parseLine(s.getLine(s.getLastCmd()));
        flipCommand();
        switch (s.getInstruction()) {
            case "jmp": unJmp(s.getArg()); break;
            case "acc": unAcc(s.getArg()); break;
            case "nop": unNop(s.getArg()); break;
        }
        s.removeLastCmd();
        s.setCheckpoint(-1);
        parseLine(s.getLine(s.getLastCmd()));
    }

    private void runInstruction() {
        switch (s.getInstruction()) {
            case "jmp": jmp(s.getArg()); break;
            case "acc": acc(s.getArg()); break;
            case "nop": nop(s.getArg()); break;
        }
    }

    public static void main( String[] args ) {
        Day8 d = new Day8();
        d.s = new State();
        d.s.setInput(getInput());
        int i = 0;
        int cmd = 0;
        d.s.setCmd(cmd);
        d.parseLine(d.s.getLine(cmd));
        while (cmd < d.s.getInputSize() && i < 2500) {

            if (! d.isCycle()) {
                // world's most convoluted DFS algorithm
                d.s.getCmds().add(d.s.getCmd());
                d.runInstruction();
                i += 1;
                cmd = d.s.getCmd();
                if (cmd != d.s.getInputSize()) {
                    d.parseLine(d.s.getLine(cmd));
                }
            } else {
                int j;
                if (d.s.getCheckpoint() != -1) {
                    for (j = d.s.getCmds().size() - 1; j > d.s.getCheckpoint(); j -= 1) {
                        d.rewind();
                        cmd = d.s.getCmd();
                        d.parseLine(d.s.getLine(cmd));
                    }
                    d.undoFlip();
                } else {
                    d.rewind(); // wtf
                    cmd = d.s.getCmd();
                    d.parseLine(d.s.getLine(cmd));
                };
                while (d.s.getInstruction().equals("acc") || d.s.isFlipped(cmd)) {
                    d.rewind();
                    cmd = d.s.getCmd();
                    d.parseLine(d.s.getLine(cmd));
                }
                d.flipCommand();
                d.s.setCheckpoint(d.s.getCmds().size());
                d.s.getCmds().add(d.s.getCmd());
                d.runInstruction();
                cmd = d.s.getCmd();
                if (cmd != d.s.getInputSize()) {
                    d.parseLine(d.s.getLine(cmd));
                }
                i += 1;
            }
        }
        System.out.println(d.s.getCmds().toString());
        System.out.println(d.s.getAcc());
        System.out.println(d.s.getCheckpoint());
    }
}
