package HttpClient;

import HttpClient.Client;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaNamespaceSupport;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

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

        Scanner scanner = new Scanner(System.in);
        String cmds = scanner.nextLine();
        String methods = cmds.split(" ")[0];
        Socket lastSocket;
        while (!methods.equals("bye")) {
            String uri = cmds.split(" ")[1];
            if (methods.equals("get") || methods.equals("GET")) {
                lastSocket = Client.doGet(socketReturned,uri);
            }
            if (methods.equals("delete") || methods.equals("DELETE")) {
               lastSocket = Client.doDelete(socketReturned, uri);
            }
            if (methods.equals("post") || methods.equals("POST")) {
               lastSocket =  Client.doPost(socketReturned, uri);
            }
            cmds = scanner.nextLine();
            methods = cmds.split(" ")[0];
            }
        Client.doBye(socketReturned);
        }

    }

