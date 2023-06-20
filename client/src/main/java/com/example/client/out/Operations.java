package com.example.client.out;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Operations {
    private final static org.apache.logging.log4j.Logger Logger = LogManager.getLogger();
    private final Socket clientSocket = new Socket();
    private Server server;
    private final InetAddress serverIpAddress = InetAddress.getByName("127.0.0.1");
    private final int SERVER_PORT = 12345;

    public Operations() throws IOException {
        start();
    }

    private void start() throws IOException {
        Logger.info("Connecting to server...");
        clientSocket.connect(new InetSocketAddress(serverIpAddress, SERVER_PORT));
        server = new Server(clientSocket);
    }

    public boolean checkUser(String userName, String passwd) throws Exception {
        JSONObject message = new JSONObject();
        message.put("command", "checkUser");
        message.put("userName", userName);
        message.put("passwd", passwd);
        String jsonMessage = message.toString();
        server.send(jsonMessage);
        String response = server.receive();
        JSONObject jsonObject = new JSONObject(response);
        Logger.info("Check user response: " + response);
        return jsonObject.getBoolean("result");
    }

    public void sendRequestToGetSomething(){
        Logger.info("request participants by team");
        JSONObject requestMessage = new JSONObject();
        requestMessage.put("command", "getParticipantsByTeam");
        try {
            server.send(requestMessage.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Object> getSomething(){
        try {
            String response = server.receive();
            Logger.info("Response1 -> " + response);
            JSONObject json = new JSONObject(response);
            List<Object> objects = new ArrayList<>();
            JSONArray jsonParticipants = json.getJSONArray("objects");
            for (int i = 0; i < jsonParticipants.length(); i++) {
                objects.add(jsonParticipants.getJSONObject(i).getString("field1")
                        + " -> "
                        + ((Integer) jsonParticipants.getJSONObject(i).getInt("field2")).toString());
            }
            return objects;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
