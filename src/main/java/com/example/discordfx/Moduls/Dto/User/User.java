package com.example.discordfx.Moduls.Dto.User;

import com.example.discordfx.Lateral.Notification;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class User implements Serializable {

    private final int id;
    private final String username;
    private final String password;
    private final String email;
    private final String phone;
    private UserLateralInformation information;
    private String jwtToken;

    //Constructor :
    public User(int id,String username, String password, String email, String phone){
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        information = new UserLateralInformation();
    }

    public void loadInformation(){
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Files/UsersInfo/"+id+".bin"))){
            information = (UserLateralInformation) inputStream.readObject();
            System.out.println(information.getBlockedId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setStatus(Status status){
        information.setStatus(status);
        updateInformation();
    }

    public void setJwtToken(String jwtToken){
        this.jwtToken = jwtToken;
    }

    public int getId(){
        return id;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public String getEmail(){
        return email;
    }

    public String getPhone(){
        return phone;
    }

    public Status getStatus(){
        return information.getStatus();
    }

    public ArrayList<Notification> getNotifications(){
        return information.getNotifications();
    }

    public String getJwtToken(){
        return jwtToken;
    }

    public HashMap<String,Integer> getPrivateChats(){
        return information.getPrivateChats();
    }

    public UserLateralInformation getInformation(){
        return information;
    }

    public void addPrivateChat(int port,String targetUsername){
        information.addPrivateChat(targetUsername,port);
        updateInformation();
    }

    public void addBlock(Integer targetUserId){
        information.addBlock(targetUserId);
        System.out.println(information.getBlockedId());
        updateInformation();
    }

    public void addFriend(int targetUserId){
        information.addFriend(targetUserId);
        updateInformation();
    }

    public void addInvitation(int senderId){
        information.addInvitation(senderId);
        updateInformation();
    }

    public void addNotification(Notification notification){
        information.addNotification(notification);
        updateInformation();
    }

    public boolean checkIsFriend(int targetUserId){
        return information.checkIsFriend(targetUserId);
    }

    public boolean checkIsBlock(Integer targetUserId){
        return information.checkIsBlock(targetUserId);
    }

    public void removeFriend(int targetUser){
        information.removeFriend(targetUser);
        updateInformation();
    }

    public void deleteFriendshipRequest(int targetId){
        information.deleteInvitation(targetId);
        updateInformation();
    }

    public void deleteNotifications(){
        information.deleteNotifications();
        updateInformation();
    }

    public void updateInformation() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("Files/UsersInfo/"+this.getId()+".bin"))){
            outputStream.writeObject(information);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
