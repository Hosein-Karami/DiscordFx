package com.example.discordfx;

import com.example.discordfx.Lateral.Checker;
import com.example.discordfx.Lateral.Exceptions.EmailException;
import com.example.discordfx.Lateral.Exceptions.PasswordExceptions.PasswordException;
import com.example.discordfx.Lateral.Exceptions.PhoneException;
import com.example.discordfx.Lateral.Exceptions.UsernameException;
import com.example.discordfx.Moduls.Dto.User.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

public class Profile implements Initializable {

    @FXML
    ImageView imageView;
    @FXML
    Text text;
    @FXML
    Text username;
    @FXML
    Text password;
    @FXML
    Text email;
    @FXML
    Text phone;
    @FXML
    TextField newUsername;
    @FXML
    TextField newPassword;
    @FXML
    TextField newEmail;
    @FXML
    TextField newPhone;
    @FXML
    Button changeUsernameButton;
    @FXML
    Button changePassButton;
    @FXML
    Button changeEmailButton;
    @FXML
    Button changePhoneButton;

    private final Socket socket = Start.socket;
    private OutputStream out;
    private InputStream in;
    public static User user;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();
            Image image = new Image("file:ClientFiles/Profile.jpg");
            imageView.setImage(image);
            username.setText("Username : " + user.getUsername());
            password.setText("Password : " + user.getPassword());
            email.setText("Email : " + user.getEmail());
            phone.setText("Phone : " + user.getPhone());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setPicture(ActionEvent event){
        try{
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            FileChooser fileChooser = new FileChooser();
            File image = fileChooser.showOpenDialog(stage);
            if(image != null){
                String fileName = image.getName();
                if(fileName.contains(".jpg") || fileName.contains(".png") || fileName.contains(".jpeg")){
                    out.write(1);
                    ObjectOutputStream outputStream = new ObjectOutputStream(out);
                    ObjectInputStream inputStream = new ObjectInputStream(in);
                    byte[] imageBytes = Files.readAllBytes(image.toPath());
                    outputStream.writeObject(imageBytes);
                    FileOutputStream fileOutputStream = new FileOutputStream("ClientFiles/Profile.jpg");
                    fileOutputStream.write(imageBytes);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    imageView.setImage(new Image(image.getPath()));
                    text.setText((String) inputStream.readObject());
                }
                else
                    text.setText("Invalid file format,format should be jpg,png,jpeg");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void wantToChangeUsername(){
        newUsername.setVisible(true);
        changeUsernameButton.setVisible(true);
    }

    public void changeUsername(){
        try {
            Checker.checkUsername(newUsername.getText());
            out.write(2);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            outputStream.writeObject(newUsername.getText());
            String status = (String) inputStream.readObject();
            if(status.equals("OK")){
                text.setText("Username changed successfully");
                user.setUsername(newUsername.getText());
                username.setText("Username : " + newUsername.getText());
                changeUsernameButton.setVisible(false);
                newUsername.setVisible(false);
            }
            else
                text.setText("This username is used before");
        }catch (UsernameException e){
            text.setText(e.getErrorMessage());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void wantToChangePass(){
        newPassword.setVisible(true);
        changePassButton.setVisible(true);
    }

    public void changePassword(){
        try {
            Checker.checkPassword(newPassword.getText());
            out.write(3);
            change(newPassword.getText());
            password.setText("Password : " + newPassword.getText());
            user.setPassword(newPassword.getText());
            newPassword.setVisible(false);
            changePassButton.setVisible(false);
        } catch (PasswordException e) {
            text.setText(e.getErrorMessage());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void wantToChangeEmail(){
        newEmail.setVisible(true);
        changeEmailButton.setVisible(true);
    }

    public void changeEmail(){
        try {
            Checker.checkEmail(newEmail.getText());
            out.write(4);
            change(newEmail.getText());
            email.setText("Email : " + newEmail.getText());
            user.setEmail(newEmail.getText());
            newEmail.setVisible(false);
            changeEmailButton.setVisible(false);
        }catch (EmailException e){
            text.setText(e.getErrorMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void wantToChangePhone(){
        newPhone.setVisible(true);
        changePhoneButton.setVisible(true);
    }

    public void changePhone() {
        try {
            Checker.checkPhone(newPhone.getText());
            out.write(5);
            change(newPhone.getText());
            phone.setText("Phone : " + newPhone.getText());
            user.setPhone(newPhone.getText());
            newPhone.setVisible(false);
            changePhoneButton.setVisible(false);
        } catch (PhoneException e) {
            text.setText(e.getErrorMessage());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void change(String newValue) throws Exception {
        try{
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            ObjectInputStream inputStream = new ObjectInputStream(in);
            outputStream.writeObject(newValue);
            text.setText((String) inputStream.readObject());
        }catch (Exception e){
            throw new Exception();
        }
    }

    public void logout(ActionEvent event){
        try {
            out.write(6);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Start.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void back(ActionEvent event){
        try {
            out.write(7);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
