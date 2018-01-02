package client;

import java.io.*;
import java.net.Socket;
import java.util.Random;

/**
 * Created by Alicja on 2017-12-28.
 */
public class Client {
    private final static String ADDRESS = "localhost";
    private final static int PORT = 9090;
    private Integer NIU;
    Random rand = new Random();

    private Socket client;

    public void setNIU(int NIU) {
        this.NIU = NIU;
    }

    public String getNIU() {
        return NIU.toString();
    }



    public Client() {
        try {
            this.client = new Socket(ADDRESS, PORT);

            this.NIU = rand.nextInt(6000);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public DataOutputStream OutputConnection() {
        OutputStream outToServer = null;
        try {
            outToServer = client.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        DataOutputStream outServ = new DataOutputStream(outToServer);


        return outServ;
    }

    public DataInputStream InputConnection() {
        InputStream inFromServer = null;
        try {
            inFromServer = client.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        DataInputStream in = new DataInputStream(inFromServer);

        return in;
    }
    public void close()
    {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

