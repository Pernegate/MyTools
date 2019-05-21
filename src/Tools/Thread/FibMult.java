package Tools.Thread;


import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class FibMult {

    public static void main(String[] args) throws InterruptedException {
        long result;
        int n=50;
        long startTime = System.currentTimeMillis();
        result=fib(n);
        long endTime = System.currentTimeMillis();
        System.out.println(result);
        System.out.println("单线程程序运行时间： " + (endTime - startTime) + "ms");

        startTime = System.currentTimeMillis();
        result=fibMult(n);
        endTime = System.currentTimeMillis();
        System.out.println(result);
        System.out.println("多线程程序运行时间： " + (endTime - startTime) + "ms");
    }

    public static Long fib(Integer n) {
        Long x;
        Long y;
        if (1 >= n) {
            return n.longValue();
        } else {
            x = fib(n - 1);
            y = fib(n - 2);
        }
        return x + y;
    }

    public static long fibMult(Integer n) throws InterruptedException {
        final long[] x = new long[1];
        final long[] y = new long[1];
        if (1 >= n) {
            return n.longValue();
        } else {
            CountDownLatch latch = new CountDownLatch(2);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        x[0] = fib(n - 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    latch.countDown();
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        y[0] = fib(n - 2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    latch.countDown();
                }
            }).start();
            latch.await();
        }
        return x[0] + y[0];
    }
}

