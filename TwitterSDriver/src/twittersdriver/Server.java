/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twittersdriver;

import java.util.*;

/**
 *
 * @author johnr
 */
public class Server {
    private static HashMap<String, User> userList;
    protected static HashMap<String, ArrayList<Tweet>> tweetTopics;
    
    public Server(){
        this.userList = new HashMap(); 
        this.tweetTopics = new HashMap();
    }
    
    //retrieves and returns user status
    public boolean getUserStatus(String username){
        boolean status = false;
        for(String un : userList.keySet()){
            if(un.toLowerCase().equals(username.toLowerCase())){
                status = userList.get(un).getStatus();
            }
        }
        return status;
    }
    
    //retrieves user object
    public User getUser(String username){
        return userList.get(username);
    }
    
    public HashMap getUserList(){
        return userList;
    }
    
    //retrieves an arrayList of tweets from followed users
    public ArrayList<Tweet> getTweets(String username){
        for(String un : userList.keySet()){
            if(un.toLowerCase().equals(username.toLowerCase())){
               return userList.get(un).getTweets();
            }
        }
        return null;
    }
    
    public ArrayList<Tweet> getByUsername(String username){
        for(String un : userList.keySet()){
            if(un.toLowerCase().equals(username.toLowerCase()))
                return userList.get(un).getPosts();
        }
        return null;
    }
    
    public ArrayList<Tweet> getByTopics(String topic){
        for(String top : tweetTopics.keySet()){
            if(top.toLowerCase().equals(topic.toLowerCase())){
                return tweetTopics.get(top);
            }
        }
        return null;
    }
    
    //adds a Tweet by the logged in User
    public String addTweet(String username, Tweet t){
        for(String un : userList.keySet()){
            if(un.toLowerCase().equals(username.toLowerCase())){
                //check if user is logged in
                if(userList.get(un).getStatus() == true){
                    userList.get(un).postTweet(t);
                    return addTopic(t);
                }
            }
        }
        return "Failed";
    }
    
    //adds tweets to hashmap sorted by topic
    public String addTopic(Tweet t){
        boolean[] found = new boolean[t.getTopic().length];
        int i = 0;
        
        for(String top : t.getTopic()){
            found[i] = false;
            for(String hashTop : tweetTopics.keySet()){
                if(top.toLowerCase().equals(hashTop.toLowerCase())){
                    tweetTopics.get(top).add(t);
                    found[i] = true;
                }
            }
            //creates new topic if not found
            if(found[i] == false){
                ArrayList<Tweet> hold = new ArrayList<>();
                hold.add(t);
                tweetTopics.put(top, hold);
                found[i] = true;
            }
            i++;
        }
        for(boolean test : found){
            if(test == false)
                return "Failed";
        }
        return "Successful";
    }
    
    /*
    public void removeTweet(String username, Tweet t){
        for(String un : userList.keySet()){
            if(un.toLowerCase().equals(username.toLowerCase())){
                userList.get(un).removeTweet(t);
            }
        }
    }
    */
    
    //adds a user to the system
    public String addUser(String username, String password, String address){
        boolean inSystem = false;
        for(String un : userList.keySet()){
            if(un.toLowerCase().equals(username.toLowerCase())){
                inSystem = true;
                return "Name in use";
            }      
        }
        
        if(inSystem == false){
            userList.put(username, new User(username, password, address));
            System.out.println("added" + username);
            return "Added";
        }

        return "Failed";
    }
    
    //logs in user unless user is already logged in
    public String logInUser(String username, String password, String address){
        for(String un : userList.keySet()){
            if(un.toLowerCase().equals(username.toLowerCase())){
                if(userList.get(un).getStatus() == false){
                    if(userList.get(un).getPassword().equals(password)){
                        userList.get(un).inChangeStatus();
                        userList.get(un).setAddress(address);
                        return "Successful";
                    }
                    else{
                        return "Incorrect";
                    }
                }
                else{
                    return "LoggedIn";
                }
            }
        }
        System.out.println("logInUser");
        return "Failed";
    }
    
    //logs out user
    public String logOutUser(String username){
        for(String un : userList.keySet()){
            if(un.toLowerCase().equals(username.toLowerCase())){
                if(userList.get(un).getStatus() == true){
                    userList.get(un).outChangeStatus(); 
                    return "Successful";
                }
                else 
                    return "Failed";
            }
        }
        return "Failed";
    }
    
    //gets follow status
    public String setFollowStatus(String actor, String toChange, String operation){
        User user = null, used = null;
        
        //steps through userList to find actor and toChange objects
        for(String un : userList.keySet()){
            if(un.toLowerCase().equals(actor.toLowerCase())){
               user = userList.get(un); 
            }
            if(un.toLowerCase().equals(toChange.toLowerCase())){
                used = userList.get(un);
            }
        }
        
        //if both users exist, checks status
        if(user != null && used != null){
            if(user.getStatus() == true){
                String status = user.getFollowStatus(used);
                if(status.equals("Not Following")){
                    if(operation.equals("FOLLOW")){
                        String check1 = user.follow(used);
                        String check2 = used.newFollower(user);
                        if(check1.equals("success") && check2.equals("success")){
                            return "Follow Successful";
                        }
                        else{
                            return "Follow Failed";
                        }
                    }
                    else{
                        return "Not Following Already";
                    }
                }
                else if(status.equals("Following")){
                    if(operation.equals("UNFOLLOW")){
                        String check1 = user.unfollow(used);
                        String check2 = used.lostFollower(user);
                        if(check1.equals("success") && check2.equals("success")){
                            return "Unfollow Successful";
                        }
                        else{
                            return "Unfollow Failed";
                        }
                    }
                    else{
                        return "Following Already";
                    }
                }
                else
                    return "Error in Protocol";
            }
            else {
                System.out.println("User Offline");
                return "User Offline";
            }
        }
        else
            return "User does not exist";
    }
    
    //prints user list, specifically for server test
    public void printUserList(){
        for(String s : userList.keySet()){
            System.out.println(s);
        }
    }
    
    //retrieves users following the given user
    public void getFollowers(String username){
        for(String un: userList.keySet()){
            if(un.toLowerCase().equals(username.toLowerCase())){
                userList.get(un).getFollowers();
            }
        }
    }
    
    //retrieves users followed by given user
    public void getFollowing(String username){
        for(String un: userList.keySet()){
            if(un.toLowerCase().equals(username.toLowerCase())){
                userList.get(un).getFollowers();
            }
        }
    }
   
    //retrieves ArrayList of tweets with given topic
   public ArrayList<Tweet> search(String topic){
       for(String top : tweetTopics.keySet()){
           if(top.toLowerCase().equals(topic.toLowerCase()))
               return tweetTopics.get(topic);
       }
       
       return null;
   }
}
