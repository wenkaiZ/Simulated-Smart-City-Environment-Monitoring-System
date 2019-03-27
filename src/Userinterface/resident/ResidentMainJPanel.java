/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Userinterface.resident;

import Business.EcoSystem;
import Business.Enterprise.Enterprise;
import Business.MessageNotification.MessageQueue;
import Business.MessageNotification.PostMessage;
import Business.MessageNotification.ResidentRelatedMessage;
import Business.MessageNotification.SensorMessage;
import Business.Network.Network;
import Business.Organization.Organization;
import Business.Organization.SensorOrganization;
import Business.Role.ResidentRole;
import Business.Sensor.Sensor;
import Business.UserAccount.UserAccount;
import java.awt.CardLayout;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author luoyu
 */
public class ResidentMainJPanel extends javax.swing.JPanel {

    /**
     * Creates new form UserJPanel
     */
    JPanel cardSequenceJPanel;
    EcoSystem ecoSystem;
    Network network;
    UserAccount userAccount;
    public ResidentMainJPanel(JPanel cardSequenceJPanel, EcoSystem ecoSystem, Network network, UserAccount userAccount) {
        this.cardSequenceJPanel = cardSequenceJPanel;
        this.ecoSystem = ecoSystem;
        this.network = network;
        this.userAccount = userAccount;
        initComponents();
        valueLabel.setText(userAccount.getArea());
        populateVitalSignTable();
        populateEnvironmentTable();
        populateMessageBoardTable();
    }
    
    public void populateVitalSignTable(){
        DefaultTableModel model = (DefaultTableModel) vitalSHJTable.getModel();
        
        model.setRowCount(0);
        for (PostMessage vs : userAccount.getMessageQueue().getMessages()){
            
            Object[] row = new Object[5];
            row[0] = ((ResidentRelatedMessage)vs).getPostDate();
            row[1] = ((ResidentRelatedMessage)vs).getTemperature();
            row[2] = ((ResidentRelatedMessage)vs).getBloodPressure();
            row[3] = ((ResidentRelatedMessage)vs).getPulse();
            row[4] = ((ResidentRole)userAccount.getRole()).getDisease();
            
            model.addRow(row);
        }
    }
    
    public void populateEnvironmentTable(){
        Sensor sensor = null;
        DefaultTableModel model = (DefaultTableModel) environmentJTable.getModel();
        model.setRowCount(0);

        for(Enterprise e : network.getEnterpriseDirectory().getEnterpriseList()){
            if(e.getEnterpriseType() == Enterprise.EnterpriseType.SensorCompany){
                for(Organization o : e.getOrganizationDirectory().getOrganizationList()){
                    if(o.getOrganizationType() == Organization.Type.Sensor){
                        for(Sensor s : ((SensorOrganization)o).getSensorDirectory()){
                            if(s.getArea().equals(userAccount.getArea())){
                                sensor = s;
                                for (PostMessage sMessage : sensor.getMessageQueue().getMessages()){
                                    Object[] row = new Object[4];
                                    row[0] = ((SensorMessage)sMessage).getPostDate();
                                    row[1] = ((SensorMessage)sMessage).getTemperature();
                                    row[2] = ((SensorMessage)sMessage).getHumidity();
                                    row[3] = ((SensorMessage)sMessage).getAirQuality();

                                    model.addRow(row);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void populateMessageBoardTable(){
        MessageQueue messageQueue = null;
        for(Enterprise e : network.getEnterpriseDirectory().getEnterpriseList()){
            if(e.getEnterpriseType() == Enterprise.EnterpriseType.Community){
                if(e.getName().equals(userAccount.getArea())){
                    messageQueue = e.getMessageQueue();
                }
                
            }
        }
        DefaultTableModel model = (DefaultTableModel) messageJTable.getModel();
        
        model.setRowCount(0);
        if(messageQueue!=null){
            if(messageQueue.getMessages()!=null){
                for(PostMessage p : messageQueue.getMessages()){
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
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        createVSJBtn = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        vitalSHJTable = new javax.swing.JTable();
        vitalSHJLabel = new javax.swing.JLabel();
        analyzeVitalSHJBtn = new javax.swing.JButton();
        environmentJLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        environmentJTable = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        messageJTable = new javax.swing.JTable();
        environmentJLabel1 = new javax.swing.JLabel();
        valueLabel = new javax.swing.JLabel();
        enterpriseLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        refreshJBtn = new javax.swing.JButton();

        setBackground(new java.awt.Color(153, 204, 255));

        createVSJBtn.setText("Create Vital Sign");
        createVSJBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createVSJBtnActionPerformed(evt);
            }
        });

        vitalSHJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Time", "Temperature", "Blood Pressure", "Pulse", "Disease"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(vitalSHJTable);

        vitalSHJLabel.setText("Vital Sign History");

        analyzeVitalSHJBtn.setText("Analyze Vital Sign History");
        analyzeVitalSHJBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                analyzeVitalSHJBtnActionPerformed(evt);
            }
        });

        environmentJLabel.setText("Environment");

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
        if (environmentJTable.getColumnModel().getColumnCount() > 0) {
            environmentJTable.getColumnModel().getColumn(3).setResizable(false);
            environmentJTable.getColumnModel().getColumn(3).setHeaderValue("Air Quality");
        }

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

        valueLabel.setText("<value>");

        enterpriseLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        enterpriseLabel.setText("Community");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("Resident Work Area");

        refreshJBtn.setText("Refresh");
        refreshJBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshJBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(enterpriseLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(valueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(refreshJBtn))
                    .addComponent(environmentJLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(environmentJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(vitalSHJLabel)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane2)
                        .addComponent(jScrollPane1)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(createVSJBtn)
                            .addGap(18, 18, 18)
                            .addComponent(analyzeVitalSHJBtn))
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 743, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(57, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(refreshJBtn))
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(valueLabel)
                    .addComponent(enterpriseLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addComponent(vitalSHJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(createVSJBtn)
                    .addComponent(analyzeVitalSHJBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(environmentJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(environmentJLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void createVSJBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createVSJBtnActionPerformed
        // TODO add your handling code here:
        CreateVitalJPanel createVitalJPanel = new CreateVitalJPanel(cardSequenceJPanel, ecoSystem, userAccount);
        cardSequenceJPanel.add("createVitalJPanel", createVitalJPanel);
        CardLayout layout = (CardLayout) cardSequenceJPanel.getLayout();
        layout.next(cardSequenceJPanel);
    }//GEN-LAST:event_createVSJBtnActionPerformed

    private void analyzeVitalSHJBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_analyzeVitalSHJBtnActionPerformed
        // TODO add your handling code here:
        ResidentAnalyzeJPanel residentAnalyzeJPanel = new ResidentAnalyzeJPanel(cardSequenceJPanel, ecoSystem, network, userAccount);
        cardSequenceJPanel.add("residentAnalyzeJPanel", residentAnalyzeJPanel);
        CardLayout layout = (CardLayout) cardSequenceJPanel.getLayout();
        layout.next(cardSequenceJPanel);
    }//GEN-LAST:event_analyzeVitalSHJBtnActionPerformed

    private void refreshJBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshJBtnActionPerformed
        // TODO add your handling code here:
        populateVitalSignTable();
        populateEnvironmentTable();
        populateMessageBoardTable();
    }//GEN-LAST:event_refreshJBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton analyzeVitalSHJBtn;
    private javax.swing.JButton createVSJBtn;
    private javax.swing.JLabel enterpriseLabel;
    private javax.swing.JLabel environmentJLabel;
    private javax.swing.JLabel environmentJLabel1;
    private javax.swing.JTable environmentJTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable messageJTable;
    private javax.swing.JButton refreshJBtn;
    private javax.swing.JLabel valueLabel;
    private javax.swing.JLabel vitalSHJLabel;
    private javax.swing.JTable vitalSHJTable;
    // End of variables declaration//GEN-END:variables
}
