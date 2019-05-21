package Tools.Thread;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class TestMergeSort {

//    private ExecutorService exec;
//    private int cpuCoreNumber;
//    private List<Future<Long>> tasks = new ArrayList<Future<Long>>();
//
//    private CountDownLatch latch;
//
//    class MergeCalculator implements Callable<Long> {
//        int[] a ;
//
//        public MergeCalculator(int[] a){
//            this.a=a;
//        }
//
//        @Override
//        public Long call() throws Exception {
//            MergeSort.sort(a);
//            latch.countDown();
//            return null;
//        }
//    }
//
//    public Long mergeSort(final int[] a) {
//        Semaphore semaphore = new Semaphore(8);
//        // 根据CPU核心个数拆分任务，创建FutureTask并提交到Executor
//        for (int i = 0; i < cpuCoreNumber; i++) {
////            SumCalculator subCalc = new SumCalculator(numbers, start, end,semaphore);
//            MergeCalculator mergeCalculator = new MergeCalculator(a);
//            FutureTask<Long> task = new FutureTask<>(mergeCalculator);
//            tasks.add(task);
//            if (!exec.isShutdown()) {
//                exec.submit(task);
//            }
//        }
//        return null;
//    }
//
//    public void ExecutorTest() {
//        cpuCoreNumber = Runtime.getRuntime().availableProcessors();
//        System.out.println("计算机核心数" + cpuCoreNumber);
//        exec = Executors.newFixedThreadPool(cpuCoreNumber);
//    }

    public static void main(String[] args) throws InterruptedException {

        //单线程
        long time1 = System.currentTimeMillis();
        int length = 20000000;
        int[] array = new int[length];
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int x = random.nextInt();
            array[i] = x;
        }

        long time2 = System.currentTimeMillis();
        System.out.println("生成数组时间：" + (time2 - time1) + "毫秒");
        //归并排序
        MergeSort.sort(array);
        long time3 = System.currentTimeMillis();
        System.out.println("单线程归并排序时间" + (time3 - time2) + "毫秒");
        System.out.println();

        //多线程
        time2 = System.currentTimeMillis();

        int minLength = length / 8;
        int[][] a = new int[8][minLength];

        for (int i = 0; i < 8; i++) {
            int start = minLength * i;
            int end = minLength * (i + 1);
            for (int j = start, k = 0; j < end; j++, k++) {
                a[i][k] = array[j];
            }
        }

        //使用CountDownLatch来确保两个子线程都处理完毕后才执行最后的归并操作
        CountDownLatch latch = new CountDownLatch(8);
        for(int i=0;i<8;i++){
            final int tmp=i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MergeSort.sort(a[tmp]);
                    latch.countDown();
                }

            }).start();
        }

        //等待
        latch.await();

        int[] arr = new int[length];
        for(int i=0;i<8;i++){
            int[] tmp = new int[length];
            merge(arr,a[i] , tmp);
            arr=tmp;
        }
        //合并两个有序序列


        time3 = System.currentTimeMillis();
        System.out.println("多线程归并排序时间：" + (time3 - time2) + "毫秒");
    }


    //合并序列
    private static void merge(int[] a1, int[] a2, int[] tmpArray) {
        int length1 = a1.length;
        int length2 = a2.length;

        int left = 0;
        int right = 0;
        int pos = 0;

        while (left < length1 && right < length2) {
            if(0 == a1[left+1]){
                break;
            }
            if (a1[left] <= a2[right]) {
                tmpArray[pos] = a1[left];
                left++;
            } else {
                tmpArray[pos] = a2[right];
                right++;
            }
            pos++;
        }
        while (left < length1) {
            if(0 == a1[left+1]){
                break;
            }
            tmpArray[pos++] = a1[left++];
        }

        while (right < length2) {
            tmpArray[pos++] = a2[right++];
        }

    }
}

