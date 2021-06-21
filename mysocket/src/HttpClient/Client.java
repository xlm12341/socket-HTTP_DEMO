package HttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Author:varCode
 * Date:2019-03-22 22:20
 * Description:<描述>
 */
public class Client {

    //实现get方式访问http server
    public static void doGet(String host,int port,String uri){
        Socket socket = null;
        try {
            socket = new Socket(host,port);
            //创建http请求  第一行 注意空格
            StringBuffer sb = new StringBuffer("GET "+uri+" HTTP/1.1\r\n");
            //构建请求头
            sb.append("Accept: */*\r\n");
            sb.append("Accept-Language: zh-cn\r\n");
            sb.append("Accept-Encoding: gzip, deflate\r\n");
            sb.append("User-Agent: HTTPClient\r\n");
            sb.append("Host: localhost:8080\r\n");
            sb.append("Connection: Keep-Alive\r\n");
            //发送http请求
            OutputStream socketOut = socket.getOutputStream();
            socketOut.write(sb.toString().getBytes());
            Thread.sleep(2000);
            InputStream socketIn = socket.getInputStream();
            int size = socketIn.available();
            byte[] b = new byte[size];
            socketIn.read(b);
            //将相应结果输出到控制台 模拟浏览器界面
            System.out.println(new String(b));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

