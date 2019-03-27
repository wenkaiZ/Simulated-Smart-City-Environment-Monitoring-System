/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.Organization;

import Business.Role.CommunityOfficialRole;
import Business.Role.MayorRole;
import Business.Role.Role;
import java.util.ArrayList;

/**
 *
 * @author zhengwenkai
 */
public class CommunityOfficialOrganization extends Organization{
    public CommunityOfficialOrganization(){
        super(Organization.Type.CommunityOfficial.getValue());
        super.setOrganizationType(Type.CommunityOfficial);
    }
    
    @Override
    public ArrayList<Role> getSupportedRole() {
        ArrayList<Role> roles = new ArrayList();
        roles.add(new CommunityOfficialRole());
        return roles;
    }
}
