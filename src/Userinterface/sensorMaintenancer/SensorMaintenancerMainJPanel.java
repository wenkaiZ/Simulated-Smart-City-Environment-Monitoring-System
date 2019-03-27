/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Userinterface.sensorMaintenancer;

import Business.EcoSystem;
import Business.Enterprise.Enterprise;
import Business.MessageNotification.PostMessage;
import Business.MessageNotification.SensorMessage;
import Business.Network.Network;
import Business.Organization.Organization;
import Business.Organization.SensorOrganization;
import Business.Sensor.Sensor;
import Business.UserAccount.UserAccount;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Popup;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author zhengwenkai
 */
public class SensorMaintenancerMainJPanel extends javax.swing.JPanel {

    /**
     * Creates new form SensorMaintenancerMainJPanel
     */
    JPanel cardSequenceJPanel;
    EcoSystem ecoSystem;
    Enterprise enterprise;
    Organization organization;
    UserAccount userAccount;
    public SensorMaintenancerMainJPanel(JPanel cardSequenceJPanel, EcoSystem ecoSystem, Enterprise enterprise, Organization organization, UserAccount userAccount) {
        this.cardSequenceJPanel = cardSequenceJPanel;
        this.ecoSystem = ecoSystem;
        this.enterprise = enterprise;
        this.organization = organization;
        this.userAccount = userAccount;
        initComponents();
        valueLabel.setText(userAccount.getArea());
        populateTable();
        populateEnvironmentTable();
        
    }

    public void populateTable(){
        DefaultTableModel model = (DefaultTableModel) messageFromCommunityJTable.getModel();
         model.setRowCount(0);
         
        for(Network n : ecoSystem.getNetworkList()){
            if(n.getName().equals(enterprise.getUserAccountDirectory().getUserAccountList().get(0).getArea())){
                for(Enterprise e : n.getEnterpriseDirectory().getEnterpriseList()){
                    if(e.getEnterpriseType() == Enterprise.EnterpriseType.Community){
                        for(PostMessage p : e.getMessageQueue().getMessages()){
                            if(p.getReceiver() == userAccount){
                                        
                                        Object[] row = new Object[3];
                                        row[0] = ((PostMessage)p).getMessage();
                                        row[1] = ((PostMessage)p).getSender().getRole().getClass();
                                        row[2] = ((PostMessage)p).getPostDate();

                                        model.addRow(row);
                                       
                             }
                        }
                    }
                }
            }
        }
    }
    
    public void populateEnvironmentTable(){
        DefaultTableModel model = (DefaultTableModel) environmentJTable.getModel();
        
        model.setRowCount(0);
        Sensor sensor = null;
        for(Organization o : enterprise.getOrganizationDirectory().getOrganizationList()){
            if(o.getOrganizationType() == Organization.Type.Sensor){
                for(Sensor s : ((SensorOrganization)o).getSensorDirectory()){
                    if(s.getArea().equals(userAccount.getArea())){
                        sensor = s;
                        for(PostMessage sMessage : sensor.getMessageQueue().getMessages()){
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
//        for(Network n : ecoSystem.getNetworkList()){
//            //社区的管理员地区就是整个网络的地址
//            if(n.getName().equals(userAccount.getArea())){
//                for(Enterprise e : n.getEnterpriseDirectory().getEnterpriseList()){
//                    if(e.getEnterpriseType() == Enterprise.EnterpriseType.SensorCompany){
//                        for(Organization o : e.getOrganizationDirectory().getOrganizationList()){
//                            if(o.getOrganizationType() == Organization.Type.Sensor){
//                                for(Sensor s : ((SensorOrganization)o).getSensorDirectory()){
//                                    //社区的名字就是地区
//                                    if(s.getArea().equals(enterprise.getName())){
//                                        sensor = s;
//                                        for (PostMessage sMessage : sensor.getMessageQueue().getMessages()){
//                                            System.out.println("get data");
//                                            Object[] row = new Object[4];
//                                            row[0] = ((SensorMessage)sMessage).getPostDate();
//                                            row[1] = ((SensorMessage)sMessage).getTemperature();
//                                            row[2] = ((SensorMessage)sMessage).getHumidity();
//                                            row[3] = ((SensorMessage)sMessage).getAirQuality();
//
//                                            model.addRow(row);
//                                        }
//                                        return;
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }        
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel15 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        messageFromCommunityJTable = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        communityJLabel = new javax.swing.JLabel();
        valueLabel = new javax.swing.JLabel();
        fetchSensorDataJButton = new javax.swing.JButton();
        refreshJBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        environmentJTable = new javax.swing.JTable();
        environmentJLabel = new javax.swing.JLabel();

        setBackground(new java.awt.Color(153, 204, 255));

        jLabel15.setText("Message From Community Official: ");

        messageFromCommunityJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Message", "SendBy", "Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(messageFromCommunityJTable);

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel13.setText("Sensor Maintenance Work Area");

        communityJLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        communityJLabel.setText("Community :");

        valueLabel.setText("<value>");

        fetchSensorDataJButton.setText("Fetch Sensor Data");
        fetchSensorDataJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fetchSensorDataJButtonActionPerformed(evt);
            }
        });

        refreshJBtn.setText("Refresh");
        refreshJBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshJBtnActionPerformed(evt);
            }
        });

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 742, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(environmentJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(fetchSensorDataJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 742, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel15)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(communityJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(30, 30, 30)
                            .addComponent(valueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel13)
                            .addGap(18, 18, 18)
                            .addComponent(refreshJBtn))))
                .addContainerGap(101, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(refreshJBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(communityJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(valueLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(fetchSensorDataJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(environmentJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(111, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void fetchSensorDataJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fetchSensorDataJButtonActionPerformed
        // TODO add your handling code here:
        for(Organization o : enterprise.getOrganizationDirectory().getOrganizationList()){
                            if(o.getOrganizationType() == Organization.Type.Sensor){
                                for(Sensor s : ((SensorOrganization)o).getSensorDirectory()){
                                    //社区的名字就是地区
                                    if(s.getArea().equals(userAccount.getArea())){
                                        s.generateInfo();
                                        JOptionPane.showMessageDialog(null, "Fetch successfully.");
                                        populateEnvironmentTable();
                                        return;
                                    }
                                }
                            }
        }
        JOptionPane.showMessageDialog(null, "Failed to fetch.");
    }//GEN-LAST:event_fetchSensorDataJButtonActionPerformed

    private void refreshJBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshJBtnActionPerformed
        // TODO add your handling code here:
        populateTable();
        populateEnvironmentTable();
    }//GEN-LAST:event_refreshJBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel communityJLabel;
    private javax.swing.JLabel environmentJLabel;
    private javax.swing.JTable environmentJTable;
    private javax.swing.JButton fetchSensorDataJButton;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable messageFromCommunityJTable;
    private javax.swing.JButton refreshJBtn;
    private javax.swing.JLabel valueLabel;
    // End of variables declaration//GEN-END:variables
}
