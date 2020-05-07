package cn.ximoon.plugin;

import cn.ximoon.plugin.ui.edit.EditDialog;
import cn.ximoon.plugin.ui.search.SearchForm;
import com.intellij.ide.ui.UISettings;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.openapi.wm.impl.IdeFrameImpl;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Locale;
import java.util.Map;

public class CodeTemplateAction extends AnAction implements SearchForm.ActionListener {

    private Editor mEditor;
    private SearchForm mCommView;
    private JBPopup mPopup;
    private Project mProject;

    @Override
    public void actionPerformed(AnActionEvent e) {
        mEditor = e.getData(DataKeys.EDITOR);
        if (mEditor != null) {
            //得到编辑器的光标类
            mProject = e.getProject();
            show();
        }
    }

    public void show() {
        mCommView = new SearchForm(this);
        JBPopupFactory factory = JBPopupFactory.getInstance();
        showSearchView(factory);
    }

    private void showSearchView(JBPopupFactory factory) {
        RelativePoint showPoint = getRelativePoint(mProject);
        mPopup = factory.createComponentPopupBuilder(mCommView, null)
                .setRequestFocus(true)
                .setResizable(true)
                .setMayBeParent(true)
                .setMinSize(new Dimension(540, 400))
                .setMovable(true)
                .setProject(mProject)
                .createPopup();
        mPopup.show(showPoint);
        Locale.setDefault(Locale.ENGLISH);
        System.setProperty("user.language","en");
        mCommView.inputRequest();
    }

    @NotNull
    private RelativePoint getRelativePoint(Project project) {
        RelativePoint showPoint;
        Component parent = UIUtil.findUltimateParent(WindowManager.getInstance().suggestParentWindow(project));
        if (parent != null) {
            int height = UISettings.getInstance().SHOW_MAIN_TOOLBAR ? 135 : 115;
            if (parent instanceof IdeFrameImpl && ((IdeFrameImpl) parent).isInFullScreen()) {
                height -= 20;
            }
            showPoint = new RelativePoint(parent, new Point((parent.getSize().width - mCommView.getPreferredSize().width) / 2, height));
        } else {
            showPoint = JBPopupFactory.getInstance().guessBestPopupLocation(mEditor);
        }
        return showPoint;
    }

    @Override
    public void edit(Map<String, String> result) {
        mPopup.cancel();
        EditDialog dialog = new EditDialog(WindowManager.getInstance().suggestParentWindow(mProject));
        dialog.setData(result);
        dialog.pack();
        dialog.setVisible(true);
    }

    @Override
    public void setting() {
        EditDialog dialog = new EditDialog(WindowManager.getInstance().suggestParentWindow(mProject));
        dialog.pack();
        dialog.setVisible(true);
    }

    @Override
    public void complete(Map<String, String> result) {
        mPopup.cancel();
        WriteCommandAction.runWriteCommandAction(mProject, () -> {
            SelectionModel selectionModel = mEditor.getSelectionModel();
            CaretModel caretModel = mEditor.getCaretModel();
            int offset;
            String param = StringUtil.checkEmpty(result.get("fparam"));
            Document document = mEditor.getDocument();
            boolean isSelect;
            String replaceText = StringUtil.checkEmpty(selectionModel.getSelectedText());
            if (0 == param.length()) {
                isSelect = false;
                if (0 == replaceText.length()) {
                    offset = caretModel.getOffset();
                } else {
                    offset = selectionModel.getSelectionEnd();
                }
            } else {
                if (0 == replaceText.length()) {
                    isSelect = false;
                    offset = caretModel.getOffset();
                } else {
                    isSelect = true;
                    offset = selectionModel.getSelectionStart();
                }
            }
            if (isSelect) {
                document.deleteString(offset, selectionModel.getSelectionEnd());
                document.insertString(offset, result.get("fdetail").replaceAll("\\b" + param + "(?!\")\\b", replaceText));
            } else {
                document.insertString(offset, result.get("fdetail"));
            }

        });
    }
}
