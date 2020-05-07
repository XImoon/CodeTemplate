/*
 * Created by JFormDesigner on Mon Apr 27 11:21:44 CST 2020
 */

package cn.ximoon.plugin.ui.search;

import cn.ximoon.plugin.DBUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;

/**
 * @author Brainrain
 */
public class SearchForm extends JPanel implements KeyListener{

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel panel1;
    private JTextField mInput;
    private JLabel mClear;
    private JLabel mSetting;
    private JPanel panel2;
    private JScrollPane scrollPane1;
    private JList mResList;
    private JLayeredPane mLayerPane;
    private JScrollPane mDetailPane;
    private JTextArea mResDetail;
    private JLabel mEdit;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
    private ActionListener mListener;

    public SearchForm() {
        initComponents();
        initAction();
    }

    public SearchForm(ActionListener listener) {
        mListener = listener;
        initComponents();
        initAction();
    }

    private void initAction() {
        mResList.setCellRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

                if(value instanceof Map){
                    Map<String, String> map = (Map<String, String>) value;
                    return super.getListCellRendererComponent(list, map.get("fkey_words"), index, isSelected, cellHasFocus);
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
        mResList.setModel(DBUtil.getInstance().queryAll());
        mInput.getDocument().addDocumentListener(new DocumentListener() {


            @Override
            public void insertUpdate(DocumentEvent e) {
                String key = mInput.getText();
                mResList.setModel(DBUtil.getInstance().queryKey(key));
                mResDetail.setText("");
                mResList.setSelectedIndex(0);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String key = mInput.getText();
                mResList.setModel(DBUtil.getInstance().queryKey(key));
                mResDetail.setText("");
                mResList.setSelectedIndex(0);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                String key = mInput.getText();
                mResList.setModel(DBUtil.getInstance().queryKey(key));
            }
        });
        mResList.addListSelectionListener(e -> {
            if (mResList.getSelectedValue() instanceof Map) {
                Map<String, String> map = (Map<String, String>) mResList.getSelectedValue();
                if (null != map) {
                    String detail = map.get("fdetail");
                    if (null != detail || 0 != detail.length()) {
                        mResDetail.setText(detail);
                    } else {
                        mResDetail.setText("");
                    }
                }
            } else {
                mResDetail.setText(String.valueOf(mResList.getSelectedValue()));
            }
        });
        mResList.setSelectedIndex(0);
        mResList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (mResList.getSelectedIndex() != -1) {
                    if (e.getClickCount() == 2) {
                        mListener.complete((Map<String, String>) mResList.getModel().getElementAt(mResList.getSelectedIndex()));
                    } else {
                        super.mouseClicked(e);
                    }
                }
            }
        });
        mClear.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mInput.setText("");
            }
        });
        mSetting.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mListener.setting();
            }
        });
        addComponentListener(new ComponentAdapter() {

            final Point origin = new Point(0, 0);

            @Override
            public void componentResized(ComponentEvent e) {
                System.out.println(mLayerPane.getSize()); //此处必须用getSize()而不是用getPreferredSize()
                Dimension dimensionNew = mLayerPane.getSize();
                Rectangle rectangleNew = new Rectangle(origin, dimensionNew);
                mDetailPane.setBounds(rectangleNew);
                mResDetail.setBounds(rectangleNew);
                Point point = new Point(dimensionNew.width - mEdit.getSize().width - 20, dimensionNew.height - mEdit.getSize().height - 20);
                Rectangle editRectangle = new Rectangle(point, mEdit.getSize());
                mEdit.setBounds(editRectangle);
            }
        });
        mEdit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
//                JOptionPane.showConfirmDialog(null, "修改", "修改", JOptionPane.DEFAULT_OPTION);
                mListener.edit((Map<String, String>) mResList.getModel().getElementAt(mResList.getSelectedIndex()));
            }
        });
        mInput.addKeyListener(this);
    }

    public void inputRequest() {
        mInput.requestFocus();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        panel1 = new JPanel();
        mInput = new JTextField();
        mClear = new JLabel();
        mSetting = new JLabel();
        panel2 = new JPanel();
        scrollPane1 = new JScrollPane();
        mResList = new JList();
        mLayerPane = new JLayeredPane();
        mDetailPane = new JScrollPane();
        mResDetail = new JTextArea();
        mEdit = new JLabel();

        //======== this ========
        setMinimumSize(new Dimension(550, 400));
        setPreferredSize(new Dimension(550, 400));
        setFocusable(false);
        setLayout(new MigLayout(
            "fill,insets 5,hidemode 3,gap 2 4",
            // columns
            "[fill]" +
            "[grow,fill]",
            // rows
            "[]" +
            "[grow]"));

        //======== panel1 ========
        {
            panel1.setFocusable(false);
            panel1.setLayout(new MigLayout(
                "fill,hidemode 3",
                // columns
                "[fill]" +
                "[50!,center]",
                // rows
                "[]"));

            //---- mInput ----
            mInput.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
            panel1.add(mInput, "cell 0 0");

            //---- mClear ----
            mClear.setIcon(new ImageIcon(getClass().getResource("/clear.png")));
            mClear.setFocusable(false);
            panel1.add(mClear, "cell 1 0,height 45%");

            //---- mSetting ----
            mSetting.setIcon(new ImageIcon(getClass().getResource("/setting.png")));
            mSetting.setFocusable(false);
            panel1.add(mSetting, "cell 1 0,height 45%");
        }
        add(panel1, "cell 0 0 2 1");

        //======== panel2 ========
        {
            panel2.setFocusable(false);
            panel2.setEnabled(false);
            panel2.setLayout(new MigLayout(
                "fill,hidemode 3,gap 2 4",
                // columns
                "1%[18.5%]1%" +
                "[78.5%]1%",
                // rows
                "[]"));

            //======== scrollPane1 ========
            {

                //---- mResList ----
                mResList.setFocusable(false);
                mResList.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
                scrollPane1.setViewportView(mResList);
            }
            panel2.add(scrollPane1, "cell 0 0,grow");

            //======== mLayerPane ========
            {
                mLayerPane.setEnabled(false);
                mLayerPane.setFocusable(false);
                mLayerPane.setRequestFocusEnabled(false);
                mLayerPane.setVerifyInputWhenFocusTarget(false);
                mLayerPane.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));

                //======== mDetailPane ========
                {
                    mDetailPane.setFocusable(false);

                    //---- mResDetail ----
                    mResDetail.setEditable(false);
                    mResDetail.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
                    mDetailPane.setViewportView(mResDetail);
                }
                mLayerPane.add(mDetailPane, JLayeredPane.DEFAULT_LAYER);
                mDetailPane.setBounds(new Rectangle(new Point(0, 0), mDetailPane.getPreferredSize()));

                //---- mEdit ----
                mEdit.setIcon(new ImageIcon(getClass().getResource("/modify.png")));
                mLayerPane.add(mEdit, JLayeredPane.PALETTE_LAYER);
                mEdit.setBounds(new Rectangle(new Point(260, 210), mEdit.getPreferredSize()));
            }
            panel2.add(mLayerPane, "cell 1 0,grow");
        }
        add(panel2, "cell 0 1 2 1,grow");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            if (mResList.getSelectedIndex() != -1) {
                mListener.complete((Map<String, String>) mResList.getModel().getElementAt(mResList.getSelectedIndex()));
            }
        } else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
            int index = mResList.getSelectedIndex();
            if (index < mResList.getModel().getSize() - 1) {
                mResList.setSelectedIndex(index + 1);
            }

        }else if(e.getKeyCode() == KeyEvent.VK_UP) {
            int index = mResList.getSelectedIndex();
            if (index > 0) {
                mResList.setSelectedIndex(index - 1);
            }
        }
    }

    public interface ActionListener{
        void edit(Map<String, String> result);
        void setting();
        void complete(Map<String, String> result);
    }
}
