/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.UserAccount;

import Business.Role.Role;
import java.util.ArrayList;

/**
 *
 * @author zhengwenkai
 */
public class UserAccountDirectory {
//    private static UserAccountDirectory userAccountList;
    private ArrayList<UserAccount> userAccountList;

    public UserAccountDirectory(){
        userAccountList = new ArrayList<>();
    }
    
//    public static UserAccountDirectory getUserAccountList(){
//        if(userAccountList == null){
//            userAccountList = new UserAccountDirectory();
//        }
//        return userAccountList;
//    }

    public ArrayList<UserAccount> getUserAccountList() {
        return userAccountList;
    }

    public void setUserAccountList(ArrayList<UserAccount> userAccountList) {
        this.userAccountList = userAccountList;
    }
    
    
    public UserAccount authenticateUser(String username, String password){
        for (UserAccount ua : userAccountList)
            if (ua.getUserName().equals(username) && ua.getPassword().equals(password)){
                return ua;
            }
        return null;
    }
    
    public UserAccount createUserAccount(String username, String password, Role role, String area){
        UserAccount userAccount = new UserAccount();
        userAccount.setUserName(username);
        userAccount.setPassword(password);
        userAccount.setRole(role);
        userAccount.setArea(area);
        userAccountList.add(userAccount);
        return userAccount;
    }
    
    public boolean checkIfUsernameIsUnique(String username){
        for (UserAccount ua : userAccountList){
            if (ua.getUserName().equals(username))
                return false;
        }
        return true;
    }
    
    
}
