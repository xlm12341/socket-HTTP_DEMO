package HttpServer;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class ServerThread extends Thread{

    public Socket socket;
    public InputStream ins;
    public OutputStream ous;

    public ServerThread (Socket socket){
        this.socket = socket;
    }
    public void userLogin() {
        try {
            //读取HTTP请求信息
            ins = socket.getInputStream();
            ous = socket.getOutputStream();

            String msg = "Welcome to sugarxu's server!";

            sendMsg(ous, msg);
            // 发送要求登录信息给客户端
            String user = "Please input your user name:";
            sendMsg(ous, user);
            // 获取客户端输入的用户名
            String userName = readMsg(ins);
            System.out.println(userName);
            // 发送要求密码信息给客户端
            String pwd = "Please input your password:";
            sendMsg(ous,  pwd);
            // 获取客户端输入的密码
            String pass = readMsg(ins);
            System.out.println(pass);

            // 登录验证
            boolean flag = loginCheck(userName, pass);
            System.out.println(flag);
            // 校验不通过时，循环校验
            while (!flag) {
                msg="NO";
                sendMsg(ous, msg);
                msg = "Fail to connect server......";
                sendMsg(ous, msg);
                msg = "Please check your name and password and login again.....";
                sendMsg(ous, msg);
                msg = "Please input your user name:";
                sendMsg(ous, msg);
                // 获取客户端输入的用户名
                userName = readMsg(ins);
                // 发送要求密码信息给客户端
                msg = "Please input your password:";
                sendMsg(ous, msg);
                // 获取客户端输入的密码
                pass = readMsg(ins);
                flag = loginCheck(userName, pass);
            }

            //发送登录成功的结果给客户端
            msg="OK\r\n";
            sendMsg(ous, msg);
            // 校验成功后：开始聊天
            msg = "------successfully connected------";
            sendMsg(ous, msg);
        } catch (Exception e) {
            System.out.println("The client was closed unexpectedly");
            e.printStackTrace();
        }
    }

    public void run() {
        userLogin();
        try {
            Thread.sleep(500);
            int size = ins.available();
            byte[] b = new byte[size];
            ins.read(b);
            String request = new String(b);
            System.out.println(request);
            System.out.println("----------------");
            //解析请求方式、uri、协议，获取uri
            String typeUriHttp = request.substring(0,request.indexOf("\r\n"));
            System.out.println(typeUriHttp);
            String uri = typeUriHttp.split(" ")[1];
            System.out.println((uri));
            //简化处理响应头content-Type
            String contentType;
            if(uri.indexOf("html")!=-1||uri.indexOf("htm")!=-1||uri.indexOf("txt")!=-1){
                contentType = "text/html";
            }else if(uri.indexOf("jpg")!=-1||uri.indexOf("jpeg")!=-1){
                contentType = "image/jpeg";
            }else if(uri.indexOf("gif")!=-1){
                contentType = "image/gif";
            }else contentType = "application/octet-stream";

            //创建HTTP响应结果
            //创建响应协议、状态
            String httpStatus = "HTTP/1.1 200 OK\r\n";
            //创建响应头
            String responseHeader = "Content-Type:" + contentType + "\r\n\r\n";
            InputStream in = HttpServer.class.getResourceAsStream("/Resources" + uri);
            OutputStream socketOut = socket.getOutputStream();
            //发送响应协议、状态码及响应头、正文
            System.out.println(httpStatus);
            System.out.println(responseHeader);
            socketOut.write(httpStatus.getBytes());
            socketOut.write(responseHeader.getBytes());
            int len = 0;
            byte[] bytes = new byte[1024];

            while((len = in.read(bytes)) != -1)
            {
                socketOut.write(bytes,0, len);
            }
            Thread.sleep(1000);
            //socket.close();
        } catch (Exception e) {
            System.out.println("The client was closed unexpectedly");
            e.printStackTrace();
        }


        //有异常后统一将流关闭
        try {
            ins.close();
            ous.close();
            socket.close();
            //将当前已经关闭的客户端从容器中移除
         //   Myserver.list.remove(this);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private boolean loginCheck(String userName, String pass) {
        //这里可以调用数据库？
        if(userName.equals("root") && pass.equals("123456"))
            return true;
        return false;
    }

    public void sendMsg(OutputStream ous, String s) throws IOException {
        byte[] b = s.getBytes();
        ous.write(b);
        ous.write(13);//\r
        ous.write(10);//\n
        ous.flush();
    }

    public String readMsg(InputStream ins) throws Exception {

        // 读取客户端的信息
        int value = ins.read();
        // 读取整行 读取到回车（13）换行（10）时停止读
        String str = "";
        while (value != 10) {
            // 点击关闭客户端时会返回-1值
            if (value == -1) {
                throw new Exception();
            }
            str = str + ((char) value);
            value = ins.read();
        }
        str = str.trim();
        return str;

    }
}
