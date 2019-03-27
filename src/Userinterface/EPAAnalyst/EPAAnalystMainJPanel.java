/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Userinterface.EPAAnalyst;

import Business.EcoSystem;
import Business.Enterprise.CommunityEnterprise;
import Business.Enterprise.Enterprise;
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
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author zhengwenkai
 */
public class EPAAnalystMainJPanel extends javax.swing.JPanel {

    /**
     * Creates new form EPAAnalystMainJPanel
     */
    JPanel cardSequenceJPanel;
    EcoSystem ecoSystem;
    Enterprise enterprise;
    Organization organization;
    UserAccount userAccount;
    Organization communityResidentOrganization;
    UserAccount communityOfficialAccount;
    public EPAAnalystMainJPanel(JPanel cardSequenceJPanel, EcoSystem ecoSystem, Enterprise enterprise, Organization organization, UserAccount userAccount) {
        this.cardSequenceJPanel = cardSequenceJPanel;
        this.ecoSystem = ecoSystem;
        this.enterprise = enterprise;
        this.organization = organization;
        this.userAccount = userAccount;
        initComponents();
        valueLabel.setText(userAccount.getArea());
        getCommunityAccount();
        populateVitalTable();
        populateEnvironmentTable();
        populateMessageTable();
        
    }

    public void getCommunityAccount(){
        for(Network n : ecoSystem.getNetworkList()){
            //在同一城市
            if(n.getName().equals(enterprise.getUserAccountDirectory().getUserAccountList().get(0).getArea())){
                for(Enterprise e : n.getEnterpriseDirectory().getEnterpriseList()){
                    //是社区类型
                    if(e.getEnterpriseType() == Enterprise.EnterpriseType.Community){
                        //在同一个地区，因为一个城市有很多地区，所以需要找到是相同地区的
                        if(e.getName().equals(userAccount.getArea())){
                            communityOfficialAccount = e.getUserAccountDirectory().getUserAccountList().get(0);
                        }
                    }
                }
            }
        }
    }
    public DefaultTableModel populateVitalTable(){
        DefaultTableModel model = (DefaultTableModel) vitalSHJTable.getModel();
        model.setRowCount(0);
        for(Network n : ecoSystem.getNetworkList()){
            //在同一城市
            if(n.getName().equals(enterprise.getUserAccountDirectory().getUserAccountList().get(0).getArea())){
                for(Enterprise e : n.getEnterpriseDirectory().getEnterpriseList()){
                    //是社区类型
                    if(e.getEnterpriseType() == Enterprise.EnterpriseType.Community){
                        //在同一个地区，因为一个城市有很多地区，所以需要找到是相同地区的
                        if(e.getName().equals(userAccount.getArea())){
                            //社区只有resident organization
                            communityResidentOrganization = e.getOrganizationDirectory().getOrganizationList().get(0);
                            for(UserAccount u : communityResidentOrganization.getUserAccountDirectory().getUserAccountList()){
                                //找到用户最新更新的生命体征
                                if(u.getMessageQueue().getMessages().size()>0){
                                    int a = 0;
                                    a = u.getMessageQueue().getMessages().size()-1;
                                    PostMessage message = null;
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
                        }
                    }
                }
            }
        }
        return model;
    }
    
    public void populateEnvironmentTable(){
        Sensor sensor = null;
        for(Network n : ecoSystem.getNetworkList()){
            //EPA的管理员地区就是整个网络的地址
            if(n.getName().equals(enterprise.getUserAccountDirectory().getUserAccountList().get(0).getArea())){
                for(Enterprise e : n.getEnterpriseDirectory().getEnterpriseList()){
                    //获取sensor公司
                    if(e.getEnterpriseType() == Enterprise.EnterpriseType.SensorCompany){
                        for(Organization o : e.getOrganizationDirectory().getOrganizationList()){
                            //获取其中sensor类
                            if(o.getOrganizationType() == Organization.Type.Sensor){
                                for(Sensor s : ((SensorOrganization)o).getSensorDirectory()){
                                    //分析师的地址就是sensor的地址
                                    if(s.getArea().equals(userAccount.getArea())){
                                        sensor = s;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        
        DefaultTableModel model = (DefaultTableModel) environmentJTable.getModel();
        
        model.setRowCount(0);
        if(sensor!=null){
            if(sensor.getMessageQueue()!=null){
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
    
    public void populateMessageTable(){
        DefaultTableModel model = (DefaultTableModel) officialMessageJTable.getModel();
        model.setRowCount(0);
        for(Organization o : enterprise.getOrganizationDirectory().getOrganizationList()){
            //在同一个enterprise下找到EPAOfficial类型的orga
            if(o.getOrganizationType() == Organization.Type.EPAOfficial){
                for(PostMessage p : o.getMessageQueue().getMessages()){
                    //在EPAOffical的organization消息列表中找到属于自己的消息
                    if(p.getReceiver() == userAccount){
                        Object[] row = new Object[2];
                        row[0] = ((PostMessage)p).getMessage();
                        row[1] = ((PostMessage)p).getPostDate();

                        model.addRow(row);
                    }
                }
            }
        }
    }
    
    public void populateResult(){
        double averageTemp = 0, avergeBP = 0, averagePul = 0;
        DefaultTableModel model = (DefaultTableModel) vitalSHJTable.getModel();
        
        int rowCount = model.getRowCount();
        int columnCount = model.getColumnCount();
        double sum = 0;
        for(int column = 2; column < columnCount-1; column++){
            sum = 0;
            for(int row = 0; row < rowCount; row++){
                sum = sum + Double.parseDouble((String.valueOf(model.getValueAt(row, column))).split(" ")[0]);
            }
            switch(column){
                case 2:
                    averageTemp = sum/rowCount;
                    break;
                case 3:
                    avergeBP = sum/rowCount;
                    break;
                case 4:
                    averagePul = sum/rowCount;
                    break;
            }
        }
        
        temperAVG.setText(averageTemp+" degree centigrade");
        bloodPAVG.setText(avergeBP+" mmHg");
        pulseAVG.setText(averagePul+" times/min");
        
        
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel13 = new javax.swing.JLabel();
        communityJLabel = new javax.swing.JLabel();
        valueLabel = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        setECriteriaJBtn = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        temHeaLowTxt = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        setHCriteriaJBtn = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        vitalSHJTable = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        environmentJTable = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        temHeaHigTxt = new javax.swing.JTextField();
        BPLTxt = new javax.swing.JTextField();
        BPHTxt = new javax.swing.JTextField();
        pulseLTxt = new javax.swing.JTextField();
        pulseHTxt = new javax.swing.JTextField();
        temEnvLowTxt = new javax.swing.JTextField();
        temEnvHigTxt = new javax.swing.JTextField();
        humLTxt = new javax.swing.JTextField();
        humHTxt = new javax.swing.JTextField();
        airCLTxt = new javax.swing.JTextField();
        airCHTxt = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        officialMessageJTable = new javax.swing.JTable();
        responJButton = new javax.swing.JButton();
        refreshJBtn = new javax.swing.JButton();
        analyzeJButton = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        temperAVG = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        bloodPAVG = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        pulseAVG = new javax.swing.JLabel();

        setBackground(new java.awt.Color(153, 204, 255));

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel13.setText("EPA Analyst Work Area");

        communityJLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        communityJLabel.setText("Community :");

        valueLabel.setText("<value>");

        jLabel8.setText("Temperature");

        jLabel7.setText("Blood Pressure");

        setECriteriaJBtn.setText("Set Environment Criteria");
        setECriteriaJBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setECriteriaJBtnActionPerformed(evt);
            }
        });

        jLabel6.setText("-");

        jLabel5.setText("-");

        jLabel4.setText("-");

        jLabel3.setText("Air Condition");

        jLabel2.setText("Humidity");

        jLabel1.setText("Temperature");

        jLabel12.setText("Pulse");

        jLabel11.setText("-");

        temHeaLowTxt.setSize(new java.awt.Dimension(81, 26));

        jLabel10.setText("-");

        jLabel9.setText("-");

        setHCriteriaJBtn.setText("Set Health Criteria");
        setHCriteriaJBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setHCriteriaJBtnActionPerformed(evt);
            }
        });

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

        jLabel14.setText("Residents Healthy Situation: ");

        jLabel15.setText("Community Environment Situation:");

        temHeaHigTxt.setSize(new java.awt.Dimension(81, 26));

        BPLTxt.setSize(new java.awt.Dimension(81, 26));

        BPHTxt.setSize(new java.awt.Dimension(81, 26));

        pulseLTxt.setSize(new java.awt.Dimension(81, 26));

        pulseHTxt.setSize(new java.awt.Dimension(81, 26));

        temEnvLowTxt.setSize(new java.awt.Dimension(81, 26));

        temEnvHigTxt.setSize(new java.awt.Dimension(81, 26));

        humLTxt.setSize(new java.awt.Dimension(81, 26));

        humHTxt.setSize(new java.awt.Dimension(81, 26));

        airCLTxt.setSize(new java.awt.Dimension(81, 26));

        airCHTxt.setSize(new java.awt.Dimension(81, 26));

        jLabel16.setText("Message From EPA Official: ");

        officialMessageJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Message Content", "Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(officialMessageJTable);

        responJButton.setText("Respond");
        responJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                responJButtonActionPerformed(evt);
            }
        });

        refreshJBtn.setText("Refresh");
        refreshJBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshJBtnActionPerformed(evt);
            }
        });

        analyzeJButton.setText("Analyze");
        analyzeJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                analyzeJButtonActionPerformed(evt);
            }
        });

        jLabel17.setText("Temperature AVG.");

        jLabel18.setText("Blood Press AVG.");

        jLabel19.setText("Pulse AVG.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(responJButton)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel16)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(setECriteriaJBtn))
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addGap(18, 18, 18)
                            .addComponent(temEnvLowTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(12, 12, 12)
                            .addComponent(jLabel4)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(temEnvHigTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel2)
                            .addGap(27, 27, 27)
                            .addComponent(humLTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel5)
                            .addGap(1, 1, 1)
                            .addComponent(humHTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(airCLTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel6)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(airCHTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane2)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(communityJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(30, 30, 30)
                            .addComponent(valueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(jLabel13)
                            .addGap(18, 18, 18)
                            .addComponent(refreshJBtn))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(temHeaLowTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(analyzeJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel11)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(temHeaHigTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabel7)
                                    .addGap(6, 6, 6)
                                    .addComponent(BPLTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel10)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(BPHTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel19)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(pulseAVG, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel18)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(bloodPAVG, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel17)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(temperAVG, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGap(12, 12, 12)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(pulseLTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel9)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(pulseHTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addGap(121, 121, 121)
                                    .addComponent(setHCriteriaJBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(refreshJBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(communityJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(valueLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addGap(3, 3, 3)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(temHeaLowTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel8)
                    .addComponent(jLabel7)
                    .addComponent(jLabel10)
                    .addComponent(jLabel12)
                    .addComponent(jLabel9)
                    .addComponent(temHeaHigTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BPLTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BPHTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pulseLTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pulseHTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(analyzeJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(setHCriteriaJBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(temperAVG, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(28, 28, 28)
                                        .addComponent(jLabel15))
                                    .addComponent(pulseAVG, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel6)
                                    .addComponent(temEnvLowTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(temEnvHigTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(humLTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(humHTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(airCLTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(airCHTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(setECriteriaJBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel16)))
                            .addComponent(jLabel19))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(responJButton))
                    .addComponent(bloodPAVG, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(51, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void setHCriteriaJBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setHCriteriaJBtnActionPerformed
        // TODO add your handling code here:
        boolean rightType = true;
        try {
            Double.parseDouble(temHeaHigTxt.getText());
            Double.parseDouble(temHeaLowTxt.getText());
            Double.parseDouble(BPHTxt.getText());
            Double.parseDouble(BPLTxt.getText());
            Double.parseDouble(pulseHTxt.getText());
            Double.parseDouble(pulseLTxt.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Only numbers allowed in blank.");
            rightType = false;
            return;
        }
        
        if(!temHeaHigTxt.getText().equals("")&&!temHeaLowTxt.getText().equals("")&&
                !BPHTxt.getText().equals("")&&!BPLTxt.getText().equals("")&&
                !pulseHTxt.getText().equals("")&&!pulseLTxt.getText().equals("")){
            
            PostMessage newMessage = new NeedConfirmMessage();
            newMessage.setMessage("Health,"+temHeaHigTxt.getText()+","+temHeaLowTxt.getText()+","+BPHTxt.getText()+","+BPLTxt.getText()+","+pulseHTxt.getText()+","+pulseLTxt.getText());
            newMessage.setPostDate(new Date());
            newMessage.setReceiver(communityOfficialAccount);
            newMessage.setSender(userAccount);
            organization.getMessageQueue().getMessages().add(newMessage);
            JOptionPane.showMessageDialog(null, "Successfully");
        }
        else{
            JOptionPane.showMessageDialog(null, "Please input all of text areas.","Warning",JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_setHCriteriaJBtnActionPerformed

    private void setECriteriaJBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setECriteriaJBtnActionPerformed
        // TODO add your handling code here:
        boolean rightType = true;
        try {
            Double.parseDouble(temEnvHigTxt.getText());
            Double.parseDouble(temEnvLowTxt.getText());
            Double.parseDouble(humHTxt.getText());
            Double.parseDouble(humLTxt.getText());
            Double.parseDouble(airCHTxt.getText());
            Double.parseDouble(airCLTxt.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Only numbers allowed in blank.");
            rightType = false;
            return;
        }
        
        if(!temEnvHigTxt.getText().equals("")&&!temEnvLowTxt.getText().equals("")&&
                !humHTxt.getText().equals("")&&!humLTxt.getText().equals("")&&
                !airCHTxt.getText().equals("")&&!airCLTxt.getText().equals("")){
            PostMessage newMessage = new NeedConfirmMessage();
            newMessage.setMessage("Environment,"+temEnvHigTxt.getText()+","+temEnvLowTxt.getText()+","+humHTxt.getText()+","+humLTxt.getText()+","+airCHTxt.getText()+","+airCLTxt.getText());
            newMessage.setPostDate(new Date());
            newMessage.setReceiver(communityOfficialAccount);
            newMessage.setSender(userAccount);
            
            organization.getMessageQueue().getMessages().add(newMessage);
            JOptionPane.showMessageDialog(null, "Successfully");
        }
        else{
            JOptionPane.showMessageDialog(null, "Please input all of text areas.","Warning",JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_setECriteriaJBtnActionPerformed

    private void responJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_responJButtonActionPerformed
        // TODO add your handling code here:
        int dialogButton = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog(null, "Would you like to SEND RESPOND?", "Warning", dialogButton);
            if(dialogResult == JOptionPane.YES_OPTION){
                PostMessage newMessage = new NeedConfirmMessage();
                newMessage.setMessage("I have updated!");
                newMessage.setPostDate(new Date());
                UserAccount ua = null;
                for(Organization o : enterprise.getOrganizationDirectory().getOrganizationList()){
                    //在同一个enterprise下找到EPAOrganization类型的orga
                    if(o.getOrganizationType() == Organization.Type.EPAOfficial){
                        for(UserAccount u : o.getUserAccountDirectory().getUserAccountList()){
                            //在EPAOfficial的organization中找到一个地区的
                            if(u.getArea().equals(userAccount.getArea())){
                                ua = u;
                            }
                        }
                    }
                }
                newMessage.setReceiver(ua);
                newMessage.setSender(userAccount);
                
                organization.getMessageQueue().getMessages().add(newMessage);
            }
    }//GEN-LAST:event_responJButtonActionPerformed

    private void refreshJBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshJBtnActionPerformed
        // TODO add your handling code here:
        populateVitalTable();
        populateEnvironmentTable();
        populateMessageTable();
    }//GEN-LAST:event_refreshJBtnActionPerformed

    private void analyzeJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_analyzeJButtonActionPerformed
        // TODO add your handling code here:
        populateResult();

    }//GEN-LAST:event_analyzeJButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField BPHTxt;
    private javax.swing.JTextField BPLTxt;
    private javax.swing.JTextField airCHTxt;
    private javax.swing.JTextField airCLTxt;
    private javax.swing.JButton analyzeJButton;
    private javax.swing.JLabel bloodPAVG;
    private javax.swing.JLabel communityJLabel;
    private javax.swing.JTable environmentJTable;
    private javax.swing.JTextField humHTxt;
    private javax.swing.JTextField humLTxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
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
    private javax.swing.JTable officialMessageJTable;
    private javax.swing.JLabel pulseAVG;
    private javax.swing.JTextField pulseHTxt;
    private javax.swing.JTextField pulseLTxt;
    private javax.swing.JButton refreshJBtn;
    private javax.swing.JButton responJButton;
    private javax.swing.JButton setECriteriaJBtn;
    private javax.swing.JButton setHCriteriaJBtn;
    private javax.swing.JTextField temEnvHigTxt;
    private javax.swing.JTextField temEnvLowTxt;
    private javax.swing.JTextField temHeaHigTxt;
    private javax.swing.JTextField temHeaLowTxt;
    private javax.swing.JLabel temperAVG;
    private javax.swing.JLabel valueLabel;
    private javax.swing.JTable vitalSHJTable;
    // End of variables declaration//GEN-END:variables
}
