/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.Organization;

import Business.Role.ResidentRole;
import Business.Role.Role;
import Business.Role.SensorMaintenancerRole;
import Business.Role.SensorManagerRole;
import java.util.ArrayList;

/**
 *
 * @author zhengwenkai
 */
public class SensorManagerOrganization extends Organization{
    public SensorManagerOrganization(){
        super(Type.SensorManager.getValue());
        super.setOrganizationType(Type.SensorManager);
    }
    
    @Override
    public ArrayList<Role> getSupportedRole() {
        ArrayList<Role> roles = new ArrayList();
        roles.add(new SensorManagerRole());
        return roles;
    }
}
