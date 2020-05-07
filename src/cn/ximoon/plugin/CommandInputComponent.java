package cn.ximoon.plugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;

public class CommandInputComponent extends DialogWrapper {

    private JPanel mRoot;
    private JTextField mTextField;
    private JLabel mLabel;
    private KeyListener mListener;

    protected CommandInputComponent(@Nullable Project project, KeyListener listener) {
        super(project);
        this.mListener = listener;
        initComponent();
        init();
    }

    private void initComponent() {
        mRoot = new JPanel();
        mRoot.setLayout(new GridLayout(2, 1));
        mLabel = new JLabel("Please input command: ");
        mLabel.setFont(new Font("微软雅黑", Font.PLAIN, 26)); //字体样式
        mLabel.setHorizontalAlignment(SwingConstants.CENTER); //水平居中
        mLabel.setVerticalAlignment(SwingConstants.CENTER); //垂直居中
        mTextField = new JTextField();
        mRoot.add(mTextField);
        mTextField.addKeyListener(mListener);
    }

    // 重写下面的方法，返回一个自定义的swing样式，该样式会展示在会话框的最上方的位置
    @Override
    protected JComponent createNorthPanel() {
        return mLabel;
    }
 
    // 重写下面的方法，返回一个自定义的swing样式，该样式会展示在会话框的最下方的位置
    @Override
    protected JComponent createSouthPanel() {
        return null;
    }
 
    // 重写下面的方法，返回一个自定义的swing样式，该样式会展示在会话框的中央位置
    @Override
    protected JComponent createCenterPanel() {
        return mTextField;
    }

    public String getText(){
        return mTextField.getText();
    }
}