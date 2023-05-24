package socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

public class gerenciador extends Thread {
	private Socket cliente;
	private String nomeClient;
	private BufferedReader leitor;
	private PrintWriter escritor;
	private static final Map<String, gerenciador> clientes = new HashMap<String, gerenciador>();
	
	
	public gerenciador(Socket cliente)
	{
		this.cliente = cliente;
		start();
	}
	
	public gerenciador(ClienteSocket clienteSocket, boolean b) {
		
	}


	// inicializando, com tratamento de erros caso o client é fechado
	public void run()
	{
		try {
			
			// prepara e mostra as informações na tela do cliente
			leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
			escritor = new PrintWriter(cliente.getOutputStream(), true);
			escritor.println(Comandos.LOGIN);
			String msg = leitor.readLine();
			this.nomeClient = msg.toLowerCase().replaceAll(",", "");
			escritor.println(Comandos.LOGINACEITO);
			escritor.println("Bem-vindo(a) "+ this.nomeClient);
			clientes.put(this.nomeClient, this);
			 
			
			
			for (String cliente: clientes.keySet()){

				AtualizarLista(clientes.get(cliente));
			}
			
			while (true){
				msg = leitor.readLine();

				if(msg.equalsIgnoreCase(Comandos.SAIR)){
					this.cliente.close();
				}

				else if(msg.startsWith(Comandos.MENSAGEM))
				{
					String nomedestinatario = msg.substring(Comandos.MENSAGEM.length(), msg.length());
					System.out.println("Enviando mensagem para " + nomedestinatario);
					gerenciador destinatario = clientes.get(nomedestinatario);

					if(destinatario == null){

						escritor.println("O cliente nao existe");

					} else {
						destinatario.getEscritor().println(this.nomeClient + ":" + leitor.readLine());
					}
					
				// listando os clientes
				} 

				else if(msg.equals(Comandos.LISTA_USUARIOS)){
					AtualizarLista(this);
				}

				else{
					escritor.println(this.nomeClient + msg);
				}
			}
		} catch (IOException e) {
			System.err.println("O cliente fechou a conexao");
			clientes.remove(this.nomeClient);			
			e.printStackTrace();
			for(String cliente: clientes.keySet())
			{
				AtualizarLista(clientes.get(cliente));
				System.err.println(this.nomeClient + " fechou a conexao");
			}
		}
	}

	private void AtualizarLista(gerenciador gerenciador) {

		StringBuffer str = new StringBuffer();
		for(String c: clientes.keySet()){
			str.append(c);
			str.append(",");
		}

		str.delete(str.length()-1, str.length());
		gerenciador.getEscritor().println(Comandos.LISTA_USUARIOS);
		gerenciador.getEscritor().println(str.toString());
		
	}

	public PrintWriter getEscritor() {
		return escritor;
	}

	public String getNomeClient() {
		return nomeClient;
	}
	
	
}
