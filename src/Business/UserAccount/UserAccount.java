/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.UserAccount;

import Business.MessageNotification.MessageQueue;
import Business.Role.Role;

/**
 *
 * @author zhengwenkai
 */
public class UserAccount {
    private String userName;
    private String password;
    private Role role;
    private String area;
    private MessageQueue messageQueue;

    public UserAccount() {
        messageQueue = new MessageQueue();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public MessageQueue getMessageQueue() {
        return messageQueue;
    }

    public void setMessageQueue(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    
    @Override
    public String toString() {
        return getUserName();
    }
}
