package server;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import org.json.JSONObject;
import routes.Rotas;

public class ClienteHandler extends Thread {
	
	private Socket clientSocket;
    private Connection conn;

    public ClienteHandler(Socket socket, Connection conn) {
        this.clientSocket = socket;
        this.conn = conn;
    }
    
    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("Recebido do cliente: " + line);
                
                if ("exit".equalsIgnoreCase(line)) {
                    out.println("Conexão encerrada.");
                    break;
                }
                
                JSONObject response = new Rotas(this.conn).handleRequest(new JSONObject(line));
                
                System.out.println("Enviado pro cliente: " + response.toString());
                
                out.println(response.toString());
            }
        } catch (IOException e) {
        	System.err.println("Erro na comunicação com o cliente: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                System.out.println("Cliente desconectado: " + clientSocket.getInetAddress());
            } catch (IOException e) {
            	System.err.println("Erro ao fechar o socket do cliente: " + e.getMessage());
            }
        }
    }
}
