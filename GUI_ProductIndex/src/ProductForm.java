import com.sun.tools.javac.Main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerAdapter;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ProductForm {

    private static SimpleAttributeSet simpleAttributeSet_Error = new SimpleAttributeSet(),
                                      simpleAttributeSet_Success = new SimpleAttributeSet(),
                                      simpleAttributeSet_Default = new SimpleAttributeSet(),
                                      simpleAttributeSet_Time = new SimpleAttributeSet();
    private static final String STR_FORMAT_APPEND_FILE_ERROR = "Произошла ошибка при загрузке текста из файла {0}",
                                STR_FORMAT_APPEND_FILE_OPEN_SUCCESS = "Файл {0} успешно выбран!",
                                STR_STARTED_APPENDING = "Начата загрузка записей в бинарный файл...",
                                STR_STARTED_ZIP_APPENDING = "Начата загрузка zip-записей в бинарный файл...",
                                STR_PRINT_ERROR = "Ошибка во время вывода сохранных значений",
                                STR_DELETE_FILE_BY_KEY_ERROR = "Ошибка удаления ключа по значению (с) лаба из примера",
                                STR_PRINT_SORTED_BY_KEY_ERROR = "Произошла ошибка при выводе сортированных значений",
                                STR_CLEAR_ALL_FILE_SUCCESS = "Файл успешно очищен!",
                                STR_CLEAR_FILE_BY_KEY_SUCCESS = "Запись успешно успешно удалена!",
                                STR_PRINT_INTO_TABLE_SUCCESS = "Успешно выведено в таблицу!",
                                STR_APPEND_SUCCESS_FINISH = "Успешно загружено в бинарный файл!";

    String columnNames[] = new String[] {ProductRecord.P_StorageId,
                                        ProductRecord.P_ProductId,
                                        ProductRecord.P_ArrivalTime,
                                        ProductRecord.P_ProductName,
                                        ProductRecord.P_ProductQuantity,
                                        ProductRecord.P_ShelfTimeInDays};
    DefaultTableModel model = new DefaultTableModel(null, columnNames);
    private ArrayList<ProductRecord> productRecords;

    private boolean isRadioChosen = false;
    private String currentStrRadioKey = null, currentKeyForSearch = null;


    private JPanel MainPanel;
    private static JFrame frame;
    private JTable Table_ShownData;
    private JPanel JPanel_Buttons;
    private JPanel Jpane_PrintedTableData;
    private JPanel JPanel_Key;
    private JTabbedPane Panel_FileJob;
    private JButton Button_AppendFromTxt;
    private JButton Button_AppendZipTxt;
    private JRadioButton RadioB_StorageId;
    private JPanel JPanel_TextProtocol;
    private JPanel Panel_PrintJob;
    private JButton Button_PrintDataWithoutSorting;
    private JTextField TextField_KeyForSearch;
    private JButton Button_GetKey;
    private JButton Button_ClearAllData;
    private JButton Button_ClearAllDataByKey;
    private JPanel Panel_BinaryJob;
    private JRadioButton RadioB_ProductId;
    private JRadioButton RadioB_ArrivalTime;
    private JRadioButton RadioB_ShelfTime;
    private JButton Button_PrintSortedByKey;
    private JButton Button_PrintReversedSortedByKey;
    private JButton Button_FindBetterData;
    private JLabel Label_ValueForSearch;
    private JButton Button_FindByValue;
    private JButton Button_FindWorseByValue;
    private JEditorPane editorPane1;
    private JButton Button_ClearHistory;
    private JTextPane TextPane_History;
    private StyledDocument historyTextDoc = TextPane_History.getStyledDocument();
    private JRadioButton[] jRadioButtons = new JRadioButton[]{RadioB_ArrivalTime, RadioB_ProductId, RadioB_ShelfTime, RadioB_StorageId};
    private String[] strRadioButtons = new String[] {Index.CODE_ARG_ARRIVAL_TIME, Index.CODE_ARG_PRODUCT_ID,
                                                     Index.CODE_ARG_SHELFTIME, Index.CODE_ARG_STORAGE_ID};
    private JButton[] keyIndexButton = new JButton[]{Button_ClearAllDataByKey, Button_FindWorseByValue, Button_FindBetterData, Button_FindByValue};
    Calendar calendar = Calendar.getInstance(); // gets current instance of the calendar


    private enum enTextType
    {
        eError,
        eSuccess,
        eDefault
    }



    private File getUserFile(String format)
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));


        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
        {
            return fileChooser.getSelectedFile();
        }
        else
            return null;
    }


    private String currentTime()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return (formatter.format(calendar.getTime()));
    }


    private SimpleAttributeSet getAttribute(enTextType textType)
    {
        switch (textType)
        {
            case eError:
                return simpleAttributeSet_Error;
            case eSuccess:
                return simpleAttributeSet_Success;
            default:
                return simpleAttributeSet_Default;
        }
    }


    private void appendTextToPanel(String text, enTextType textType) {
        try {
            historyTextDoc.insertString(historyTextDoc.getLength(), "\n" + currentTime(), simpleAttributeSet_Time);
            historyTextDoc.insertString(historyTextDoc.getLength(),": " + text, getAttribute(textType));
        } catch (BadLocationException e) {
            System.exit(-1);
        }
    }


    public ProductForm() {
        Button_AppendFromTxt.addActionListener(actionEvent -> {
            File userFile;
            if ((userFile = getUserFile("txt")) != null)
            {
                try {
                    appendTextToPanel(MessageFormat.format(STR_FORMAT_APPEND_FILE_OPEN_SUCCESS, userFile.getName()), enTextType.eSuccess);
                    appendTextToPanel(STR_STARTED_APPENDING, enTextType.eDefault);
                    MainProductTester.appendFile(false, userFile);
                    appendTextToPanel(STR_APPEND_SUCCESS_FINISH, enTextType.eSuccess);
                }
                catch(Exception ex)
                {
                    appendTextToPanel(MessageFormat.format(STR_FORMAT_APPEND_FILE_ERROR, userFile.getName()), enTextType.eError);
                }
            }

        });

        Button_PrintDataWithoutSorting.addActionListener(actionEvent -> {
            try {
                productRecords =  MainProductTester.getDataToPrint();
                addRecordsToTable(productRecords);
                appendTextToPanel(STR_PRINT_INTO_TABLE_SUCCESS,enTextType.eSuccess);
            }
            catch (Exception e) {
                appendTextToPanel(STR_PRINT_ERROR, enTextType.eError);
            }
            //Table_ShownData.set
        });


        for(JRadioButton radioButton : jRadioButtons)
        {
            radioButton.addActionListener(ActionEvent ->
            {
                if (radioButton.isSelected())
                {
                    setEnableExcept(false, radioButton);
                    Button_PrintSortedByKey.setEnabled(true);
                    Button_PrintReversedSortedByKey.setEnabled(true);
                    for(JButton jButton : keyIndexButton)
                    {
                        jButton.setEnabled(currentKeyForSearch != null && currentStrRadioKey != null);
                    }
                }
                else
                {
                    setEnableExcept(true, radioButton);
                    Button_PrintSortedByKey.setEnabled(false);
                    Button_ClearAllDataByKey.setEnabled(false);
                    Button_PrintReversedSortedByKey.setEnabled(false);
                }
            });
        }


        Button_AppendZipTxt.addActionListener(actionEvent -> {
            File userFileToZip;
            if ((userFileToZip = getUserFile("txt")) != null)
            {
                try {
                    appendTextToPanel(MessageFormat.format(STR_STARTED_ZIP_APPENDING, userFileToZip.getName()), enTextType.eDefault);

                    MainProductTester.appendFile(true, userFileToZip);
                    appendTextToPanel(STR_APPEND_SUCCESS_FINISH, enTextType.eSuccess);
                }
                catch(Exception ex)
                {
                    appendTextToPanel(MessageFormat.format(STR_FORMAT_APPEND_FILE_ERROR, userFileToZip.getName()),enTextType.eError);
                }
            }
        });

        Button_ClearAllData.addActionListener(actionEvent -> {
            MainProductTester.deleteFile();
            addRecordsToTable(MainProductTester.sortedProductRecords);
            appendTextToPanel(STR_CLEAR_ALL_FILE_SUCCESS, enTextType.eSuccess);
        });

        TextField_KeyForSearch.addCaretListener(caretEvent -> {
            Button_GetKey.setEnabled(TextField_KeyForSearch.getText().length() != 0);
        });

        Button_GetKey.addActionListener(actionEvent -> {
            currentKeyForSearch = TextField_KeyForSearch.isEnabled() ? TextField_KeyForSearch.getText() : null;
            TextField_KeyForSearch.setEnabled(!TextField_KeyForSearch.isEnabled());
            Button_GetKey.setText(TextField_KeyForSearch.isEnabled() ? "ОК" : "ОТМЕНА");
            for(JButton jButton : keyIndexButton)
            {
                jButton.setEnabled(currentKeyForSearch != null && currentStrRadioKey != null);
            }
        });
        Button_ClearAllDataByKey.addActionListener(actionEvent -> {
            try {
                MainProductTester.deleteFile(currentStrRadioKey, currentKeyForSearch);
                addRecordsToTable(MainProductTester.sortedProductRecords);
                appendTextToPanel(STR_CLEAR_FILE_BY_KEY_SUCCESS, enTextType.eSuccess);
            } catch (Exception e) {
                appendTextToPanel(STR_DELETE_FILE_BY_KEY_ERROR, enTextType.eSuccess);
            }
        });

        Button_PrintSortedByKey.addActionListener(actionEvent -> {
            try {
                MainProductTester.getDataToPrint(currentStrRadioKey, false);
                addRecordsToTable(MainProductTester.sortedProductRecords);

                appendTextToPanel(STR_PRINT_INTO_TABLE_SUCCESS, enTextType.eSuccess);
            } catch (Exception e) {
                appendTextToPanel(STR_PRINT_SORTED_BY_KEY_ERROR, enTextType.eError);
            }
        });
        Button_FindByValue.addActionListener(actionEvent -> {
            try {
                addRecordsToTable(MainProductTester.findByKey(currentStrRadioKey, currentKeyForSearch));
                appendTextToPanel(STR_PRINT_INTO_TABLE_SUCCESS, enTextType.eSuccess);
            } catch (Exception e) {
                appendTextToPanel("Невозможно найти значение (с) лаба из примеров", enTextType.eError);
            }
        });


        Button_FindBetterData.addActionListener(actionEvent -> {
            try {
                addRecordsToTable(MainProductTester.findByKey(currentStrRadioKey, currentKeyForSearch, new KeyCompReverse()));
                appendTextToPanel(STR_PRINT_INTO_TABLE_SUCCESS, enTextType.eSuccess);
            } catch (Exception e) {
                appendTextToPanel(e.toString(), enTextType.eError);
            }
        });

        Button_FindWorseByValue.addActionListener(actionEvent -> {
            try
            {
                addRecordsToTable(MainProductTester.findByKey(currentStrRadioKey, currentKeyForSearch, new KeyComp()));
                appendTextToPanel(STR_PRINT_INTO_TABLE_SUCCESS, enTextType.eSuccess);
            }
            catch (Exception e)
            {
                appendTextToPanel(e.toString(), enTextType.eError);
            }
        });
        Table_ShownData.addContainerListener(new ContainerAdapter() {
        });
        Button_PrintReversedSortedByKey.addActionListener(actionEvent -> {
            try {
                MainProductTester.getDataToPrint(currentStrRadioKey, true);
                addRecordsToTable(MainProductTester.sortedProductRecords);
                appendTextToPanel(STR_PRINT_INTO_TABLE_SUCCESS, enTextType.eSuccess);
            } catch (Exception e) {
                appendTextToPanel(STR_PRINT_SORTED_BY_KEY_ERROR, enTextType.eError);
            }
        });

        Button_ClearHistory.addActionListener(actionEvent -> {
            TextPane_History.setText("");
        });
    }


    private void addRecordsToTable(ArrayList<ProductRecord> productRecords)
    {
        model = new DefaultTableModel(arrToObjects(productRecords), columnNames);
        Table_ShownData.setModel(model);
    }


    private void setEnableExcept(boolean enabled, JRadioButton except)
    {
        int index = 0;
        for(JRadioButton radioButton : jRadioButtons)
        {
            if (radioButton != except)
            {
                radioButton.setEnabled(enabled);
            }
            else
            {
                currentStrRadioKey = except.isSelected() ? strRadioButtons[index] : null;
//                if (except.isSelected())
//                {
//                    currentStrRadioKey = strRadioButtons[index];
//                }
//                else
//                {
//                    currentStrRadioKey = null;
//                }
            }
            ++index;
        }
    }


    private Object[][] arrToObjects(ArrayList<ProductRecord> productRecords) {
        Object[][] objToReturn = new Object[productRecords.size()][6];
        int currentRecord = 0;
        for(ProductRecord record : productRecords)
        {
            objToReturn[currentRecord][0] = record.getStorageId();
            objToReturn[currentRecord][1] = record.getProductId();
            objToReturn[currentRecord][2] = record.getArrivalTime();
            objToReturn[currentRecord][3] = record.getProductName();
            objToReturn[currentRecord][4] = record.getQuantity();
            objToReturn[currentRecord][5] = record.getShelfInDays();
            ++currentRecord;
        }

        return objToReturn;
    }


    private static void setFeelAndLook(){
        try {
            // Set cross-platform Java L&F (also called "Metal")
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        }
        catch(Exception ex)
        {
            System.err.println(ex);
        }
        for(UIManager.LookAndFeelInfo lf : UIManager.getInstalledLookAndFeels())
        {
            System.out.println(lf);
        }
    }


    private static void setKeyWords()
    {
        StyleConstants.setForeground(simpleAttributeSet_Error, Color.RED);
        StyleConstants.setForeground(simpleAttributeSet_Success, Color.GREEN);
        StyleConstants.setForeground(simpleAttributeSet_Default, Color.BLACK);
        StyleConstants.setForeground(simpleAttributeSet_Default, Color.BLACK);
        StyleConstants.setForeground(simpleAttributeSet_Time, Color.GRAY);
        StyleConstants.setItalic(simpleAttributeSet_Time, true);
    }


    private static void setDefaults()
    {
        setKeyWords();
        frame = new JFrame("ProductForm");
        frame.setContentPane(new ProductForm().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }


    public static void main(String[] args)
    {
        setFeelAndLook();
        setDefaults();
    }

    private void createUIComponents() {
    }
}
