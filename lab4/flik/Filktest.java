package flik;

import static org.junit.Assert.*;
import org.junit.Test;

public class Filktest {

    @Test
    public void testSameNumbers() {

        assertTrue(Flik.isSameNumber(1, 1)); // 测试相同的基本值
        assertFalse(Flik.isSameNumber(1, 2)); // 测试不同的基本值

        assertFalse(Flik.isSameNumber(null, 1)); // null 和非 null
        assertFalse(Flik.isSameNumber(1, null)); // 非 null 和 null
        assertTrue(Flik.isSameNumber(null, null)); // 两个 null
    }
}