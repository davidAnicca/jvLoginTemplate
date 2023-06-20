package org.example.out;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Objects;

public class Client {
    private final Socket socket;
    private Boolean open;

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String userName;

    public Client(Socket socket) {
        this.socket = socket;
        open = true;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client client)) return false;
        return Objects.equals(socket.getPort(), client.socket.getPort());
    }

    @Override
    public int hashCode() {
        return Objects.hash(socket.getPort());
    }

    public void send(String message) throws Exception {
        if(!open){
            throw new Exception("Unused client");
        }
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(message.getBytes());
        outputStream.flush();
    }

    public String receive() throws Exception {
        if(!open){
            throw new Exception("Unused client");
        }
        InputStream inputStream = socket.getInputStream();
        byte[] buffer = new byte[1024];
        int bytesRead = inputStream.read(buffer);
        return new String(buffer, 0, bytesRead);
    }

    public void close() throws IOException {
        open = false;
        socket.close();
    }

}
