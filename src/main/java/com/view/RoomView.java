/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.view;

import com.controller.ContractController;
import com.controller.GuestController;
import com.controller.RoomController;
import com.controller.ServiceController;
import com.controller.dto.GuestDTO;
import com.controller.dto.RoomDTO;
import com.controller.dto.ServiceDTO;
import com.controller.dto.UserDTO;
import com.model.Guest;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author hadt2
 */
public final class RoomView extends javax.swing.JFrame {

    DefaultTableModel currentGuestTableModel;
    DefaultTableModel registerGuestTableModel;
    DefaultTableModel serviceListTableModel;
    DefaultTableModel billDetailTableModel;
    UserDTO user;
    RoomDTO currentRoom;
    public List<GuestDTO> gListDTO;
    public List<Guest> gListModel = new ArrayList<>();
    GuestController gController = new GuestController();
    ContractController cController = new ContractController();
    RoomController rController = new RoomController();
    List<ServiceDTO> serviceList = new ArrayList<>();
    ServiceController serviceController = new ServiceController();
    LocalDate currentDate = LocalDate.now();

    /**
     * Creates new form RoomView
     */
    //Ẩn/Hiện cửa sổ trước
    private JFrame roomListViewFrame;
    //Sử dụng WindowListener để override sự kiện bấm nút "X" (close)
    WindowListener exitListener = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            roomListViewFrame.setVisible(true); //Hiển thị lại form cha
            dispose(); //Đóng form hiện tại
        }
    };

    private BillDetail billDetail;

    public RoomView(JFrame roomList, UserDTO user, String roomNo) {
        initComponents();
        this.roomListViewFrame = roomList;
        this.user = user;
        currentRoom = rController.getRoom(roomNo);
//        System.out.println(currentRoom.toString());
        currentGuestTableModel = (DefaultTableModel) tbCurrentGuest.getModel();
        registerGuestTableModel = (DefaultTableModel) tbRegisterGuest.getModel();
        serviceListTableModel = (DefaultTableModel) tbnServiceList.getModel();
        billDetailTableModel = (DefaultTableModel) tbBillDetail.getModel();

        //Combobox year:
        DefaultComboBoxModel billYearModel = (DefaultComboBoxModel) txtBillYear.getModel();
        int currentYear = currentDate.getYear();
        billYearModel.removeAllElements();
        billYearModel.addElement(currentYear - 1);
        billYearModel.addElement(currentYear);
        txtBillYear.setModel(billYearModel);

        //Combobox month:
        int currentMonth = currentDate.getMonthValue();
        if (currentMonth == 1) {
            txtBillYear.setSelectedItem(0);
            txtBillMonth.setSelectedIndex(11);
        } else {
            txtBillYear.setSelectedItem(1);
            txtBillMonth.setSelectedIndex(currentMonth - 1);

        }

        if (user.getAuthority() == 0) {
            txtSquare.setEditable(false);
        }
        displayServiceList();
        if (currentRoom.getStatus() == 0) {
            prepareBill();

        }
        this.addWindowListener(exitListener); //Gọi sự kiện đóng nút "X"
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); //Set nút "X" không đóng chương trình theo mặc định
    }

    public void prepareBill() {
        NumberFormat nf = NumberFormat.getInstance();
        txtBillRoomPrice.setText(currentRoom.getPrice());
        txtBillRoomPrice.setEditable(false);
        billRoomLabel.setText(currentRoom.getRoomNumber());
        billDateLabel.setText(currentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        billPeriodLabel.setText(txtBillMonth.getSelectedItem() + "/" + txtBillYear.getSelectedItem());
        billNoLabel.setText(currentRoom.getRoomNumber() + "_" + billPeriodLabel.getText());

        int subTotalRoom = Integer.parseInt(currentRoom.getPrice().replace(",", "")) * Integer.parseInt(billMonthQuantity.getValue().toString());
        billDetailTableModel.setRowCount(0);
        billDetailTableModel.addRow(new Object[]{
            billDetailTableModel.getRowCount() + 1,
            "Tiền phòng",
            currentRoom.getPrice(),
            "",
            "",
            billMonthQuantity.getValue(),
            nf.format(subTotalRoom)
        }
        );

        serviceList.stream()
                .forEach(s -> {
                    billDetailTableModel.addRow(new Object[]{
                        billDetailTableModel.getRowCount() + 1,
                        s.getServiceName(),
                        s.getPrice()
                    });
                }
                );

        billDetailTableModel.setValueAt(currentRoom.getElectricCounter(), 1, 3);
        billDetailTableModel.setValueAt(txtBillCurElect.getText(), 1, 4);
        int electricQuantity = 0;

        try {
            electricQuantity = Integer.parseInt(txtBillCurElect.getText()) - Integer.parseInt(currentRoom.getElectricCounter());
        } catch (NumberFormatException e) {
        }
        billDetailTableModel.setValueAt(nf.format(electricQuantity), 1, 5);
        billDetailTableModel.setValueAt(
                nf.format(Integer.parseInt(billDetailTableModel.getValueAt(1, 2).toString().replace(",", "")) * electricQuantity),
                1, 6);

        billDetailTableModel.setValueAt(currentRoom.getWaterCounter(), 2, 3);
        billDetailTableModel.setValueAt(txtBillCurWater.getText(), 2, 4);
        int waterQuantity = 0;

        try {
            waterQuantity = Integer.parseInt(txtBillCurWater.getText()) - Integer.parseInt(currentRoom.getWaterCounter());
        } catch (NumberFormatException e) {
        }
        billDetailTableModel.setValueAt(nf.format(waterQuantity), 2, 5);
        billDetailTableModel.setValueAt(
                nf.format(Integer.parseInt(billDetailTableModel.getValueAt(2, 2).toString().replace(",", "")) * waterQuantity),
                2, 6);

        for (int i = 3; i < billDetailTableModel.getRowCount(); i++) {
            billDetailTableModel.setValueAt(1, i, 5);
        }
        for (int i = 3; i < billDetailTableModel.getRowCount(); i++) {
            billDetailTableModel.setValueAt(billDetailTableModel.getValueAt(i, 2), i, 6);
        }
        int total = 0;
        for (int i = 0; i < billDetailTableModel.getRowCount(); i++) {
            total += Integer.parseInt(billDetailTableModel.getValueAt(i, 6).toString().replace(",", ""));
        }
        billTotalLabel.setText(nf.format(total));
    }

    public RoomView() {
        initComponents();
        displayRoomInfo();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of
     * this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtSquare = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescription = new javax.swing.JTextArea();
        btnUpdate = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtPrice = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtElectric = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtWater = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbCurrentGuest = new javax.swing.JTable();
        roomNum = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        btnLiquidate = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtFullName = new javax.swing.JTextField();
        txtCCCD = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtPhone = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbRegisterGuest = new javax.swing.JTable();
        btnAddGuest = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtElectContract = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtWaterContract = new javax.swing.JTextField();
        btnAddContract = new javax.swing.JButton();
        btnSelectGuestList = new javax.swing.JButton();
        datePick = new com.toedter.calendar.JDateChooser();
        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jTextField16 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        jTextField2 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        txtBillRoomPrice = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        txtBillCurElect = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        txtBillCurWater = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        btnCreateBill = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        txtBillDescription = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tbnServiceList = new javax.swing.JTable();
        jLabel18 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        billMonthQuantity = new javax.swing.JSpinner();
        txtBillMonth = new javax.swing.JComboBox<>();
        txtBillYear = new javax.swing.JComboBox<>();
        jLabel25 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        billRoomLabel = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tbBillDetail = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel31 = new javax.swing.JLabel();
        billNoLabel = new javax.swing.JLabel();
        billDateLabel = new javax.swing.JLabel();
        billTotalLabel = new javax.swing.JLabel();
        billPeriodLabel = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        titleRoomNo = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Quản lý phòng");

        jTabbedPane2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTabbedPane2FocusGained(evt);
            }
        });

        jLabel2.setText("Diện tích sử dụng:");

        jLabel3.setText("Mô tả:");

        txtDescription.setColumns(20);
        txtDescription.setRows(5);
        jScrollPane1.setViewportView(txtDescription);

        btnUpdate.setText("Lưu");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        jLabel4.setText("Giá phòng:");

        jLabel5.setText("Chỉ số điện:");

        txtElectric.setEditable(false);

        jLabel6.setText("Chỉ số nước:");

        txtWater.setEditable(false);
        txtWater.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtWaterActionPerformed(evt);
            }
        });

        jLabel7.setText("Khách đang thuê:");

        tbCurrentGuest.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Họ tên", "CCCD", "Điện thoại"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tbCurrentGuest);

        roomNum.setText("...");

        jLabel24.setText("Số phòng:");

        btnLiquidate.setText("Trả phòng");
        btnLiquidate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLiquidateActionPerformed(evt);
            }
        });

        jButton1.setText("Cập nhật hợp đồng");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(roomNum, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(txtElectric, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtPrice, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                                    .addComponent(txtSquare))
                                .addComponent(txtWater, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(240, 240, 240))))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnLiquidate, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 744, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(roomNum)
                            .addComponent(jLabel24)
                            .addComponent(btnLiquidate)
                            .addComponent(jButton1)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel7)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSquare, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtElectric, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtWater, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnUpdate))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(175, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Thông tin", jPanel1);

        jLabel9.setText("Thêm khách mới:");

        jLabel10.setText("Họ tên:");

        jLabel11.setText("Ngày sinh:");

        jLabel12.setText("CCCD:");

        jLabel13.setText("Phone:");

        tbRegisterGuest.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Họ tên", "Ngày sinh", "CCCD", "Phone"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tbRegisterGuest);

        btnAddGuest.setText("Thêm khách");
        btnAddGuest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddGuestActionPerformed(evt);
            }
        });

        jLabel15.setText("Thông tin phòng:");

        jLabel16.setText("Đồng hồ điện:");

        jLabel17.setText("Đồng hồ nước:");

        btnAddContract.setText("Lưu");
        btnAddContract.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddContractActionPerformed(evt);
            }
        });

        btnSelectGuestList.setText("Chọn khách đã có");
        btnSelectGuestList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectGuestListActionPerformed(evt);
            }
        });

        datePick.setDateFormatString("dd/MM/yyyy");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel17))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtWaterContract)
                            .addComponent(txtElectContract)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPhone)
                            .addComponent(txtFullName)
                            .addComponent(txtCCCD)
                            .addComponent(datePick, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addComponent(btnSelectGuestList)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAddGuest, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(168, 168, 168))
                            .addComponent(btnAddContract, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 783, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtFullName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addGap(9, 9, 9)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(datePick, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCCCD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13))
                        .addGap(40, 40, 40)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAddGuest)
                            .addComponent(btnSelectGuestList))
                        .addGap(10, 10, 10)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(txtElectContract, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtWaterContract, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAddContract))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE))
                .addContainerGap(142, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Tạo hợp đồng", jPanel2);

        jLabel8.setText("Tổng hợp danh sách khách thuê:");

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Họ tên", "CCCD", " Ngày sinh", "Điện thoại", "Hợp đồng", "Trạng thái"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTable2);

        jButton5.setText("Tìm kiếm");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 630, Short.MAX_VALUE)
                        .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton5)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Khách", jPanel4);

        jTable5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Số hợp đồng", "Ngày lập", "Title 3", "Title 4"
            }
        ));
        jScrollPane6.setViewportView(jTable5);

        jButton2.setText("Tìm kiếm");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 1111, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
                .addGap(19, 19, 19))
        );

        jTabbedPane2.addTab("Tra cứu hợp đồng", jPanel5);

        jLabel19.setText("Kỳ thanh toán:");

        jLabel20.setText("Tiền phòng:");

        txtBillCurElect.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBillCurElectFocusLost(evt);
            }
        });

        jLabel22.setText("Số điện:");

        txtBillCurWater.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBillCurWaterFocusLost(evt);
            }
        });

        jLabel23.setText("Số nước:");

        btnCreateBill.setText("Lưu");
        btnCreateBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateBillActionPerformed(evt);
            }
        });

        jButton3.setText("Tìm kiếm");

        txtBillDescription.setColumns(20);
        txtBillDescription.setRows(5);
        jScrollPane7.setViewportView(txtBillDescription);

        jLabel1.setText("Diễn giải:");

        tbnServiceList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Dịch vụ", "Đơn giá"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane8.setViewportView(tbnServiceList);

        jLabel18.setText("Giá dịch vụ:");

        jLabel21.setText("Số tháng:");

        billMonthQuantity.setModel(new javax.swing.SpinnerNumberModel(1, 1, 12, 1));
        billMonthQuantity.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                billMonthQuantityStateChanged(evt);
            }
        });

        txtBillMonth.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12" }));

        jLabel25.setText("Năm:");

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Preview hóa đơn"));

        jLabel26.setText("Số hóa đơn:");

        jLabel27.setText("Ngày tạo:");

        jLabel28.setText("HÓA ĐƠN THUÊ PHÒNG");

        billRoomLabel.setText("...");

        jLabel30.setText("Kỳ thanh toán:");

        tbBillDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "STT", "Nội dung", "Đơn giá", "Chỉ số đầu", "Chỉ số cuối", "Số lượng", "Thành tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(tbBillDetail);
        if (tbBillDetail.getColumnModel().getColumnCount() > 0) {
            tbBillDetail.getColumnModel().getColumn(0).setMinWidth(20);
            tbBillDetail.getColumnModel().getColumn(0).setPreferredWidth(35);
            tbBillDetail.getColumnModel().getColumn(0).setMaxWidth(40);
            tbBillDetail.getColumnModel().getColumn(1).setPreferredWidth(70);
            tbBillDetail.getColumnModel().getColumn(2).setPreferredWidth(50);
            tbBillDetail.getColumnModel().getColumn(3).setPreferredWidth(40);
            tbBillDetail.getColumnModel().getColumn(4).setPreferredWidth(40);
            tbBillDetail.getColumnModel().getColumn(5).setPreferredWidth(30);
        }

        jLabel31.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel31.setText("TỔNG CỘNG:");

        billNoLabel.setText("...");

        billDateLabel.setText("...");

        billTotalLabel.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        billTotalLabel.setText("...");

        billPeriodLabel.setText("...");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel28)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(billRoomLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(375, 375, 375)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(billDateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                            .addComponent(billNoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(billTotalLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane5)
                            .addComponent(jSeparator1)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(billPeriodLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel26)
                            .addComponent(billNoLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel27)
                            .addComponent(billDateLabel)))
                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(billRoomLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(billPeriodLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(billTotalLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(58, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnCreateBill, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                                    .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addComponent(txtBillMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtBillYear, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(txtBillCurElect, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(txtBillRoomPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                                        .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(billMonthQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                                        .addComponent(jLabel23)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(txtBillCurWater, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))))))))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(380, 380, 380)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtBillMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtBillYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtBillRoomPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21)
                            .addComponent(billMonthQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBillCurElect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtBillCurWater, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane7))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18))
                        .addGap(20, 20, 20)
                        .addComponent(btnCreateBill))
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Hóa đơn", jPanel3);

        jPanel6.setBackground(new java.awt.Color(0, 102, 204));

        titleRoomNo.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        titleRoomNo.setForeground(new java.awt.Color(255, 255, 255));
        titleRoomNo.setText("...");

        jLabel14.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("QUẢN LÝ PHÒNG");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(150, 150, 150)
                .addComponent(titleRoomNo, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(925, Short.MAX_VALUE))
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addGap(16, 16, 16)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(756, Short.MAX_VALUE)))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleRoomNo, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1111, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 511, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void displayCurrentGuest() {
        currentGuestTableModel.setRowCount(0);
        gListDTO = GuestController.displayCurrentGuestDTO(roomNum.getText(), 1);
        gListDTO.stream().forEach(g -> {
            currentGuestTableModel.addRow(new String[]{
                g.getFullName(),
                g.getCitizenId(),
                g.getPhone()
            });
        });
    }

    public void displayRoomInfo() {
        RoomDTO rdto = rController.getRoom(roomNum.getText());
        if (rdto != null) {
            txtPrice.setText(rdto.getPrice());
            txtSquare.setText(rdto.getSquare());
            txtElectric.setText(rdto.getElectricCounter());
            txtElectContract.setText(rdto.getElectricCounter());
            txtWater.setText(rdto.getWaterCounter());
            txtWaterContract.setText(rdto.getWaterCounter());
            txtDescription.setText(rdto.getDescription());
        }
    }

    //<editor-fold desc="Input-field validators">
    private boolean validateNumber(String value) {
        boolean res = true;
        try {
            int intValue = Integer.parseInt(value);
            if (intValue <= 0) {
                res = false;
            }
        } catch (NumberFormatException e) {
            res = false;
        }
        return res;
    }

    public boolean validateName(String value) {
        boolean res = true;
        if (value.equals("") || value.length() > 50) {
            res = false;
        }
        return res;
    }

    public boolean validateCCCD(String value) {
        boolean res = true;
        if (!value.matches("^[0-9]{9,12}$")) {
            res = false;
        }
        return res;
    }

    public boolean validatePhone(String value) {
        boolean res = true;
        if (!value.matches("^[0-9]{10}$")) {
            res = false;
        }
        return res;
    }
    // </editor-fold>

    public void updateRoom() {
        String roomNumber = roomNum.getText();
        String price = txtPrice.getText();
        price = price.replaceAll("\\s|,|\\.", "");
        String square = txtSquare.getText();
        String description = txtDescription.getText();
        String elec = txtElectric.getText();
        String water = txtWater.getText();
        if (!validateNumber(price) || !validateNumber(square)) {
            String squareErr = !validateNumber(square) ? "Diện tích phòng không hợp lệ. Diện tích phải là số >0.\n" : "";
            String priceErr = !validateNumber(price) ? "Giá phòng không hợp lệ. Giá phòng phải là số >0.\n" : "";
            JOptionPane.showMessageDialog(null, squareErr + priceErr);
        } else {
            boolean res = RoomController.updateRoomObj(roomNumber, price, square, description, elec, water);
            if (res) {
                JOptionPane.showMessageDialog(null, "Cập nhật thành công");
            }
        }
    }

    public void addRegisterGuestList() {
        String name = txtFullName.getText();
        Date dob = datePick.getDate();
        String cccd = txtCCCD.getText();
        String phone = txtPhone.getText();
        if (validateName(name) | validateCCCD(cccd) | validatePhone(phone)) {

            JOptionPane.showMessageDialog(null, "Tên không được để trống");
        } else {
            Guest g = new Guest();
            g.setFullName(name);
            g.setDateOfBirth(dob);
            g.setCitizenId(cccd);
            g.setPhone(phone);
            if (gListModel.isEmpty()) {
                g.setRole(1);
            } else {
                g.setRole(0);
            }
            gListModel.add(g);
        }

    }

    public void displayRegisterGuestList() {
        registerGuestTableModel.setRowCount(0);
        List<GuestDTO> list = gController.displaySelectedGuestDTO(gListModel);
        if (!list.isEmpty()) {
            JOptionPane.showMessageDialog(null, list.get(0).getFullName());
        }
        list.stream().forEach(g -> {
            registerGuestTableModel.addRow(new String[]{
                g.getFullName(),
                g.getDateOfBirth(),
                g.getCitizenId(),
                g.getPhone()
            });
        });
    }

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        updateRoom();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnAddGuestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddGuestActionPerformed
        addRegisterGuestList();
        displayRegisterGuestList();
    }//GEN-LAST:event_btnAddGuestActionPerformed

    private void btnAddContractActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddContractActionPerformed
        if (!gListModel.isEmpty()) {
            gListModel.stream().map(g -> gController.addGuest(g));

            //test with admin account (id=1) will change later to a variable base on logged in account id
            //"fileLocation" set to a dump string since this function is not yet available.
            if (cController.addContract(rController.getRoom(roomNum.getText()), gListModel, user.getId(), "no file yet!")) {
                rController.updateRoomStatus(roomNum.getText(), 0); //Set current status to "0" => "rented".
                JOptionPane.showMessageDialog(null, "Tạo hợp đồng thành công.");
                txtFullName.setText("");
                datePick.setDate(null);
                txtCCCD.setText("");
                txtPhone.setText("");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Danh sách khách không được để trống");
        }

    }//GEN-LAST:event_btnAddContractActionPerformed

    private void jTabbedPane2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTabbedPane2FocusGained
        currentRoom = rController.getRoom(roomNum.getText());
        if (currentRoom.getStatus() == 0) {
            txtFullName.setEnabled(false);
            datePick.setEnabled(false);
            txtCCCD.setEnabled(false);
            txtPhone.setEnabled(false);
            btnAddGuest.setEnabled(false);
            btnAddContract.setEnabled(false);
            btnSelectGuestList.setEnabled(false);
            txtElectContract.setEnabled(false);
            txtWaterContract.setEnabled(false);

            txtBillCurElect.setEnabled(true);
            txtBillCurWater.setEnabled(true);
            txtBillRoomPrice.setEnabled(true);
        } else {
            displayCurrentGuest();
            txtFullName.setEnabled(true);
            datePick.setEnabled(true);
            txtCCCD.setEnabled(true);
            txtPhone.setEnabled(true);
            btnAddGuest.setEnabled(true);
            btnAddContract.setEnabled(true);
            btnSelectGuestList.setEnabled(true);
            txtElectContract.setEnabled(true);
            txtWaterContract.setEnabled(true);

            txtBillCurElect.setEnabled(false);
            txtBillCurWater.setEnabled(false);
            txtBillRoomPrice.setEnabled(false);
        }
    }//GEN-LAST:event_jTabbedPane2FocusGained

    private void btnLiquidateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLiquidateActionPerformed
        int ans = JOptionPane.showConfirmDialog(null, "Bạn có muốn thanh lý hợp đồng hiện tại?", "Thanh lý hợp đồng", JOptionPane.YES_NO_OPTION);
        switch (ans) {
            case 0:
                System.out.println("Xóa");
                break;
            case 1:
                System.out.println("Không xóa");
                break;
        }
    }//GEN-LAST:event_btnLiquidateActionPerformed
    GuestListSelectView gSelectView;
    private void btnSelectGuestListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectGuestListActionPerformed
        // TODO add your handling code here:
        gSelectView = new GuestListSelectView(this);
        gSelectView.setVisible(true);
        gSelectView.setLocationRelativeTo(this);

    }//GEN-LAST:event_btnSelectGuestListActionPerformed

    private void txtWaterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtWaterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtWaterActionPerformed

    private void btnCreateBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateBillActionPerformed
        // TODO add your handling code here:

//        String month = (monthPick.getMonth() + 1) + "-" + year;
        billDetail = new BillDetail();
        billDetail.setVisible(true);
        billDetail.pack();
        billDetail.setLocationRelativeTo(null);
    }//GEN-LAST:event_btnCreateBillActionPerformed

    private void txtBillCurElectFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBillCurElectFocusLost
        // TODO add your handling code here:
        boolean isValid = true;
        try {
            if (Integer.parseInt(txtBillCurElect.getText()) < Integer.parseInt(currentRoom.getElectricCounter())) {
                txtBillCurElect.setText("");
                txtBillCurElect.setBackground(Color.yellow);
                isValid = false;
                JOptionPane.showMessageDialog(null, "Chỉ số điện hiện tại phải lớn hơn hoặc bằng chỉ số đầu kỳ.", "Dữ liệu không hợp lệ", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            txtBillCurElect.setText("");
            txtBillCurElect.setBackground(Color.yellow);
            isValid = false;
            JOptionPane.showMessageDialog(null, "Dữ liệu không hợp lệ.", "Dữ liệu không hợp lệ", JOptionPane.ERROR_MESSAGE);

        }
        if (isValid) {
            txtBillCurElect.setBackground(Color.white);
            prepareBill();
        }
    }//GEN-LAST:event_txtBillCurElectFocusLost

    private void txtBillCurWaterFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBillCurWaterFocusLost
        // TODO add your handling code here:
        boolean isValid = true;
        try {
            if (Integer.parseInt(txtBillCurWater.getText()) < Integer.parseInt(currentRoom.getWaterCounter())) {
                txtBillCurWater.setText("");
                txtBillCurWater.setBackground(Color.yellow);
                isValid = false;
                JOptionPane.showMessageDialog(null, "Chỉ số nước hiện tại phải lớn hơn hoặc bằng chỉ số đầu kỳ.", "Dữ liệu không hợp lệ", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            txtBillCurWater.setText("");
            txtBillCurWater.setBackground(Color.yellow);
            isValid = false;
            JOptionPane.showMessageDialog(null, "Dữ liệu không hợp lệ.", "Dữ liệu không hợp lệ", JOptionPane.ERROR_MESSAGE);
        }
        if (isValid) {
            txtBillCurWater.setBackground(Color.white);
            prepareBill();
        }
    }//GEN-LAST:event_txtBillCurWaterFocusLost

    private void billMonthQuantityStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_billMonthQuantityStateChanged
        // TODO add your handling code here:
        prepareBill();
    }//GEN-LAST:event_billMonthQuantityStateChanged

    private void displayServiceList() {
        serviceList = serviceController.displayService();
        if (serviceList != null) {
            serviceListTableModel.setRowCount(0);
            serviceList.stream().forEach(s -> {
                serviceListTableModel.addRow(new String[]{
                    s.getServiceName(),
                    s.getPrice() + "/" + s.getUnit()
                }
                );
            }
            );
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(RoomView.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RoomView.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RoomView.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RoomView.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new RoomView().setVisible(true);
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel billDateLabel;
    private javax.swing.JSpinner billMonthQuantity;
    private javax.swing.JLabel billNoLabel;
    private javax.swing.JLabel billPeriodLabel;
    private javax.swing.JLabel billRoomLabel;
    private javax.swing.JLabel billTotalLabel;
    private javax.swing.JButton btnAddContract;
    private javax.swing.JButton btnAddGuest;
    private javax.swing.JButton btnCreateBill;
    private javax.swing.JButton btnLiquidate;
    private javax.swing.JButton btnSelectGuestList;
    private javax.swing.JButton btnUpdate;
    private com.toedter.calendar.JDateChooser datePick;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
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
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable5;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField2;
    public javax.swing.JLabel roomNum;
    private javax.swing.JTable tbBillDetail;
    private javax.swing.JTable tbCurrentGuest;
    private javax.swing.JTable tbRegisterGuest;
    private javax.swing.JTable tbnServiceList;
    public javax.swing.JLabel titleRoomNo;
    private javax.swing.JTextField txtBillCurElect;
    private javax.swing.JTextField txtBillCurWater;
    private javax.swing.JTextArea txtBillDescription;
    private javax.swing.JComboBox<String> txtBillMonth;
    private javax.swing.JTextField txtBillRoomPrice;
    private javax.swing.JComboBox<String> txtBillYear;
    private javax.swing.JTextField txtCCCD;
    public javax.swing.JTextArea txtDescription;
    private javax.swing.JTextField txtElectContract;
    public javax.swing.JTextField txtElectric;
    private javax.swing.JTextField txtFullName;
    private javax.swing.JTextField txtPhone;
    public javax.swing.JTextField txtPrice;
    public javax.swing.JTextField txtSquare;
    public javax.swing.JTextField txtWater;
    private javax.swing.JTextField txtWaterContract;
    // End of variables declaration//GEN-END:variables

}
