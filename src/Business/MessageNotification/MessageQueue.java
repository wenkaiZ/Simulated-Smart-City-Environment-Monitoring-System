/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.MessageNotification;

import java.util.ArrayList;

/**
 *
 * @author zhengwenkai
 */
public class MessageQueue {
    
    private ArrayList<PostMessage> messages;

    public MessageQueue() {
        messages = new ArrayList();
    }

    public ArrayList<PostMessage> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<PostMessage> messages) {
        this.messages = messages;
    }
    
    
}
