/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twittersdriver;

/**
 *
 * @author johnr
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.*;

public class TwitterSDriver {
    
    public static void main(String[] args){
        Server server = new Server();
        server.addUser("John", "rugen", null);
        server.addUser("Tyler", "toofly", null);
        server.setFollowStatus("John", "Tyler", "FOLLOW");
        server.setFollowStatus("Tyler", "John", "FOLLOW");
        Date date = new Date();
        long time = date.getTime();
        Timestamp timeStamp = new Timestamp(time);
        String[] topics = {"world"};
        server.addTweet("John", new Tweet("John", "Hello #world", topics, timeStamp));
        server.getTweets("Tyler");
        server.getTweets("Tyler");
        server.addTweet("John", new Tweet("John" , "Hello #world two", topics, timeStamp));
        server.logOutUser("John");
        server.logOutUser("Tyler");
        
        ClientCommunicator fromClient = new ClientCommunicator(server);
        
    }
       
}
