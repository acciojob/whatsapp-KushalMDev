package com.driver;

import java.time.LocalDate;
import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    // Assume that each user belongs to at most one group
    // You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashMap<Integer, Message> messageHashMap = new HashMap<>();
    private HashSet<String> userMobile;
    private int customGroupCount;
    private int messageId;

    // Mobile no -- User
    private HashMap<String, User> userHashMap = new HashMap<>();

    public WhatsappRepository() {
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }

    public String createUser(String name, String mobile) {
        if (userMobile.contains(mobile)) {
            return "-1";
        } else {
            userMobile.add(mobile);
            userHashMap.put(mobile, new User(name, mobile));
        }
        return "SUCCESS";
    }

    public Group createGroup(List<User> users) {

        // Personal chat
        if (users.size() == 2) {
            User admin = users.get(0);
            String adminName = admin.getName();
            Group personalChat = new Group(users.get(1).getName(), 2);
            adminMap.put(personalChat, admin);
            groupUserMap.put(personalChat, users);
            return personalChat;
        } else {
            customGroupCount++;
            Group newGroup = new Group("Group " + customGroupCount, users.size());
            User admin = users.get(0);
            String adminName = admin.getName();
            adminMap.put(newGroup, admin);
            groupUserMap.put(newGroup, users);
            return newGroup;
        }
    }

    public int createMessage(String content) {
        messageId++;
        Date date = new Date();
        Message newMessage = new Message(messageId, content, date);
        messageHashMap.put(messageId, newMessage);

        return messageId;
    }

    public int sendMessage(Message message, User sender, Group group) {
        if (!groupUserMap.containsKey(group)) {
            return -1;
        } else {
            List<User> users = groupUserMap.get(group);
            if (!users.contains(sender)) {
                return -2;
            } else {
                if (!groupMessageMap.containsKey(group)) {
                    groupMessageMap.put(group, new ArrayList<Message>());
                }
                List<Message> messageList = groupMessageMap.get(group);
                messageList.add(message);
                senderMap.put(message, sender);
                return messageList.size();

            }
        }
    }

    public String changeAdmin(User approver, User user, Group group) {
        if (!groupUserMap.containsKey(group) || !adminMap.containsKey(group)) {
            return "groupdoesnotexit";
        }
        List<User> users = groupUserMap.get(group);
        if (users.size() == 0 || !users.contains(user)) {
            return "notaparticipant";
        }
        User admin = adminMap.get(group);
        if (!admin.getName().equals(approver.getName())) {
            return "insufficientrights";
        }
        adminMap.put(group, user);

        return null;
    }

}
