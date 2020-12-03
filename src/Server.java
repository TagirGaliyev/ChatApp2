import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) throws IOException {
        int port = 9000;
        ServerSocket serverSocket = new ServerSocket(port);
        while(true){
            System.out.println("About to accept a connection...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Accepted connection from: " + clientSocket);

            InputStream inputStream = clientSocket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);

            OutputStream outputStream = clientSocket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

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
            if(!isLoggedIn){
                dataOutputStream.writeUTF("Error: Incorrect username and/or password. Please, try again later. Good bye!");
                dataOutputStream.writeUTF("--close connection--");
            }else {
                dataOutputStream.writeUTF("Successful connection");
                dataOutputStream.writeUTF("--end--");
                String messageFromClient = "";
                while (!messageFromClient.equals("exit")) {
                    messageFromClient = dataInputStream.readUTF();
                    dataOutputStream.writeUTF("You typed: " + messageFromClient);
                    dataOutputStream.writeUTF("--end--");
                    dataOutputStream.flush();
                    if (messageFromClient.equals("exit"))
                        break;
                }
                clientSocket.close();
            }
        }
    }

    public static boolean login(String username, String password){
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
