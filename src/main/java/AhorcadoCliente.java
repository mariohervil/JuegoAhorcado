import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class AhorcadoCliente {


        public static void main(String[] args) throws IOException {
            Boolean salir = false;

            String letra;
            String mensaje=null;
            Socket clientSocket = null; // Socket de conexi�n

            System.out.println("###########################");
            System.out.println("CLIENTE DE AHORCADO");
            System.out.println("###########################");
            try {

                clientSocket = new Socket("PCSG-DAM-06", 5555); // Conexi�n al servidor
                DataInputStream one = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream two = new DataOutputStream(clientSocket.getOutputStream());
                System.out.println("Conectado al servidor...");
                mensaje = one.readUTF().toString();
                System.out.println(mensaje);
                Scanner sc = new Scanner(System.in);

                while (!salir) {
                    mensaje = one.readUTF().toString();
                    System.out.println(mensaje);
                    letra=sc.next();
                    String letraEnviada = letra.substring(0, 1);
                    two.writeUTF(letraEnviada);
                    mensaje = one.readUTF().toString();
                    System.out.println(mensaje);
                    mensaje = one.readUTF().toString();
                    System.out.println(mensaje);
                    if (mensaje.equals("Has ganado") || mensaje.equals("Has perdido") ) {salir = true;};
                }
                //mensaje = one.readUTF().toString();
                //System.out.println(mensaje);
                System.out.println("Conexión cerrada. ");
                sc.close();
                if (one != null)
                    one.close();
                if (two != null)
                    two.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            // Cierra conexiones


            if (clientSocket != null)
                clientSocket.close();

        }

    }



