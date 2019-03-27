/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.Organization;

import Business.MessageNotification.MessageQueue;
import Business.Role.Role;
import Business.UserAccount.UserAccountDirectory;
import Business.MessageNotification.PostMessage;
import java.util.ArrayList;

/**
 *
 * @author zhengwenkai
 */
public abstract class Organization {
    private String name;
    private MessageQueue messageQueue;
    private UserAccountDirectory userAccountDirectory;
    private int organizationID;
    private static int counter=0;
    private Type organizationType;
    
    public enum Type{
        Admin("Admin Organization"), Sensor("Sensor Organization"), Resident("Resident Organization"),
        EPAOfficial("EPAOfficial Organization"), EPAAnalyst("EPAAnalyst Organization"), Mayor("Mayor Organization"), EPA("EPA Organization"), 
        SensorManager("SernsorManager Organization"), CommunityOfficial("CommunityOfficial Organization"), 
        SensorMaintenancer("SensorMaintenancer Organization");
        private String value;
        private Type(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }
    }
    
    public Organization(String name) {
        this.name = name;
        messageQueue = new MessageQueue();
        userAccountDirectory = new UserAccountDirectory();
        organizationID = counter;
        ++counter;
        System.out.println("counter:"+counter);
    }

    public abstract ArrayList<Role> getSupportedRole();
    
    public UserAccountDirectory getUserAccountDirectory() {
        return userAccountDirectory;
    }

    public int getOrganizationID() {
        return organizationID;
    }

    
    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public Type getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(Type organizationType) {
        this.organizationType = organizationType;
    }

    public MessageQueue getMessageQueue() {
        return messageQueue;
    }

    public void setMessageQueue(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    
    @Override
    public String toString() {
        return name;//this.getOrganizationType().getValue()
    }    
    
}
