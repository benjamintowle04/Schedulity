package onetomany.websockets;


import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import onetomany.Users.User;
import onetomany.Users.UserController;
import onetomany.Users.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@ServerEndpoint("/websocket/login/{user}")
@Component
public class WebSocketServer {
    private static Map < Session, String > sessionUsernameMap = new Hashtable < > ();
    private static Map< String, Session> usernameSessionMap = new Hashtable< >();

    private final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    private static UserRepository userRepository;
    @Autowired
    public void setUserRespository(UserRepository repo) {
        userRepository = repo;
    }

    private static UserController userController;
    @Autowired
    public void setUserController(UserController repo) {
        userController = repo;
    }

//    @OnOpen
//    public void onOpen2(Session session, @PathParam("user") String username) throws IOException {
//        logger.info("Entered into Open");
//
//        session.addMessageHandler(new MessageHandler.Whole<String>() {
//            public void onMessage(String message) {
//                logger.info("Received message: " + message);
//            }
//        });
//    }


    @OnOpen
    public void onOpen(Session session, @PathParam("user") String username)
            throws IOException {
        logger.info("Entered into Open");
        /**
         * on open I have to send a message to all of the users friends and then send a message to
         * update their friends
         */


        sessionUsernameMap.put(session, username);
        usernameSessionMap.put(username, session);

        User user = userController.findByUsername(username);

        List<String> friends = user.getFriends_usernames();

        System.out.println(friends.size());
        System.out.println(sessionUsernameMap.size());
        System.out.println(sessionUsernameMap == null);
        String message = "User:" + username + " is online";

        for(int i = 0;i < friends.size();i++)
        {
            if(sessionUsernameMap.toString().contains(friends.get(i))) {
                    sendMessageToPArticularUser(friends.get(i), message);
                }

        }
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        // Handle new messages
        logger.info("Entered into Message: Got Message:" + message);
        String username = sessionUsernameMap.get(session);

        User user = userController.findByUsername(username);

        List<String> friends = user.getFriends_usernames();

        System.out.println(friends.size());
        System.out.println(sessionUsernameMap.size());
        System.out.println(sessionUsernameMap == null);
//        String message = "User:" + username + " is online";

        for(int i = 0;i < friends.size();i++)
        {
            System.out.println(friends.get(i));

                System.out.println(sessionUsernameMap.values());
                System.out.println(sessionUsernameMap.values().contains(friends.get(i)));
                if(sessionUsernameMap.values().contains(friends.get(i))) {
                    sendMessageToPArticularUser(friends.get(i), message);
                }

        }
//        } else // Message to whole chat
//        {
//            broadcast(username + ": " + message);
//        }
    }



    @OnClose
    public void onClose(Session session) throws IOException {
        logger.info("Entered into Close");
        /**
         * when it is closed then send message to all friends to switch to red
         */

        String username = sessionUsernameMap.get(session);

        System.out.println(username);
        System.out.println(userController == null);
        System.out.println(userRepository == null);
        System.out.println(sessionUsernameMap == null);
        User user = userController.findByUsername(username);

        List<String> friends = user.getFriends_usernames();

        System.out.println(user.getId());
        String message = "User:" + username + " is offline";
        System.out.println(sessionUsernameMap.size());
        System.out.println(sessionUsernameMap);

        for(int i = 0;i < friends.size();i++)
        {
                if(sessionUsernameMap.toString().contains(friends.get(i))) {
                    sendMessageToPArticularUser(friends.get(i), message);
                }
        }


        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
        logger.info("Entered into Error");
    }

    private void sendMessageToPArticularUser(String username, String message) {
        try {
            System.out.print(username);
            System.out.print(message);
            usernameSessionMap.get(username).getBasicRemote().sendText(message);
        } catch (IOException e) {
            logger.info("Exception: " + e.getMessage().toString());
            e.printStackTrace();
        }
    }


}
