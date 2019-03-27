/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.Organization;

import Business.Role.ResidentRole;
import Business.Role.Role;
import Business.Role.SensorMaintenancerRole;
import java.util.ArrayList;

/**
 *
 * @author zhengwenkai
 */
public class SensorMaintenancerOrganization extends Organization{
    public SensorMaintenancerOrganization(){
        super(Type.SensorMaintenancer.getValue());
        super.setOrganizationType(Type.SensorMaintenancer);
    }
    
    @Override
    public ArrayList<Role> getSupportedRole() {
        ArrayList<Role> roles = new ArrayList();
        roles.add(new SensorMaintenancerRole());
        return roles;
    }
}
