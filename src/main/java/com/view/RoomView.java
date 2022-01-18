/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.view;

import com.controller.BillController;
import com.controller.ContractController;
import com.controller.GuestController;
import com.controller.RoomController;
import com.controller.ServiceController;
import com.controller.UserController;
import com.controller.dto.BillDTO;
import com.controller.dto.ContractDTO;
import com.controller.dto.GuestDTO;
import com.controller.dto.RoomDTO;
import com.controller.dto.ServiceDTO;
import com.controller.dto.UserDTO;
import com.model.BillDetail;
import com.model.Guest;
import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    DefaultTableModel billListTableModel;
    DefaultTableModel billDetailViewTableModel;
    DefaultTableModel tbContractListModel;
    DefaultTableModel tbGuestInContractListModel;
    UserDTO user;
    RoomDTO currentRoom;
    public List<GuestDTO> gListDTO;
    public List<Guest> gListModel = new ArrayList<>();
    UserController uController = new UserController();
    GuestController gController = new GuestController();
    ContractController cController = new ContractController();
    RoomController rController = new RoomController();
    BillController bController = new BillController();
    List<ServiceDTO> serviceList = new ArrayList<>();
    ServiceController serviceController = new ServiceController();
    LocalDate currentDate = LocalDate.now();
    int electricCount;
    int waterCount;
    int monthQuantity;

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

    public RoomView(JFrame roomList, UserDTO user, String roomNo) {
        initComponents();
        //Only insert custom code after this line:
        this.roomListViewFrame = roomList;
        this.user = user;
        currentRoom = rController.getRoom(roomNo);

        //Date Picker set default:
        JTextFieldDateEditor dateEditor = (JTextFieldDateEditor) datePick.getDateEditor();
        dateEditor.setEditable(false);
        datePickDefault("01/01/1990");

        currentGuestTableModel = (DefaultTableModel) tbCurrentGuest.getModel();
        registerGuestTableModel = (DefaultTableModel) tbRegisterGuest.getModel();
//        serviceListTableModel = (DefaultTableModel) tbServiceList.getModel();
        billDetailTableModel = (DefaultTableModel) tbBillDetail.getModel();
        billListTableModel = (DefaultTableModel) tbBillList.getModel();
        billDetailViewTableModel = (DefaultTableModel) tbBillDetailView.getModel();
        tbContractListModel = (DefaultTableModel) tbContractList.getModel();
        tbGuestInContractListModel = (DefaultTableModel) tbGuestInContractList.getModel();

        //Combobox year:
        DefaultComboBoxModel billYearModel = (DefaultComboBoxModel) txtBillYear.getModel();
        int currentYear = currentDate.getYear();
        billYearModel.removeAllElements();
        billYearModel.addElement(currentYear - 1);
        billYearModel.addElement(currentYear);
        txtBillYear.setModel(billYearModel);

        txtExistGuestNotify.setText("");

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
        if (currentRoom.getStatus() == 1) {
            btnCreateBill.setEnabled(false);
            btnLiquidate.setEnabled(false);
            billMonthQuantity.setEnabled(false);
        }

        txtLastECountBill.setText(currentRoom.getElectricCounter());
        txtLastWCountBill.setText(currentRoom.getWaterCounter());

        displayListBill();
        displayRentalStatus();

        //Contract list display:
        displayContractsList(currentRoom.getId());

        this.addWindowListener(exitListener); //Gọi sự kiện đóng nút "X"
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); //Set nút "X" không đóng chương trình theo mặc định
    }
    public static List<ContractDTO> contractList;

    private void displayContractsList(int roomId) {
        contractList = cController.getContractsOfRoom(roomId);
        if (contractList != null) {
            tbContractListModel.setRowCount(0);
            contractList.forEach(c -> {
                tbContractListModel.addRow(new Object[]{
                    tbContractListModel.getRowCount() + 1,
                    c.getContractNumber(),
                    c.getCreatedDate(),
                    c.getUpdatedDate(),
                    c.getStatus(),
                    uController.getUserById(c.getUserId()).getUsername()
                });
            });
        }
    }

    public void datePickDefault(String dfDate) {
        try {
            Date defaultDate = new SimpleDateFormat("dd/MM/yyyy").parse(dfDate);
            datePick.setDate(defaultDate);
        } catch (ParseException ex) {
        }

    }

    public void prepareBill() {
        NumberFormat nf = NumberFormat.getInstance();
        txtBillRoomPrice.setText(currentRoom.getPrice());
        txtBillRoomPrice.setEditable(false);
        billRoomLabel.setText(currentRoom.getRoomNumber());
        billDateLabel.setText(currentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        billPeriodLabel.setText(txtBillMonth.getSelectedItem() + "/" + txtBillYear.getSelectedItem());
        billNoLabel.setText(currentRoom.getRoomNumber() + "_" + billPeriodLabel.getText());
        monthQuantity = Integer.parseInt(billMonthQuantity.getValue().toString());

        int subTotalRoom = Integer.parseInt(currentRoom.getPrice().replace(",", "")) * monthQuantity;
        billDetailTableModel.setRowCount(0);
        billDetailTableModel.addRow(new Object[]{
            billDetailTableModel.getRowCount() + 1,
            "Tiền phòng",
            currentRoom.getPrice(),
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

//        billDetailTableModel.setValueAt(currentRoom.getElectricCounter(), 1, 3);
//        billDetailTableModel.setValueAt(txtBillCurElect.getText(), 1, 4);
        int electricQuantity = 0;

        try {
            electricCount = Integer.parseInt(txtBillCurElect.getText());
            electricQuantity = electricCount - Integer.parseInt(currentRoom.getElectricCounter());
        } catch (NumberFormatException e) {
        }
        billDetailTableModel.setValueAt(nf.format(electricQuantity), 1, 3);
        billDetailTableModel.setValueAt(
                nf.format(Integer.parseInt(billDetailTableModel.getValueAt(1, 2).toString().replace(",", "")) * electricQuantity),
                1, 4);

//        billDetailTableModel.setValueAt(currentRoom.getWaterCounter(), 2, 3);
//        billDetailTableModel.setValueAt(txtBillCurWater.getText(), 2, 4);
        int waterQuantity = 0;

        try {
            waterCount = Integer.parseInt(txtBillCurWater.getText());
            waterQuantity = waterCount - Integer.parseInt(currentRoom.getWaterCounter());
        } catch (NumberFormatException e) {
        }
        billDetailTableModel.setValueAt(nf.format(waterQuantity), 2, 3);
        billDetailTableModel.setValueAt(
                nf.format(Integer.parseInt(billDetailTableModel.getValueAt(2, 2).toString().replace(",", "")) * waterQuantity),
                2, 4);

        for (int i = 3; i < billDetailTableModel.getRowCount(); i++) {
            billDetailTableModel.setValueAt(1, i, 3);
        }
        for (int i = 3; i < billDetailTableModel.getRowCount(); i++) {
            billDetailTableModel.setValueAt(billDetailTableModel.getValueAt(i, 2), i, 4);
        }
        int total = 0;
        for (int i = 0; i < billDetailTableModel.getRowCount(); i++) {
            total += Integer.parseInt(billDetailTableModel.getValueAt(i, 4).toString().replace(",", ""));
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
        jPanel11 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        roomNum = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtSquare = new javax.swing.JTextField();
        txtPrice = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtElectric = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtWater = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        btnUpdate = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescription = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        btnLiquidate = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbCurrentGuest = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
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
        jPanel4 = new javax.swing.JPanel();
        btnCreateBill = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        txtBillMonth = new javax.swing.JComboBox<>();
        jLabel25 = new javax.swing.JLabel();
        txtBillYear = new javax.swing.JComboBox<>();
        jLabel22 = new javax.swing.JLabel();
        txtBillCurElect = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        txtBillCurWater = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtBillRoomPrice = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txtLastECountBill = new javax.swing.JTextField();
        txtLastWCountBill = new javax.swing.JTextField();
        billMonthQuantity = new javax.swing.JSpinner();
        jPanel5 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tbBillList = new javax.swing.JTable();
        txtSearchBill = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        txtRoomNoView = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        btnBillPayment = new javax.swing.JButton();
        txtBillNumberView = new javax.swing.JLabel();
        txtBillDateView = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tbBillDetailView = new javax.swing.JTable();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        txtTotalBillView = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        txtCCCD = new javax.swing.JTextField();
        txtExistGuestNotify = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtFullName = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        datePick = new com.toedter.calendar.JDateChooser();
        txtPhone = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        btnAddGuest = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtElectContract = new javax.swing.JTextField();
        txtWaterContract = new javax.swing.JTextField();
        jPanel15 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbRegisterGuest = new javax.swing.JTable();
        btnAddContract = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbContractList = new javax.swing.JTable();
        jPanel17 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tbGuestInContractList = new javax.swing.JTable();
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

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder("Thông tin chung"));

        jLabel24.setText("Số phòng:");

        roomNum.setText("...");

        jLabel2.setText("Diện tích sử dụng:");

        jLabel4.setText("Giá phòng:");

        txtElectric.setEditable(false);

        jLabel5.setText("Chỉ số điện:");

        txtWater.setEditable(false);
        txtWater.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtWaterActionPerformed(evt);
            }
        });

        jLabel6.setText("Chỉ số nước:");

        btnUpdate.setText("Lưu");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        txtDescription.setColumns(20);
        txtDescription.setRows(5);
        jScrollPane1.setViewportView(txtDescription);

        jLabel3.setText("Mô tả:");

        btnLiquidate.setText("Thanh lý hợp đồng");
        btnLiquidate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLiquidateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(roomNum, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel6)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPrice)
                            .addComponent(txtSquare)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                                .addComponent(btnLiquidate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                            .addComponent(txtWater)
                            .addComponent(txtElectric))))
                .addGap(6, 6, 6))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(roomNum)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtSquare, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtElectric, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtWater, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnUpdate)
                    .addComponent(btnLiquidate))
                .addContainerGap())
        );

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Khách đang thuê"));

        tbCurrentGuest.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Họ tên", "CCCD", "Điện thoại"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tbCurrentGuest);
        if (tbCurrentGuest.getColumnModel().getColumnCount() > 0) {
            tbCurrentGuest.getColumnModel().getColumn(0).setMinWidth(50);
            tbCurrentGuest.getColumnModel().getColumn(0).setPreferredWidth(60);
            tbCurrentGuest.getColumnModel().getColumn(0).setMaxWidth(70);
        }

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 790, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(101, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Thông tin", jPanel1);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Xem trước hóa đơn"));

        jLabel26.setText("Số hóa đơn:");

        jLabel27.setText("Ngày tạo:");

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel28.setText("HÓA ĐƠN THUÊ PHÒNG");

        billRoomLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        billRoomLabel.setText("...");

        jLabel30.setText("Kỳ thanh toán:");

        tbBillDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Nội dung", "Đơn giá", "Số lượng", "Thành tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
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
            tbBillDetail.getColumnModel().getColumn(3).setPreferredWidth(30);
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
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(billTotalLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane5)
                    .addComponent(jSeparator1)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(billRoomLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(375, 375, 375)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(billPeriodLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(billDateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
                            .addComponent(billNoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(billTotalLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Tạo hóa đơn"));

        btnCreateBill.setText("Lưu");
        btnCreateBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateBillActionPerformed(evt);
            }
        });

        jLabel19.setText("Kỳ thanh toán:");

        txtBillMonth.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12" }));
        txtBillMonth.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBillMonthFocusLost(evt);
            }
        });

        jLabel25.setText("Năm:");

        txtBillYear.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBillYearFocusLost(evt);
            }
        });

        jLabel22.setText("Số điện:");

        txtBillCurElect.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBillCurElectFocusLost(evt);
            }
        });
        txtBillCurElect.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBillCurElectKeyReleased(evt);
            }
        });

        jLabel32.setText("Kỳ trước:");

        jLabel23.setText("Số nước:");

        txtBillCurWater.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBillCurWaterFocusLost(evt);
            }
        });

        jLabel34.setText("Kỳ trước:");

        jLabel20.setText("Tiền phòng:");

        jLabel21.setText("Số tháng:");

        txtLastECountBill.setEditable(false);
        txtLastECountBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLastECountBillActionPerformed(evt);
            }
        });

        txtLastWCountBill.setEditable(false);

        billMonthQuantity.setModel(new javax.swing.SpinnerNumberModel(1, 0, 12, 1));
        billMonthQuantity.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                billMonthQuantityStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnCreateBill, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtBillMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                                .addComponent(txtBillYear, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel23)
                                    .addComponent(jLabel20))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtBillRoomPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtBillCurWater, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtBillCurElect, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel34)
                                    .addComponent(jLabel21)
                                    .addComponent(jLabel32))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtLastECountBill, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtLastWCountBill, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(billMonthQuantity, javax.swing.GroupLayout.Alignment.TRAILING))))
                        .addGap(4, 4, 4)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBillMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25)
                    .addComponent(txtBillYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtBillCurElect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel32))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtBillCurWater, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel34)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(txtLastECountBill, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtLastWCountBill, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBillRoomPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21)
                    .addComponent(billMonthQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCreateBill)
                .addGap(12, 12, 12))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(21, 21, 21))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(151, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Hóa đơn", jPanel3);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Danh sách hóa đơn"));

        tbBillList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Số hóa đơn", "Tổng số tiền", "Trạng thái", "Hợp đồng", "Người tạo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbBillList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbBillListMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(tbBillList);

        txtSearchBill.setForeground(new java.awt.Color(204, 204, 204));
        txtSearchBill.setText("Tìm kiếm");
        txtSearchBill.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtSearchBillFocusGained(evt);
            }
        });
        txtSearchBill.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchBillKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(txtSearchBill, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(txtSearchBill, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Nội dung"));

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel29.setText("HÓA ĐƠN THUÊ PHÒNG");

        txtRoomNoView.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtRoomNoView.setText("...");

        jLabel33.setText("Số hóa đơn:");

        jLabel35.setText("Ngày tạo:");

        btnBillPayment.setText("Xác nhận thanh toán");
        btnBillPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBillPaymentActionPerformed(evt);
            }
        });

        txtBillNumberView.setText("...");

        txtBillDateView.setText("...");

        tbBillDetailView.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Nội dung", "Đơn giá", "Số lượng", "Thành tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane9.setViewportView(tbBillDetailView);
        if (tbBillDetailView.getColumnModel().getColumnCount() > 0) {
            tbBillDetailView.getColumnModel().getColumn(0).setMinWidth(30);
            tbBillDetailView.getColumnModel().getColumn(0).setPreferredWidth(35);
            tbBillDetailView.getColumnModel().getColumn(0).setMaxWidth(40);
        }

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("TỔNG CỘNG:");

        txtTotalBillView.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtTotalBillView.setText("...");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE)
                    .addComponent(jSeparator2)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtRoomNoView, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnBillPayment))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 160, Short.MAX_VALUE)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel33, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtBillNumberView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtBillDateView, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTotalBillView, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtRoomNoView, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBillPayment))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel33)
                            .addComponent(txtBillNumberView))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBillDateView)
                            .addComponent(jLabel35))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTotalBillView, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(82, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Tra cứu hóa đơn", jPanel5);

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Thêm khách"));

        jLabel12.setText("CCCD:");

        txtCCCD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCCCDKeyReleased(evt);
            }
        });

        txtExistGuestNotify.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        txtExistGuestNotify.setText("...");

        jLabel10.setText("Họ tên:");

        jLabel11.setText("Ngày sinh:");

        datePick.setToolTipText("");
        datePick.setDate(new Date());
        datePick.setDateFormatString("dd/MM/yyyy");
        datePick.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                datePickFocusLost(evt);
            }
        });

        jLabel13.setText("Phone:");

        btnAddGuest.setText("Thêm khách");
        btnAddGuest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddGuestActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtExistGuestNotify, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtCCCD, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtFullName)
                            .addComponent(datePick, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtPhone)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnAddGuest, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCCCD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtExistGuestNotify)
                .addGap(18, 18, 18)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtFullName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(datePick, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addGap(18, 18, 18)
                .addComponent(btnAddGuest)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder("Cập nhật số liệu"));

        jLabel16.setText("Đồng hồ điện:");

        jLabel17.setText("Đồng hồ nước:");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addGap(19, 19, 19)
                        .addComponent(txtElectContract))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtWaterContract, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(txtElectContract, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtWaterContract, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder("Danh sách khách tạo hợp đồng"));

        tbRegisterGuest.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Họ tên", "Ngày sinh", "CCCD", "Phone"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tbRegisterGuest);
        if (tbRegisterGuest.getColumnModel().getColumnCount() > 0) {
            tbRegisterGuest.getColumnModel().getColumn(0).setMinWidth(40);
            tbRegisterGuest.getColumnModel().getColumn(0).setPreferredWidth(50);
            tbRegisterGuest.getColumnModel().getColumn(0).setMaxWidth(60);
        }

        btnAddContract.setText("Lưu");
        btnAddContract.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddContractActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 764, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnAddContract, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAddContract)
                .addContainerGap(7, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(33, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(206, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Tạo hợp đồng", jPanel2);

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder("Danh sách hợp đồng"));

        tbContractList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "STT", "Số hợp đồng", "Ngày lập", "Ngày cập nhật", "Trạng thái", "Người tạo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbContractList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbContractListMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tbContractList);
        if (tbContractList.getColumnModel().getColumnCount() > 0) {
            tbContractList.getColumnModel().getColumn(0).setMinWidth(35);
            tbContractList.getColumnModel().getColumn(0).setPreferredWidth(40);
            tbContractList.getColumnModel().getColumn(0).setMaxWidth(50);
        }

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 522, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3)
                .addContainerGap())
        );

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder("Danh sách khách"));

        tbGuestInContractList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Họ tên", "Điện thoại", "Số CCCD", "Ngày sinh"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane7.setViewportView(tbGuestInContractList);
        if (tbGuestInContractList.getColumnModel().getColumnCount() > 0) {
            tbGuestInContractList.getColumnModel().getColumn(0).setMinWidth(35);
            tbGuestInContractList.getColumnModel().getColumn(0).setPreferredWidth(45);
            tbGuestInContractList.getColumnModel().getColumn(0).setMaxWidth(50);
        }

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(7, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Tra cứu hợp đồng", jPanel10);

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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addContainerGap()
                .addComponent(jTabbedPane2))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane2)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTabbedPane2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTabbedPane2FocusGained
        currentRoom = rController.getRoom(roomNum.getText());
        if (currentRoom.getStatus() == 0) {
            txtFullName.setEnabled(false);
            datePick.setEnabled(false);
            txtCCCD.setEnabled(false);
            txtPhone.setEnabled(false);
            btnAddGuest.setEnabled(false);
            btnAddContract.setEnabled(false);
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
            txtElectContract.setEnabled(true);
            txtWaterContract.setEnabled(true);

            txtBillCurElect.setEnabled(false);
            txtBillCurWater.setEnabled(false);
            txtBillRoomPrice.setEnabled(false);
        }
    }//GEN-LAST:event_jTabbedPane2FocusGained

    private void btnBillPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBillPaymentActionPerformed
        // TODO add your handling code here:
        int row = tbBillList.getSelectedRow();
        if (row >= 0) {
            String billNo = billListTableModel.getValueAt(row, 0).toString();
            System.out.println(billNo);
            if (bController.updateBillStatus(billNo)) {
                JOptionPane.showMessageDialog(null, "Hóa đơn đã thanh toán.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                displayListBill();
            } else {
                JOptionPane.showMessageDialog(null, "Hãy chọn 1 hóa đơn.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnBillPaymentActionPerformed

    private void txtSearchBillFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSearchBillFocusGained
        // TODO add your handling code here:
        txtSearchBill.setText("");
        txtSearchBill.setForeground(Color.BLACK);
    }//GEN-LAST:event_txtSearchBillFocusGained

    private void tbBillListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbBillListMouseClicked
        // TODO add your handling code here:
        //        btnBillPayment.setEnabled(true);
        int row = tbBillList.getSelectedRow(); // tìm dòng chứa dữ liệu
//        System.out.println(row);
        String billNo = billListTableModel.getValueAt(row, 0).toString(); //Lấy dữ liệu tại dòng + cột (cột định)
//        System.out.println(billNo);
        BillDTO bill = bController.getBillByNumber(billNo);
        List<BillDetail> bdList = bController.getBillDetails(billNo);

        txtBillNumberView.setText(billNo);
        txtBillDateView.setText(bill.getCreatedDate());
        txtRoomNoView.setText(currentRoom.getRoomNumber());
        txtTotalBillView.setText(bill.getTotal());

        billDetailViewTableModel.setRowCount(0);
        billDetailViewTableModel.addRow(new Object[]{
            1, "Tiền phòng",
            NumberFormat.getInstance().format(Integer.valueOf(bill.getRoomPrice())),
            bill.getRentalQuantity(),
            NumberFormat.getInstance().format(Integer.valueOf(bill.getRoomPrice() * bill.getRentalQuantity()))
        });
        bdList.stream().forEach(b -> {
            billDetailViewTableModel.addRow(new Object[]{
                billDetailViewTableModel.getRowCount() + 1,
                b.getServiceName(),
                NumberFormat.getInstance().format(Integer.valueOf(b.getPrice())),
                NumberFormat.getInstance().format(Integer.valueOf(b.getQuantity())),
                NumberFormat.getInstance().format(Integer.valueOf(b.getPrice() * b.getQuantity()))
            });
        });
    }//GEN-LAST:event_tbBillListMouseClicked

    private void txtLastECountBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLastECountBillActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLastECountBillActionPerformed

    private void txtBillYearFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBillYearFocusLost
        // TODO add your handling code here:
        prepareBill();
    }//GEN-LAST:event_txtBillYearFocusLost

    private void txtBillMonthFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBillMonthFocusLost
        // TODO add your handling code here:
        prepareBill();
    }//GEN-LAST:event_txtBillMonthFocusLost

    private void billMonthQuantityStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_billMonthQuantityStateChanged
        // TODO add your handling code here:
        prepareBill();
    }//GEN-LAST:event_billMonthQuantityStateChanged

    private void btnCreateBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateBillActionPerformed
        //
        if ("".equals(txtBillCurElect.getText()) || "".equals(txtBillCurWater.getText())) {
            JOptionPane.showMessageDialog(
                    null,
                    "Bạn phải nhập đủ dữ liệu tiêu thụ điện/nước.",
                    "Cảnh báo",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            boolean result = bController.addBill(
                    currentRoom,
                    monthQuantity,
                    electricCount,
                    waterCount,
                    billNoLabel.getText(),
                    user.getId()
            );
            if (result) {
                JOptionPane.showMessageDialog(null, "Hóa đơn đã được lưu thành công.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                txtBillCurElect.setText("");
                txtBillCurWater.setText("");
                billMonthQuantity.setValue(1);
                txtDescription.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "Số hóa đơn này đã được lập.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            }
        }

        displayListBill();
    }//GEN-LAST:event_btnCreateBillActionPerformed

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
        }
        if (isValid) {
            txtBillCurWater.setBackground(Color.white);
            prepareBill();
        }
    }//GEN-LAST:event_txtBillCurWaterFocusLost

    private void txtBillCurElectKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBillCurElectKeyReleased
        // TODO add your handling code here:
        try {
            long test = Long.parseLong(txtBillCurElect.getText());
        } catch (NumberFormatException e) {
            txtBillCurElect.setText("");
        }
    }//GEN-LAST:event_txtBillCurElectKeyReleased

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
        }
        if (isValid) {
            txtBillCurElect.setBackground(Color.white);
            prepareBill();
        }
    }//GEN-LAST:event_txtBillCurElectFocusLost

    private void datePickFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_datePickFocusLost
        // TODO add your handling code here:\
    }//GEN-LAST:event_datePickFocusLost

    private void btnAddContractActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddContractActionPerformed
        if (!gListModel.isEmpty()) {
            gListModel.stream().map(g -> gController.addGuest(g));

            //test with admin account (id=1) will change later to a variable base on logged in account id
            if (cController.addContract(rController.getRoom(roomNum.getText()), gListModel, user.getId())) {
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

    private void btnAddGuestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddGuestActionPerformed
        addRegisterGuestList();
        displayRegisterGuestList();
    }//GEN-LAST:event_btnAddGuestActionPerformed

    private void txtCCCDKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCCCDKeyReleased
        // TODO add your handling code here:
        if (txtCCCD.getText().length() >= 10) {
            Guest g = gController.searchExistGuest(txtCCCD.getText());
            if (g != null) {
                txtFullName.setText(g.getFullName());
                txtPhone.setText(g.getPhone());
                datePick.setDate(g.getDateOfBirth());
                txtExistGuestNotify.setText("Khách hàng " + g.getFullName() + " đã có trong hệ thống.");
            }

        }
    }//GEN-LAST:event_txtCCCDKeyReleased

    private void btnLiquidateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLiquidateActionPerformed
        int ans = JOptionPane.showConfirmDialog(null, "Bạn có muốn thanh lý hợp đồng hiện tại?", "Thanh lý hợp đồng", JOptionPane.YES_NO_OPTION);
        switch (ans) {
            case 0:
                if (!gListDTO.isEmpty()) {
                    rController.updateRoomStatus(currentRoom.getRoomNumber(), 1);
                    gListDTO.stream().forEach(g -> {
                        gController.updateGuestStatus(g.getId(), 0);
                        cController.updateContractDetail(cController.getCurrentContract(currentRoom.getId()), g.getId(), 0);
                    });
                    if (cController.updateContractStatus(cController.getCurrentContract(currentRoom.getId()), 0)) {
                        JOptionPane.showMessageDialog(null, "Hợp đồng đã được thanh lý.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                break;
            case 1:
                //                System.out.println("Không xóa");
                break;
        }
        displayCurrentGuest();
        displayRoomInfo();
        displayContractsList(currentRoom.getId());
    }//GEN-LAST:event_btnLiquidateActionPerformed

    private void txtWaterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtWaterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtWaterActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        updateRoom();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void txtSearchBillKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchBillKeyReleased
        // TODO add your handling code here:
        billListTableModel.setRowCount(0);
        List<BillDTO> searchList = bController.searchBillDTOs(currentRoom.getId(), txtSearchBill.getText());
        searchList.forEach(b -> {
            billListTableModel.addRow(new Object[]{
                b.getBillNumber(),
                b.getTotal(),
                b.getSttDescription(),
                b.getContractNumber(),
                b.getUserName()
            });
        });
    }//GEN-LAST:event_txtSearchBillKeyReleased

    private void tbContractListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbContractListMouseClicked
        // TODO add your handling code here:
        int row = tbContractList.getSelectedRow();
        if (row >= 0) {
            ContractDTO selectedContract = contractList.stream()
                    .filter(c -> c.getContractNumber().equals(tbContractList.getValueAt(row, 1)))
                    .findFirst().orElse(null);
            displayGuestsInContract(selectedContract.getId());
        }
    }//GEN-LAST:event_tbContractListMouseClicked

    public void displayGuestsInContract(int contractId) {
        List<GuestDTO> guestList = gController.getGuestsByContract(contractId);
        if (guestList != null) {
            tbGuestInContractListModel.setRowCount(0);
            guestList.forEach(g -> {
                tbGuestInContractListModel.addRow(new Object[]{
                    tbGuestInContractListModel.getRowCount() + 1,
                    g.getFullName(),
                    g.getPhone(),
                    g.getCitizenId(),
                    g.getDateOfBirth()
                });

            });
        }
    }

    public void displayCurrentGuest() {
        currentGuestTableModel.setRowCount(0);
        gListDTO = GuestController.displayCurrentGuestDTO(roomNum.getText(), 1);
        gListDTO.stream().forEach(g -> {
            currentGuestTableModel.addRow(new String[]{
                String.valueOf(currentGuestTableModel.getRowCount() + 1),
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
            boolean res = RoomController.updateRoom(roomNumber, price, square, description, elec, water);
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
        String errName = !validateName(name) ? "Tên không được để trống và có ít hơn 50 ký tự.\n" : "";
        String errCCCD = !validateCCCD(cccd) ? "Căn cước công dân phải có từ 9-12 chữ số.\n" : "";
        String errPhone = !validatePhone(phone) ? "Số điện thoại phải có 10 chữ số.\n" : "";
        String errDate = dob == null ? "Ngày không hợp lệ.\n" : "";
        String errDup = "";
        if (!gListModel.isEmpty() && gListModel.stream().filter(g -> g.getCitizenId().equals(cccd)).findFirst().isPresent()) {
            errDup = "Đã có thông tin khách hàng này trong danh sách khởi tạo.";
        }
        String errMessage = errName + errCCCD + errPhone + errDate + errDup;
        if (!errMessage.isEmpty()) {
            JOptionPane.showMessageDialog(null, errMessage, "Lỗi", JOptionPane.ERROR_MESSAGE);
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
//        if (!list.isEmpty()) {
//            JOptionPane.showMessageDialog(null, list.get(0).getFullName());
//        }
        list.stream().forEach(g -> {
            registerGuestTableModel.addRow(new String[]{
                String.valueOf(registerGuestTableModel.getRowCount() + 1),
                g.getFullName(),
                g.getDateOfBirth(),
                g.getCitizenId(),
                g.getPhone()
            });
        });
    }

    private void displayServiceList() {
        serviceList = serviceController.displayService();
//        if (serviceList != null) {
//            serviceListTableModel.setRowCount(0);
//            serviceList.stream().forEach(s -> {
//                serviceListTableModel.addRow(new String[]{
//                    s.getServiceName(),
//                    s.getPrice() + "/" + s.getUnit()
//                }
//                );
//            }
//            );
//        }
    }

    private void displayRentalStatus() {
        int contractId = cController.getCurrentContract(currentRoom.getId());
        int countMonth = 0;
        int countPayment = 0;
//        System.out.println(contractId);
        if (contractId > 0) {
            countMonth = bController.countMonth(contractId);
            countPayment = bController.sumRentalMonth(contractId);
            System.out.println("Month stay: " + countMonth);
            System.out.println("Month payment: " + countPayment);
            System.out.println("Month need to pay: " + (countMonth - countPayment));
        }
    }

    private void displayListBill() {
        billListTableModel.setRowCount(0);
        List<BillDTO> lBillDTO = bController.getBillByRoom(currentRoom.getId());
        lBillDTO.stream().forEach(b -> {
            billListTableModel.addRow(new Object[]{
                b.getBillNumber(),
                b.getTotal(),
                b.getSttDescription(),
                b.getContractNumber(),
                b.getUserName()
            });
        });
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
    private javax.swing.JButton btnBillPayment;
    private javax.swing.JButton btnCreateBill;
    private javax.swing.JButton btnLiquidate;
    private javax.swing.JButton btnUpdate;
    private com.toedter.calendar.JDateChooser datePick;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
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
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane2;
    public javax.swing.JLabel roomNum;
    private javax.swing.JTable tbBillDetail;
    private javax.swing.JTable tbBillDetailView;
    private javax.swing.JTable tbBillList;
    private javax.swing.JTable tbContractList;
    private javax.swing.JTable tbCurrentGuest;
    private javax.swing.JTable tbGuestInContractList;
    private javax.swing.JTable tbRegisterGuest;
    public javax.swing.JLabel titleRoomNo;
    private javax.swing.JTextField txtBillCurElect;
    private javax.swing.JTextField txtBillCurWater;
    private javax.swing.JLabel txtBillDateView;
    private javax.swing.JComboBox<String> txtBillMonth;
    private javax.swing.JLabel txtBillNumberView;
    private javax.swing.JTextField txtBillRoomPrice;
    private javax.swing.JComboBox<String> txtBillYear;
    private javax.swing.JTextField txtCCCD;
    public javax.swing.JTextArea txtDescription;
    private javax.swing.JTextField txtElectContract;
    public javax.swing.JTextField txtElectric;
    private javax.swing.JLabel txtExistGuestNotify;
    private javax.swing.JTextField txtFullName;
    private javax.swing.JTextField txtLastECountBill;
    private javax.swing.JTextField txtLastWCountBill;
    private javax.swing.JTextField txtPhone;
    public javax.swing.JTextField txtPrice;
    private javax.swing.JLabel txtRoomNoView;
    private javax.swing.JTextField txtSearchBill;
    public javax.swing.JTextField txtSquare;
    private javax.swing.JLabel txtTotalBillView;
    public javax.swing.JTextField txtWater;
    private javax.swing.JTextField txtWaterContract;
    // End of variables declaration//GEN-END:variables

}
