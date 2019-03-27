/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.Organization;

import Business.Role.EPAAnalystRole;
import Business.Role.Role;
import java.util.ArrayList;

/**
 *
 * @author zhengwenkai
 */
public class EPAAnalystOrganization extends Organization{
    public EPAAnalystOrganization(){
        super(Organization.Type.EPAAnalyst.getValue());
        super.setOrganizationType(Organization.Type.EPAAnalyst);
    }
    
    @Override
    public ArrayList<Role> getSupportedRole() {
        ArrayList<Role> roles = new ArrayList();
        roles.add(new EPAAnalystRole());
        return roles;
    }
}
