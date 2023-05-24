package socket;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import sun.print.resources.*;

public class servidor {

	public static void main(String[] args) 
	{
		
		ServerSocket servidor = null;
		
		
		try {
			System.out.println("iniciando o servidor");
			servidor = new ServerSocket(8044);
			System.out.println("servidor iniciado");
			while(true){
				Socket cliente = servidor.accept();
				new gerenciador(cliente);
			}
		} 
		catch (IOException e) {
			
			try {
				if(servidor != null){
					servidor.close();
				}
			}
			catch(IOException e1)
			{}
			System.err.println("Erro ao conectar, porta ocupada!");
			e.printStackTrace();
		}
		
    }
}
