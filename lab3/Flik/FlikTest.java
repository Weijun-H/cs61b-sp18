import static org.junit.Assert.*;

import org.junit.Test;

public class FlikTest {

    @Test
    public void testIsSameNumber() {
        int A = 128;
        int B = 128;
        assertFalse(Flik.isSameNumber(1, 2));
    }
}
