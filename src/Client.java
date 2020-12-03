import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {

        // Establishing a socket
        Socket socket = new Socket("localhost", 9000);

        // Accessing socket's input/output streams and keyboard input
        InputStream inputStream = socket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(inputStream);

        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

        // If connection is successful, output a confirmation message to Client
        if(socket.isConnected()) {
            //Confirmation message
            System.out.println("Connected to the server");
            System.out.println("Type \"exit\" to exit");

            // Interaction between Server and Client
            // Executes continuously until Client types "exit"
            String messageFromServer, messageFromClient = "";
            while (!messageFromClient.equals("exit")) {

                // Reading and displaying all messages from Server
                while (true) {
                    messageFromServer = dataInputStream.readUTF();

                    // Different scenarios to navigate the flow of the program operation
                    // --end-- signifies the end of the messages from Server
                    // --close connection-- triggers socket.close() as requested by Server
                    if (messageFromServer.equals("--end--")) {
                        break;
                    }
                    if (messageFromServer.equals("--close connection--")) {
                        messageFromClient = "exit";
                        break;
                    }
                    System.out.println("(Server -> Client) " + messageFromServer);
                }
                if (messageFromClient.equals("exit"))
                    break;

                // Reading Client's keyboard input
                messageFromClient = keyboard.readLine();

                // Sending Client's input to Server
                dataOutputStream.writeUTF(messageFromClient);
                dataOutputStream.flush();
            }
            System.out.println("Connection is closed. Good bye!");
        }
    }
}
