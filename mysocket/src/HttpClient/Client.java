package HttpClient;

import javax.xml.crypto.dsig.keyinfo.RetrievalMethod;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class Client {

    public Socket initClient() {
        try {
            // 创建客户端对象
            Socket client = new Socket("localhost", 8080);
            // 获取客户端的输入输出流
            final InputStream ins = client.getInputStream();
            final OutputStream ous = client.getOutputStream();
            // 先接收服务器发送的欢迎词
            String msg = readMsg(ins);
            System.out.println(msg);
            // 接收服务端发送过来输入用户名的请求
            String requestName = readMsg(ins);
            System.out.println(requestName);
            // 获取用户名信息，从控制台
            final Scanner scanner = new Scanner(System.in);
            String username = scanner.nextLine();
            // 发送用户名
            sendMsg(ous, username + "\r\n");
            // 读取密码请求
            String requestPwd = readMsg(ins);
            System.out.println(requestPwd);
            // 从控制台扫描密码
            String pwd = scanner.nextLine();
            // 把密码发送给服务器
            sendMsg(ous, pwd + "\r\n");
            // 获取验证结果
            String result = readMsg(ins);
            System.out.println(result);

            //如果登录失败，则接受服务器端发过来的提示消息
            while(!result.equals("OK")){
                //接收"Fail to connect server......"
                String message = readMsg(ins);
                System.out.println(message);
                //接收"Please check your name and password and login again....."
                message = readMsg(ins);
                System.out.println(message);
                //接收 "Please input your name:""
                message = readMsg(ins);
                System.out.println(message);
                //重新发送用户名给服务器
                username = scanner.nextLine();
                // 发送用户名
                sendMsg(ous, username + "\r\n");
                //接受密码请求"Please input your password:"
                message = readMsg(ins);
                System.out.println(message);
                //
                pwd = scanner.nextLine();
                // 发送密码给服务器
                sendMsg(ous, pwd + "\r\n");
                //接收服务器返的信息
                result = readMsg(ins);
            }
            //如果登录成功，则可以开始发送信息了
            if (result.equals("OK")) {
                //接受------successfully connected------
             //   String message = readMsg(ins);
            //    System.out.println(message);
                return client;
/*                // 发送消息线程
                new Thread() {
                    public void run() {
                        try {
                            while (true) {
                                // 从控制台扫描一行数据
                                String message = scanner.nextLine();

                                sendMsg(ous, message + "\r\n");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    };
                }.start();

                //读取消息线程
                new Thread() {
                    public void run() {
                        try {
                            while (true) {
                                String message = readMsg(ins);
                                System.out.println(message);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    };
                }.start();*/
            } else {
                System.out.println("Fail to login");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    //实现get方式访问http server
    public static Socket doGet(Socket socket,String uri){
        //Socket socket = null;
        try {
            //socket = new Socket(host,port);
            //创建http请求  第一行 注意空格
            StringBuffer sb = new StringBuffer("GET "+uri+" HTTP/1.1\r\n");
            //构建请求头
            sb.append("Accept: */*\r\n");
            sb.append("Accept-Language: zh-cn\r\n");
            sb.append("Accept-Encoding: gzip, deflate\r\n");
            sb.append("User-Agent: HTTPClient\r\n");
            sb.append("Host: localhost:8080\r\n");
            //sb.append("Connection: Keep-Alive\r\n");
            sb.append("Connection: Close\r\n");
            //Connection: Close
            //发送http请求
            OutputStream socketOut = socket.getOutputStream();
            socketOut.write(sb.toString().getBytes());
            Thread.sleep(3000);


            //接受http请求
            InputStream socketIn = socket.getInputStream();
            int size = socketIn.available();
            System.out.println(size);

            int len = 0;
            len = socketIn.read();
            System.out.println("head len " + len);
            byte[] b = new byte[len];
            socketIn.read(b);
            //         byte[] b2 = new byte[size-43];
            //       socketIn.read(b2);
            //将相应结果输出到控制台 模拟浏览器界面
            System.out.println(new String(b));
            File file = new File("D:\\code repository\\socket\\socket-HTTP_DEMO\\mysocket\\src\\clientResources\\images" + uri);
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));  //你要保存在哪个目录下面
            int i;
            while ((i = socketIn.read()) != -1) {
//                System.out.println(i);
                out.write(i);
            }
            out.flush();
            System.out.println("flushed");
            out.close();

            return socket;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    //delete方式访问http server
    public static Socket doDelete(Socket socket,String uri){
        //Socket socket = null;
        try {
            //socket = new Socket(host,port);
            //创建http请求  第一行 注意空格
            StringBuffer sb = new StringBuffer("DELETE "+uri+" HTTP/1.1\r\n");
            //构建请求头
            sb.append("Accept: */*\r\n");
            sb.append("Accept-Language: zh-cn\r\n");
            sb.append("Accept-Encoding: gzip, deflate\r\n");
            sb.append("User-Agent: HTTPClient\r\n");
            sb.append("Host: localhost:8080\r\n");
          //  sb.append("Connection: Keep-Alive\r\n");
            sb.append("Connection: Close\r\n");

            //发送http请求
            OutputStream socketOut = socket.getOutputStream();
            socketOut.write(sb.toString().getBytes());
            Thread.sleep(2000);

            //接受http请求
            InputStream socketIn = socket.getInputStream();
            int size = socketIn.available();
            byte[] b = new byte[size];
            socketIn.read(b);
            //将相应结果输出到控制台 模拟浏览器界面
            System.out.println(new String(b));
            return socket;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
/*        finally {
            try {
                socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        return null;
    }
    public static Socket doPost(Socket socket, String uri) {
        try {
            //socket = new Socket(host,port);
            //创建http请求  第一行 注意空格
            StringBuffer sb = new StringBuffer("POST "+uri+" HTTP/1.1\r\n");
            //构建请求头
            sb.append("Accept: */*\r\n");
            sb.append("Accept-Language: zh-cn\r\n");
            sb.append("Accept-Encoding: gzip, deflate\r\n");
            sb.append("User-Agent: HTTPClient\r\n");
            sb.append("Host: localhost:8080\r\n");
            //sb.append("Connection: Keep-Alive\r\n\r\n");
            sb.append("Connection: Close\r\n\r\n");

            String data = "data from client for post";
            sb.append(data);
            //发送http请求
            OutputStream socketOut = socket.getOutputStream();
            socketOut.write(sb.toString().getBytes());
            Thread.sleep(2000);

            //接受http请求
            InputStream socketIn = socket.getInputStream();
            int size = socketIn.available();
            byte[] b = new byte[size];
            socketIn.read(b);
            //将相应结果输出到控制台 模拟浏览器界面
            System.out.println(new String(b));

            return socket;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /*finally {
            try {
                socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        return null;
    }


    public static Socket doBye(Socket socket) {
        try {
            //socket = new Socket(host,port);
            //创建http请求  第一行 注意空格
            StringBuffer sb = new StringBuffer("BYE\r\n");

            //发送http请求
            OutputStream socketOut = socket.getOutputStream();
            socketOut.write(sb.toString().getBytes());
            Thread.sleep(2000);
            return socket;
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
        return null;
    }
    public String readMsg(InputStream ins) throws Exception {
        int value = ins.read();
        String str = "";
        while (value != 10) {
            // 代表客户端不正常关闭
            if (value == -1) {
                throw new Exception();
            }
            str = str + (char) value;
            value = ins.read();
        }
        str = str.trim();
        return str;
    }

    // 发送消息的函数
    public void sendMsg(OutputStream ous, String str) throws Exception {
        byte[] bytes = str.getBytes();
        ous.write(bytes);
        ous.flush();
    }

}

