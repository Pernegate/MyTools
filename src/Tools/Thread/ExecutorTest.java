package Tools.Thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class ExecutorTest {

    private ExecutorService exec;
    private int cpuCoreNumber;
    private List<Future<Long>> tasks = new ArrayList<Future<Long>>();


    // 内部类
    /**
     * 大量数相加
     */
    class SumCalculator implements Callable<Long> {
        private int[] numbers;
        private int start;
        private int end;
        private Semaphore semaphore;

        public SumCalculator(final int[] numbers, int start, int end, Semaphore semaphore) {
            this.numbers = numbers;
            this.start = start;
            this.end = end;
            this.semaphore = semaphore;
        }

        public SumCalculator(final int[] numbers, int start, int end) {
            this.numbers = numbers;
            this.start = start;
            this.end = end;
        }

        @Override
        public Long call() throws Exception {
            if (null != semaphore) {
                semaphore.acquire();
            }

            Long sum = 0L;
            for (int i = start; i < end; i++) {
                for(long j =1 ;j < numbers[i];j++){
                    sum += numbers[i];
                }
            }
            if (null != semaphore) {
                semaphore.release();
            }
            return sum;
        }
    }
    //end


    public ExecutorTest() {
        cpuCoreNumber = Runtime.getRuntime().availableProcessors();
        System.out.println("计算机核心数" + cpuCoreNumber);
        exec = Executors.newFixedThreadPool(cpuCoreNumber);
    }

    public Long sum(final int[] numbers) {
        Semaphore semaphore = new Semaphore(8);
        // 根据CPU核心个数拆分任务，创建FutureTask并提交到Executor
        for (int i = 0; i < cpuCoreNumber; i++) {
            int increment = numbers.length / cpuCoreNumber + 1;
            int start = increment * i;
            int end = increment * i + increment;
            System.out.println(increment + ":" + start + ":" + end);
            if (end > numbers.length) {
                end = numbers.length;
            }
//            SumCalculator subCalc = new SumCalculator(numbers, start, end,semaphore);
            SumCalculator subCalc = new SumCalculator(numbers, start, end);
            FutureTask<Long> task = new FutureTask<>(subCalc);
            tasks.add(task);
            if (!exec.isShutdown()) {
                exec.submit(task);
            }
        }
        return getResult();
    }

    /**
     * 迭代每个只任务，获得部分和，相加返回
     *
     * @return
     */
    public Long getResult() {
        Long result = 0L;
        for (Future<Long> task : tasks) {
            try {
                // 如果计算未完成则阻塞
                Long subSum = task.get();
                result += subSum;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    public void close() {
        exec.shutdown();
    }

    public static void main(String[] args) {
        //大量数相加
        sumManyNumber();

    }

    public static void sumManyNumber() {
        //分界点1,0000,0000
        int[] numbers = new int[100];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = new Random().nextInt(1000000);
        }
        ExecutorTest calc = new ExecutorTest();
        long startTime = System.currentTimeMillis();
        Long sum = calc.sum(numbers);
        System.out.println(sum);
        calc.close();
        long endTime = System.currentTimeMillis();
        System.out.println("多线程程序运行时间： " + (endTime - startTime) + "ms");

        sum = 0L;
        startTime = System.currentTimeMillis();
        for (int i = 0; i < numbers.length; i++) {
            for(long j =1 ;j < numbers[i];j++){
                sum += numbers[i];
            }
        }
        endTime = System.currentTimeMillis();
        System.out.println(sum);
        System.out.println("单线程程序运行时间： " + (endTime - startTime) + "ms");
    }


}
