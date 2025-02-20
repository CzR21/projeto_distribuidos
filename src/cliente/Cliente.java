package cliente;
	
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import org.json.*;
import enums.Operacao;
	
public class Cliente {
	
    private static String token = "";
	
	public static void main(String[] args) throws IOException {

        System.out.println("Qual o endereço de IP?");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String server = br.readLine();
    	
        System.out.println("Qual a porta?");
        br = new BufferedReader(new InputStreamReader(System.in));
        int port = Integer.parseInt(br.readLine());

        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            echoSocket = new Socket(server, port);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            System.out.println("Servidor conectado com sucesso");
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: ");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " );
            System.exit(1);
        }
        
        while (true) {
        	JSONObject json = new JSONObject();
        	
            System.out.println("\nMenu:");
            
            if(token == "") {
            	System.out.println("1 - Login");
            	System.out.println("2 - Cadastro");
            } else {            	
            	System.out.println("4 - Listar usuarios");
            	System.out.println("5 - Localizar usuario");
            	System.out.println("6 - Editar usuario");
            	System.out.println("7 - Excluir usuario");
            	
            	System.out.println("8 - Listar categorias");
            	System.out.println("9 - Localizar categoria");
            	System.out.println("10 - Salvar categoria");
            	System.out.println("11 - Excluir categoria");
            	
            	System.out.println("12 - Listar avisos");
            	System.out.println("13 - Listar avisos do usuario");
            	System.out.println("14 - Localizar aviso");
            	System.out.println("15 - Salvar aviso");
            	System.out.println("16 - Excluir aviso");
            	
            	System.out.println("17 - Listar categorias do usuario");
            	System.out.println("18 - Cadastrar usuario em uma categoria");
            	System.out.println("19 - Remover usuario de uma categoria");
            	
            	System.out.println("3 - Logout");
            }
            
            System.out.println("0 - Sair\n");

            System.out.print("Escolha uma opção: ");
            br = new BufferedReader(new InputStreamReader(System.in));
            String opcao = br.readLine();

            switch (opcao) {
                case "1":
                    if (token != "") {
                    	System.out.println("Você já está logado. Faça logout antes de logar novamente.");
                    } else {
                        // Login
                        System.out.print("Informe o RA: ");
                        br = new BufferedReader(new InputStreamReader(System.in));
                        String ra = br.readLine();
                        System.out.print("Informe a senha: ");
                        br = new BufferedReader(new InputStreamReader(System.in));
                        String senha = br.readLine();

                        json.put("operacao", Operacao.login.toString());
                        json.put("ra", ra);
                        json.put("senha", senha);

                        System.out.println("Enviado ao servidor: " + json.toString());
                        
                        out.println(json.toString());
                        
                        String resposta = in.readLine();
                        System.out.println("Recebido do servidor: " + resposta);

                        if (verificarRetorno(resposta) == 200 || verificarRetorno(resposta) == 201) {
                            token = verificarToken(resposta);
                            
                            json = new JSONObject();	
                            
                            json.put("operacao", Operacao.listarUsuarioAvisos.toString());
                            json.put("token", token);
                            json.put("ra", token);
                            
                            System.out.println("Enviado ao servidor: " + json.toString());
                            out.println(json.toString());
                            System.out.println("Recebido do servidor: " + in.readLine());
                        }
                    }
                    break;

                case "2":
                	if (token != "") {
                    	System.out.println("Você já está logado. Faça logout antes de cadatrar um novo usuário.");
                    } else {
                        System.out.print("Informe o RA: ");
                        br = new BufferedReader(new InputStreamReader(System.in));
                        String ra = br.readLine();
                        System.out.print("Informe o nome: ");
                        br = new BufferedReader(new InputStreamReader(System.in));
                        String nome = br.readLine();
                        System.out.print("Informe a senha: ");
                        br = new BufferedReader(new InputStreamReader(System.in));
                        String senha = br.readLine();

                        json.put("operacao", Operacao.cadastrarUsuario.toString());
                        json.put("ra", ra);
                        json.put("nome", nome);
                        json.put("senha", senha);

                        System.out.println("Enviado ao servidor: " + json.toString());
                        out.println(json.toString());
                        System.out.println("Recebido do servidor: " + in.readLine());
                    }
                    break;

                case "3":
                	if (token == "") {
                    	System.out.println("Você não está logado. Faça login antes de realizar o logout.");
                    } else {
                	
	                	json.put("operacao", Operacao.logout.toString());
	                    json.put("token", token);
	
	                    System.out.println("Enviado ao servidor: " + json.toString());
	                    
	                    out.println(json.toString());
	                    
	                    String resposta = in.readLine();
	                    System.out.println("Recebido do servidor: " + resposta);
	
	                    if (verificarRetorno(resposta) == 200 || verificarRetorno(resposta) == 201) {
	                        token = "";
	                    }
                    }
                	break;
                	
                case "4":
                    json.put("operacao", Operacao.listarUsuarios.toString());
                    json.put("token", token);
                    
                    System.out.println("Enviado ao servidor: " + json.toString());
                    out.println(json.toString());
                    System.out.println("Recebido do servidor: " + in.readLine());
                    break;

                case "5":
                    System.out.print("Informe o RA do usuário: ");
                    br = new BufferedReader(new InputStreamReader(System.in));
                    String raBusca = br.readLine();
                    
                    json.put("operacao", Operacao.localizarUsuario.toString());
                    json.put("ra", raBusca);
                    json.put("token", token);
                    
                    System.out.println("Enviado ao servidor: " + json.toString());
                    out.println(json.toString());
                    System.out.println("Recebido do servidor: " + in.readLine());
                    break;

                case "6":
                	System.out.print("Informe o RA: ");
                    br = new BufferedReader(new InputStreamReader(System.in));
                    String ra = br.readLine();
                    System.out.print("Informe o nome: ");
                    br = new BufferedReader(new InputStreamReader(System.in));
                    String nome = br.readLine();
                    System.out.print("Informe a senha: ");
                    br = new BufferedReader(new InputStreamReader(System.in));
                    String senha = br.readLine();
                    
                    json.put("operacao", Operacao.editarUsuario.toString());
                    json.put("token", token);

                    JSONObject usuario = new JSONObject();
                    usuario.put("ra", ra);
                    usuario.put("senha", senha);
                    usuario.put("nome", nome);

                    json.put("usuario", usuario);
                    
                    System.out.println("Enviado ao servidor: " + json.toString());
                    out.println(json.toString());
                    System.out.println("Recebido do servidor: " + in.readLine());
                    break;

                case "7":
                    System.out.print("Informe o RA do usuário a ser excluído: ");
                    br = new BufferedReader(new InputStreamReader(System.in));
                    String raExcluir = br.readLine();
                    
                    json.put("operacao", Operacao.excluirUsuario.toString());
                    json.put("ra", raExcluir);
                    json.put("token", token);
                    
                    System.out.println("Enviado ao servidor: " + json.toString());
                    out.println(json.toString());
                    System.out.println("Recebido do servidor: " + in.readLine());
                    break;

                case "8":
                    json.put("operacao", Operacao.listarCategorias.toString());
                    json.put("token", token);
                    
                    System.out.println("Enviado ao servidor: " + json.toString());
                    out.println(json.toString());
                    System.out.println("Recebido do servidor: " + in.readLine());
                    break;

                case "9":
                	System.out.print("Informe o ID: ");
                    br = new BufferedReader(new InputStreamReader(System.in));
                    int id = Integer.parseInt(br.readLine());
                    
                    json.put("operacao", Operacao.localizarCategoria.toString());
                    json.put("token", token);
                    json.put("id", id);
                                        
                    System.out.println("Enviado ao servidor: " + json.toString());
                    out.println(json.toString());
                    System.out.println("Recebido do servidor: " + in.readLine());
                    break;

                case "10":
                	System.out.print("Informe o ID: ");
                    br = new BufferedReader(new InputStreamReader(System.in));
                    int idCategoria = Integer.parseInt(br.readLine());
                    System.out.print("Informe o nome da categoria: ");
                    br = new BufferedReader(new InputStreamReader(System.in));
                    String nomeCategoria = br.readLine();
                    
                    json.put("operacao", Operacao.salvarCategoria.toString());
                    json.put("token", token);

                    JSONObject categoria = new JSONObject();
                    categoria.put("id", idCategoria);
                    categoria.put("nome", nomeCategoria);

                    json.put("categoria", categoria);
                    
                    System.out.println("Enviado ao servidor: " + json.toString());
                    out.println(json.toString());
                    System.out.println("Recebido do servidor: " + in.readLine());
                    break;

                case "11":
                	System.out.print("Informe o ID: ");
                    br = new BufferedReader(new InputStreamReader(System.in));
                    idCategoria = Integer.parseInt(br.readLine());
                    
                    json.put("operacao", Operacao.excluirCategoria.toString());
                    json.put("token", token);
                    json.put("id", idCategoria);
                    
                    System.out.println("Enviado ao servidor: " + json.toString());
                    out.println(json.toString());
                    System.out.println("Recebido do servidor: " + in.readLine());
                    break;

                case "12":
                	System.out.print("Informe o ID da categoria: ");
                    br = new BufferedReader(new InputStreamReader(System.in));
                    idCategoria = Integer.parseInt(br.readLine());
                    
                    json.put("operacao", Operacao.listarAvisos.toString());
                    json.put("token", token);
                    json.put("categoria", idCategoria);
                    
                    System.out.println("Enviado ao servidor: " + json.toString());
                    out.println(json.toString());
                    System.out.println("Recebido do servidor: " + in.readLine());
                    break;

                case "13":
                	System.out.print("Informe o ra do usuario: ");
                    br = new BufferedReader(new InputStreamReader(System.in));
                    ra = br.readLine();
                    
                    json.put("operacao", Operacao.listarUsuarioAvisos.toString());
                    json.put("token", token);
                    json.put("ra", ra);
                    
                    System.out.println("Enviado ao servidor: " + json.toString());
                    out.println(json.toString());
                    System.out.println("Recebido do servidor: " + in.readLine());
                    break;

                case "14":
                	System.out.print("Informe o ID do aviso: ");
                    br = new BufferedReader(new InputStreamReader(System.in));
                    int idAviso = Integer.parseInt(br.readLine());
                    
                    json.put("operacao", Operacao.localizarAviso.toString());
                    json.put("token", token);
                    json.put("id", idAviso);
                    
                    System.out.println("Enviado ao servidor: " + json.toString());
                    out.println(json.toString());
                    System.out.println("Recebido do servidor: " + in.readLine());
                    break;

                case "15":
                    System.out.print("Informe o ID: ");
                    br = new BufferedReader(new InputStreamReader(System.in));
                    idAviso = Integer.parseInt(br.readLine());
                    
                    System.out.print("Informe o ID da categoria: ");
                    br = new BufferedReader(new InputStreamReader(System.in));
                    idCategoria = Integer.parseInt(br.readLine());
                    
                    System.out.print("Informe o titulo do aviso: ");
                    br = new BufferedReader(new InputStreamReader(System.in));
                    String titulo = br.readLine();
                    
                    System.out.print("Informe a descricao do aviso: ");
                    br = new BufferedReader(new InputStreamReader(System.in));
                    String descricao = br.readLine();
                    
                    json.put("operacao", Operacao.salvarAviso.toString());
                    json.put("token", token);
                    
                    JSONObject aviso = new JSONObject();
                    aviso.put("id", idAviso);
                    aviso.put("categoria", idCategoria);
                    aviso.put("titulo", titulo);
                    aviso.put("descricao", descricao);
                    
                    json.put("aviso", aviso);
                    
                    System.out.println("Enviado ao servidor: " + json.toString());
                    out.println(json.toString());
                    System.out.println("Recebido do servidor: " + in.readLine());
                    break;

                case "16":
                	System.out.print("Informe o ID do aviso: ");
                    br = new BufferedReader(new InputStreamReader(System.in));
                    idAviso = Integer.parseInt(br.readLine());
                    
                    json.put("operacao", Operacao.excluirAviso.toString());
                    json.put("token", token);
                    json.put("id", idAviso);
                    
                    System.out.println("Enviado ao servidor: " + json.toString());
                    out.println(json.toString());
                    System.out.println("Recebido do servidor: " + in.readLine());
                    break;

                case "17":
                	System.out.print("Informe o ra: ");
                    br = new BufferedReader(new InputStreamReader(System.in));
                    ra = br.readLine();
                    
                    json.put("operacao", Operacao.listarUsuarioCategorias.toString());
                    json.put("token", token);
                    json.put("ra", ra);
                    
                    System.out.println("Enviado ao servidor: " + json.toString());
                    out.println(json.toString());
                    System.out.println("Recebido do servidor: " + in.readLine());
                    break;

                case "18":
                	System.out.print("Informe o ID: ");
                    br = new BufferedReader(new InputStreamReader(System.in));
                    idCategoria = Integer.parseInt(br.readLine());
                    
                    System.out.print("Informe o ra: ");
                    br = new BufferedReader(new InputStreamReader(System.in));
                    ra = br.readLine();
                    
                    json.put("operacao", Operacao.cadastrarUsuarioCategoria.toString());
                    json.put("token", token);
                    json.put("categoria", idCategoria);
                    json.put("ra", ra);
                    
                    System.out.println("Enviado ao servidor: " + json.toString());
                    out.println(json.toString());
                    System.out.println("Recebido do servidor: " + in.readLine());
                    break;

                case "19":
                	System.out.print("Informe o ID: ");
                    br = new BufferedReader(new InputStreamReader(System.in));
                    idCategoria = Integer.parseInt(br.readLine());
                    
                    System.out.print("Informe o ra: ");
                    br = new BufferedReader(new InputStreamReader(System.in));
                    ra = br.readLine();
                    
                    json.put("operacao", Operacao.descadastrarUsuarioCategoria.toString());
                    json.put("token", token);
                    json.put("categoria", idCategoria);
                    json.put("ra", ra);
                    
                    System.out.println("Enviado ao servidor: " + json.toString());
                    out.println(json.toString());
                    System.out.println("Recebido do servidor: " + in.readLine());
                    break;
                	
                case "0":
                    System.out.println("Encerrando o cliente.");
                    out.close();
                    in.close();
                    br.close();
                    echoSocket.close();
                    return;

                default: System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }
	
	
	
	private static int verificarRetorno(String resposta) {
        try {
            JSONObject respostaJson = new JSONObject(resposta);
            
            return respostaJson.getInt("status");
        } catch (Exception e) {
            System.out.println("Erro ao processar a resposta do servidor: " + resposta);
            return 401;
        }
    }
	
	private static String verificarToken(String resposta) {
        try {
            JSONObject respostaJson = new JSONObject(resposta);
            
            return respostaJson.getString("token");
        } catch (Exception e) {
            System.out.println("Erro ao processar a resposta do servidor: " + resposta);
            return null;
        }
    }
}
		