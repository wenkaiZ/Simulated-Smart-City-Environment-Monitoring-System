/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.Enterprise;

import Business.Role.Role;
import java.util.ArrayList;

/**
 *
 * @author zhengwenkai
 */
public class SensorCompanyEnterprise extends Enterprise{
    public SensorCompanyEnterprise(String name){
        super(name, Enterprise.EnterpriseType.SensorCompany);
    }
    
    @Override
    public ArrayList<Role> getSupportedRole() {
        return null;
    }
}
