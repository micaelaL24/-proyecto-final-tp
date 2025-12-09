package online;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ServerThread extends Thread {

    private static final int PORT = 5000;

    private DatagramSocket socket;
    private final List<ClientInfo> clients = new ArrayList<>();
    private final ConcurrentHashMap<Integer, float[]> positions = new ConcurrentHashMap<>();
    private volatile boolean running = true;

    private Integer nivelActual = null;
    private boolean juegoFinalizado = false;   // ← NUEVO: evita doble victoria/derrota

    public ServerThread() {
        try {
            socket = new DatagramSocket(PORT);
            System.out.println("[SERVER] Socket abierto en puerto " + PORT);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        byte[] buffer = new byte[1024];
        while (running) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String msg = new String(packet.getData(), 0, packet.getLength()).trim();
                processMessage(msg, packet.getAddress(), packet.getPort());
            } catch (IOException e) {
                if (running) e.printStackTrace();
            }
        }
        if (socket != null && !socket.isClosed()) socket.close();
        System.out.println("[SERVER] Thread terminado.");
    }

    private void processMessage(String msg, InetAddress address, int port) {
        String[] parts = msg.split(":");
        if (parts.length == 0) return;

        switch (parts[0]) {

            case "Connect":
                if (parts.length >= 2) {
                    int nivelCliente = Integer.parseInt(parts[1]);
                    handleConnection(address, port, nivelCliente);
                }
                break;

            case "UpdatePos":
                if (parts.length >= 5) {
                    int player = Integer.parseInt(parts[1]);
                    float x = Float.parseFloat(parts[2]);
                    float y = Float.parseFloat(parts[3]);
                    String dir = parts[4];

                    positions.put(player, new float[]{x, y});

                    sendMessageToAll("UpdatePos:" + player + ":" + x + ":" + y + ":" + dir);
                }
                break;

            case "Pause":
                sendMessageToAll("Pause");
                break;

            case "Resume":
                sendMessageToAll("Resume");
                break;

            case "Win":
                if (!juegoFinalizado) {
                    juegoFinalizado = true;
                    System.out.println("[SERVER] Victoria recibida → enviando WIN a todos");
                    sendMessageToAll("Win");
                }
                break;

            case "Lose":
                if (!juegoFinalizado) {
                    juegoFinalizado = true;
                    System.out.println("[SERVER] Derrota recibida → enviando LOSE a todos");
                    sendMessageToAll("Lose");
                }
                break;

            case "Disconnect":
                removeClient(address, port);
                break;

            default:
                System.out.println("[SERVER] Mensaje desconocido: " + msg);
        }
    }

    private void handleConnection(InetAddress address, int port, int nivelCliente) {

        for (ClientInfo c : clients) {
            if (c.address.equals(address) && c.port == port) return;
        }

        // VALIDACIÓN DE NIVEL
        if (nivelActual == null) {
            nivelActual = nivelCliente;
            juegoFinalizado = false; // reset estado
            System.out.println("[SERVER] Nivel establecido en: " + nivelActual);
        } else if (nivelActual != nivelCliente) {
            sendMessage("LevelMismatch", address, port);
            return;
        }

        int playerNum = clients.size() + 1;

        if (playerNum > 2) {
            sendMessage("Full", address, port);
            return;
        }

        ClientInfo ci = new ClientInfo(address, port, playerNum);
        clients.add(ci);

        if (playerNum == 1) {
            positions.put(playerNum, new float[]{100f, 400f});
        } else {
            positions.put(playerNum, new float[]{100f, 100f});
        }

        sendMessage("PlayerNum:" + playerNum, address, port);

        // si hay 2 jugadores se inicia
        if (clients.size() == 2) {
            System.out.println("2 jugadores listos → START");
            sendMessageToAll("Start");
        }
    }

    private synchronized void removeClient(InetAddress address, int port) {
        ClientInfo target = null;
        for (ClientInfo c : clients) {
            if (c.address.equals(address) && c.port == port) {
                target = c;
                break;
            }
        }
        if (target != null) {
            clients.remove(target);
            positions.remove(target.playerNum);
            System.out.println("[SERVER] Cliente desconectado");

            if (clients.isEmpty()) {
                nivelActual = null;
                juegoFinalizado = false;
                System.out.println("[SERVER] Nivel reseteado");
            }
        }
    }

    private void sendMessage(String msg, InetAddress address, int port) {
        try {
            byte[] data = msg.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void sendMessageToAll(String msg) {
        for (ClientInfo c : clients) {
            sendMessage(msg, c.address, c.port);
        }
    }

    public void shutDown() {
        running = false;
        if (socket != null && !socket.isClosed()) socket.close();
    }

    public void terminate() {
        shutDown();
    }

    private static class ClientInfo {
        InetAddress address;
        int port;
        int playerNum;
        ClientInfo(InetAddress a, int p, int n) {
            address = a; port = p; playerNum = n;
        }
    }
}
