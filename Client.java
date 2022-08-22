import java.net.*;
import java.io.*;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.*;    
import java.awt.event.*;  



public class Client extends JFrame{

   
   Socket socket;

   BufferedReader br;
   PrintWriter out;

   //Declare components
   private JLabel heading = new JLabel("Client Room");
   private JTextArea messageArea = new JTextArea();
   private JTextField messageInput = new JTextField();
   private Font font = new Font("Roboto",Font.PLAIN,20);
   
   public Client()
   {
      try{
         System.out.println("Sending request to server");
         socket = new Socket("127.0.0.1",7777);
         System.out.println("connection done!");

         br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

         out = new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvents();

            startReading();
            //startWriting();

      } catch(Exception e){
         e.printStackTrace();
      }
   }

   private void handleEvents(){

      messageInput.addKeyListener(new KeyListener(){

         @Override
         public void keyTyped(KeyEvent e){

         }

         @Override
         public void keyPressed(KeyEvent e){

         }

         @Override
         public void keyReleased(KeyEvent e){
            
            //System.out.println("key released" +e.getKeyCode());
            
            if(e.getKeyCode()==10)
            {
               //System.out.println("You have pressed Enter button");

               String contentToSend = messageInput.getText();
               messageArea.append("Me :"+contentToSend+"\n");
               out.println(contentToSend);
               out.flush();
               messageInput.setText("");
               messageInput.requestFocus();
            }

         }

      });
   }

   private void createGUI(){

      // here this means the GUI window
      this.setTitle("Client Messenger[END]");
      this.setSize(500,500);
      this.setLocationRelativeTo(null);
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      //coding for component
      heading.setFont(font);
      messageArea.setFont(font);
      messageInput.setFont(font);

      //set image icon
     
      heading.setHorizontalTextPosition(SwingConstants.CENTER);
      heading.setVerticalTextPosition(SwingConstants.BOTTOM);
      heading.setHorizontalAlignment(SwingConstants.CENTER);
      heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
      messageArea.setEditable(false);


      //for message box
      //messageInput.setHorizontalAlignment(SwingConstants.CENTER);

      //now set layout for frame
      this.setLayout(new BorderLayout());

      //adding components to Frame
      this.add(heading,BorderLayout.NORTH);
      JScrollPane jScrollPane = new JScrollPane(messageArea);
      this.add(jScrollPane,BorderLayout.CENTER);
      this.add(messageInput,BorderLayout.SOUTH);

      this.setVisible(true);

   }

   public void startReading()
    {
        // thread reads and give the reads data
        Runnable r1 = ()->{

            System.out.println("reader started..");

            try{

            while(true)
            {
                
               String msg = br.readLine();
               if(msg.equals("Exit"))
               {
                  System.out.println("Server terminated the chat");
                  JOptionPane.showMessageDialog(this, "Server terminated the chat");
                  messageInput.setEnabled(false);
                  socket.close();
                  break;
               }

               //System.out.println("Server : "+msg);
               messageArea.append("Server : " + msg+"\n");
            

            }

         }catch(Exception e)
         {
            //e.printStackTrace();
            System.out.println("Connection closed!");
         }

        };

        new Thread(r1).start();
    }

    public void startWriting()
    {
        //thread receive data from user & send it to client
        Runnable r2 = ()->{
            System.out.println("writer started..");
            
            try{
            
            while(!socket.isClosed())
            {
                
                    
               BufferedReader br1 = new BufferedReader(new 
               InputStreamReader(System.in));
               String content = br1.readLine();
               out.println(content);
               out.flush();

               if(content.equals("Exit"))
               {
                  socket.close();
                  break;
               }

               
            }

            

         }catch(Exception e)
         {
            e.printStackTrace();
         }

        };

        new Thread(r2).start();
    }


   public static void main(String[] args) {

      System.out.println("This is Client..");
      new Client();
   }

}