package online;

import com.badlogic.gdx.Gdx;
import pantallas.PantallaDerrota;
import pantallas.PantallaVictoria;
import utiles.Render;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClientThread extends Thread {

    private final int serverPort = 5000;
    private String serverIpStr = "127.0.0.1";

    private DatagramSocket socket;
    private InetAddress serverIp;

    private volatile boolean end = false;

    private ClientNetworkListener listener;

    private int myPlayerNum = -1;
    private int nivelSeleccionado = -1;

    public ClientThread(ClientNetworkListener listener, int nivel) {
        this.listener = listener;
        this.nivelSeleccionado = nivel;

        try {
            serverIp = InetAddress.getByName(serverIpStr);
            socket = new DatagramSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setListener(ClientNetworkListener listener) {
        this.listener = listener;
    }

    public int getMyPlayerNum() {
        return myPlayerNum;
    }

    @Override
    public void run() {
        if (socket == null) return;

        // Enviar nivel al servidor
        sendMessage("Connect:" + nivelSeleccionado);
        System.out.println("[CLIENT] -> Connect enviado para nivel " + nivelSeleccionado);

        while (!end) {
            try {
                DatagramPacket packet =
                    new DatagramPacket(new byte[1024], 1024);
                socket.receive(packet);

                processMessage(packet);

            } catch (IOException e) {
                if (!end) e.printStackTrace();
            }
        }

        if (!socket.isClosed()) socket.close();
    }

    private void processMessage(DatagramPacket packet) {
        String msg = new String(packet.getData(), 0, packet.getLength()).trim();
        String[] parts = msg.split(":");

        switch (parts[0]) {

            case "PlayerNum":
                myPlayerNum = Integer.parseInt(parts[1]);
                if (listener != null) listener.onConnected(myPlayerNum);
                break;

            case "Start":
                if (listener != null) listener.onStart();
                break;

            case "Full":
                if (listener != null) listener.onFull();
                break;

            case "LevelMismatch":
                if (listener != null) listener.onLevelMismatch();
                break;

            case "UpdatePos":
                if (parts.length >= 5) {
                    int player = Integer.parseInt(parts[1]);
                    float x = Float.parseFloat(parts[2]);
                    float y = Float.parseFloat(parts[3]);
                    boolean dir = parts[4].equals("1");

                    if (listener != null)
                        listener.onUpdatePos(player, x, y, dir);
                }
                break;

            case "Pause":
                listener.onPauseGame();
                break;

            case "Resume":
                listener.onResumeGame();
                break;

            case "Win":
                System.out.println("[CLIENT] Recibí WIN del servidor");
                if (listener != null) listener.onWin();
                break;

            case "Lose":
                System.out.println("[CLIENT] Recibí LOSE del servidor");
                if (listener != null) listener.onLose();
                break;



        }
    }

    public void sendMessage(String message) {
        try {
            byte[] data = message.getBytes();
            DatagramPacket packet =
                new DatagramPacket(data, data.length, serverIp, serverPort);
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendPosition(float x, float y, boolean mirandoDerecha) {
        if (myPlayerNum == -1) return;

        int dir = mirandoDerecha ? 1 : 0;
        String msg = "UpdatePos:" + myPlayerNum + ":" + x + ":" + y + ":" + dir;
        sendMessage(msg);
    }


    public void sendWin() {
        sendMessage("Win");
    }


    public void sendLose() {
        sendMessage("Lose");
    }

    public void terminate() {
        end = true;
        sendMessage("Disconnect");

        if (socket != null && !socket.isClosed())
            socket.close();

        interrupt();
    }
}
