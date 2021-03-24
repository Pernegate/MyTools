package Learning;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

public class NIOLearn {

    private static final String DISK_URL = "C:\\Users\\37645\\Desktop\\";
    private static final String READ_FILE_NAME_JPG = "1587314889(1).jpg";
    private static final String WRITE_FILE_NAME_JPG = "1587314889(2).jpg";
    private static final String READ_FILE_NAME_TXT = "外出学习数据结构.txt";
    private static final String WRITE_FILE_NAME_TXT = "外出学习数据结构_2.txt";


    public static void main(String[] args) {
        channelScatterAndGather();
    }

    /**
     * 非直接缓冲区
     */
    public static void notDirectBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        String strTmp = "abcdefgh";
        // 存数据(position变为8，limit、capacity都还是1024)
        buffer.put(strTmp.getBytes());
        // 切换读取数据模式 (position变为0，limit变为8，capacity还是1024)
        buffer.flip();
        // 读取数据 (position=8，limit=8，capacity=1024)
//        byte[] dst = new byte[buffer.limit()];
//        buffer.get(dst);
        byte[] dst2 = new byte[buffer.limit()];
        buffer.get(dst2, 0, 3);
        // 标记position的位置
        buffer.mark();
        buffer.get(dst2, 3, 3);
        // 判断缓冲区中是否有剩余数据并获取可以操作的数量
        if (buffer.hasRemaining()) {
            System.out.println(buffer.remaining());
        }
        // reser() 恢复到mark位置, position = 3
        buffer.reset();
        // rewind() 重复读数据 (position=0，limit=8，capacity=1024)
        buffer.rewind();
        // clear() 清空缓冲区,但是缓冲区数据还在，处于被遗忘状态  （get相应位置数据还是可以读出）（position=0，limit=1024，capacity=1024）
        buffer.clear();
    }

    public static void directBuffer() {
        ByteBuffer directBuffer = ByteBuffer.allocateDirect(1024);
        // 判断是否直接缓冲区
        System.out.println(directBuffer.isDirect());
    }

    /**
     * 通道的分散与聚集
     */
    public static void channelScatterAndGather() {
        try {
            int bufferSize = 2 * 1024;

            // 解决中文乱码问题
            Charset charset = Charset.forName("GBK");
            CharsetDecoder decoder = charset.newDecoder();

            RandomAccessFile raf = new RandomAccessFile(DISK_URL + READ_FILE_NAME_TXT, "rw");
            FileChannel inFileChannel = raf.getChannel();

            ByteBuffer bb1 = ByteBuffer.allocate(bufferSize);
            ByteBuffer bb2 = ByteBuffer.allocate(bufferSize);

            CharBuffer cb1 = CharBuffer.allocate(bufferSize);
            CharBuffer cb2 = CharBuffer.allocate(bufferSize);

            ByteBuffer[] byteBuffers = {bb1, bb2};
            CharBuffer[] charBuffers = {cb1, cb2};

            // 分散读
            inFileChannel.read(byteBuffers);
            for (int i = 0; i < byteBuffers.length; i++) {
                byteBuffers[i].flip();
                decoder.decode(byteBuffers[i], charBuffers[i], false);
                byteBuffers[i].flip();
            }
            Arrays.stream(charBuffers).forEach(item -> {
                System.out.println("###########################################");
                System.out.println(new String(item.array(), 0, item.limit()));
                item.clear();
            });

            // 聚集写入
            RandomAccessFile raf2 = new RandomAccessFile(DISK_URL + WRITE_FILE_NAME_TXT, "rw");
            FileChannel outChannel = raf2.getChannel();

            outChannel.write(byteBuffers);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void channelTransfer() {
        try {
            FileChannel inChannel = FileChannel.open(Paths.get(DISK_URL, READ_FILE_NAME_JPG), StandardOpenOption.READ);
            FileChannel outChannel = FileChannel.open(Paths.get(DISK_URL, WRITE_FILE_NAME_JPG), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE);

            //下面2种都可以
            inChannel.transferTo(0, inChannel.size(), outChannel);
            outChannel.transferFrom(inChannel, 0, inChannel.size());

            inChannel.close();
            outChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * transferFrom和transferTo也是直接缓冲区的方式
     */
    public static void channelLearn() {
        try {
            FileChannel inChannel = FileChannel.open(Paths.get(DISK_URL, READ_FILE_NAME_JPG), StandardOpenOption.READ);
            FileChannel outChannel = FileChannel.open(Paths.get(DISK_URL, WRITE_FILE_NAME_JPG), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE);

            // inMappedBuffer为直接缓冲区（内存映射文件）
            MappedByteBuffer inMappedBuffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
            //只有读写模式，因此通道也要加上读
            MappedByteBuffer outMappedBuffer = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());

            //直接对缓冲区读写
            byte[] dst = new byte[inMappedBuffer.limit()];
            inMappedBuffer.get(dst);
            outMappedBuffer.put(dst);

            inChannel.close();
            outChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 流模式通道学习（不常用？）
     */
    public static void channelLearnWithStream() {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        // 通道
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            fis = new FileInputStream("C:\\Users\\37645\\Desktop\\1587314889(1).jpg");
            fos = new FileOutputStream("C:\\Users\\37645\\Desktop\\1587314889(2).jpg");

            inChannel = fis.getChannel();
            outChannel = fos.getChannel();
            //非直接缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (inChannel.read(buffer) != -1) {
                buffer.flip();
                outChannel.write(buffer);
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inChannel != null) {
                try {
                    inChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outChannel != null) {
                try {
                    outChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
