import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorSocket {

    public static void main(String[] args) {
        // Inicia o servidor socket e aceita novas conexões
        ServerSocket servidor = null;
        try {
            System.out.println("Iniciando o servidor.");
            servidor = new ServerSocket(9999);
            System.out.println("Servidor iniciado.");

            // Espera conexões
            while (true) {
                Socket cliente = servidor.accept();
                new GerenciadorDeClientes(cliente);
            }
        } catch (IOException e) {
            try {
                if(servidor != null) {
                    servidor.close();
                }
            } catch (IOException err) {}

            System.err.println("A porta está ocupada ou o servidor foi fechado.");
            e.printStackTrace();
        }

    }
}
