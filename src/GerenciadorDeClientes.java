import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

// Faz a comunicação entre cliente-servidor e cliente-cliente
// Utiliza threads para ficar aceitando vários clientes ao mesmo tempo
public class GerenciadorDeClientes extends Thread {

    private Socket cliente;
    private String nomeCliente;
    private BufferedReader leitor;

    private PrintWriter escritor;

    // Lista com todos os clientes
    private static final Map<String, GerenciadorDeClientes> clientes = new HashMap<String, GerenciadorDeClientes>();

    // Um gerenciador para cada cliente
    public GerenciadorDeClientes(Socket cliente) {
        this.cliente = cliente;
        start(); // Inicia a Thread
    }

    // Conversa entre os clientes
    @Override
    public void run() {
        /**
         * Conexão de dados que são enviados em tempo real de pacote em pacote
         * Onde fica a mensagem do cliente
         * cliente.getInputStream(); -> Ler apenas em bytes
         * BufferedReader -> ler e passa para String
         */
        try {
            // Ler as mensagens do cliente
            leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            /**
             * Escreve strings para o cliente
             * autoFlush: true -> sempre que chama o println a mensagem é enviada automaticamente
             */
            escritor = new PrintWriter(cliente.getOutputStream(), true);
            efetuarLogin();
            String msg;


            while(true) {
                msg = leitor.readLine();
                // Fecha a conexão do cliente
                if(msg.equalsIgnoreCase(Comandos.SAIR)) {
                    this.cliente.close();

                // Envia msg para um cliente
                } else if(msg.startsWith(Comandos.MENSAGEM)) {
                    String nomeDestinatario = msg.substring(Comandos.MENSAGEM.length(), msg.length()); // Pega o nome do destinatário
                    System.out.println("Enviando para " + nomeDestinatario);
                    GerenciadorDeClientes destinatario = clientes.get(nomeDestinatario); // Pega o destinatário pelo nome

                    if(destinatario == null) {
                        escritor.println("O cliente informado não existe.");
                    } else {
                        //escritor.println("Digite uma mensagem para " + destinatario.getNomeCliente());
                        // Envia a mensagem para o destinatário
                        destinatario.getEscritor().println(this.nomeCliente + ": " + leitor.readLine());
                    }

                // Lista o nome dos clientes
                } else if(msg.equals(Comandos.LISTA_USUARIOS)) {
                    atualizarListaUsuarios(this);
                }else {
                    escritor.println(this.nomeCliente + ", você disse:" + msg);
                }
            }

        } catch (IOException e) {
            System.err.println("O cliente fechou a conexão.");
            clientes.remove(this.nomeCliente);
            e.printStackTrace();
        }
    }

    private void efetuarLogin() throws IOException {
        while(true) {
            escritor.println(Comandos.LOGIN);
            this.nomeCliente = leitor.readLine().toLowerCase().replaceAll(",", "");

            if(this.nomeCliente.equalsIgnoreCase("null") || this.nomeCliente.isEmpty()) {
                escritor.println(Comandos.LOGIN_NEGADO);
            } else if (clientes.containsKey(this.nomeCliente)) { // Se já existir
                escritor.println(Comandos.LOGIN_NEGADO);
            } else {
                escritor.println(Comandos.LOGIN_ACEITO);
                escritor.println("Olá, " + this.nomeCliente);
                // Armazena o cliente na lista
                clientes.put(this.nomeCliente, this);

                // Atualiza a lista dos clientes
                for (String cliente : clientes.keySet()) {
                    atualizarListaUsuarios(clientes.get(cliente));
                }
                break;
            }
        }
    }

    private void atualizarListaUsuarios(GerenciadorDeClientes gerenciadorDeClientes) {
        StringBuffer str = new StringBuffer();
        for(String c: clientes.keySet()) {
            if (gerenciadorDeClientes.getNomeCliente().equals(c)){
                continue;
            }
            str.append(c);
            str.append(",");
        }
        if(str.length() > 0) {
            str.delete(str.length() - 1, str.length());
        }
        gerenciadorDeClientes.getEscritor().println(Comandos.LISTA_USUARIOS);
        gerenciadorDeClientes.getEscritor().println(str.toString());
    }

    public PrintWriter getEscritor() {
        return escritor;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

}
