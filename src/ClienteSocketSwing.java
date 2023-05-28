import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClienteSocketSwing extends JFrame implements Serializable {

    private static final long serialVersionUID = -3546948957208310415L;
    private JTextArea taEditor = new JTextArea("Digite uma mensagem...");
    private JTextArea taVisor = new JTextArea("");
    private JList liUsuarios = new JList();
    private PrintWriter escritor;
    private BufferedReader leitor;
    private JScrollPane scrollTaVisor = new JScrollPane(taVisor);

    public ClienteSocketSwing() {
        setTitle("Chat");
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        liUsuarios.setBackground(Color.BLACK);
        taEditor.setBackground(Color.GRAY);

        taEditor.setPreferredSize(new Dimension(400, 40));
        // taVisor.setPreferredSize(new Dimension(350, 100));
        taVisor.setEditable(false);
        liUsuarios.setPreferredSize(new Dimension(100, 140));

        add(taEditor, BorderLayout.SOUTH);
        add(scrollTaVisor, BorderLayout.CENTER);
        add(new JScrollPane(liUsuarios), BorderLayout.WEST);

        pack();
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        String[] usuarios = new String[]{"elvis", "maria", "joao", "jose", "renata", "marizete", "mateus", "robson"};
        preencherListaUsuarios(usuarios);
    }

    private void preencherListaUsuarios(String[] usuarios) {
        DefaultListModel modelo = new DefaultListModel<>();
        liUsuarios.setModel(modelo);
        for(String usuario: usuarios) {
            modelo.addElement(usuario);
        }
    }

    private void iniciarEscritor() {
        taEditor.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Se for mensagem vazia para
                    if (taVisor.getText().isEmpty()) {
                        return;
                    }

                    Object usuario = liUsuarios.getSelectedValue();
                    // Se selecionar um usuário
                    if(usuario != null) {
                        // Escreve na tela
                        taVisor.append("Eu: ");
                        taVisor.append(taEditor.getText());
                        taVisor.append("\n");

                        // Comando para enviar mensagem
                        escritor.println(Comandos.MENSAGEM + usuario);

                        // Envia para o servidor
                        escritor.println(taEditor.getText());

                        // Limpa o editor
                        taEditor.setText("");
                        e.consume();
                    } else {
                        // Fecha a conexão do cliente
                        if (taVisor.getText().equalsIgnoreCase(Comandos.SAIR)) {
                            System.exit(0);
                        }

                        JOptionPane.showMessageDialog(ClienteSocketSwing.this, "Selecione um usuário");
                        return;
                    }

                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });
    }

    public void iniciarChat() {
        try {
            // Conecta no servidor
            final Socket cliente = new Socket("127.0.0.1", 9999);
            escritor = new PrintWriter(cliente.getOutputStream(), true);
            leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));

        } catch (UnknownHostException e) {
            System.out.println("O endereço passado é inválido.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("O servidor pode estar fora do ar.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ClienteSocketSwing cliente = new ClienteSocketSwing();
        cliente.iniciarChat();
        cliente.iniciarEscritor();
        cliente.iniciarLeitor();
    }

    public void atualizarListaUsuarios() {
        // Comando para ver a lista de usuários
        escritor.println(Comandos.LISTA_USUARIOS);
    }

    public void iniciarLeitor() {
        // Lendo a mensagem do servidor
        try {
            while(true) {
                String mensagem = leitor.readLine();

                // Recebe mensagem
                if (mensagem.equals(Comandos.LISTA_USUARIOS)) {
                    // Pega a lista de usuários
                    String[] usuarios = leitor.readLine().split(",");
                    preencherListaUsuarios(usuarios);
                } else if (mensagem.equals(Comandos.LOGIN)) {
                    String login = JOptionPane.showInputDialog("Qual o seu login?");
                    escritor.println(login);
                } else if (mensagem.equals(Comandos.LOGIN_NEGADO)) {
                    JOptionPane.showMessageDialog(ClienteSocketSwing.this, "O login é inválido.");
                } else if(mensagem.equals(Comandos.LOGIN_ACEITO)) {
                    atualizarListaUsuarios();
                }else {
                    taVisor.append(mensagem);
                    taVisor.append("\n");
                    taVisor.setCaretPosition(taVisor.getDocument().getLength());
                }

            }
        } catch (IOException e) {
            System.out.println("Impossível ler a mensagem do servidor.");
            e.printStackTrace();
            System.exit(0);
        }
    }

    private DefaultListModel getListaUsuarios() {
        return (DefaultListModel) liUsuarios.getModel();
    }
}
