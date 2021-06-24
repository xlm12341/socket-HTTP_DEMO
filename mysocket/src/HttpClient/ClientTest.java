package HttpClient;

import HttpClient.Client;

import java.net.Socket;

/**
 * Author:sugarxu
 * Date:2021-06-16 21:02
 * Description:客户端
 */
public class ClientTest {
    public static void main(String[] args) {



        Client client = new Client();
        Socket socketReturned = client.initClient();
        if (socketReturned == null)
            System.exit(0);
        //客户端发请求
        Client.doGet(socketReturned,"/index2.html");

    }
}
