package de.qx.game.omikron.client.bezier;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import de.qx.game.omikron.client.bezier.editor.*;
import de.qx.game.omikron.client.bezier.file.BezierFileController;
import de.qx.game.omikron.client.bezier.file.BezierFileModel;
import de.qx.game.omikron.client.bezier.list.BezierPathListController;
import de.qx.game.omikron.client.bezier.list.BezierPathListEntry;
import de.qx.game.omikron.client.bezier.list.BezierPathListModel;
import de.qx.game.omikron.datatype.Vector2f;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

/**
 * Date: 05.06.13
 * Time: 07:08
 */
public class BezierEditor implements DrawCallback {
    private ImagePanel imagePanel;
    private JPanel menuPanel;
    private JPanel editorPanel;
    private JButton openFileButton;
    private JList pathList;
    private JButton removePathButton;
    private JButton addPathButton;
    private JButton savePathListButton;
    private JButton saveAsButton;
    private JTextField pathIdentifierTextField;
    private JButton addCurveButton;
    private JButton removeCurve;
    private JLabel filenameLabel;
    private JButton newPathListButton;
    private JButton duplicateButton;
    private JButton mirrorHorizontalButton;
    private JButton mirrorVerticalButton;

    private JFileChooser openFileChooser;

    private BezierRenderer renderer;
    private PrimitiveRenderer primitiveRenderer;

    private int width = 650;
    private int height = 800;

    private Vector2f smallWindowPosition = new Vector2f((250 - 200) / 2, (420 - 341) / 2);
    private Vector2f smallWindowSize = new Vector2f(200, 341);

    private Vector2f bigWindowPosition = new Vector2f((250 - 320) / 2, (420 - 480) / 2);
    private Vector2f bigWindowSize = new Vector2f(320, 480);

    private Vector2f virtualWindowPosition = new Vector2f((width - 250) / 2, (height - 420) / 2);
    private Vector2f virtualWindowSize = new Vector2f(250, 420);


    private BezierEditorController editorController;
    private BezierEditorModel editorModel;

    private BezierPathListModel pathListModel;
    private SelectionInList<BezierPathListEntry> pathListSelectionInList;
    private BezierPathListController pathListController;

    private PresentationModel<BezierPathListEntry> detailModel;

    private BezierFileController fileController;
    private BezierFileModel fileModel;

    public BezierEditor() {
        editorController = new BezierEditorController();
        editorModel = editorController.getModel();

        primitiveRenderer = new PrimitiveRenderer(width, height, virtualWindowPosition);
        renderer = new BezierRenderer(editorModel, primitiveRenderer);

        pathListModel = new BezierPathListModel();
        pathListController = new BezierPathListController(pathListModel);
        pathListSelectionInList = new SelectionInList<BezierPathListEntry>(pathListModel.getEntries());

        fileController = new BezierFileController(pathListModel);
        fileModel = fileController.getModel();

        $$$setupUI$$$();

        final BezierDragAndDropListener mouseListener = new BezierDragAndDropListener(editorController, editorModel, this, imagePanel, virtualWindowPosition);
        imagePanel.addMouseListener(mouseListener);
        imagePanel.addMouseMotionListener(mouseListener);

        imagePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent event) {
                switch (event.getKeyCode()) {
                    case KeyEvent.VK_Q:
                        editorController.addCurve();
                        break;
                    case KeyEvent.VK_W:
                        editorController.removeCurve();
                        break;
                    case KeyEvent.VK_ADD:
                        renderer.increasePoints();
                        break;
                    case KeyEvent.VK_SUBTRACT:
                        renderer.decreasePoints();
                        break;
                    case KeyEvent.VK_S:
                        editorController.exportPath(Vector2f.ZERO);
                        break;
                    default:
                        break;
                }

                paintIt();
            }
        });

        paintIt();

        addPathButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pathListController.addNewPath();
            }
        });
        removePathButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!pathListSelectionInList.isSelectionEmpty()) {
                    pathListController.removePath(pathListSelectionInList.getSelectionIndex());
                }
            }
        });
        savePathListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileController.save(pathListModel.getEntries());
            }
        });
        saveAsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = openFileChooser.showSaveDialog(saveAsButton);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    final File file = openFileChooser.getSelectedFile();

                    fileModel.setFile(file);

                    fileController.save(pathListModel.getEntries());
                }
            }
        });
        addCurveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorController.addCurve();
                paintIt();
            }
        });
        removeCurve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorController.removeCurve();
                paintIt();
            }
        });
        openFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = openFileChooser.showOpenDialog(openFileButton);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = openFileChooser.getSelectedFile();
                    fileController.open(file);
                }
            }
        });
        duplicateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!pathListSelectionInList.isSelectionEmpty()) {
                    pathListController.duplicatePath(pathListSelectionInList.getSelectionIndex());
                }
            }
        });
        mirrorHorizontalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorController.mirrorHorizontal(virtualWindowSize.x() / 2f);
                paintIt();
            }
        });
        mirrorVerticalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorController.mirrorVertical();
                paintIt();
            }
        });
    }

    @Override
    public void paintIt() {
        final int[] imagedata = imagePanel.getImageData();

        for (int i = 0; i < imagedata.length; i++) {
            imagedata[i] = 0;
        }

        primitiveRenderer.drawRect(imagedata, bigWindowPosition, bigWindowSize, 0xAAAAAA);
        primitiveRenderer.drawRect(imagedata, smallWindowPosition, smallWindowSize, 0xAAAAAA);
        primitiveRenderer.drawRect(imagedata, Vector2f.ZERO, virtualWindowSize, 0xAA1111);

        renderer.render(imagedata);

        imagePanel.repaint();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignore) {
        }

        JFrame frame = new JFrame("BezierEditor");
        frame.setContentPane(new BezierEditor().editorPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        imagePanel = new ImagePanel(new Dimension(width, height));

        openFileChooser = new JFileChooser();
        //openFileChooser.show
        openFileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f != null && (f.getName().endsWith("bezier") || f.isDirectory());
            }

            @Override
            public String getDescription() {
                return "bezier editor files";
            }
        });
        final PresentationModel<BezierFileModel> filePresentationModel = new PresentationModel<BezierFileModel>(fileModel);
        filenameLabel = BasicComponentFactory.createLabel(filePresentationModel.getModel(BezierFileModel.FILENAME));


        pathList = BasicComponentFactory.createList(pathListSelectionInList);

        detailModel = new PresentationModel<BezierPathListEntry>(pathListSelectionInList.getSelectionHolder());
        pathIdentifierTextField = BasicComponentFactory.createTextField(
                detailModel.getModel(BezierPathListEntry.IDENTIFIER), true);

        pathListSelectionInList.getSelectionHolder().addValueChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                final BezierPathListEntry entry = (BezierPathListEntry) evt.getNewValue();
                if (entry != null) {
                    editorModel.setPath(entry.getPath());
                } else {
                    editorModel.setPath(null);
                }
                paintIt();
            }
        });

    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        editorPanel = new JPanel();
        editorPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), 0, 0));
        editorPanel.setBackground(new Color(-10079233));
        editorPanel.setMaximumSize(new Dimension(904, 804));
        menuPanel = new JPanel();
        menuPanel.setLayout(new FormLayout("left:4dlu:noGrow,fill:185px:grow,left:4dlu:noGrow", "center:138px:noGrow,top:4dlu:noGrow,center:150px:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        editorPanel.add(menuPanel, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(250, 800), new Dimension(250, 800), 0, true));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new FormLayout("fill:107px:grow,left:5dlu:noGrow,fill:max(d;4px):grow", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:74px:grow"));
        CellConstraints cc = new CellConstraints();
        menuPanel.add(panel1, cc.xyw(2, 5, 2));
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Bezier Path"));
        panel1.add(pathIdentifierTextField, cc.xyw(1, 1, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        addCurveButton = new JButton();
        addCurveButton.setText("add");
        panel1.add(addCurveButton, cc.xy(1, 3));
        removeCurve = new JButton();
        removeCurve.setText("remove");
        panel1.add(removeCurve, cc.xy(3, 3));
        mirrorHorizontalButton = new JButton();
        mirrorHorizontalButton.setText("mirror horizontal");
        panel1.add(mirrorHorizontalButton, cc.xy(1, 5));
        mirrorVerticalButton = new JButton();
        mirrorVerticalButton.setText("mirror vertical");
        panel1.add(mirrorVerticalButton, cc.xy(3, 5));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new FormLayout("fill:180px:grow,left:4dlu:noGrow,fill:45px:noGrow", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:6dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,fill:5dlu:grow"));
        menuPanel.add(panel2, cc.xyw(2, 3, 2, CellConstraints.DEFAULT, CellConstraints.FILL));
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Bezier Path List"));
        pathList.setMinimumSize(new Dimension(0, 100));
        pathList.setPreferredSize(new Dimension(0, 200));
        pathList.setSelectionMode(0);
        panel2.add(pathList, cc.xywh(1, 1, 1, 8, CellConstraints.FILL, CellConstraints.FILL));
        removePathButton = new JButton();
        removePathButton.setText("-");
        panel2.add(removePathButton, cc.xy(3, 5));
        addPathButton = new JButton();
        addPathButton.setText("+");
        panel2.add(addPathButton, cc.xy(3, 3));
        duplicateButton = new JButton();
        duplicateButton.setText("d");
        panel2.add(duplicateButton, cc.xy(3, 7));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new FormLayout("fill:176px:grow,left:4dlu:noGrow,fill:40px:grow", "center:p:grow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        menuPanel.add(panel3, cc.xyw(2, 1, 2, CellConstraints.DEFAULT, CellConstraints.FILL));
        panel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "File"));
        openFileButton = new JButton();
        openFileButton.setLabel("...");
        openFileButton.setText("...");
        panel3.add(openFileButton, cc.xy(3, 1));
        savePathListButton = new JButton();
        savePathListButton.setLabel("save");
        savePathListButton.setText("save");
        panel3.add(savePathListButton, cc.xy(1, 5));
        saveAsButton = new JButton();
        saveAsButton.setText("save as ..");
        panel3.add(saveAsButton, cc.xy(1, 3));
        filenameLabel.setText("");
        panel3.add(filenameLabel, cc.xy(1, 1));
        newPathListButton = new JButton();
        newPathListButton.setText("new");
        panel3.add(newPathListButton, cc.xy(1, 7));
        editorPanel.add(imagePanel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(650, 800), new Dimension(650, 800), new Dimension(650, 800), 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return editorPanel;
    }
}
