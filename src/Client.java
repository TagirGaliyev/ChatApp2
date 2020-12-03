import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 9000);

        InputStream inputStream = socket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(inputStream);

        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Connected to the server");
        System.out.println("Type \"exit\" to exit");

        String messageFromServer, messageFromClient = "";
        while(!messageFromClient.equals("exit")){
            while(true){
                messageFromServer = dataInputStream.readUTF();
                if(messageFromServer.equals("--end--")) {
                    break;
                }
                if(messageFromServer.equals("--close connection--")){
                    messageFromClient = "exit";
                    break;
                }
                System.out.println("(Server -> Client) " + messageFromServer);
            }
            if(messageFromClient.equals("exit"))
                break;
            messageFromClient = keyboard.readLine();
            dataOutputStream.writeUTF(messageFromClient);
            dataOutputStream.flush();
        }
        System.out.println("Connection is closed. Good bye!");
        //Bye
        //Inside encryption_protocol
    }
}
