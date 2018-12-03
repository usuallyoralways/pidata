package ac.common;


import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.Funnels;
import com.google.common.hash.PrimitiveSink;

import java.io.*;
import java.nio.charset.Charset;

public class Main {
    public static void main(String[] args){
        Charset charset = Charset.forName("utf-8");
        BloomFilter<String> bloomFilter = BloomFilter.create(Funnels.stringFunnel(charset),2<<21);//指定bloomFilter的容量
        String url = "www.baidu.com/a";
        bloomFilter.put(url);

        File file =new File("test.dat");
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            ObjectOutputStream objOut=new ObjectOutputStream(out);
            objOut.writeObject(bloomFilter);
            objOut.flush();
            objOut.close();
            System.out.println("write object success!");
        } catch (IOException e) {
            System.out.println("write object failed");
            e.printStackTrace();
        }


        BloomFilter<String> bloomFilters=null;
        File files =new File("test.dat");
        FileInputStream in;
        try {
            in = new FileInputStream(files);
            ObjectInputStream objIn=new ObjectInputStream(in);
            bloomFilters=(BloomFilter<String>)objIn.readObject();
            objIn.close();
            System.out.println("read object success!");
        } catch (IOException e) {
            System.out.println("read object failed");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(bloomFilters.mightContain(url)+"nihao");


    }
}
