package Tools.Thread;

public class synchronizedDemo implements Runnable {
    //共享资源(临界资源)
    static int i = 0;
    static int j = 0;
    private static String name;

    /**
     * synchronized 修饰实例方法
     */
    public  void increase(String tmp) throws InterruptedException {
        i++;
//        Thread.sleep(1 * 100);
        System.out.println(tmp + ": " + i);
    }

    @Override
    public void run() {
        try {
            String tmp;
            System.out.println("I am running");
//            synchronized (this) {
                tmp = "Thread" + j;
                j++;
//            }
            for (int j = 0; j < 50; j++) {
                increase(tmp);
            }

//
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {


        for (int i = 0; i < 3; i++) {
            synchronizedDemo instance = new synchronizedDemo();
            long startTime = System.currentTimeMillis();
            Thread t = new Thread(instance);
            t.start();
            t.join();
            long endTime = System.currentTimeMillis();
            System.out.println("Thread-" + i + "：" + (endTime - startTime) + "ms");
        }
        System.out.println(i);
    }

}
