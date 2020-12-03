import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) throws IOException {

        //Establishing a server
        int port = 9000;
        ServerSocket serverSocket = new ServerSocket(port);
        while(true){

            // Accepting a connection
            System.out.println("About to accept a connection...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Accepted connection from: " + clientSocket);

            // Accessing clientSocket's input/output stream
            InputStream inputStream = clientSocket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);

            OutputStream outputStream = clientSocket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            // Login protocol
            dataOutputStream.writeUTF("Hello Client! Please, login first to use the chat.");
            dataOutputStream.writeUTF("Enter username:");
            dataOutputStream.writeUTF("--end--");
            dataOutputStream.flush();
            String username = dataInputStream.readUTF();
            dataOutputStream.writeUTF("Enter password:");
            dataOutputStream.writeUTF("--end--");
            dataOutputStream.flush();
            String password = dataInputStream.readUTF();
            boolean isLoggedIn = login(username, password);

            // If...else statement to direct the flow of the program operation
            // If user is logged in, then output a confirmation message and start the chat
            // else output an error message and close the connection
            if(!isLoggedIn){
                dataOutputStream.writeUTF("Error: Incorrect username and/or password. Please, try again later. Good bye!");
                dataOutputStream.writeUTF("--close connection--");
                dataOutputStream.flush();
            }else {
                // Confirmation message
                dataOutputStream.writeUTF("Successful connection");
                dataOutputStream.writeUTF("--end--");
                dataOutputStream.flush();

                // Interaction between Server and Client
                // Executes continuously until Client types "exit"
                String messageFromClient = "";
                while (!messageFromClient.equals("exit")) {

                    // Receiving message from Client
                    messageFromClient = dataInputStream.readUTF();

                    // Echoeing the message back to Client
                    dataOutputStream.writeUTF("You typed: " + messageFromClient);
                    dataOutputStream.writeUTF("--end--");
                    dataOutputStream.flush();
                    if (messageFromClient.equals("exit"))
                        break;
                }

                // If Client types "exit", close the connection
                clientSocket.close();
            }
        }
    }

    public static boolean login(String username, String password){

        // Login protocol
        // It compares username and password provided by Client to
        // the information stored in "users.txt"
        File file = new File("users.txt");
        try{
            if(!file.exists()){
                throw new IOException("File does not exist");
            }else{
                Scanner scanner = new Scanner(file);
                while(scanner.hasNextLine()){
                    String line = scanner.nextLine();
                    if(line.equals((username + "::" + password)))
                        return true;
                }
            }
        }catch(IOException e){
            System.err.println("Authentication Error: " + e.getLocalizedMessage());
        }
        return false;
    }
}
