/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.Organization;

import Business.Enterprise.CommunityEnterprise;
import Business.Sensor.Sensor;
import java.util.ArrayList;

/**
 *
 * @author zhengwenkai
 */
public class OrganizationDirectory {
    private ArrayList<Organization> organizationList;

    public OrganizationDirectory() {
        organizationList = new ArrayList();
    }

    public ArrayList<Organization> getOrganizationList() {
        return organizationList;
    }
    
    public Organization createOrganization(Organization.Type type){
        Organization organization = null;
        if (type.getValue().equals(Organization.Type.Mayor.getValue())){
            organization = new MayorOrganization();
            organizationList.add(organization);
        }
        else if (type.getValue().equals(Organization.Type.Resident.getValue())){
            organization = new ResidentOrganization();
            organizationList.add(organization);
        }
        else if (type.getValue().equals(Organization.Type.EPAOfficial.getValue())){
            organization = new EPAOfficialOrganization();
            organizationList.add(organization);
        }
        else if (type.getValue().equals(Organization.Type.SensorManager.getValue())){
            organization = new SensorManagerOrganization();
            organizationList.add(organization);
        }
        else if (type.getValue().equals(Organization.Type.CommunityOfficial.getValue())){
            organization = new CommunityOfficialOrganization();
            organizationList.add(organization);
        }
        else if (type.getValue().equals(Organization.Type.SensorMaintenancer.getValue())){
            organization = new SensorMaintenancerOrganization();
            organizationList.add(organization);
        }
        else if (type.getValue().equals(Organization.Type.Sensor.getValue())){
            organization = new SensorOrganization();
            organizationList.add(organization);
        }
        else if (type.getValue().equals(Organization.Type.EPAAnalyst.getValue())){
            organization = new EPAAnalystOrganization();
            organizationList.add(organization);
        }
        
        return organization;
    }
}
