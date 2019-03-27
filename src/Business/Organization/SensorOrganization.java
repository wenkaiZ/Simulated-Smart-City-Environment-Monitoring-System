/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.Organization;

import Business.Role.Role;
import Business.Role.SensorManagerRole;
import Business.Sensor.Sensor;
import java.util.ArrayList;

/**
 *
 * @author zhengwenkai
 */
public class SensorOrganization extends Organization{
    private ArrayList<Sensor> sensorDirectory;
    public SensorOrganization(){
        super(Type.Sensor.getValue());
        super.setOrganizationType(Type.Sensor);
        sensorDirectory = new ArrayList<>();
    }
    
    @Override
    public ArrayList<Role> getSupportedRole() {
//        ArrayList<Role> roles = new ArrayList();
//        roles.add(new SensorManagerRole());
        return null;
    }

    public ArrayList<Sensor> getSensorDirectory() {
        return sensorDirectory;
    }

    public void setSensorDirectory(ArrayList<Sensor> sensorDirectory) {
        this.sensorDirectory = sensorDirectory;
    }
    
    
}
