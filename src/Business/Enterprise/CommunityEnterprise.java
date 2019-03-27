/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.Enterprise;

import Business.Role.MayorRole;
import Business.Role.Role;
import Business.UserAccount.UserAccount;
import java.util.ArrayList;

/**
 *
 * @author zhengwenkai
 */
public class CommunityEnterprise extends Enterprise{
    
    public CommunityEnterprise(String name){
        super(name, EnterpriseType.Community);
    }
    
    @Override
    public ArrayList<Role> getSupportedRole() {
        return null;
    }
    
    
}
