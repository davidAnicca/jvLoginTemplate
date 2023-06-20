package org.example.out;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.services.Service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Operations {
    private final Service service;

    private final static Logger Logger = LogManager.getLogger();

    private final Object locker = new Object();

    private final ServerSocket serverSocket;

    private final List<Client> clients;

    public Operations(Service service) throws IOException {
        this.service = service;
        clients = new ArrayList<>();
        serverSocket = new ServerSocket(12345);
        acceptClient();
    }

    private void acceptClient() {
        Logger.info("Accepting clients...");
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();

                Logger.info(
                        "Accept a client from ip "
                                + clientSocket.getRemoteSocketAddress().toString()
                                + " port "
                                + clientSocket.getPort()
                );
                Thread clientThread = new Thread(() -> clientCommunication(clientSocket));
                clientThread.start();
            } catch (IOException e) {
                Logger.error(e.getMessage());
            }
        }
    }

    private void clientCommunication(Socket clientSocket) {
        Client client = new Client(clientSocket);
        synchronized (locker) {
            clients.add(client);
        }
        try {
            beginReceive(client);
        } catch (IOException e) {
            Logger.error(e.getMessage());
        }
    }

    private void beginReceive(Client client) throws IOException {
        while (true) {
            try {
                String message = client.receive();
                Logger.info(
                        "message from client: "
                                + client.getSocket().getRemoteSocketAddress().toString()
                                + "\n-->>"
                                + message
                );
                treatMessage(message, client);
            } catch (Exception e) {
                Logger.error(e.getMessage());
                client.close();
                clients.remove(client);
                return;
            }
        }
    }

    private void treatMessage(String message, Client client) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonObject = objectMapper.readTree(message);

            switch (jsonObject.get("command").asText()) {
                case "checkUser" -> treatCheck(jsonObject, client);
                default -> Logger.error("Unknown command:" +
                        "\n->> " + jsonObject.toString());
            }
        } catch (Exception e) {
            Logger.error("unable to treat a message");
            Logger.error(e.getMessage());
        }
    }


    private void broadcast() {
        Map<String, Object> updateMessage = new HashMap<>();
        updateMessage.put("command", "update");
        updateMessage.put("message", "New participant added.");

        String jsonMessage = JsonUtils.convertToJson(updateMessage);

        synchronized (locker) {
            for (Client client : clients) {
                try {
                    Logger.info(
                            "Sending notification to client "
                                    + ((InetSocketAddress) client.getSocket().getRemoteSocketAddress()).getPort());
                    client.send(jsonMessage);
                } catch (Exception e) {
                    Logger.error(e.getMessage());
                }
            }
        }
    }

//    private void treatGetParticipants(JsonNode jsonObject, Client client) throws Exception {
//        String team = jsonObject.get("team").asText();
//        List<Participant> participants = service.getParticipantsByTeam(team);
//        List<Map<String, Object>> participantsObjects = new ArrayList<>();
//        for (Participant participant : participants) {
//            Map<String, Object> participantObject = new HashMap<>();
//            participantObject.put("name", participant.getName());
//            participantObject.put("capacity", participant.getCapacity());
//            participantsObjects.add(participantObject);
//        }
//        Map<String, Object> stopMessage = new HashMap<>();
//        stopMessage.put("command", "stop");
//        client.send(JsonUtils.convertToJson(stopMessage));
//        Map<String, Object> response = new HashMap<>();
//        response.put("command", "participantsList");
//        response.put("participants", participantsObjects);
//        client.send(JsonUtils.convertToJson(response));
//    }


    private void treatCheck(JsonNode jsonObject, Client client) throws Exception {
        String userName = jsonObject.get("userName").asText();
        String passwd = jsonObject.get("passwd").asText();
        Boolean result = service.checkUser(userName, passwd);
        ObjectNode responseMessage = JsonNodeFactory.instance.objectNode();
        responseMessage.put("command", "checkUser");
        responseMessage.put("result", result);
        String jsonMessage = responseMessage.toString();
        client.send(jsonMessage);
    }
}
