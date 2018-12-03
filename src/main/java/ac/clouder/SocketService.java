package ac.clouder;

import ac.client.QueryInput;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketService {
    //搭建服务器端
    public static void main(String[] args) throws IOException{
        SocketService socketService = new SocketService();
        //1、a)创建一个服务器端Socket，即SocketService
        socketService.oneServer();
    }
    public  void oneServer(){
        try{
            ServerSocket server=null;
            try{
                //下面是端口，端口可以和客户端代码里面的端口一样
                server=new ServerSocket(5209);
                //b)指定绑定的端口，并监听此端口。
                System.out.println("服务器启动成功");
                //创建一个ServerSocket在端口5209监听客户请求
            }catch(Exception e) {
                System.out.println("没有启动监听："+e);
                //出错，打印出错信息
            }
            Socket socket=null;
            try{
                socket=server.accept();
                //2、调用accept()方法开始监听，等待客户端的连接
                //使用accept()阻塞等待客户请求，有客户
                //请求到来则产生一个Socket对象，并继续执行
            }catch(Exception e) {
                System.out.println("Error."+e);
                //出错，打印出错信息
            }
            //3、获取输入流，并读取客户端信息
            String line=new String("123");
            InputStream is = socket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);


            //发送
            OutputStream os = socket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            QueryInput qi=new QueryInput();


            while(!line.equals("end")){
                Object object = ois.readObject();
                //如果该字符串为 "bye"，则停止循环

                //刷新输出流，使Client马上收到该字符串
                System.out.println("服务:"+line);
                //在系统标准输出上打印读入的字符串
                System.out.println("客户:"+object);
                //从Client读入一字符串，并打印到标准输出上

                //从系统标准输入读入一字符串
            } //继续循环

            //5、关闭资源
            is.close(); //关闭Socket输出流
            socket.close(); //关闭Socket
            server.close(); //关闭ServerSocket
        }catch(Exception e) {//出错，打印出错信息
            System.out.println("Error."+e);
        }
    }
}

