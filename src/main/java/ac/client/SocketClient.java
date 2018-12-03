package ac.client;

import ac.client.trade_table.HMacMD5;
import ac.client.trade_table.TradeBlockIdCost;
import ac.client.trade_table.TradeQueryInput;
import ac.common.BlockId;
import ac.common.HashValue;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;

public class SocketClient {
    // 搭建客户端
    public static void main(String[] args) throws IOException {
        try {
            // 1、创建客户端Socket，指定服务器地址和端口

            //下面是你要传输到另一台电脑的IP地址和端口
            Socket socket = new Socket("127.0.0.1", 5209);
            System.out.println("客户端启动成功");


            OutputStream os = socket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            QueryInput qi=new QueryInput(Double.valueOf(1),Double.valueOf(200000));
            HashValue hv= new HMacMD5();
            BlockId bd= new TradeBlockIdCost();
            qi.setHv(hv);
            qi.setBd(bd);
            qi.init();



            InputStream is = socket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);




            while (qi!=null) {
                // 若从标准输入读入的字符串为 "end"则停止循环
                oos.writeObject(qi);

                Object object = ois.readObject();
            } // 继续循环
            //4、关闭资源

            socket.close(); // 关闭Socket
        } catch (Exception e) {
            System.out.println("can not listen to:" + e);// 出错，打印出错信息
        }
    }

}
