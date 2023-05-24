package socket;
import java.io.IOException;
import java.io.InputStreamReader;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.net.Socket;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClienteSocket extends JFrame implements KeyListener, ActionListener {
	JButton B1, B2;
	static JTextArea TA1, TV1; 
	static JScrollPane barra;
	static String  mensagemfinal;	
	static String ip;
	static String loginS;
	JTextField tnick;
	JList lista;
	JLabel tx1;
	static JLabel tx2;
	private PrintWriter escritor;
	private BufferedReader leitor;
	private gerenciador gerenciador;
	
	
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER){	
			//escrevendo a mensagem pela tecla enter
	   		String mensagemfinal = TA1.getText();
	   		if(TA1.getText().isEmpty())
	   		{
	   		return;	
	   		}
	   		Object usuario = lista.getSelectedValue();
	   		if(usuario != null)
	   		{ 
	   			TV1.setText(TV1.getText() + "\n" + loginS + ": \n" + TA1.getText());
	   			escritor.println(Comandos.MENSAGEM + usuario);
	   			escritor.println(TA1.getText());
	   			TA1.setText("");	   		
	   		
	   		}else{
	   			
	   			if(TA1.getText().equalsIgnoreCase(Comandos.SAIR))
		   		{
		   		System.exit(0);
		   		}
		   		
	   		JOptionPane.showMessageDialog(null,"Selecione o destinatário");
	   		return;
	   		}
	   		
	   		
	    }
		
	
	}

	
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void actionPerformed(ActionEvent arg0)
	  {
		
	     if (arg0.getSource() == B1)	    	 
	     {	 
	   		//escrevendo a mensagem pelo botão virtual
	   		String mensagemfinal = TA1.getText();
	   		if(TA1.getText().isEmpty())
	   		{
	   		return;	
	   		}
	   		Object usuario = lista.getSelectedValue();
	   		if(usuario != null)
	   		{ 
	   			TV1.setText(TV1.getText() + "\n" + loginS + ": \n" + TA1.getText());
	   			escritor.println(Comandos.MENSAGEM + usuario);
	   			escritor.println(TA1.getText());
	   			TA1.setText("");	   		
	   		
	   		}else{
	   			
	   			if(TA1.getText().equalsIgnoreCase(Comandos.SAIR))
		   		{
		   		System.exit(0);
		   		}
		   		
	   		JOptionPane.showMessageDialog(null,"Selecione o destinatário");
	   		return;
	   		}
	     }
     
	     if (arg0.getSource() == B2)	    	 
	     {
	    	ip = tnick.getText();
	     }
	     
	  }
	
	public ClienteSocket()
	{
		  JFrame frame = new JFrame("Messenger");
		  frame.setSize(850,500);
		  frame.setLocationRelativeTo(null);
		  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		  frame.setResizable(false);
		  frame.getContentPane().setBackground(new Color(255, 255, 255));
		  
		  tnick = new JTextField();
		  tnick.setSize(150,30);
		  tnick.setLocation(420,20);	
		  tnick.setForeground(Color.red);
		 
		  TA1 = new JTextArea();
		  TA1.setSize(570,60);
		  TA1.setLocation(15,400);
		  TA1.setForeground(Color.black);
		  TA1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		  TA1.addKeyListener(this);
		  
		  
		  
		  TV1 = new JTextArea();
		  TV1.setSize(665,320);
		  TV1.setLocation(15,70);
		  TV1.setForeground(Color.black);
		  TV1.setEditable(false);
		  TV1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		  
		  barra = new JScrollPane(TV1, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		  barra.setBounds(15, 70, 665, 320);
		  		  
		  B1 = new JButton("Enviar");
		  B1.setSize(80,30);
		  B1.setLocation(600,400);
		  B1.addActionListener(this);
		  
		  B2 = new JButton("Iniciar");
		  B2.setSize(100,30);
		  B2.setLocation(580,20);
		  B2.addActionListener(this);
		  
		  lista = new JList();
		  lista.setLocation(690,50);
		  lista.setSize(140,410);
		  lista.setBackground(Color.GRAY);
		  
		  tx1 = new JLabel("Usuários Online:");
		  tx1.setLocation(690,20);
		  tx1.setSize(100,20);
		  
		  tx2 = new JLabel("Nome");
		  tx2.setLocation(20,20);
		  tx2.setSize(100,20);
		  tx2.setForeground(Color.blue);
		  
		  
		  frame.getContentPane().setLayout(null);
		  frame.getContentPane().add(B1);
		  frame.getContentPane().add(TA1);	
		  frame.getContentPane().add(barra);
		  frame.getContentPane().add(lista);
		  frame.getContentPane().add(tx1);
		  frame.getContentPane().add(tx2);

		  
		  frame.show();
		  
		  String[] usuarios = new String[]{""};
		  preencherlista(usuarios);
		  this.gerenciador = new gerenciador(this, true);


	}
	
 
 private void preencherlista(String[] usuarios)
 {
	 DefaultListModel modelo = new DefaultListModel(); 
	 lista.setModel(modelo);
	 for(String usuario: usuarios)
	 {
	 modelo.addElement(usuario);
	 }
 }
 
	
 public void IniciarChat()
 {
	try {
		//por o ip da máquina na qual vai rodar o servidor /a porta é 8044
		final Socket cliente = new Socket("127.0.0.1", 8044);
		escritor = new PrintWriter(cliente.getOutputStream(), true);
		leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
		
	} catch (UnknownHostException e) {
		System.out.println("Endereço invalido");
		e.printStackTrace();
	} catch (IOException e) {
		System.out.println("Conexão não possível");
		e.printStackTrace();
	}
	
 }
 public static void main(String[] args)
 {
	final ClienteSocket cliente = new ClienteSocket();
	cliente.IniciarChat();		
	cliente.IniciarLeitor();	
	cliente.AtualizarLista();

	
	
 } 
 private void AtualizarLista() {
	 escritor.println(Comandos.LISTA_USUARIOS);
	 
	
}

private void IniciarLeitor() {
		//lendo mensagem
		
		try {					 				
             while(true)
             {
          	   String mensagem = leitor.readLine();
          	  if(mensagem == null || mensagem.isEmpty()){
          		 continue;
          	  	 
          	
          	  }
          	  if(mensagem.startsWith(Comandos.LISTA_USUARIOS))
          	  {
          		String[] usuarios = leitor.readLine().split(",");
          		preencherlista(usuarios);
          	  }else if(mensagem.equals(Comandos.LOGIN)){
          		 String login = JOptionPane.showInputDialog(null, "Escreva seu nome:");		    	         		 
          		 escritor.println(login);
          		 tx2.setText(login);
          		 loginS = login;
          	
          		 
          		 
          	  }else if(mensagem.equals(Comandos.LOGINACEITO)){
          		 AtualizarLista();
          		   
          	  }
          	  else
          	  {
          	  String TVA1 = TV1.getText();
          	  TV1.setText("");
          	  TV1.setText(TVA1 + "\n" + "\n " + mensagem);
          	  TA1.setText("");	
             }
             }
					
				} catch (IOException e) {
					System.out.println("Impossível ler a mensagem do servidor");
					e.printStackTrace();
				}
			}
		
		
	


private DefaultListModel getLista() {
		
		return (DefaultListModel) lista.getModel();
	}


}


