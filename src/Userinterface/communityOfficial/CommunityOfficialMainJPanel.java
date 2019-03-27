/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Userinterface.communityOfficial;

import Business.EcoSystem;
import Business.Enterprise.CommunityEnterprise;
import Business.Enterprise.Enterprise;
import Business.MessageNotification.MessageQueue;
import Business.MessageNotification.NeedConfirmMessage;
import Business.MessageNotification.PostMessage;
import Business.MessageNotification.ResidentRelatedMessage;
import Business.MessageNotification.SensorMessage;
import Business.Network.Network;
import Business.Organization.Organization;
import Business.Organization.SensorOrganization;
import Business.Role.ResidentRole;
import Business.Sensor.Sensor;
import Business.UserAccount.UserAccount;
import Userinterface.resident.CreateVitalJPanel;
import java.awt.CardLayout;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author zhengwenkai
 */
public class CommunityOfficialMainJPanel extends javax.swing.JPanel {

    /**
     * Creates new form CommunityOfficialMainJPanel
     */
    JPanel cardSequenceJPanel;
    EcoSystem ecoSystem;
    Enterprise enterprise;
    UserAccount userAccount;
    public CommunityOfficialMainJPanel(JPanel cardSequenceJPanel, EcoSystem ecoSystem, Enterprise enterprise, UserAccount userAccount) {
        this.cardSequenceJPanel = cardSequenceJPanel;
        this.ecoSystem = ecoSystem;
        this.enterprise = enterprise;
        this.userAccount = userAccount;
        initComponents();
        valueLabel.setText(enterprise.getName());
        populateTable();
        populateEnvironmentTable();
        populateMessageBoardTable();
        populateCriteria();
    }
    
    public DefaultTableModel populateTable(){
        DefaultTableModel model = (DefaultTableModel) vitalSHJTable.getModel();
        
        model.setRowCount(0);
        for(UserAccount u : ((CommunityEnterprise)enterprise).getOrganizationDirectory().getOrganizationList().get(0).getUserAccountDirectory().getUserAccountList()){
            //找到用户最新更新的生命体征
            int a = u.getMessageQueue().getMessages().size()-1;
            PostMessage message = null;
            if(a>=0){
                message = u.getMessageQueue().getMessages().get(a);
                Object[] row = new Object[6];
                row[0] = u;
                row[1] = ((ResidentRelatedMessage)message).getPostDate();
                row[2] = ((ResidentRelatedMessage)message).getTemperature();
                row[3] = ((ResidentRelatedMessage)message).getBloodPressure();
                row[4] = ((ResidentRelatedMessage)message).getPulse();
                row[5] = ((ResidentRole)u.getRole()).getDisease();

                model.addRow(row);
            }
            
        }
        return model;
    }
    
     public void populateEnvironmentTable(){
        DefaultTableModel model = (DefaultTableModel) environmentJTable.getModel();
        
        model.setRowCount(0);
        Sensor sensor = null;
        for(Network n : ecoSystem.getNetworkList()){
            //社区的管理员地区就是整个网络的地址
            if(n.getName().equals(userAccount.getArea())){
                for(Enterprise e : n.getEnterpriseDirectory().getEnterpriseList()){
                    if(e.getEnterpriseType() == Enterprise.EnterpriseType.SensorCompany){
                        for(Organization o : e.getOrganizationDirectory().getOrganizationList()){
                            if(o.getOrganizationType() == Organization.Type.Sensor){
                                for(Sensor s : ((SensorOrganization)o).getSensorDirectory()){
                                    //社区的名字就是地区
                                    if(s.getArea().equals(enterprise.getName())){
                                        sensor = s;
                                        for (PostMessage sMessage : sensor.getMessageQueue().getMessages()){
                                            System.out.println("get data");
                                            Object[] row = new Object[4];
                                            row[0] = ((SensorMessage)sMessage).getPostDate();
                                            row[1] = ((SensorMessage)sMessage).getTemperature();
                                            row[2] = ((SensorMessage)sMessage).getHumidity();
                                            row[3] = ((SensorMessage)sMessage).getAirQuality();

                                            model.addRow(row);
                                        }
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }        
    }

     //下级发给上级在账户里，上级发给下级在organization里
     public void populateMessageBoardTable(){
         DefaultTableModel model = (DefaultTableModel) messageJTable.getModel();
         model.setRowCount(0);
         
        for(Network n : ecoSystem.getNetworkList()){
            //是这个城市，社区官员的地区就是network名
            if(n.getName().equals(userAccount.getArea())){
                for(Enterprise e : n.getEnterpriseDirectory().getEnterpriseList()){
                    //是EPA类型的
                    if(e.getEnterpriseType() == Enterprise.EnterpriseType.EPA){
                        for(Organization o : e.getOrganizationDirectory().getOrganizationList()){
                            //是EPA官员
                            if(o.getOrganizationType()==Organization.Type.EPAOfficial){
                                
                                //在官员organization的消息列表里找到属于自己的消息
                                for(PostMessage p : o.getMessageQueue().getMessages()){
                                    
                                    if(p.getReceiver() == userAccount){
                                        
                                        Object[] row = new Object[3];
                                        row[0] = ((PostMessage)p).getPostDate();
                                        row[1] = ((PostMessage)p).getSender().getRole().getClass();
                                        row[2] = ((PostMessage)p).getMessage();

                                        model.addRow(row);
                                       
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
     }
     //等设置好了新的官员来补
     public void populateCriteria(){
         for(Network n : ecoSystem.getNetworkList()){
            //是这个城市，社区官员的地区就是network名
            if(n.getName().equals(userAccount.getArea())){
                for(Enterprise e : n.getEnterpriseDirectory().getEnterpriseList()){
                    //是EPA类型的
                    if(e.getEnterpriseType() == Enterprise.EnterpriseType.EPA){
                        for(Organization o : e.getOrganizationDirectory().getOrganizationList()){
                            //是EPA官员
                            if(o.getOrganizationType()==Organization.Type.EPAAnalyst){
                                //在分析师organization的消息列表里找到属于自己的消息,一直替换到最新版本
                                for(PostMessage p : o.getMessageQueue().getMessages()){
                                    if(p.getReceiver() == userAccount){
                                        String [] criteriaStrings = p.getMessage().split(",");
                                        System.out.println(criteriaStrings[0]);
                                        if(criteriaStrings[0].equals("Health")){
                                            tempHealthHighTXT.setText(criteriaStrings[1]);
                                            tempHealthLowTXT.setText(criteriaStrings[2]);
                                            bPHighTXT.setText(criteriaStrings[3]);
                                            bPLowTXT.setText(criteriaStrings[4]);
                                            pulseHighTXT.setText(criteriaStrings[5]);
                                            pulseLowTXT.setText(criteriaStrings[6]);
                                        }
                                        else if(criteriaStrings[0].equals("Environment")){
                                            tempEnHighTXT.setText(criteriaStrings[1]);
                                            temEnLowTXT.setText(criteriaStrings[2]);
                                            humiHighTXT.setText(criteriaStrings[3]);
                                            humiLowTXT.setText(criteriaStrings[4]);
                                            aQHighTXT.setText(criteriaStrings[5]);
                                            aQLowTXT.setText(criteriaStrings[6]);
                                        }
                                    }
                                    
                                }
                            }
                        }
                    }
                }
            }
        }
     }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        remindJButton = new javax.swing.JButton();
        alertJButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        communityJLabel = new javax.swing.JLabel();
        valueLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        vitalSHJTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        environmentJTable = new javax.swing.JTable();
        environmentJLabel = new javax.swing.JLabel();
        sensorMaJButton = new javax.swing.JButton();
        warningAllJButton = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        messageJTable = new javax.swing.JTable();
        environmentJLabel1 = new javax.swing.JLabel();
        sendReportJButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        tempHealthLowTXT = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        tempHealthHighTXT = new javax.swing.JTextField();
        bPHighTXT = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        bPLowTXT = new javax.swing.JTextField();
        pulseHighTXT = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        pulseLowTXT = new javax.swing.JTextField();
        aQLowTXT = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        aQHighTXT = new javax.swing.JTextField();
        humiLowTXT = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        humiHighTXT = new javax.swing.JTextField();
        tempEnHighTXT = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        temEnLowTXT = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        refreshJBtn = new javax.swing.JButton();
        fileterJButton = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        percentNum = new javax.swing.JLabel();

        setBackground(new java.awt.Color(153, 204, 255));

        remindJButton.setText("Remind Resident Update");
        remindJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                remindJButtonActionPerformed(evt);
            }
        });

        alertJButton.setText("Alert Resident");
        alertJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alertJButtonActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("Community Official Work Area");

        communityJLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        communityJLabel.setText("Community :");

        valueLabel.setText("<value>");

        vitalSHJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "UserName", "Time", "Temperature", "Blood Pressure", "Pulse", "Disease"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(vitalSHJTable);

        jLabel2.setText("Users' Vital Sign");

        environmentJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Time", "Temperature", "Humidity", "Air Quality"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(environmentJTable);

        environmentJLabel.setText("Environment");

        sensorMaJButton.setText("Sensor Require Maintenance");
        sensorMaJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sensorMaJButtonActionPerformed(evt);
            }
        });

        warningAllJButton.setText("Warning All Residents");
        warningAllJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                warningAllJButtonActionPerformed(evt);
            }
        });

        messageJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Time", "Sent By", "Message"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(messageJTable);

        environmentJLabel1.setText("Message Board");

        sendReportJButton.setText("Send Report to Official");
        sendReportJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendReportJButtonActionPerformed(evt);
            }
        });

        jLabel3.setText("Health Area:");

        jLabel4.setText("Temperature:");

        jLabel5.setText("Blood Pressure:");

        jLabel6.setText("Pulse:");

        tempHealthLowTXT.setEnabled(false);

        jLabel7.setText("-");

        tempHealthHighTXT.setEnabled(false);

        bPHighTXT.setEnabled(false);

        jLabel8.setText("-");

        bPLowTXT.setEnabled(false);

        pulseHighTXT.setEnabled(false);

        jLabel9.setText("-");

        pulseLowTXT.setEnabled(false);

        aQLowTXT.setEnabled(false);

        jLabel10.setText("-");

        aQHighTXT.setEnabled(false);

        humiLowTXT.setEnabled(false);

        jLabel11.setText("-");

        humiHighTXT.setEnabled(false);

        tempEnHighTXT.setEnabled(false);

        jLabel12.setText("-");

        temEnLowTXT.setEnabled(false);

        jLabel13.setText("Air Quality");

        jLabel14.setText("Humidity:");

        jLabel15.setText("Temperature:");

        jLabel16.setText("Health Area:");

        refreshJBtn.setText("Refresh");
        refreshJBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshJBtnActionPerformed(evt);
            }
        });

        fileterJButton.setText("Filter Unhealth");
        fileterJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileterJButtonActionPerformed(evt);
            }
        });

        jLabel17.setText("Unhealthy Percent:");

        percentNum.setText("0%");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sensorMaJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addGap(18, 18, 18)
                            .addComponent(refreshJBtn))
                        .addComponent(environmentJLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(communityJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(30, 30, 30)
                                    .addComponent(valueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel4)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(tempHealthLowTXT, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel7)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(tempHealthHighTXT, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel5)
                            .addGap(18, 18, 18)
                            .addComponent(bPLowTXT, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel8)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(bPHighTXT, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jLabel6)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(pulseLowTXT, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel9)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(pulseHighTXT, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane2)
                        .addComponent(jScrollPane1)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 902, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(remindJButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(warningAllJButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGap(18, 18, 18)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(fileterJButton, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
                                        .addComponent(alertJButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGap(18, 18, 18)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(sendReportJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel17)
                                            .addGap(18, 18, 18)
                                            .addComponent(percentNum, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGap(67, 67, 67))))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(environmentJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel15)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(temEnLowTXT, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel12)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(tempEnHighTXT, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel14)
                                    .addGap(18, 18, 18)
                                    .addComponent(humiLowTXT, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel11)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(humiHighTXT, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabel13)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(aQLowTXT, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel10)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(aQHighTXT, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(224, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(refreshJBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(communityJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(valueLabel)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(tempHealthLowTXT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(tempHealthHighTXT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(bPLowTXT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(bPHighTXT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(pulseLowTXT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(pulseHighTXT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fileterJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(remindJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(percentNum, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(alertJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sendReportJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(warningAllJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(environmentJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(temEnLowTXT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(tempEnHighTXT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(humiLowTXT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(humiHighTXT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(aQLowTXT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(aQHighTXT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sensorMaJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(environmentJLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void remindJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_remindJButtonActionPerformed
        // TODO add your handling code here:
        int selectRow = vitalSHJTable.getSelectedRow();
        if(selectRow>=0){
            int dialogButton = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog(null, "Would you like to remind the resident?", "Warning", dialogButton);
            if(dialogResult == JOptionPane.YES_OPTION){
                UserAccount residentAccount = (UserAccount)vitalSHJTable.getValueAt(selectRow, 0);
                PostMessage newMessage = new NeedConfirmMessage();
                newMessage.setMessage("Please update your vital sign timely.");
                newMessage.setPostDate(new Date());
                newMessage.setReceiver(residentAccount);
                newMessage.setSender(userAccount);
                
                enterprise.getMessageQueue().getMessages().add(newMessage);
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "Please select a row from table first to remind.","Warning",JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_remindJButtonActionPerformed

    private void alertJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alertJButtonActionPerformed
        // TODO add your handling code here:
        int selectRow = vitalSHJTable.getSelectedRow();
        if(selectRow>=0){
            int dialogButton = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog(null, "Would you like to alert the resident?", "Warning", dialogButton);
            if(dialogResult == JOptionPane.YES_OPTION){
                UserAccount residentAccount = (UserAccount)vitalSHJTable.getValueAt(selectRow, 0);
                PostMessage newMessage = new NeedConfirmMessage();
                newMessage.setMessage("Attention!!! The environment is bad for you. You'd better stay at home.");
                newMessage.setPostDate(new Date());
                newMessage.setReceiver(residentAccount);
                newMessage.setSender(userAccount);
                
                enterprise.getMessageQueue().getMessages().add(newMessage);
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "Please select a row from table first to alert.","Warning",JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_alertJButtonActionPerformed

    //下级发给上级在账户里，上级发给下级在organization里
    private void sendReportJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendReportJButtonActionPerformed
        // TODO add your handling code here:
        CommunityOfficialSendReportJPanel communityOfficialSendReportJPanel = new CommunityOfficialSendReportJPanel(cardSequenceJPanel, ecoSystem, enterprise, userAccount, Double.parseDouble(percentNum.getText().split("%")[0]));
        cardSequenceJPanel.add("communityOfficialSendReportJPanel", communityOfficialSendReportJPanel);
        CardLayout layout = (CardLayout) cardSequenceJPanel.getLayout();
        layout.next(cardSequenceJPanel);
    }//GEN-LAST:event_sendReportJButtonActionPerformed

    private void warningAllJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_warningAllJButtonActionPerformed
        // TODO add your handling code here:
            int dialogButton = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog(null, "Would you like to alert the resident?", "Warning", dialogButton);
            if(dialogResult == JOptionPane.YES_OPTION){
                for(UserAccount u : enterprise.getOrganizationDirectory().getOrganizationList().get(0).getUserAccountDirectory().getUserAccountList()){
                    PostMessage newMessage = new NeedConfirmMessage();
                    newMessage.setMessage("Attention!!! The environment is bad for all of you. You'd better stay at home.");
                    newMessage.setPostDate(new Date());
                    newMessage.setReceiver(u);
                    newMessage.setSender(userAccount);
                    enterprise.getMessageQueue().getMessages().add(newMessage);
                    
                }
                JOptionPane.showMessageDialog(null, "Successfully.");
            }
    }//GEN-LAST:event_warningAllJButtonActionPerformed

    private void sensorMaJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sensorMaJButtonActionPerformed
        // TODO add your handling code here:
        int dialogButton = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog(null, "Would you like to require sensor maintenance?", "Warning", dialogButton);
            if(dialogResult == JOptionPane.YES_OPTION){
                UserAccount receiverAccount = null;
                for(Network n : ecoSystem.getNetworkList()){
                    
                    //sensor maintain接收时候的循环，一会用sensor main也能用
                        for(Enterprise e : n.getEnterpriseDirectory().getEnterpriseList()){
                            if(e.getUserAccountDirectory().getUserAccountList().get(0).getArea().equals(enterprise.getUserAccountDirectory().getUserAccountList().get(0).getArea())){
                                for(Organization o : e.getOrganizationDirectory().getOrganizationList()){
                                    if(o.getOrganizationType()==Organization.Type.SensorMaintenancer){
                                        for(UserAccount u : o.getUserAccountDirectory().getUserAccountList()){
                                            if(u.getArea().equals(enterprise.getName())){
                                                receiverAccount = u;
                                                PostMessage newMessage = new NeedConfirmMessage();
                                                newMessage.setMessage("I think the sensor has some issues, please help us.");
                                                newMessage.setPostDate(new Date());
                                                newMessage.setReceiver(receiverAccount);
                                                newMessage.setSender(userAccount);
                                                enterprise.getMessageQueue().getMessages().add(newMessage);
                                                JOptionPane.showMessageDialog(null, "Successfully.");
                                                return;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    
                }
                    
            }
    }//GEN-LAST:event_sensorMaJButtonActionPerformed

    private void refreshJBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshJBtnActionPerformed
        // TODO add your handling code here:
        populateTable();;
        populateEnvironmentTable();
        populateCriteria();
        populateMessageBoardTable();
    }//GEN-LAST:event_refreshJBtnActionPerformed

    private void fileterJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileterJButtonActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = populateTable();
        Boolean[] booleans = new Boolean[model.getRowCount()];
        double rows = model.getRowCount();
        double value = 0;
        for(int column = 2; column < model.getColumnCount()-1; column++){
            for(int row = 0; row < model.getRowCount(); row++){
                System.out.println(Double.parseDouble((String.valueOf(model.getValueAt(row, column))).split(" ")[0]));
                value = Double.parseDouble((String.valueOf(model.getValueAt(row, column))).split(" ")[0]);
                //找到不健康的值，将这一行的bool设置成false
                switch(column){
                    case 2:
                        booleans[row] = (value<Double.parseDouble(tempHealthLowTXT.getText())||value>Double.parseDouble(tempHealthHighTXT.getText()));
                        break;
                    case 3:
                        booleans[row] = (value<Double.parseDouble(bPLowTXT.getText())||value>Double.parseDouble(bPHighTXT.getText()));
                        break;
                    case 4:
                        booleans[row] = (value<Double.parseDouble(pulseLowTXT.getText())||value>Double.parseDouble(pulseHighTXT.getText()));
                        break;
                }
            }
        }
        int removeCount = 0;
        for(int a = 0; a < rows; a++){
            if(!booleans[a]){
                model.removeRow(a-removeCount);
                removeCount++;
            }
        }
        percentNum.setText(100*model.getRowCount()/rows+"%");
        
    }//GEN-LAST:event_fileterJButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField aQHighTXT;
    private javax.swing.JTextField aQLowTXT;
    private javax.swing.JButton alertJButton;
    private javax.swing.JTextField bPHighTXT;
    private javax.swing.JTextField bPLowTXT;
    private javax.swing.JLabel communityJLabel;
    private javax.swing.JLabel environmentJLabel;
    private javax.swing.JLabel environmentJLabel1;
    private javax.swing.JTable environmentJTable;
    private javax.swing.JButton fileterJButton;
    private javax.swing.JTextField humiHighTXT;
    private javax.swing.JTextField humiLowTXT;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable messageJTable;
    private javax.swing.JLabel percentNum;
    private javax.swing.JTextField pulseHighTXT;
    private javax.swing.JTextField pulseLowTXT;
    private javax.swing.JButton refreshJBtn;
    private javax.swing.JButton remindJButton;
    private javax.swing.JButton sendReportJButton;
    private javax.swing.JButton sensorMaJButton;
    private javax.swing.JTextField temEnLowTXT;
    private javax.swing.JTextField tempEnHighTXT;
    private javax.swing.JTextField tempHealthHighTXT;
    private javax.swing.JTextField tempHealthLowTXT;
    private javax.swing.JLabel valueLabel;
    private javax.swing.JTable vitalSHJTable;
    private javax.swing.JButton warningAllJButton;
    // End of variables declaration//GEN-END:variables
}
