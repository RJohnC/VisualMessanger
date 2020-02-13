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
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class User {
    private boolean status;
    private ArrayList<Tweet> sent = new ArrayList();
    private ArrayList<Tweet> toRead = new ArrayList();
    private ArrayList<Tweet> read = new ArrayList();
    private HashMap<Tweet, boolean[]> reading = new HashMap<>();
    private ArrayList<User> followedUsers = new ArrayList();
    private ArrayList<User> usersFollowing = new ArrayList();
    public String username;
    private String password;
    private String address;
    
    
    public User(String username, String password, String address){
        this.username = username;
        this.password = password; 
        this.address = address;
        this.status = true;
        try {
            this.address = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //gets users online status
    public boolean getStatus(){
       return this.status;
    }
    
    //changes users online status
    public void inChangeStatus(){
        this.status = true;
    }
    
    public void outChangeStatus(){
        this.status = false;
    }
    
    //gets an ArrayList of Tweets from follwed that have not been read 
    public ArrayList<Tweet> getTweets() {
        for(User u: followedUsers){
            for(Tweet t: u.sent){
                try{
                    if(reading.containsKey(t) == true && read.contains(t) == false){ //compare note
                        boolean changed = false;
                        //System.out.println("bool" + reading.get(t).length);
                        if(reading.containsKey(t)){
                            for(int i = 0; i < reading.get(t).length; i ++){
                                if(reading.get(t)[i] == false && changed == false){
                                    System.out.println("Switched to true");
                                    reading.get(t)[i] = true;
                                    changed = true;
                                }
                                else if(changed == false && i == reading.get(t).length - 1){
                                    System.out.println("Removed");
                                    toRead.remove(t);
                                    reading.remove(t);
                                    read.add(t);
                                }
                            }
                        }
                    }    
                    else if(!read.contains(t)){
                        System.out.println("Added to toRead");
                        toRead.add(t);
                        boolean[] fill = {true, false, false, false, false, false, false};
                        reading.put(t, fill);
                    }
                    else
                        toRead = new ArrayList<>();
                }
                catch(NullPointerException e){
                    System.out.println(e.toString());
                }
            }
        }

        return toRead; 
    }
    
    public ArrayList<Tweet> getPosts(){
        return sent;
    }
    
    //adds posted tweet to "sent" arraylist
    public void postTweet(Tweet t){
        sent.add(t);
    }
    
    //removes posted tweet from "sent"
    public void removeTweet(Tweet t){
        sent.remove(t);
    }
    
    //adds user to followedUsers
    public String follow(User toFollow){
        followedUsers.add(toFollow);
        for(Tweet t : toFollow.getTweets())
            toRead.add(t);
        return "success";
    }
    
    //removes user from followed user
    public String unfollow(User toUnfollow){
        for(User u : followedUsers){
            if(u.username.toLowerCase().equals(toUnfollow.username.toLowerCase())){
                followedUsers.remove(u);
                for(Tweet t : toUnfollow.getTweets())
                    toRead.remove(t);
                return "success";  
            }
        }
        return "failed";
    }
    
    //adds follower to usersFollowing
    public String newFollower(User newFollower){
            usersFollowing.add(newFollower);
            return "success";
    }
    
    //removes follower from usersFollowing
    public String lostFollower(User userLost){
        for(User u : usersFollowing){
            if(u.username.toLowerCase().equals(userLost.username.toLowerCase())){
                usersFollowing.remove(u);
                return "success";  
            }
        }
        return "failed";
    }
    
    public String getPassword(){
        return password;
    }
    
    public String getFollowStatus(User used){
        for(User u : followedUsers){
            if(u.equals(used)){
                return "Following";
            }
        }
        return "Not Following";
    }
    
    public ArrayList<User> getFollowers(){
        return usersFollowing;
    }
    
    public ArrayList<User> getFollowing(){
        return followedUsers;
    }
    
    public String getAddress(){
        return address;
    }
    
    public void setAddress(String address){
        this.address = address;
        
    }
}


