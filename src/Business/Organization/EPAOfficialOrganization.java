/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.Organization;

import Business.Role.EPAOfficialRole;
import Business.Role.Role;
import java.util.ArrayList;

/**
 *
 * @author zhengwenkai
 */
public class EPAOfficialOrganization extends Organization{
    public EPAOfficialOrganization(){
        super(Type.EPAOfficial.getValue());
        super.setOrganizationType(Type.EPAOfficial);
    }
    
    @Override
    public ArrayList<Role> getSupportedRole() {
        ArrayList<Role> roles = new ArrayList();
        roles.add(new EPAOfficialRole());
        return roles;
    }
}
