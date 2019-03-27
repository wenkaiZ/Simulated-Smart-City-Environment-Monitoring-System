/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.MessageNotification;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author zhengwenkai
 */
public class ResidentRelatedMessage extends PostMessage{
    private String temperature;
    private String bloodPressure;
    private String pulse;
    private MessageQueue receivedMessages;
    //sensor 的信息处理先放一放
    private SensorMessage sensorMessage;
    
    public ResidentRelatedMessage(){
        receivedMessages = new MessageQueue();
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public String getPulse() {
        return pulse;
    }

    public void setPulse(String pulse) {
        this.pulse = pulse;
    }

    public SensorMessage getSensorMessage() {
        return sensorMessage;
    }

    public void setSensorMessage(SensorMessage sensorMessage) {
        this.sensorMessage = sensorMessage;
    }

    public MessageQueue getReceivedMessages() {
        return receivedMessages;
    }

    public void setReceivedMessages(MessageQueue receivedMessages) {
        this.receivedMessages = receivedMessages;
    }

    
}
