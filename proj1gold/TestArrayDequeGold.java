import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.LinkedList;

public class TestArrayDequeGold {
    @Test
    public void testGeneral(){
        StudentArrayDeque<Integer> A = new StudentArrayDeque<Integer>();
        ArrayDequeSolution<Integer> expect = new ArrayDequeSolution<Integer>();
        String output = "", temp;
        for (int i = 0; i < 100; i += 1) {
            double numberBetweenZeroAndOne = StdRandom.uniform();
            LinkedList<String> warnings = new LinkedList<>();
            if (numberBetweenZeroAndOne < 0.25) {
                A.addLast(i);
                expect.addLast(i);
                temp = output + ("addLast(" + i +")\n");
                output = temp;
            } else if (numberBetweenZeroAndOne < 0.5) {
                A.addFirst(i);
                expect.addFirst(i);
                temp = output + ("addFirst(" + i +")\n");
                output = temp;
            } else if (numberBetweenZeroAndOne < 0.75) {
                Integer actual= A.removeFirst();
                Integer E = expect.removeFirst();
                assertEquals(output + "removeFirst(), student was " + actual + ", correct was " + E + "\n", actual, E);
            } else {
                Integer actual= A.removeLast();
                Integer E = expect.removeLast();
                assertEquals(output + "removeLast(), student was " + actual + ", correct was " + E + "\n", actual, E);
            }
            assertEquals(A.size(), expect.size());
        }
    }
}
