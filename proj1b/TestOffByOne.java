import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByOne {
    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByOne();

    // Your tests go here.
    @Test
    public void testEqualChars(){
        OffByOne A = new OffByOne();
        assertTrue(A.equalChars('A', 'B'));
        assertTrue(A.equalChars('B', 'A'));
        assertFalse(A.equalChars('A', 'b'));
        assertFalse(A.equalChars('A', 'a'));
        assertFalse(A.equalChars('%', '#'));
    }
}
