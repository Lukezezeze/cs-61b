package randomizedtest;

//import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
  @Test
  public void testThreeAddThreeRemove() {
    AListNoResizing<Integer> correct = new AListNoResizing<>();
    BuggyAList<Integer> broken = new BuggyAList<>();

    correct.addLast(5);
    correct.addLast(10);
    correct.addLast(15);

    broken.addLast(5);
    broken.addLast(10);
    broken.addLast(15);

    assertEquals(correct.size(), broken.size());

    assertEquals(correct.removeLast(), broken.removeLast());
    assertEquals(correct.removeLast(), broken.removeLast());
    assertEquals(correct.removeLast(), broken.removeLast());
  }

  @Test
  public void randomizedTest() {
    BuggyAList<Integer> ba = new BuggyAList<>();
    AListNoResizing<Integer> anr = new AListNoResizing<>();

    int N = 5000;
    for (int i = 0; i < N; i += 1) {
      int operationNumber = StdRandom.uniform(0, 4);
      if (operationNumber == 0) {
        // addLast
        int randVal = StdRandom.uniform(0, 100);
        ba.addLast(randVal);
        anr.addLast(randVal);
        System.out.println("addLast(" + randVal + ")");
      } else if (operationNumber == 1) {
        // size
        int size = ba.size();
        int size2 = anr.size();
        System.out.println("size: " + size + " size2: " + size2);
        assertEquals(size, size2);
      } else if (operationNumber == 2) {
        // getLast
        if (ba.size() > 0 && anr.size() > 0) {
          int ret = ba.getLast();
          int ret2 = anr.getLast();
          System.out.println("getLast(" + ret + ")" + " getLast(" + ret2 + ")");
          assertEquals(ret, ret2);
        }
      } else if (operationNumber == 3) {
        // removeLast
        if (ba.size() > 0 && anr.size() > 0) {
          int ret = ba.removeLast();
          int ret2 = anr.removeLast();
          System.out.println("removeLast(" + ret + ")" + " removeLast(" + ret2 + ")");
          assertEquals(ret, ret2);
        }
      }
    }
  }
}