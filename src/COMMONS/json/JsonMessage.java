package COMMONS.json;

import COMMONS.services.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class JsonMessage {
    public int COMMAND = 0;
    public User user = new User();
    public String object = "";
    public String image = "";

    public String getObject() {
        object = object.replaceAll("u005B","\\[");
        object = object.replaceAll("u005D","]");

        object = object.replaceAll("u007B","\\{");
        object = object.replaceAll("u007D","}");

        object = object.replaceAll("u0022","\"");
        object = object.replaceAll("u003A",":");

        object = object.replaceAll("u002C",",");

        return object;
    }

    public Image getImg(){
        image = image.replaceAll("u005B","\\[");
        image = image.replaceAll("u005D","]");

        image = image.replaceAll("u007B","\\{");
        image = image.replaceAll("u007D","}");

        image = image.replaceAll("u0022","\"");
        image = image.replaceAll("u003A",":");

        image = image.replaceAll("u002C",",");


        if (image!=""){
            byte[] bytes = image.getBytes();
            try {
                BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));
                ImageIO.write(bufferedImage,"jpg", new File("C:\\Users\\Игнат\\Desktop\\newImg.jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getStringAnswer(){

        Gson GSON = new GsonBuilder().setPrettyPrinting().create();


        object = object.replaceAll("\\[","\\u005B");
        object = object.replaceAll("]","\\u005D");

        object = object.replaceAll("\\{","\\u007B");
        object = object.replaceAll("}","\\u007D");

        object = object.replaceAll("\"","\\u0022");
        object = object.replaceAll(":","\\u003A");

        object = object.replaceAll(",","\\u002C");

        //object = object.replaceAll(" ","");
        //object = object.replace("\u0000","");
        object = object.replace("\n","");


        return "{" +
                "\"COMMAND\":" +COMMAND+ "," +
                "\"user\":" +GSON.toJson(user)+ "," +
                "\"object\":\"" + object + "\"," +
                "\"image\":\"" +
                "\"}";
    }

}
