/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.Sensor;

import Business.MessageNotification.MessageQueue;
import Business.MessageNotification.SensorMessage;
import java.util.Date;

/**
 *
 * @author zhengwenkai
 */
public class Sensor {
    private String temperature, humidity, airQuality, area;
    private Date date;
    private SensorMessage sensorMessage;
    private MessageQueue messageQueue;

    public Sensor() {
        messageQueue = new MessageQueue();
    }
    
    public void generateInfo(){
        temperature = String.valueOf(1+(int)(Math.random()*20))+"degree centigrade";
        humidity = String.valueOf(30+(int)(Math.random()*50))+"%RH";
        airQuality = "PM2.5: "+ String.valueOf(5+(int)(Math.random()*200));
        date = new Date();
        sensorMessage = new SensorMessage();
        sensorMessage.setAirQuality(airQuality);
        sensorMessage.setHumidity(humidity);
        sensorMessage.setTemperature(temperature);
        sensorMessage.setPostDate(date);
        messageQueue.getMessages().add(sensorMessage);
    }
        
    public String getTemperature() {
        return temperature;
    }


    public String getHumidity() {
        return humidity;
    }

    public String getAirQuality() {
        return airQuality;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Date getDate() {
        return date;
    }

    public SensorMessage getSensorMessage() {
        return sensorMessage;
    }

    public MessageQueue getMessageQueue() {
        return messageQueue;
    }
    
    
}
