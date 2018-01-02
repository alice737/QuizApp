package kolokwium;

import database.Database;

import java.net.*;
import java.io.*;


public class ServerThread extends Thread {
    private Socket server;


    public ServerThread(Socket socket) throws IOException {
        super();
        server = socket;

    }

    public void run() {

            try {
                DataInputStream in = new DataInputStream(server.getInputStream());
                DataOutputStream out = new DataOutputStream(server.getOutputStream());

                //if(in.readUTF()=="JAVA_PROJEKT")
                if (Database.getInstance().open("JAVA_PROJEKT")) {
                    System.out.println(" database");


                Integer ilePytan = Database.getInstance().ilePytan();
                System.out.println(ilePytan);

               String numerNIU = in.readUTF();
                int numerNIUint = Integer.parseInt(numerNIU);
                int wynik = 0;
               System.out.println("Polaczono z...." + numerNIU);
               out.writeUTF(ilePytan.toString());

                for (int i = 1; i <= ilePytan; i++) {
                    out.writeUTF( i+"."+Database.getInstance().readQuestion(i));
                    out.writeUTF(Database.getInstance().readAnswers(i, "A"));
                    out.writeUTF(Database.getInstance().readAnswers(i, "B"));
                    out.writeUTF(Database.getInstance().readAnswers(i, "C"));
                    out.writeUTF(Database.getInstance().readAnswers(i, "D"));

                    Database.getInstance().insertwynik(numerNIUint, i, in.readUTF());
                    System.out.println("czekam na odpowiedz " + Database.getInstance().compareAnswersToTrue(i, numerNIUint));
                    wynik += Database.getInstance().compareAnswersToTrue(i, numerNIUint);
                }
                Database.getInstance().dodajWynik(wynik, numerNIUint);
                System.out.println(" TWÓJ WYNIK    " + Database.getInstance().podajWynik(numerNIUint));

                out.writeUTF(Database.getInstance().podajWynik(numerNIUint));

                        out.close();
                        in.close();
                     //   Database.getInstance().close();




                server.close();
                } else {
                    System.out.println("FATAL ERROR: Couldn't connect to database");

                   // Platform.exit();
                }

            } catch (SocketTimeoutException s) {
                System.out.println("Socket timed out!");
              //  break;
            } catch (IOException e) {
                System.out.println("KONCZYMY");
               // break;
            }

    }


}