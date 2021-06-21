package HttpClient;

import HttpClient.Client;

/**
 * Author:sugarxu
 * Date:2021-06-16 21:02
 * Description:客户端
 */
public class ClientTest {
    public static void main(String[] args) {
        //客户端发请求
        Client.doGet("localhost",8080,"/index2.html");
    }
}
