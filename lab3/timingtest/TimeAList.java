package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        // TODO: YOUR CODE HERE

        /*
        1. 准备工作：创建存储数据的容器

       Ns：存储每次测试时的 N 值（即添加了多少个元素）。这些 N 值对应数组中的大小。
       times：存储每次测试的耗时。每次我们向 AList 中添加 N 个元素时，会记录所花的时间，以秒为单位。
       opCounts：存储每次操作的次数。因为每次操作的次数与 N 相同，意味着我们调用了 N 次 addLast 方法。
         */
        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> ops = new AList<>();

        //2. 指定要测试的不同数据规模
        int []size = {1000,2000,4000,8000,16000,32000,64000,128000};

        /*
        循环进行测试
        循环遍历每个 N 值：对于每个 N 值（1000、2000、4000...），我们都会执行一次测试。

      AList<Integer> testList = new AList<>();：每次测试时，我们都会创建一个新的 AList 实例，这样我们能确保测试是从空列表开始的。
      Stopwatch sw = new Stopwatch();：创建一个计时器实例，用于记录从开始向 AList 添加元素到操作结束的时间。这个计时器通过调用 sw.elapsedTime() 来获得经过的秒数。

      for (int i = 0; i < N; i++)：这是一个简单的循环，向 testList 中添加 N 个整数（从 0 到 N-1）。每次调用 addLast 方法将新元素添加到列表的末尾。

       记录时间：当我们完成对 N 个元素的添加后，调用 sw.elapsedTime() 来获取整个操作花费的时间，结果以秒为单位。
         */
        for(int N:size){
            AList<Integer> testlist= new AList<>();
            Stopwatch sw = new Stopwatch();

            for(int i = 0;i<N;i++){
                testlist.addLast(i);
            }

            double timesconds = sw.elapsedTime();

            Ns.addLast(N);
            times.addLast(timesconds);
            ops.addLast(N);
        }

        //将 N 添加到 Ns 列表中：Ns.addLast(N) 将当前测试的 N 值存储起来。
        //将消耗的时间添加到 times 列表中：times.addLast(timeInSeconds) 将本次测试消耗的时间存储起来。
        //将操作次数添加到 opCounts 列表中：opCounts.addLast(N) 存储当前测试的操作次数，因为每次测试中我们添加了 N 次元素。
        printTimingTable(Ns,times,ops);





    }
}
