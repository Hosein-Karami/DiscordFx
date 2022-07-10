package com.example.discordfx.Client.RoomHandler.Sender;

import com.example.discordfx.Moduls.Dto.Messages.TextMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ChannelChatSender extends GeneralSender{

    public ChannelChatSender(Socket socket, String senderUsername) {
        super(socket, senderUsername);
    }

    public void pinMessage(int messageNumber){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject("#PIN");
            outputStream.writeObject(messageNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getPinnedMessage(){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject("#GETPIN");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void react(int messageNumber,String react){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject("#REACT");
            outputStream.writeObject(messageNumber);
            outputStream.writeObject(react);
            outputStream.writeObject(senderUsername);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tagMember(String messageText,String taggedUsername){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject("#TAG");
            TextMessage message = new TextMessage(senderUsername,"Tagged member : " + taggedUsername +"   message : "+messageText);
            outputStream.writeObject(message);
            outputStream.writeObject(taggedUsername);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
