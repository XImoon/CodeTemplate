/*
 * Created by JFormDesigner on Mon Apr 27 17:19:16 CST 2020
 */

package cn.ximoon.plugin.ui.edit;

import cn.ximoon.plugin.DBUtil;
import cn.ximoon.plugin.StringUtil;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.VerticalLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Brainrain
 */
public class EditDialog extends JDialog {

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel label1;
    private JTextField mKey;
    private JLabel label2;
    private JScrollPane scrollPane1;
    private JTextArea mDescription;
    private JLabel label3;
    private JTextField mParam;
    private JLabel label5;
    private JTextField mTag;
    private JLabel label4;
    private JPanel panel1;
    private JButton mCancel;
    private JButton mSave;
    private JScrollPane scrollPane2;
    private JTextArea mDetail;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
    private Map<String, String> mData;

    public EditDialog(Window owner) {
        super(owner);
        initComponents();
        initAction();
    }

    private void initAction() {
        if (null == mData){
            mData = new HashMap<>();
        }
        mSave.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                DBUtil.getInstance().save(getData());
                EditDialog.this.setVisible(false);
            }
        });
        mCancel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                EditDialog.this.setVisible(false);
            }
        });
    }

    public void setData(Map<String, String> map){
        mData = map;
        mKey.setText(StringUtil.checkEmpty(map.get("fkey_words")));
        mDescription.setText(StringUtil.checkEmpty(map.get("fdescription")));
        mTag.setText(StringUtil.checkEmpty(map.get("ftag")));
        mParam.setText(StringUtil.checkEmpty(map.get("fparam")));
        mDetail.setText(StringUtil.checkEmpty(map.get("fdetail")));
    }

    private Map<String, String> getData(){
        mData.put("fkey_words", mKey.getText());
        mData.put("fdescription", mDescription.getText());
        mData.put("fparam", mParam.getText());
        mData.put("fdetail", mDetail.getText());
        mData.put("ftag", mTag.getText());
        return mData;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label1 = new JLabel();
        mKey = new JTextField();
        label2 = new JLabel();
        scrollPane1 = new JScrollPane();
        mDescription = new JTextArea();
        label3 = new JLabel();
        mParam = new JTextField();
        label5 = new JLabel();
        mTag = new JTextField();
        label4 = new JLabel();
        panel1 = new JPanel();
        mCancel = new JButton();
        mSave = new JButton();
        scrollPane2 = new JScrollPane();
        mDetail = new JTextArea();

        //======== this ========
        setResizable(false);
        setMinimumSize(new Dimension(550, 600));
        setName("EditDialog");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setPreferredSize(new Dimension(550, 600));
            dialogPane.setMinimumSize(new Dimension(129, 600));
            dialogPane.setLayout(new BorderLayout(4, 0));

            //======== contentPanel ========
            {
                contentPanel.setBorder(new EmptyBorder(5, 5, 5, 0));
                contentPanel.setLayout(new VerticalLayout(8));

                //---- label1 ----
                label1.setText("\u5173\u952e\u8bcd\u6c47\uff1a");
                contentPanel.add(label1);

                //---- mKey ----
                mKey.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
                contentPanel.add(mKey);

                //---- label2 ----
                label2.setText("\u4ee3\u7801\u63cf\u8ff0\uff1a");
                contentPanel.add(label2);

                //======== scrollPane1 ========
                {

                    //---- mDescription ----
                    mDescription.setRows(2);
                    mDescription.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
                    scrollPane1.setViewportView(mDescription);
                }
                contentPanel.add(scrollPane1);

                //---- label3 ----
                label3.setText("\u66ff\u6362\u53c2\u6570\uff1a");
                contentPanel.add(label3);

                //---- mParam ----
                mParam.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
                contentPanel.add(mParam);

                //---- label5 ----
                label5.setText("\u4ee3\u7801\u6807\u7b7e\uff1a");
                contentPanel.add(label5);

                //---- mTag ----
                mTag.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
                contentPanel.add(mTag);

                //---- label4 ----
                label4.setText("\u5b8c\u6574\u4ee3\u7801\uff1a");
                contentPanel.add(label4);
            }
            dialogPane.add(contentPanel, BorderLayout.NORTH);

            //======== panel1 ========
            {
                panel1.setLayout(new MigLayout(
                    "fill,hidemode 3",
                    // columns
                    "[fill]" +
                    "[fill]",
                    // rows
                    "[]"));

                //---- mCancel ----
                mCancel.setText("\u53d6\u6d88");
                mCancel.setActionCommand("cancel");
                panel1.add(mCancel, "cell 0 0");

                //---- mSave ----
                mSave.setText("\u4fdd\u5b58");
                mSave.setActionCommand("save");
                panel1.add(mSave, "cell 1 0");
            }
            dialogPane.add(panel1, BorderLayout.SOUTH);

            //======== scrollPane2 ========
            {
                scrollPane2.setBorder(new EmptyBorder(0, 5, 5, 5));

                //---- mDetail ----
                mDetail.setRows(15);
                mDetail.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
                scrollPane2.setViewportView(mDetail);
            }
            dialogPane.add(scrollPane2, BorderLayout.CENTER);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }


}
