import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class AhorcadoHiloServer extends Thread {
    static String palabra;

    Socket socket = null; // IP + Puerto
    static List<String> acertadas;
    static List<String> dichas;
    static List<String> letrasPalabra;
    static String guiones = "";
    static int vidas = 8;
    static String letrasDichas;
    static List<String> caracteresEspeciales;
    static boolean abandono = false;
    DataOutputStream dos;
    DataInputStream uno;
    String user;
    String ordenador;
    String sistema;
    public AhorcadoHiloServer(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try{
            uno = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF("Cuál es tu user?");
            user = uno.readUTF();
            dos.writeUTF("Cuál es el nombre de tu ordenador?");
           ordenador = uno.readUTF();
            dos.writeUTF("Cuál es tu sistema operativo?");
            sistema = uno.readUTF();

        } catch (IOException e) {
            e.printStackTrace();
        }

        letrasDichas = "";
        // Cada vez que se inicia el hilo, recibe una conexi�n
        String letraIntento;
        String[] palabras = new String[]{"abeto", "gato", "amarillo", "alerta", "pantalla", "perdido", "vaciar", "imperio", "fuego", "corona",
                "trueno", "terremoto", "llamada", "fiesta", "febrero"};


        palabra = palabras[((int) (Math.random() * palabras.length))];
        acertadas = new ArrayList<String>();
        dichas = new ArrayList<String>();
        letrasPalabra = new ArrayList<String>();
        for (int i = 1; i < palabra.length(); i++) {
            letrasPalabra.add(palabra.substring((i - 1), i));
        }
        for (int i = 0; i < palabra.length(); i++) {
            guiones += "_ ";
        }

        boolean salir = false;
        try {

            dos.writeUTF("Bienvenido al juego del ahorcado, " + user + ", tu palabra tiene: " + palabra.length() + " letras." +
                    "\nTendrás que decir letras hasta que aciertes la palabra, tienes 8 VIDAS, si las agotas, pierdes.");
            while (vidas != 0 && guiones.contains("_") && !abandono) {
                dos.writeUTF("Di una letra");
                letraIntento = uno.readUTF();
                if (letraIntento.substring(0, 1).equals("*")) {
                    abandono = true;
                }


                // letraIntento.matches("[a-zA-Z*]");
                if (!letraIntento.matches("[a-zA-Z*]") && !abandono) {
                    dos.writeUTF("Esa letra no está permitida");
                } else if (palabra.contains(letraIntento) && !acertadas.contains(letraIntento.substring(0, 1)) && !dichas.contains((letraIntento.substring(0, 1)))
                        && !abandono) {
                    addToFound(letraIntento.substring(0, 1));
                    addToDichas(letraIntento.substring(0, 1));
                    generateDashes();
                    dos.writeUTF("Has acertado la letra, " + letraIntento + " está en tu palabra!\n\n" + guiones);
                } else if (palabra.contains(letraIntento) && acertadas.contains(letraIntento.substring(0, 1)) && !abandono) {
                    dos.writeUTF("Ya has dicho la letra " + letraIntento + "!\n\n" + guiones);
                    addToDichas(letraIntento.substring(0, 1));
                    vidas--;
                } else if (!palabra.contains(letraIntento) && !abandono) {
                    dos.writeUTF("Has fallado\n\n" + guiones);
                    addToDichas(letraIntento.substring(0, 1));
                    vidas--;
                }
                if (vidas == 0) {
                    dos.writeUTF("Has perdido");
                }
                if (!guiones.contains("_")) {
                    dos.writeUTF("Has ganado");
                }


                generarListaDichas();
                dos.writeUTF("\nLetras dichas: " + letrasDichas + " \nVIDAS: " + vidas);
                if (abandono) dos.writeUTF("Abandono");
            }
abandono = false;
vidas = 8;
guiones = "";
dichas.clear();
acertadas.clear();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            if (socket != null)
                socket.close(); // Cierra socket

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("Conexion cerrada.");
        System.out.println("#####################################");
    }

    public void addToFound(String c) {
        this.acertadas.add(c);
    }

    public void addToDichas(String c) {
        this.dichas.add(c);
    }

    public boolean ifFoundListIncludes(char[] s) {
        if (acertadas.contains(s[0])) {
            return true;
        } else {
            return false;
        }
    }

    public void generateDashes() {
        if (acertadas.size() > 0) {
            StringBuilder result = new StringBuilder(acertadas.size());
            acertadas.stream().forEach((c) -> {
                result.append(c);
            });
            String output = result.toString();
            String except = "[^" + output + "]";
            guiones = palabra.replaceAll(except, " _ ");
        }

    }

    public void generarListaDichas() {
        if (dichas.size() > 0) {
            StringBuilder result = new StringBuilder(dichas.size());
            dichas.stream().forEach((c) -> {
                result.append(c + " ");
            });
            letrasDichas = result.toString();

        }


    }
}



