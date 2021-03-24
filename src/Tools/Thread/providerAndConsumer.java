package Tools.Thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

class Data<T> {
    private String id;
    private T node;

    public Data(String id, T node) {
        this.id = id;
        this.node = node;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public T getNode() {
        return node;
    }

    public void setNode(T node) {
        node = node;
    }
}

/**
 * 生产者
 */
class Provider implements Runnable {
    private BlockingQueue<Data> queue;
    private boolean isRun = true;
    private int productCount = 0;
    private int sleepTime = 0;

    public Provider(BlockingQueue<Data> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (isRun) {
            try {
                productCount++;
                Data data = new Data(Thread.currentThread().getName() + "-" + productCount, "数据");
                System.out.println("生产者" + Thread.currentThread().getName() + "生成了第" + productCount + "个数据！");
                while (!this.queue.offer(data, 1, TimeUnit.SECONDS)) {
                    System.out.println("加入队列失败！");
                    sleepTime = (sleepTime >= 10) ? 10 : sleepTime++;
                    Thread.sleep(sleepTime * 100);
                }
                sleepTime = 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        this.isRun = false;
    }
}

/**
 * 消费者
 */
class Consumer implements Runnable {
    private BlockingQueue<Data> queue;
    private boolean isRun = true;

    public Consumer(BlockingQueue<Data> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (isRun) {
            try {
                Data data = this.queue.take();
                System.out.println("消费者" + Thread.currentThread().getName() + "取出数据：" + data.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        this.isRun = false;
    }
}

public class providerAndConsumer {
    public static void main(String[] args) {
        int providerNums = 3;
        int consumerNums = 3;
        BlockingQueue<Data> queue = new LinkedBlockingDeque<>(10);
        List<Provider> providerList = new ArrayList<>(providerNums);
        List<Consumer> consumerList = new ArrayList<>(consumerNums);
        for (int i = 0; i < providerNums; i++) {
            providerList.add(new Provider(queue));
        }
        for (int i = 0; i < consumerNums; i++) {
            consumerList.add(new Consumer(queue));
        }
        ExecutorService es = Executors.newCachedThreadPool();
        providerList.forEach(provider -> es.submit(provider));
        consumerList.forEach(consumer -> es.submit(consumer));

        try {
            Thread.sleep(1 * 100);
        } catch (Exception e) {
            e.printStackTrace();
        }

        providerList.forEach(provider -> provider.stop());
        consumerList.forEach(consumer -> consumer.stop());
    }
}

