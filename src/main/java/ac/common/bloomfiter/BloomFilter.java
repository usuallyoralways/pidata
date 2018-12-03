package ac.common.bloomfiter;


import java.io.*;
import java.util.BitSet;

public class BloomFilter {
    private int defaultSize = 5000 << 10000;
    private int size;
    //private int basic = defaultSize - 1;
    private int basic;
    private BitSet bits;

    private BloomFilter() {

    }

    private void createBloomFilter() {
        bits = new BitSet(defaultSize);
        basic = defaultSize - 1;
    }

    public void createBloomFilter(int multi) {
        if (multi > 1000) {
            createBloomFilter();
            basic = defaultSize - 1;
            return;
        }
        int size = 500 << multi;
        basic = size - 1;
        bits=new BitSet(size);
    }

    private int[] lrandom(String key) { // 产生八个随机数并返回
        int[] randomsum = new int[8];
        for (int i = 0; i < 8; i++)
            randomsum[0] = hashCode(key, i + 1);
        for (int i = 0; i < 8; i++) {
            System.out.println(randomsum[i]);
        }

        return randomsum;
    }

    // 将一个数据项加入
    public synchronized void add(String key) {
        int keyCode[] = lrandom(key);
        for (int i = 0; i < 8; i++)
        {

            bits.set(keyCode[i]); // 将指定索引处的位设置为 true

        }
    }


    // 判断一个数据项是否存在
    public boolean exist(String key) {
        int keyCode[] = lrandom(key);
        if (bits.get(keyCode[0])
                && bits.get(keyCode[1]) // 返回指定索引处的位值。
                && bits.get(keyCode[2]) && bits.get(keyCode[3])
                && bits.get(keyCode[4]) && bits.get(keyCode[5])
                && bits.get(keyCode[6]) && bits.get(keyCode[7])) {
            return true;
        }
        return false;
    }

    public void writeFiles(String path){
        try
        {
            DataOutputStream out=new DataOutputStream(
                    new BufferedOutputStream(
                            new FileOutputStream(path)));
            out.write(bits.toByteArray());
            out.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    private  BitSet byteArrayBitSet(byte[] bytes) {
        BitSet bitSet = new BitSet(bytes.length * 8);
        int index = 0;
        for (int i = 0; i < bytes.length; i++) {
            for (int j = 7; j >= 0; j--) {
                bitSet.set(index++, (bytes[i] & (1 << j)) >> j == 1 ? true
                        : false);
            }
        }
        return bitSet;
    }

    public void readBloomFromFiles(String path){
        try
        {
            DataInputStream in=new DataInputStream(
                    new BufferedInputStream(
                            new FileInputStream(path)));
           /* bits=in.readByte();*/
           byte temps[]=new byte[bits.size()/8];
           in.read(temps);
           bits.and(byteArrayBitSet(temps));
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public int getDefaultSize() {
        return defaultSize;
    }

    public void setDefaultSize(int defaultSize) {
        this.defaultSize = defaultSize;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getBasic() {
        return basic;
    }

    public void setBasic(int basic) {
        this.basic = basic;
    }

    public BitSet getBits() {
        return bits;
    }

    public void setBits(BitSet bits) {
        this.bits = bits;
    }

    private int hashCode(String key, int Q) {
        int h = 0;
        int off = 0;
        char val[] = key.toCharArray(); // 将此URl转换为一个新的字符数组
        int len = key.length();
        for (int i = 0; i < len; i++) {
            h = (30 + Q) * h + val[off++];
        }
        return basic & h;
    }


    public static void main(String[] args) { // TODO Auto-generated method
        long pre = 0;
        long post = 0;
        pre = System.nanoTime();
        BloomFilter f = new BloomFilter(); //初始化
        f.createBloomFilter(200);
        f.add("http://www.agrilink.cn/");
        f.add("http://www.baidu.com/");
        f.add("http://www.baidu.comsdfs/");

        //f.writeFiles("bloom.data");

       // f.readBloomFromFiles("bloom.data");

        System.out.println(f.exist("http://www.baidu.com/"));
        System.out.println(f.exist("http://www.baidud.com/"));
        post = System.nanoTime();
        System.out.println("Time: " + (post - pre));
        System.out.println(f.getBits().cardinality());


        BitSet s = new BitSet(128);
        s.set(1);
        s.toByteArray();
        //int i = Integer.parseInt(s, 2);//按照2进制提取为十进制
        System.out.println(s.get(1));
    }



}