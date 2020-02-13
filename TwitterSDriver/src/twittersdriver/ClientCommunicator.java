/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Server side - communicates with the client
 * @author johnr
 */
package twittersdriver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
//import java.util.Arrays;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;


public class ClientCommunicator {
    private static BufferedReader in;
    private static PrintWriter out;
    private Socket client;
    public Server server;
    
    public ClientCommunicator(Server server){
        this.server = server;
        
        try{    
            ServerSocket sock = new ServerSocket(2005);
            System.out.println("Lisetning on port " + sock.getLocalPort());
            System.out.println("Address: " + InetAddress.getLocalHost().getHostAddress());
            
            
            while (true) {
                
                client = sock.accept();
            
                in = new BufferedReader(
                        new InputStreamReader(client.getInputStream()));
                out = new PrintWriter(client.getOutputStream(), true);
                    
                String cmd = in.readLine();
                System.out.println(cmd);

                if(cmd.toUpperCase().equals("REG"))
                    register();

                else if(cmd.toUpperCase().equals("LOGI"))
                    loggingIn();

                else if(cmd.toUpperCase().equals("LOGO"))
                    loggingOff();

                else if(cmd.toUpperCase().equals("POST"))
                    postTweet();

                else if(cmd.toUpperCase().equals("FOLL"))
                    followUser();

                else if(cmd.toUpperCase().equals("UNFO"))
                    unfollowUser();

                else if(cmd.toUpperCase().equals("GETF"))
                    retrieveFollowing();
                
                else if(cmd.toUpperCase().equals("GTFG"))
                    retrieveFollowers();
                
                else if(cmd.toUpperCase().equals("MESS"))
                    privateMessage();
                    
                else if(cmd.toUpperCase().equals("SRCH"))
                    search();
                
                else if(cmd.toUpperCase().equals("TWTS"))
                    retrieveTweets();

                else{
                    System.out.println("Error in protocol - command does not exist");
                    close();
                }
            }
        }
        catch(IOException e){
            System.out.println(e.toString());
        }
    }
    
    private static void close(){
        try {
            in.close();
            out.close();
        } 
        catch (IOException e) {
            System.out.println(e.toString());
        }     
    }
    
    private void register() {
        try{
            String name = in.readLine();
            String pass = in.readLine();
            System.out.println(name + " " + pass);
            String address = client.getInetAddress().toString();
            System.out.println(address);

            String result = server.addUser(name, pass, address);
            System.out.println(result);
           
            if(result.equals("Name in use")){
                out.println("RESULT");
                out.println("Used");
            }
            if(result.equals("Added")){
                out.println("RESULT");
                out.println("Added");
            }
            if(result.equals("Failed")){
                out.println("RESULT");
                out.println("Failed");
            }
        }
        catch(IOException e){
               System.out.println(e.toString());
        } 
        close();
    }
    
    private void loggingIn(){
        try{
            String name = in.readLine();
            String pass = in.readLine();
            String address = client.getInetAddress().toString();
            System.out.println(address);

            String result = server.logInUser(name, pass, address);
            System.out.println(result);
            if(result.equals("Successful")){
                out.println("RESULT");
                out.println("Success");
                
            }
            if(result.equals("Incorrect")){
                out.println("RESULT");
                out.println("Incorrect");
            }
            if(result.equals("LoggedIn")){
                out.println("RESULT");
                out.println("LoggedIn");
            }
            if(result.equals("Failed")){
                out.println("RESULT");
                out.println("Failed");
            }
        }
        catch(IOException e){
                System.out.println(e.toString());
        }  
        close();
    }
       
    private void loggingOff(){
        try{
            String name = in.readLine();

            String result = server.logOutUser(name);
            System.out.println(result);
     
            if(result.equals("Successful")){
                out.println("RESULT");
                out.println("Success");
            }
            if(result.equals("Failed")){
                out.println("RESULT");
                out.println("Failed");
            }
        }
        catch(IOException e){
                System.out.println(e.toString());
        }   
        close();
    }
    
    
    private void postTweet() {
        try{
            boolean cont = true;
            String name= null, text = null, timeStamp = null, result = null;
            String[] topics = null;
            
            while(cont == true){
                String next = in.readLine();
                System.out.println(next);
                //receives username
                if(next.equals("NAME")){
                   name = in.readLine();
                   System.out.println(name);
                }
                //receives tweetText
                if(next.equals("TWEET")){
                    text = in.readLine();
                    System.out.println(text);
                }
                
                //receives topics
                if(next.equals("TOPICS")){
                    String count = in.readLine();
                    int numTopics = Integer.parseInt(count);
                    System.out.println(numTopics);
                    topics = new String[numTopics];
                    
                    for(int i = 0; i < numTopics; i++){
                        topics[i] = in.readLine();
                        System.out.println(topics[i]);
                    }
                }
                    
                //receives timeStamp
                if(next.equals("TIME")){
                    timeStamp = in.readLine();
                    System.out.println(timeStamp);
                }
                if(next.equals("DONE")){
                    result = server.addTweet(name, 
                            new Tweet(name, text, topics,
                            Timestamp.valueOf(timeStamp)));
                    cont = false;
                }
            }
            
            System.out.println(result);
            
            if(result.equals("Successful")){
                out.println("RESULT");
                out.println("Successful");
            }
            else if(result.equals("Failed")){
                out.println("RESULT");
                out.println("Failed");
            }
            else{
                out.println("RESULT");
                out.println("Error");   
            }        
        }
        catch(IOException e){
                System.out.println(e.toString());
        }   
        close();
    }   

    private void followUser(){
        try{
            String user = in.readLine();
            String toFollow = in.readLine();
            
            String result = server.setFollowStatus(user, toFollow, "FOLLOW");
            System.out.println(result);
            
            out.println("RESULT");
            if(result.equals("Follow Successful")){
                out.println("Success");
            }
            if(result.equals("Follow Failed")){
                out.println("Failed");
            }
            if(result.equals("Following Already"))
                out.println("Already");
            if(result.equals("User does not exist"))
                out.println("User does not exist.");
        }
        catch(IOException e){
                System.out.println(e.toString());
        }  
        close();
    }

    private void unfollowUser(){
        try{
            String username = in.readLine();
            String toUnfollow = in.readLine();
            
            String result = server.setFollowStatus(username, toUnfollow, "UNFOLLOW");
            
            out.println("RESULT");
            if(result.equals("Unfollow Successful")){
                out.println("Success");
            }
            if(result.equals("Unfollow Failed")){
                out.println("Failed");
            }
            if(result.equals("Not Following Already"))
                out.println("Already");
            if(result.equals("User does not exist"))
                out.println("User does not exist.");
        }
        catch(IOException e){
                System.out.println(e.toString());
        }   
        close();
    }
    
    private void retrieveFollowing(){
        try{
            String username = in.readLine();
            User user = server.getUser(username);
            ArrayList<User> following = user.getFollowing();
                   
            
            out.println(following.size());
            for(User u : following){
                out.println(u.username);
                System.out.println("Printed");
            }
            out.println("DONE");
            System.out.println("Done Printing");
             /*           
            if(in.readLine().equals("DONE"))
                out.println("DONE");
            else
                out.println("ERROR");  
                    */
        }
        catch(IOException e){
                System.out.println(e.toString());
        } 
        close();
    }    
    
    private void retrieveFollowers(){
        try{
            String username = in.readLine();
            User user = server.getUser(username);
            ArrayList<User> followers = user.getFollowers();
                   
            
            out.println(followers.size());
            for(User u : followers){
                out.println(u.username);
                System.out.println("Printed");
            }
            out.println("DONE");
            System.out.println("Done Printing");
                        
        }
        catch(IOException e){
                System.out.println(e.toString());
        } 
        close();
    }    
    
    private void privateMessage(){
        try{
            String username = in.readLine();
            User user = server.getUser(username);
            
            if(user.getStatus() == false){
                out.println("OFFL");
            }
            else{
                out.println("ADDR");
                out.println(user.getAddress());
            }   
        }
        catch(IOException e){
            System.out.println(e.toString());
        }
    }
    
    private void search(){
        try{
            
            ArrayList<Tweet> tweets;
                    
            String topic = in.readLine();
            System.out.println(topic);
            
            if(server.tweetTopics.containsKey(topic)){
                tweets = server.search(topic);
            }
            else if(server.getUserList().keySet().contains(topic))
                tweets = server.getByUsername(topic);
            else
                tweets = new ArrayList<>();
            
            out.println(tweets.size());
            System.out.println(tweets.size());
            for(Tweet t : tweets){
                out.println(t.getUsername());                
                out.println(t.getText());
                out.println(t.getTime().toString());
                System.out.println(t.getUsername());                
                System.out.println(t.getText());
                System.out.println(t.getTime().toString());
            }
            System.out.println("Sent all");
            System.out.println("DONE");
            out.println("DONE");  
        }
        catch(IOException e){
                System.out.println(e.toString());
        } 
        close();
    }
    
    public void retrieveTweets(){       
        try{
            String username = in.readLine();
            ArrayList<Tweet> toRead 
                    = server.getUser(username).getTweets();
            
            out.println(toRead.size());

            for(Tweet t : toRead){
                out.println(t.getText());
                out.println(t.getUsername());
            }
            
            System.out.println("Done");
        }
        catch(IOException e){
            System.out.println(e.toString());
        }
        close();
    }
}

