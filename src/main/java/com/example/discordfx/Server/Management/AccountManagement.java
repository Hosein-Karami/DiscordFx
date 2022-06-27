package com.example.discordfx.Server.Management;

import com.example.discordfx.Log.ServerLog;
import com.example.discordfx.Moduls.Dto.User.Status;
import com.example.discordfx.Moduls.Dto.User.User;
import com.example.discordfx.Server.Start.Server;

import java.io.*;
import java.net.Socket;

public class AccountManagement {

    ServerLog log = new ServerLog();

    void signUp(Socket clientSocket) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            String username = (String) inputStream.readObject();
            User userWithParticularUsername = Server.accountsService.getParticularUser(username);
            if(userWithParticularUsername != null){
                outputStream.writeObject("This username signed up before.");
                System.out.println("SALAM");
                return;
            }
            outputStream.writeObject("Username is ok :)");
            User newUser = (User)inputStream.readObject();
            Server.accountsService.signUp(newUser);
            //Make user directory for uploading files :
            File file = new File("Files/UserDirectories/"+Server.accountsService.getParticularUser(username).getId());
            file.mkdir();
            outputStream.writeObject("You signed up successfully.\n");
        } catch (Exception e) {
            log.openStreamError(clientSocket.getInetAddress());
            e.printStackTrace();
        }
    }

    User logIn(Socket clientSocket){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            String username = (String) inputStream.readObject();
            String password = (String) inputStream.readObject();
            User user = Server.accountsService.logIn(username,password);
            outputStream.writeObject(user);
            if(user != null) {
                user.setStatus(Status.Online);
                return user;
            }
        } catch (Exception e) {
            log.openStreamError(clientSocket.getInetAddress());
            e.printStackTrace();
        }
        return null;
    }

    public void setPicture(User user, Socket clientSocket){
        Server.accountsService.setPicture(user,clientSocket);
    }

    public void changePassword(User user, Socket clientSocket){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            String jwtToken = (String) inputStream.readObject();
            if(jwtToken.equals(user.getJwtToken())){
                outputStream.writeObject("Ok");
                String newPassword = (String) inputStream.readObject();
                Server.accountsService.changePassword(user.getUsername(),newPassword);
                outputStream.writeObject("Password changed successfully.\n");
            }
            else
                outputStream.writeObject("Verification failed.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setStatus(User user,Socket clientSocket){
        try {
            InputStream inputStream = clientSocket.getInputStream();
            int choice = inputStream.read();
            if(choice == 1)
                user.setStatus(Status.Invisible);
            else if(choice == 2)
                user.setStatus(Status.Idle);
            else if(choice == 3)
                user.setStatus(Status.Do_Not_Disturb);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
