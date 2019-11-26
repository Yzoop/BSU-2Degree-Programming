import com.sun.tools.javac.Main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;

public class ProductForm {

    private static final String STR_FORMAT_APPEND_FILE_ERROR = "Произошла ошибка при загрузке текста из файла {0}",
                                STR_FORMAT_APPEND_FILE_OPEN_SUCCESS = "Файл {0} успешно выбран!",
                                STR_PRINT_SORTED_BY_KEY_ERROR = "Произошла ошибка при выводе сортированных значений",
                                STR_STARTED_APPENDING = "Начата загрузка записей в бинарный файл...",
                                STR_STARTED_ZIP_APPENDING = "Начата загрузка zip-записей в бинарный файл...",
                                STR_PRINT_ERROR = "Ошибка во время вывода сохранных значений",
                                STR_DELETE_FILE_BY_KEY_ERROR = "Ошибка удаления ключа по значению",
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
    private String currentStrRadioKey = null, currentKeyForSearch = "";


    private JPanel MainPanel;
    private static JFrame frame;
    private JTable Table_ShownData;
    private JPanel JPanel_Buttons;
    private JTabbedPane Panel_FileJob;
    private JButton Button_AppendFromTxt;
    private JButton Button_AppendZipTxt;
    private JRadioButton RadioB_StorageId;
    private JPanel JPanel_TextProtocol;
    private JTextPane TextPanel_Console;
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
    private JPanel Jpane_PrintedTableData;
    private JPanel Jpanel_RadioButtonsKeys;
    private JPanel JPanel_Key;

    private JRadioButton[] jRadioButtons = new JRadioButton[]{RadioB_ArrivalTime, RadioB_ProductId, RadioB_ShelfTime, RadioB_StorageId};
    private String[] strRadioButtons = new String[] {Index.CODE_ARG_ARRIVAL_TIME, Index.CODE_ARG_PRODUCT_ID,
                                                     Index.CODE_ARG_SHELFTIME, Index.CODE_ARG_STORAGE_ID};


    private File getUserFile(String format)
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

        //disable main window before choosing file
        MainPanel.setEnabled(false);

        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
        {
            return fileChooser.getSelectedFile();
        }
        else
            return null;
    }


    private void appendTextToPanel(String text)
    {
        TextPanel_Console.setText(TextPanel_Console.getText() + "\n" + text);
    }


    public ProductForm() {
        Button_AppendFromTxt.addActionListener(actionEvent -> {
            File userFile;
            if ((userFile = getUserFile("txt")) != null)
            {
                try {
                    appendTextToPanel(MessageFormat.format(STR_FORMAT_APPEND_FILE_OPEN_SUCCESS, userFile.getName()));
                    appendTextToPanel(STR_STARTED_APPENDING);
                    MainProductTester.appendFile(false, userFile);
                    appendTextToPanel(STR_APPEND_SUCCESS_FINISH);
                }
                catch(Exception ex)
                {
                    appendTextToPanel(MessageFormat.format(STR_FORMAT_APPEND_FILE_ERROR, userFile.getName()));
                }
            }

        });

        Button_PrintDataWithoutSorting.addActionListener(actionEvent -> {
            try {
                productRecords =  MainProductTester.getDataToPrint();
                addRecordsToTable(productRecords);
                appendTextToPanel(STR_PRINT_INTO_TABLE_SUCCESS);
            }
            catch (Exception e) {
                appendTextToPanel(STR_PRINT_ERROR);
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
                    Button_ClearAllDataByKey.setEnabled(currentKeyForSearch != "" && currentStrRadioKey != "");
                    Button_PrintSortedByKey.setEnabled(true);
                    Button_PrintReversedSortedByKey.setEnabled(true);
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
                    appendTextToPanel(MessageFormat.format(STR_STARTED_ZIP_APPENDING, userFileToZip.getName()));
                    appendTextToPanel(STR_STARTED_APPENDING);

                    MainProductTester.appendFile(true, userFileToZip);
                    appendTextToPanel(STR_APPEND_SUCCESS_FINISH);
                }
                catch(Exception ex)
                {
                    appendTextToPanel(MessageFormat.format(STR_FORMAT_APPEND_FILE_ERROR, userFileToZip.getName()));
                }
            }
        });

        Button_ClearAllData.addActionListener(actionEvent -> {
            MainProductTester.deleteFile();
            appendTextToPanel(STR_CLEAR_ALL_FILE_SUCCESS);
        });

        TextField_KeyForSearch.addCaretListener(caretEvent -> {
            Button_GetKey.setEnabled(TextField_KeyForSearch.getText().length() != 0);
        });

        Button_GetKey.addActionListener(actionEvent -> {
            if (TextField_KeyForSearch.isEnabled())
            {
                currentKeyForSearch = TextField_KeyForSearch.getText();
                TextField_KeyForSearch.setEnabled(false);
                Button_GetKey.setText("CANCEL");
                Button_ClearAllDataByKey.setEnabled(currentKeyForSearch != "" && currentStrRadioKey != "");
                //enable all buttons which use key
                Button_FindBetterData.setEnabled(true);
                Button_FindWorseByValue.setEnabled(true);
                Button_FindByValue.setEnabled(true);
            }
            else {
                currentKeyForSearch = "";
                TextField_KeyForSearch.setEnabled(true);
                Button_GetKey.setText("OK");
                Button_ClearAllDataByKey.setEnabled(false);
                //disable all buttons which use key
                Button_FindBetterData.setEnabled(false);
                Button_FindWorseByValue.setEnabled(false);
                Button_FindByValue.setEnabled(false);
            }
        });
        Button_ClearAllDataByKey.addActionListener(actionEvent -> {
            try {
                MainProductTester.deleteFile(currentStrRadioKey, currentKeyForSearch);
                appendTextToPanel(STR_CLEAR_FILE_BY_KEY_SUCCESS);
            } catch (Exception e) {
                appendTextToPanel(STR_DELETE_FILE_BY_KEY_ERROR);
            }
        });

        Button_PrintSortedByKey.addActionListener(actionEvent -> {
            try {
                MainProductTester.getDataToPrint(currentStrRadioKey, false);
                addRecordsToTable(MainProductTester.sortedProductRecords);
                appendTextToPanel(STR_PRINT_INTO_TABLE_SUCCESS);
            } catch (Exception e) {
                appendTextToPanel(STR_PRINT_SORTED_BY_KEY_ERROR);
            }
        });
        Button_FindByValue.addActionListener(actionEvent -> {
            try {
                addRecordsToTable(MainProductTester.findByKey(currentStrRadioKey, currentKeyForSearch));
            } catch (Exception e) {
                appendTextToPanel(e.toString());
            }
        });


        Button_FindBetterData.addActionListener(actionEvent -> {
            try {
                addRecordsToTable(MainProductTester.findByKey(currentStrRadioKey, currentKeyForSearch, new KeyCompReverse()));
                appendTextToPanel(STR_PRINT_INTO_TABLE_SUCCESS);
            } catch (Exception e) {
                appendTextToPanel(e.toString());
            }
        });

        Button_FindWorseByValue.addActionListener(actionEvent -> {
            try
            {
                addRecordsToTable(MainProductTester.findByKey(currentStrRadioKey, currentKeyForSearch, new KeyComp()));
                appendTextToPanel(STR_PRINT_INTO_TABLE_SUCCESS);
            }
            catch (Exception e)
            {
                appendTextToPanel(e.toString());
            }
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
                if (except.isSelected())
                {
                    currentStrRadioKey = strRadioButtons[index];
                }
                else
                {
                    currentStrRadioKey = "";
                }
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






    private static void setDefaults()
    {
        frame = new JFrame("ProductForm");
        frame.setContentPane(new ProductForm().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }


    public static void main(String[] args) {
        setDefaults();
    }

    private void createUIComponents() {
    }
}
