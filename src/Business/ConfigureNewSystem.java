/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business;

import Business.Enterprise.CommunityEnterprise;
import Business.Enterprise.EPAEnterprise;
import Business.MessageNotification.ResidentRelatedMessage;
import Business.Network.Network;
import Business.Organization.Organization;
import Business.Role.CommunityOfficialRole;
import Business.Role.EPAOfficialRole;
import Business.Role.MayorRole;
import Business.Role.ResidentRole;
import Business.Role.SystemAdminRole;
import Business.UserAccount.UserAccount;
import java.util.Date;

/**
 *
 * @author zhengwenkai
 */
public class ConfigureNewSystem {
    public static EcoSystem configure(){
        
        EcoSystem system = EcoSystem.getInstance();
        system.getUserAccountDirectory().createUserAccount("admin", "admin", new SystemAdminRole(), "MA");
//        Network defalutNetwork = system.createAndAddNetwork();
//        defalutNetwork.setName("boston");
//        EPAEnterprise ePAEnterprise = new EPAEnterprise("BostonEPA");
//        defalutNetwork.getEnterpriseDirectory().getEnterpriseList().add(ePAEnterprise);
//        UserAccount userAccount = new UserAccount();
//        userAccount.setArea("boston");
//        userAccount.setPassword("mayor");
//        userAccount.setUserName("mayor");
//        userAccount.setRole(new MayorRole());
//        ePAEnterprise.getUserAccountDirectory().getUserAccountList().add(userAccount);
//        Organization organization = ePAEnterprise.getOrganizationDirectory().createOrganization(Organization.Type.EPAOfficial);
//        organization.setName("EPAofficials");
//        organization.setOrganizationType(Organization.Type.EPAOfficial);
//        userAccount = new UserAccount();
//        userAccount.setArea("Westland");
//        userAccount.setPassword("official");
//        userAccount.setUserName("official");
//        userAccount.setRole(new EPAOfficialRole());
//        organization.getUserAccountDirectory().getUserAccountList().add(userAccount);
//        
//        CommunityEnterprise communityEnterprise = new CommunityEnterprise("Westland");
//        defalutNetwork.getEnterpriseDirectory().getEnterpriseList().add(communityEnterprise);
//        userAccount = new UserAccount();
//        userAccount.setArea("boston");
//        userAccount.setPassword("community");
//        userAccount.setUserName("community");
//        userAccount.setRole(new CommunityOfficialRole());
//        communityEnterprise.getUserAccountDirectory().getUserAccountList().add(userAccount);
//        organization = communityEnterprise.getOrganizationDirectory().createOrganization(Organization.Type.Resident);
//        organization.setName("WestlandResidents");
//        organization.setOrganizationType(Organization.Type.Resident);
//        userAccount = new UserAccount();
//        userAccount.setArea("Westland");
//        userAccount.setPassword("resident1");
//        userAccount.setUserName("resident1");
//        ResidentRelatedMessage rrm = new ResidentRelatedMessage();
//        rrm.setBloodPressure("90 mmHG");
//        rrm.setPulse("60 times/min");
//        rrm.setTemperature("36.1 degree centigrade");
//        rrm.setPostDate(new Date());
//        userAccount.getMessageQueue().getMessages().add(rrm);
//        userAccount.setRole(new ResidentRole());
//        organization.getUserAccountDirectory().getUserAccountList().add(userAccount);
//        
//        userAccount = new UserAccount();
//        userAccount.setArea("Westland");
//        userAccount.setPassword("resident2");
//        userAccount.setUserName("resident2");
//        rrm = new ResidentRelatedMessage();
//        rrm.setBloodPressure("140 mmHG");
//        rrm.setPulse("100 times/min");
//        rrm.setTemperature("37.0 degree centigrade");
//        rrm.setPostDate(new Date());
//        userAccount.getMessageQueue().getMessages().add(rrm);
//        userAccount.setRole(new ResidentRole());
//        organization.getUserAccountDirectory().getUserAccountList().add(userAccount);
        return system;
    }
}
