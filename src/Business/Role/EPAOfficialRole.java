/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.Role;

import Business.EcoSystem;
import Business.Enterprise.Enterprise;
import Business.Network.Network;
import Business.Organization.Organization;
import Business.UserAccount.UserAccount;
import Userinterface.EPAOfficial.EPAOfficialMainJPanel;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author zhengwenkai
 */
public class EPAOfficialRole extends Role{
    
    @Override
    public JPanel createWorkArea(JPanel userProcessContainer, UserAccount account, Organization organization, Enterprise enterprise,Network network, EcoSystem system) {
        return new EPAOfficialMainJPanel(userProcessContainer, system, enterprise, organization, account);
    }
}
