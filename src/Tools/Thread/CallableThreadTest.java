package Tools.Thread;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class CallableThreadTest implements Callable<Integer> {
    public static void main(String[] args)
    {
        CallableThreadTest ctt = new CallableThreadTest();

        for(int i = 0;i < 100;i++)
        {
//            System.out.println(Thread.currentThread().getName()+" 的循环变量i的值"+i);

            if(0 == i % 10){
                FutureTask<Integer> ft = new FutureTask<>(ctt);
                new Thread(ft,"有返回值的线程"+i).start();
            }

        }
//        try
//        {
//            System.out.println("子线程的返回值："+ft.get());
//        } catch (InterruptedException e)
//        {
//            e.printStackTrace();
//        } catch (Exception e)
//        {
//            e.printStackTrace();
//        }

    }

    @Override
    public Integer call() throws Exception
    {
        int i = 0;

            for(;i<20;i++)
            {
                System.out.println(Thread.currentThread().getName()+": "+i);
                if(10 == i){
                    Thread.yield();
                }
            }
        return i;
    }
}
