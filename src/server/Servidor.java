package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;

import dao.BancoDados;

public class Servidor {
	
	public static void main(String[] args) throws SQLException, IOException {
		
		System.out.println("Qual a porta?");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int port = Integer.parseInt(br.readLine());
		
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			
			System.out.println("Servidor iniciado na porta " + port);
			
			Connection conn = BancoDados.conectar();
			
			while (true) {
				Socket clientSocket = serverSocket.accept();
				
				System.out.println("Cliente conectado.");
				
				new ClienteHandler(clientSocket, conn).start();
			}
		} catch (IOException e) {
			System.out.println("Cliente desconectado.");
		}
	}	
}