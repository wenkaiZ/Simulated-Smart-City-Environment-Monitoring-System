/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.Organization;

import Business.Role.MayorRole;
import Business.Role.Role;
import java.util.ArrayList;

/**
 *
 * @author zhengwenkai
 */
public class MayorOrganization extends Organization{
    public MayorOrganization(){
        super(Organization.Type.Mayor.getValue());
        super.setOrganizationType(Type.Mayor);
    }
    
    @Override
    public ArrayList<Role> getSupportedRole() {
        ArrayList<Role> roles = new ArrayList();
        roles.add(new MayorRole());
        return roles;
    }
}
