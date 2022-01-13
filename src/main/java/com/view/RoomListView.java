/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.view;

import com.controller.RoomController;
import com.controller.dto.RoomDTO;
import com.controller.dto.UserDTO;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author hadt2
 */
public final class RoomListView extends javax.swing.JFrame {

    UserDTO user = new UserDTO();
    DefaultTableModel tableModel;
    RoomDTO room;
    RoomView roomView;
    CreateRoomView createRoomView;

    /**
     * Creates new form MainFrame
     *
     * @param user
     */
    public RoomListView(UserDTO user) {
        initComponents();
        this.user = user;
        tableModel = (DefaultTableModel) tbRoom.getModel();
        loadRoomDTO();
    }

    public RoomListView() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of
     * this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tbRoom = new javax.swing.JTable();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        btnRoomView = new javax.swing.JButton();
        ckBoxAll = new javax.swing.JCheckBox();
        btnNewRoomForm = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Danh sách phòng");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        tbRoom.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Số phòng", "Diện tích (m2)", "Mô tả", "Giá thuê (VND/tháng)", "Trạng thái"
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
        tbRoom.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tbRoom);
        if (tbRoom.getColumnModel().getColumnCount() > 0) {
            tbRoom.getColumnModel().getColumn(0).setMinWidth(45);
            tbRoom.getColumnModel().getColumn(0).setPreferredWidth(70);
            tbRoom.getColumnModel().getColumn(0).setMaxWidth(80);
            tbRoom.getColumnModel().getColumn(1).setMinWidth(120);
            tbRoom.getColumnModel().getColumn(1).setPreferredWidth(120);
            tbRoom.getColumnModel().getColumn(1).setMaxWidth(120);
            tbRoom.getColumnModel().getColumn(3).setMinWidth(130);
            tbRoom.getColumnModel().getColumn(3).setPreferredWidth(130);
            tbRoom.getColumnModel().getColumn(3).setMaxWidth(130);
            tbRoom.getColumnModel().getColumn(4).setMinWidth(120);
            tbRoom.getColumnModel().getColumn(4).setPreferredWidth(120);
            tbRoom.getColumnModel().getColumn(4).setMaxWidth(120);
        }

        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });

        btnSearch.setText("Tìm kiếm");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        btnRoomView.setText("Quản lý phòng");
        btnRoomView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRoomViewActionPerformed(evt);
            }
        });

        ckBoxAll.setText("Chỉ hiển thị phòng còn trống");
        ckBoxAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckBoxAllActionPerformed(evt);
            }
        });

        btnNewRoomForm.setText("Thêm phòng");
        btnNewRoomForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewRoomFormActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 102, 204));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("DANH SÁCH PHÒNG");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 915, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(14, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(ckBoxAll)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnNewRoomForm, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnRoomView)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSearch)
                        .addGap(18, 18, 18))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch)
                    .addComponent(ckBoxAll)
                    .addComponent(btnNewRoomForm)
                    .addComponent(btnRoomView))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRoomViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRoomViewActionPerformed
        try {
            int row = tbRoom.getSelectedRow();
            tableModel = (DefaultTableModel) tbRoom.getModel();
            String roomNo = tableModel.getValueAt(row, 0).toString();
            roomView = new RoomView(this, this.user, roomNo);
            roomView.setVisible(true);
            roomView.pack();
            roomView.setLocationRelativeTo(null);
            roomView.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            roomView.roomNum.setText(roomNo);
            roomView.titleRoomNo.setText(roomNo);
            roomView.displayRoomInfo();
            roomView.displayCurrentGuest();
            this.setVisible(false);
            //Không cho phép tự ý sửa số điện nước:
            roomView.txtElectric.setEditable(false);
            roomView.txtWater.setEditable(false);

        } catch (ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn phòng để xem thông tin.", "Thông báo", 1);
        }

    }//GEN-LAST:event_btnRoomViewActionPerformed

    private void ckBoxAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckBoxAllActionPerformed
        loadRoomDTO();
    }//GEN-LAST:event_ckBoxAllActionPerformed

    private void btnNewRoomFormActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewRoomFormActionPerformed
        if (user.getAuthority() == 0) {
            JOptionPane.showMessageDialog(null, "Tài khoản của bạn không có quyền thực hiện chức năng này.", "Cảnh báo", JOptionPane.ERROR_MESSAGE);
        } else {
            createRoomView = new CreateRoomView();
            createRoomView.setVisible(true);
            createRoomView.txtRoomNumber.setText("");
            createRoomView.txtPrice.setText("");
            createRoomView.txtRoomSquare.setText("");
            createRoomView.txtRoomDescription.setText("");
            createRoomView.pack();
            createRoomView.setLocationRelativeTo(null);
            createRoomView.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        }

    }//GEN-LAST:event_btnNewRoomFormActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        search(txtSearch.getText(), !ckBoxAll.isSelected());
    }//GEN-LAST:event_btnSearchActionPerformed

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        search(txtSearch.getText(), !ckBoxAll.isSelected());
    }//GEN-LAST:event_txtSearchKeyReleased

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        loadRoomDTO();
    }//GEN-LAST:event_formWindowActivated
    static List<RoomDTO> rList = new ArrayList<>();

    public void loadRoomDTO() {
        tableModel.setRowCount(0);
        rList = RoomController.displayRooms(!ckBoxAll.isSelected());
        rList.stream().forEach(r -> {
            tableModel.addRow(new String[]{
                r.getRoomNumber(),
                r.getSquare(),
                r.getDescription(),
                r.getPrice(),
                r.getSttDescription()
            });
        });
    }

    public void search(String key, boolean allCheck) {
        tableModel.setRowCount(0);
        rList = RoomController.searchRoom(key, allCheck);
        rList.stream().forEach(r -> {
            tableModel.addRow(new String[]{
                r.getRoomNumber(),
                r.getSquare(),
                r.getDescription(),
                r.getPrice(),
                r.getSttDescription()
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
            java.util.logging.Logger.getLogger(RoomListView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RoomListView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RoomListView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RoomListView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new RoomListView().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNewRoomForm;
    private javax.swing.JButton btnRoomView;
    private javax.swing.JButton btnSearch;
    private javax.swing.JCheckBox ckBoxAll;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbRoom;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
