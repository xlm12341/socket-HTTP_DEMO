package HttpServer;

import com.sun.net.httpserver.HttpServer;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    public static final int DEFAULT_PORT =8080;//默认8080端口
  //public static ArrayList<ServerThread>list = new ArrayList<ServerThread>();
    public static void start(){
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(DEFAULT_PORT);
            System.out.println("the server is listening for a socket connection at port:"+serverSocket.getLocalPort());
            while(true){//死循环时刻监听客户端链接
                Socket socket = serverSocket.accept();
                socket.setSoTimeout(10*1000);
                System.out.println("a socket connection has been established, and the socket address is:"+socket.getInetAddress()
                        +":"+socket.getPort());
                //开始服务
                ServerThread st = new ServerThread(socket);
                st.start();

                //list.add(st);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
