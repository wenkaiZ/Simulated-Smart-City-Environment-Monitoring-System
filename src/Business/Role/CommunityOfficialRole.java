package Business.Role;


import Business.EcoSystem;
import Business.Enterprise.Enterprise;
import Business.Network.Network;
import Business.Organization.Organization;
import Business.Role.Role;
import Business.UserAccount.UserAccount;
import Userinterface.communityOfficial.CommunityOfficialMainJPanel;
import Userinterface.mayor.MayorMainJPanel;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author zhengwenkai
 */
public class CommunityOfficialRole extends Role{
    @Override
    public JPanel createWorkArea(JPanel rightJPanel, UserAccount account, Organization organization, Enterprise enterprise,Network network, EcoSystem system) {
        return new CommunityOfficialMainJPanel(rightJPanel, system, enterprise, account);
    }
}
