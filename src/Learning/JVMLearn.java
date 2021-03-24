package Learning;

import java.util.ArrayList;
import java.util.List;

public class JVMLearn {

    /**
     * 100KB
     */
    byte[] buffer = new byte[100 * 1024];

    public static void main(String[] args) {
        watchHeap();
    }

    public static void watchHeap(){
        List<JVMLearn> heapList = new ArrayList<>(16);
        while (true){
            heapList.add(new JVMLearn());
            try {
                Thread.sleep(20);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
