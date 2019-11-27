import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class SwingForm {

    private String colNames[] = new String[] {Baggage.P_flightNumber, Baggage.P_date, Baggage.P_destination, Baggage.P_weight};

    private static JFrame frame;
    private JPanel MainPanel;
    private JButton Button_AppendFIle;
    private JButton Button_ZipAppendFile;
    private JButton Button_ClearAllData;
    private JButton Button_PrintSortedByRadio;
    private JButton Button_PrintDataUnsorted;
    private JButton Button_PrintReversedSortedByRadio;
    private JButton Button_ClearAllDataByKey;
    private JButton Button_FindByKey;
    private JButton Button_FindMoreThanKey;
    private JButton Button_FindLessThanKey;
    private JRadioButton RButton_FlightNumber;
    private JRadioButton RButton_Date;
    private JPanel Panel_Keys;
    private JRadioButton RButton_To;
    private JRadioButton RButton_Weight;
    private JLabel Label_Key;
    private JTextField TextField_Key;
    private JButton Button_ApplyKey;
    private JPanel Panel_ButtonsLeft;
    private JPanel Panel_ButtonsRight;
    private JPanel Panel_Ruler;
    private JScrollPane ScrollPanel_BaggageTableq;
    private JTable Table_Baggage;
    private JLabel Label_Index;

    private JRadioButton jRadioButton[] = new JRadioButton[] {RButton_Date, RButton_Weight, RButton_FlightNumber, RButton_To};
    private String strIndex[] = new String[] {"dat", "w", "fn", "dest"};
    private JButton indexKeyButton[] = new JButton[]{Button_ClearAllDataByKey, Button_FindByKey, Button_FindLessThanKey, Button_FindMoreThanKey};
    private String strCurrentIndex = null, strCurrentKey = null;
    private boolean isApplyButtonOn = true;

    private void printBaggageToTable(Object[][] oBaggage)
    {
        Table_Baggage.setModel(new DefaultTableModel(oBaggage, colNames));
    }


    private Object[][] arrConvertToObjects(ArrayList<Baggage> baggages)
    {
        Object[][] objToReturn = new Object[baggages.size()][6];

        int currentRecord = 0;
        for(Baggage baggage : baggages)
        {
            objToReturn[currentRecord][0] = baggage.flight_number;
            objToReturn[currentRecord][1] = baggage.date;
            objToReturn[currentRecord][2] = baggage.destination;
            objToReturn[currentRecord][3] = baggage.weight;
            ++currentRecord;
        }

        return objToReturn;
    }


    private File getUserFile(String format) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        } else
            return null;
    }



    public SwingForm() {
        Button_AppendFIle.addActionListener(actionEvent -> {
            File userFile;
            if ((userFile = getUserFile("txt")) != null)
            {
                try {
                    Main.appendFile(false, userFile);
                }
                catch(Exception ex)
                {
                    System.err.println(ex);
                    //appendTextToPanel(MessageFormat.format(STR_FORMAT_APPEND_FILE_ERROR, userFile.getName()));
                }
            }
        });


        Button_PrintDataUnsorted.addActionListener(actionEvent -> {
            try {
                printBaggageToTable(arrConvertToObjects(Main.printFile()));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });


        Button_ZipAppendFile.addActionListener(actionEvent -> {
            File userFile;
            if ((userFile = getUserFile("txt")) != null)
            {
                try {
                    Main.appendFile(true, userFile);
                    System.out.println("suc");
                }
                catch(Exception ex)
                {
                    System.err.println(ex);
                    //appendTextToPanel(MessageFormat.format(STR_FORMAT_APPEND_FILE_ERROR, userFile.getName()));
                }
            }
        });
        Button_ClearAllData.addActionListener(actionEvent -> {
            Main.deleteFile();
        });
        TextField_Key.addCaretListener(caretEvent -> {
            if (TextField_Key.getText().length() > 0)
            {
                Button_ApplyKey.setEnabled(true);
            }
            else
            {
                Button_ApplyKey.setEnabled(false);
            }
        });

        int curRbuttonIndex = 0;
        for(JRadioButton rbutton : jRadioButton)
        {
            int finalCurRbuttonIndex = curRbuttonIndex;

            rbutton.addActionListener(actionEvent -> {
                disableAllExcept(rbutton);
                strCurrentIndex = rbutton.isSelected() ? strIndex[finalCurRbuttonIndex] : null;

                Button_PrintSortedByRadio.setEnabled(rbutton.isSelected());
                Button_PrintReversedSortedByRadio.setEnabled(rbutton.isSelected());
                setEnabledIndexKeyButtons();
            });
            ++curRbuttonIndex;
        }


        Button_ApplyKey.addActionListener(actionEvent -> {
            isApplyButtonOn = !isApplyButtonOn;
            Button_ApplyKey.setText(isApplyButtonOn ? "Принять" :"Отменить");
            TextField_Key.setEnabled(isApplyButtonOn);
            strCurrentKey= isApplyButtonOn ? null : TextField_Key.getText();
            setEnabledIndexKeyButtons();
        });


        Button_PrintSortedByRadio.addActionListener(actionEvent -> {
            try {
                printBaggageToTable(arrConvertToObjects(Objects.requireNonNull(Main.printFile(strCurrentIndex, false))));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Button_PrintReversedSortedByRadio.addActionListener(actionEvent -> {
            try {
                printBaggageToTable(arrConvertToObjects(Objects.requireNonNull(Main.printFile(strCurrentIndex, true))));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Button_FindMoreThanKey.addActionListener(actionEvent -> {
            try {
                printBaggageToTable(arrConvertToObjects(Objects.requireNonNull(Main.findByKey(strCurrentIndex,strCurrentKey, new KeyComp()))));
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        });
        Button_FindLessThanKey.addActionListener(actionEvent -> {
            try {
                printBaggageToTable(arrConvertToObjects(Objects.requireNonNull(Main.findByKey(strCurrentIndex,strCurrentKey, new KeyCompReverse()))));
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        });
        Button_FindByKey.addActionListener(actionEvent -> {
            try {
                printBaggageToTable(arrConvertToObjects(Objects.requireNonNull(Main.findByKey(strCurrentIndex,strCurrentKey))));
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        });
        Button_PrintReversedSortedByRadio.addActionListener(actionEvent -> {
            try {
                printBaggageToTable(arrConvertToObjects(Objects.requireNonNull(Main.printFile(strCurrentIndex,true))));
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        });
        Button_ClearAllDataByKey.addActionListener(actionEvent -> {
            try {
                Main.deleteFile(strCurrentIndex, strCurrentKey);
                printBaggageToTable(arrConvertToObjects(Objects.requireNonNull(Main.printFile())));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (KeyNotUniqueException e) {
                e.printStackTrace();
            }
        });
    }


    private void setEnabledIndexKeyButtons()
    {
        for(JButton keIndexBut : indexKeyButton)
        {
            keIndexBut.setEnabled(strCurrentIndex != null && strCurrentKey != null);
        }
    }


    private void disableAllExcept(JRadioButton except)
    {
        for(JRadioButton rbutton : jRadioButton)
        {
            if (rbutton != except)
            {
                rbutton.setSelected(false);
            }
        }
    }


    public static void main(String[] args) {
//        try {
//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel"); //Windows Look and feel
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
//            e.printStackTrace();
//        }
        frame = new JFrame("SwingForm");
        frame.setContentPane(new SwingForm().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }
}
