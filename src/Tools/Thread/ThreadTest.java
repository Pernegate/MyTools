package Tools.Thread;

import com.sun.corba.se.impl.orbutil.concurrent.Sync;

class ThreadDemo extends Thread {
    private Thread t;
    private String threadName;
    private Sync sync;
    private static int i = 0;
    private SetThread setThread;

    ThreadDemo(String name) {
        threadName = name;
        System.out.println("Creating " + threadName);
    }

    public void setThreadName(String name){
        threadName=name;
    }

     ThreadDemo(String name,SetThread setThread){
        threadName=name;
        this.setThread=setThread;
    }

    @Override
    public void run() {
        test();
    }

    public void test() {
        synchronized (this) {
            System.out.println("Running " + threadName);

            threadName = setThread.setThreadName(threadName);

            try {

                for (; i < 200; i++) {
                    System.out.println("Thread: " + threadName + ", " + i);
                    // 让线程睡眠一会
//                    Thread.sleep(50);
                }
            } catch (Exception e) {
                System.out.println("Thread " + threadName + " interrupted.");
            }
            System.out.println("Thread " + threadName + " exiting.");
        }
    }


    @Override
    public void start() {
        System.out.println("Starting " + threadName);
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }
}

 class SetThread{
    public String setThreadName(String name){
        return "Out—"+name;
    }
}


public class ThreadTest {

    public static void main(String args[]) throws InterruptedException {
        ThreadDemo T = new ThreadDemo("Thread-",new SetThread());
        for(int j=0;j<=10;j++){

            T.setThreadName("setThreadName-"+j);
            T.start();
        }
    }


}