package calculatorgui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CalculatorGUI extends JFrame implements ActionListener {

    private JTextField inputField;
    private String currentInput = "";
    private BigDecimal result = BigDecimal.ZERO;
    private String operator = "";
    private JComboBox<String> baseComboBox;
    private int currentBase = 10;
    private JPanel buttonPanel;


    public CalculatorGUI() {
        setTitle("Калькулятор");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();
        JMenu converterMenu = new JMenu("Конвертер");

        JMenuItem baseConverterMenuItem = new JMenuItem("Системы счисления");
        baseConverterMenuItem.addActionListener(e -> openBaseConverterPanel());
        converterMenu.add(baseConverterMenuItem);
        
        JMenuItem lengthMenuItem = new JMenuItem("Длина");
        lengthMenuItem.addActionListener(e -> openLengthConverter());
        
        JMenuItem temperatureMenuItem = new JMenuItem("Температура");
        temperatureMenuItem.addActionListener(e -> openTemperatureConverter());


        JMenuItem areaMenuItem = new JMenuItem("Площадь");
        areaMenuItem.addActionListener(e -> openAreaConverter());
        converterMenu.add(areaMenuItem);
        

        JMenuItem currencyMenuItem = new JMenuItem("Валюта");
        currencyMenuItem.addActionListener(e -> openCurrencyConverter());
        converterMenu.add(currencyMenuItem);

        JMenuItem massMenuItem = new JMenuItem("Масса");
        massMenuItem.addActionListener(e -> openMassConverter());
        converterMenu.add(massMenuItem);

        JMenuItem volumeMenuItem = new JMenuItem("Объем");
        volumeMenuItem.addActionListener(e -> openVolumeConverter());
        converterMenu.add(volumeMenuItem);

        JMenuItem dataMenuItem = new JMenuItem("Данные");
        dataMenuItem.addActionListener(e -> openDataConverter());
        converterMenu.add(dataMenuItem);

       
        buttonPanel = new JPanel(new GridLayout(6, 4));
        updateButtonPanel(10);
    
        converterMenu.add(lengthMenuItem);
        converterMenu.add(temperatureMenuItem);

        menuBar.add(converterMenu);
        setJMenuBar(menuBar);

        
        inputField = new JTextField(); 
        inputField.setEditable(false);

        String[] bases = {"Dec(10)", "Bin(2)", "Oct(8)", "Hex(16)","Ter(3)","Quat(4)","Pent(5)","Hexas(6)","Sept(7)","Nona(9)","UnDec(11)","DoDec(12)","TDec(13)","TrDec(14)","PDec(15)"};
        baseComboBox = new JComboBox<>(bases);
        baseComboBox.addActionListener(this);
        
        JPanel buttonPanel = new JPanel(new GridLayout(6, 4)); 
        String[] buttonLabels = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+",
                "C", "%", "√", "^" 
        };

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.addActionListener(this);
            buttonPanel.add(button);
        }
        
       // buttonPanel.add(baseComboBox);

        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(inputField, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
 
        JPanel mainPanel1 = new JPanel(new BorderLayout());
        mainPanel.add(inputField,BorderLayout.NORTH);
        
        
        add(mainPanel);
    }
    private boolean isValidForCurrentBase(String character) {
    if (currentBase <= 10) {
        return character.matches("[0-" + (currentBase - 1) + "]");
    } else {
        return character.toUpperCase().matches("[0-9A-" + (char) ('A' + currentBase - 11) + "]");
    }
}
    private void updateButtonPanel(int base) {
    buttonPanel.removeAll(); // Удалите все текущие кнопки

    String[] defaultButtons = {
        "7", "8", "9", "/",
        "4", "5", "6", "*",
        "1", "2", "3", "-",
        "0", ".", "=", "+",
        "C", "%", "√", "^"
    };

    for (String label : defaultButtons) {
        JButton button = new JButton(label);
        button.addActionListener(this);
        buttonPanel.add(button);
    }

    

    buttonPanel.revalidate();
    buttonPanel.repaint();
}
    
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (e.getSource() == baseComboBox) {
        int base = getBaseFromSelectedIndex(baseComboBox.getSelectedIndex());
        //updateButtonPanel(base); // Обновите панель кнопок на основе выбранной системы счисления
        //convertResultToSelectedBase();
    }
        if (command.matches("[0-9.]")) {
            currentInput += command;
            inputField.setText(currentInput);
        } else if (command.equals("+") || command.equals("-") || command.equals("*") || command.equals("/") || command.equals("%") || command.equals("^") || command.equals("√")) {
            if (!currentInput.isEmpty()) {
                if (!operator.isEmpty()) {
                    calculate();
                }
                operator = command;
                if (!operator.equals("√")) {
                    result = new BigDecimal(currentInput);
                } else if (command.equals("√")) {
        if (!currentInput.isEmpty()) {
            result = new BigDecimal(currentInput);
            if (result.compareTo(BigDecimal.ZERO) < 0) {
                JOptionPane.showMessageDialog(this, "Ошибка: нельзя извлечь корень из отрицательного числа", "Ошибка", JOptionPane.ERROR_MESSAGE);
                resetCalculator();
                return;
            } else {
                result = BigDecimal.valueOf(Math.sqrt(result.doubleValue()));
                currentInput = result.stripTrailingZeros().toPlainString();
                inputField.setText(currentInput);
            }
        }
        if (command.matches("[0-9A-Fa-f.]")) {
        if (isValidForCurrentBase(command)) {
            currentInput += command;
            inputField.setText(currentInput);
        } else {
            JOptionPane.showMessageDialog(this, "Недопустимый символ для выбранной системы счисления", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    } 
        
    }
                currentInput = "";
            } else if (command.equals("-") && operator.isEmpty()) {
                // Обработка отрицательного числа
                currentInput += command;
                inputField.setText(currentInput);
            }
        } else if (command.equals("=")) {
            calculate();
            operator = "";
        } else if (command.equals("C")) {
            resetCalculator();
        }
        if (e.getSource() == baseComboBox) {
            convertResultToSelectedBase();
        }
        
    }

    private void calculate() {
        if (!currentInput.isEmpty()) {
            BigDecimal secondOperand = new BigDecimal(currentInput);
            switch (operator) {
                case "+":
                    result = result.add(secondOperand);
                    break;
                case "-":
                    result = result.subtract(secondOperand);
                    break;
                case "*":
                    result = result.multiply(secondOperand);
                    break;
                case "/":
                    if (secondOperand.equals(BigDecimal.ZERO)) {
                        JOptionPane.showMessageDialog(this, "Ошибка: деление на ноль", "Ошибка", JOptionPane.ERROR_MESSAGE);
                        resetCalculator();
                        return;
                    } else {
                        result = result.divide(secondOperand, MathContext.DECIMAL128);
                    }
                    break;
                case "%":
                    if (secondOperand.equals(BigDecimal.ZERO)) {
                        JOptionPane.showMessageDialog(this, "Ошибка: деление на ноль", "Ошибка", JOptionPane.ERROR_MESSAGE);
                        resetCalculator();
                        return;
                    } else {
                        result = result.remainder(secondOperand);
                    }
                    break;
                case "^":
                    int exponent = secondOperand.intValue();
                    result = result.pow(exponent);
                    break;
            }
            currentInput = result.stripTrailingZeros().toPlainString();
            inputField.setText(currentInput);
        }
    }

    private void resetCalculator() {
        currentInput = "";
        operator = "";
        result = BigDecimal.ZERO;
        inputField.setText("");
    }

    private void openBaseConverterPanel() {
    JFrame baseConverterFrame = new JFrame("Системы счисления");
    baseConverterFrame.setSize(400, 300);
    baseConverterFrame.setLocationRelativeTo(null);
    
    JTextField inputField = new JTextField();
    inputField.setEditable(true);
    JTextField outputField = new JTextField();
    outputField.setEditable(false);
    
    String[] bases = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16"};
    JComboBox<String> inputBaseComboBox = new JComboBox<>(bases);
    JComboBox<String> outputBaseComboBox = new JComboBox<>(bases);

    JPanel buttonsPanel = new JPanel(new GridLayout(4, 4));
    String[] buttonLabels = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};

   Map<String, JButton> buttonsMap = new HashMap<>();

for (String label : buttonLabels) {
    JButton button = new JButton(label);
    button.addActionListener(e -> inputField.setText(inputField.getText() + label));
    buttonsPanel.add(button);
    buttonsMap.put(label, button);
}

inputBaseComboBox.addActionListener(e -> {
    int selectedBase = Integer.parseInt((String) inputBaseComboBox.getSelectedItem());
    for (String label : buttonLabels) {
        JButton button = buttonsMap.get(label);
        if (label.matches("[0-9A-F]")) {
            int value = label.charAt(0) <= '9' ? label.charAt(0) - '0' : 10 + label.charAt(0) - 'A';
            button.setEnabled(value < selectedBase);
        }
    }
});

// Вызываем слушатель сразу после создания, чтобы инициализировать начальное состояние кнопок.
inputBaseComboBox.setSelectedIndex(0);
    
    JButton convertButton = new JButton("Результат");
    convertButton.addActionListener(e -> {
        String inputValue = inputField.getText();
        int inputBase = Integer.parseInt((String) inputBaseComboBox.getSelectedItem());
        int outputBase = Integer.parseInt((String) outputBaseComboBox.getSelectedItem());
        try {
            int decimalValue = Integer.parseInt(inputValue, inputBase);
            String convertedValue = Integer.toString(decimalValue, outputBase).toUpperCase();
            outputField.setText(convertedValue);
        } catch (NumberFormatException ex) {
            outputField.setText("Ошибка ввода!");
        }
    });

    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    panel.add(inputBaseComboBox, BorderLayout.NORTH);
    panel.add(buttonsPanel, BorderLayout.CENTER);
    panel.add(outputField, BorderLayout.SOUTH);

    baseConverterFrame.setLayout(new BorderLayout());
    baseConverterFrame.add(inputField, BorderLayout.NORTH);
    baseConverterFrame.add(panel, BorderLayout.CENTER);
    baseConverterFrame.add(outputBaseComboBox, BorderLayout.WEST);
    baseConverterFrame.add(convertButton, BorderLayout.SOUTH);

    baseConverterFrame.pack();
    baseConverterFrame.setVisible(true);
    
}
    
    
    private void convertResultToSelectedBase() {
    int selectedIndex = baseComboBox.getSelectedIndex();
    String convertedResult;

    if (selectedIndex == 0) {
        // Decimal, no conversion needed
        convertedResult = currentInput;
    } else {
        // Determine the base
        int base = getBaseFromSelectedIndex(selectedIndex);

        try {
            if (currentInput.contains(".")) {
                // Handle the fractional part
                String[] parts = currentInput.split("\\.");
                BigInteger integerPart = new BigInteger(parts[0]);
                convertedResult = integerPart.toString(base) + "." + convertDecimalToBase(parts[1], base);
            } else {
                // Only the integer part
                BigInteger bigIntResult = new BigInteger(currentInput);
                convertedResult = bigIntResult.toString(base);
            }
        } catch (Exception ex) {
            convertedResult = "Invalid Input";
        }
    }

    inputField.setText(convertedResult);
}

    private void openTemperatureConverter() {
    JFrame converterFrame = new JFrame("Конвертер температуры");
    converterFrame.setSize(400, 200);

    String[] temperatureUnits = {"Цельсий", "Фаренгейт", "Кельвин", "Ранкин", "Реомюр"};
    JComboBox<String> fromComboBox = new JComboBox<>(temperatureUnits);
    JComboBox<String> toComboBox = new JComboBox<>(temperatureUnits);

    JTextField fromTextField = new JTextField(10);
    JTextField toTextField = new JTextField(40);
    fromTextField.setEditable(true);
    toTextField.setEditable(false);

    JButton convertButton = new JButton("Конвертировать");
    convertButton.addActionListener(e -> {
        double value = Double.parseDouble(fromTextField.getText());
        double convertedValue = convertTemperature(value, fromComboBox.getSelectedIndex(), toComboBox.getSelectedIndex());
        toTextField.setText(Double.toString(convertedValue));
    });

    JPanel panel = new JPanel();
    panel.add(fromComboBox);
    panel.add(fromTextField);
    panel.add(toComboBox);
    panel.add(toTextField);
    panel.add(convertButton);

    converterFrame.add(panel);
    converterFrame.setVisible(true);
}
    private void openMassConverter() {
    JFrame converterFrame = new JFrame("Конвертер массы");
    converterFrame.setSize(600, 300);

    String[] massUnits = {"Киллограмм", "Тонна", "Грамм", "Миллиграмм", "Микрограмм", "Центнер", "Фунт", "Унция", "Карат", "Гран", 
                          "Длинная тонна", "Короткая тонна", "Стоун", "Драхма", "Дань", "Цзинь", "Цянь", "Лян"};
    JComboBox<String> fromComboBox = new JComboBox<>(massUnits);
    JComboBox<String> toComboBox = new JComboBox<>(massUnits);

    JTextField fromTextField = new JTextField(10);  // Установите необходимую ширину поля
    JTextField toTextField = new JTextField(10);
    toTextField.setEditable(false);  // Запрет редактирования результата

    JButton convertButton = new JButton("Конвертировать");
    convertButton.addActionListener(e -> {
        BigDecimal value = new BigDecimal(fromTextField.getText());
        BigDecimal convertedValue = convertMass(value, fromComboBox.getSelectedIndex(), toComboBox.getSelectedIndex());
        toTextField.setText(convertedValue.setScale(5, BigDecimal.ROUND_HALF_UP).toString());
    });

    JPanel panel = new JPanel(new GridLayout(4, 1));
    panel.add(new JLabel("Из:"));
    panel.add(fromComboBox);
    panel.add(fromTextField);
    panel.add(new JLabel("В:"));
    panel.add(toComboBox);
    panel.add(toTextField);
    panel.add(convertButton);

    converterFrame.add(panel);
    converterFrame.setVisible(true);
}
    
    private BigDecimal convertMass(BigDecimal value, int fromIndex, int toIndex) {
    BigDecimal[] conversionRates = {
        new BigDecimal("1"),
        new BigDecimal("1000"),
        new BigDecimal("0.001"),
        new BigDecimal("0.000001"),
        new BigDecimal("0.000000001"),
        new BigDecimal("100"),
        new BigDecimal("0.45359237"),
        new BigDecimal("0.0283495"),
        new BigDecimal("0.0002"),
        new BigDecimal("0.00006479891"),
        new BigDecimal("1016.0469088"),
        new BigDecimal("907.18474"),
        new BigDecimal("6.35029318"),
        new BigDecimal("0.0017718451953125"),
        new BigDecimal("50"),
        new BigDecimal("1"),  
        new BigDecimal("0.5"),
        new BigDecimal("0.05"),
        new BigDecimal("0.002")
    };
    return value.multiply(conversionRates[fromIndex]).divide(conversionRates[toIndex], 10, BigDecimal.ROUND_HALF_UP);
}

    
    private double convertTemperature(double value, int fromIndex, int toIndex) {
    double celsiusValue;

    // Convert input temperature to Celsius
    switch (fromIndex) {
        case 0: // Celsius
            celsiusValue = value;
            break;
        case 1: // Fahrenheit
            celsiusValue = (value - 32) * 5 / 9;
            break;
        case 2: // Kelvin
            celsiusValue = value - 273.15;
            break;
        case 3: // Rankine
            celsiusValue = (value - 491.67) * 5 / 9;
            break;
        case 4: // Réaumur
            celsiusValue = value * 5 / 4;
            break;
        default:
            throw new IllegalArgumentException("Invalid temperature unit index.");
    }

    // Convert Celsius value to target temperature
    switch (toIndex) {
        case 0: // Celsius
            return celsiusValue;
        case 1: // Fahrenheit
            return celsiusValue * 9 / 5 + 32;
        case 2: // Kelvin
            return celsiusValue + 273.15;
        case 3: // Rankine
            return celsiusValue * 9 / 5 + 491.67;
        case 4: // Réaumur
            return celsiusValue * 4 / 5;
        default:
            throw new IllegalArgumentException("Invalid temperature unit index.");
    }
}
    
private int getBaseFromSelectedIndex(int selectedIndex) {
    int base = 10;
    switch (selectedIndex) {
        case 1: 
            base = 2; 
            break;
        case 2:
            base = 8; 
            break;
        case 3: 
            base = 16; 
            break;
        case 4:
            base = 3;
            break;
        case 5:
            base = 4;
            break;
        case 6:
            base = 5;
            break;
        case 7:
            base = 6;
            break;
        case 8:
            base = 7;
            break;
        case 9:
            base = 9;
            break;
        case 10:
            base = 11;
            break;
        case 11:
            base = 12;
            break;
        case 12:
            base = 13;
            break;
        case 13:
            base = 14;
            break;
        case 14:
            base = 15;
            break;
    }
    return base;
}

private String convertDecimalToBase(String decimalPart, int base) {
    StringBuilder result = new StringBuilder();
    double fraction = Double.parseDouble("0." + decimalPart);

    // Limit the precision for simplicity
    for (int i = 0; i < 20 && fraction > 0; i++) {
        fraction *= base;
        int integerPartOfFraction = (int) fraction;
        result.append(Integer.toString(integerPartOfFraction, base));
        fraction -= integerPartOfFraction;
    }

    return result.toString();
}

private void openCurrencyConverter() {
    JFrame converterFrame = new JFrame("Конвертер валюты");
    converterFrame.setSize(600, 300);

    String[] currencyUnits = {"Доллар", "Сомони", "Рубль", "Евро", "Юань", "Сум",};
    JComboBox<String> fromComboBox = new JComboBox<>(currencyUnits);
    JComboBox<String> toComboBox = new JComboBox<>(currencyUnits);

    JTextField fromTextField = new JTextField(10);  // Установите необходимую ширину поля
    JTextField toTextField = new JTextField(10);
    toTextField.setEditable(false);  // Запрет редактирования результата

    JLabel rateLabel = new JLabel("Курс:");
    JTextField rateTextField = new JTextField("1", 10); // Исходное значение 1 для удобства

    JButton convertButton = new JButton("Конвертировать");
    convertButton.addActionListener(e -> {
        BigDecimal value = new BigDecimal(fromTextField.getText());
        BigDecimal rate = new BigDecimal(rateTextField.getText());
        BigDecimal convertedValue = value.multiply(rate);
        toTextField.setText(convertedValue.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
    });

    JPanel panel = new JPanel(new GridLayout(4, 3));
    panel.add(new JLabel("Из:"));
    panel.add(fromComboBox);
    panel.add(fromTextField);
    panel.add(new JLabel("В:"));
    panel.add(toComboBox);
    panel.add(toTextField);
    panel.add(rateLabel);
    panel.add(rateTextField);
    panel.add(new JLabel());
    panel.add(convertButton);

    converterFrame.add(panel);
    converterFrame.setVisible(true);
}

private void openLengthConverter() {
    JFrame converterFrame = new JFrame("Конвертер длины");
    converterFrame.setSize(400, 200);

    String[] lengthUnits = {"Метры", "Километры", "Сантиметры", "Миллиметры","Дюмы","Футы","Фурлонг","Ярды"};
    JComboBox<String> fromComboBox = new JComboBox<>(lengthUnits);
    JComboBox<String> toComboBox = new JComboBox<>(lengthUnits);

    JTextField fromTextField = new JTextField();
    JTextField toTextField = new JTextField();
    fromTextField.setEditable(true);
    toTextField.setEditable(false);
    fromTextField.setPreferredSize(new Dimension(400, 50));
    toTextField.setPreferredSize(new Dimension(400, 50));

    JButton convertButton = new JButton("Конвертировать");
    convertButton.addActionListener(e -> {
        BigDecimal value = new BigDecimal(fromTextField.getText());
        BigDecimal convertedValue = convertLength(value, fromComboBox.getSelectedIndex(), toComboBox.getSelectedIndex());
        toTextField.setText(convertedValue.stripTrailingZeros().toPlainString());
    });

    JPanel panel = new JPanel();
    panel.add(toComboBox);
    panel.add(fromTextField);
    
    panel.add(fromComboBox);
    panel.add(toTextField);
    panel.add(convertButton);

    converterFrame.add(panel);
    converterFrame.setVisible(true);
}

private BigDecimal convertLength(BigDecimal value, int fromIndex, int toIndex) {
    BigDecimal[] conversionRates = {
        BigDecimal.ONE,
        new BigDecimal("0.001"),
        new BigDecimal("100"),
        new BigDecimal("1000"),
        //new BigDecimal("9.46073047e15"),
        new BigDecimal("0.0254"),
        new BigDecimal("0.3048"),
        new BigDecimal("201.168"),
        new BigDecimal("0.9144")
    };  
    return value.multiply(conversionRates[fromIndex]).divide(conversionRates[toIndex], MathContext.DECIMAL128);
}



private String convertToSelectedBase(String value) {
    int selectedIndex = baseComboBox.getSelectedIndex();
    if (selectedIndex == 0) {
        return value; // Decimal, no conversion needed
    }

    int base = getBaseFromSelectedIndex(selectedIndex);
    if (value.contains(".")) {
        String[] parts = value.split("\\.");
        BigInteger integerPart = new BigInteger(parts[0]);
        return integerPart.toString(base) + "." + convertDecimalToBase(parts[1], base);
    } else {
        BigInteger bigIntValue = new BigInteger(value);
        return bigIntValue.toString(base);
    }
}
   private void openAreaConverter() {
    JFrame converterFrame = new JFrame("Конвертер площади");
    converterFrame.setSize(500, 250);

    String[] areaUnits = {"кв. км", "гектар", "ары", "кв. м", "кв. дм", "кв. см", "кв. мм", "кв. мкм", "акры", "кв. мили", "кв. ярды", "кв. дюймы", "кв. перчи", "цин", "му", "кв. чи", "кв. цунь"};
    JComboBox<String> fromComboBox = new JComboBox<>(areaUnits);
    JComboBox<String> toComboBox = new JComboBox<>(areaUnits);

    JTextField fromTextField = new JTextField();
    JTextField toTextField = new JTextField();
    fromTextField.setEditable(true);
    toTextField.setEditable(false);
    fromTextField.setPreferredSize(new Dimension(200, 25));
    toTextField.setPreferredSize(new Dimension(200, 25));
    
    

    JButton convertButton = new JButton("Конвертировать");
    convertButton.addActionListener(e -> {
        BigDecimal value = new BigDecimal(fromTextField.getText());
        BigDecimal convertedValue = convertArea(value, fromComboBox.getSelectedIndex(), toComboBox.getSelectedIndex());
        toTextField.setText(convertedValue.toString());
    });

    JPanel panel = new JPanel();
    panel.add(fromComboBox);
    panel.add(fromTextField);
    panel.add(toComboBox);
    panel.add(toTextField);
    panel.add(convertButton);

    converterFrame.add(panel);
    converterFrame.setVisible(true);
}

private BigDecimal convertArea(BigDecimal value, int fromIndex, int toIndex) {
    // Rates relative to square meter (m^2)
    BigDecimal[] conversionRates = {
        new BigDecimal("1000000"),            // кв. км
        new BigDecimal("10000"),            // гектар
        new BigDecimal("100"),            // ары
        BigDecimal.ONE,                   // кв. м
        new BigDecimal("0.01"),           // кв. дм
        new BigDecimal("0.0001"),         // кв. см
        new BigDecimal("1000000"),           // кв. мм
        new BigDecimal("1000000000000"),          // кв. мкм
        new BigDecimal("4046.86"),        // акры
        new BigDecimal("2.59e6"),         // кв. мили
        new BigDecimal("0.836127"),       // кв. ярды
        new BigDecimal("0.00064516"),      // кв. дюймы
        new BigDecimal("25.2929"),        // кв. перчи
        new BigDecimal("0.071"),          // цин (предполагая, что 1 цин = 0.071 м^2)
        new BigDecimal("666.6666667"),    // му (предполагая, что 1 му = 2/3 акра)
        new BigDecimal("0.1111"),           // кв. чи (предполагая, что 1 чи = 0.01 м^2)
        new BigDecimal("0.001111")           // кв. цунь (предполагая, что 1 цунь = 0.001 м^2)
    };

    BigDecimal result = value.multiply(conversionRates[fromIndex]).divide(conversionRates[toIndex], 100, RoundingMode.HALF_UP);
    return result.stripTrailingZeros();
}

    private BigDecimal sqrt(BigDecimal value) {
        BigDecimal sqrt = BigDecimal.valueOf(Math.sqrt(value.doubleValue()));
        return sqrt.setScale(15, BigDecimal.ROUND_HALF_UP);
    }
    
    private void openVolumeConverter() {
    JFrame converterFrame = new JFrame("Конвертер объема");
    converterFrame.setSize(400, 200);

    String[] volumeUnits = {
        "Кубический сантиметр", "Кубический миллиметр", "Гектолитр", "Литр", "Децилитр", 
        "Сантилитр", "Миллилитр", "Кубический фут", "Кубический дюйм", 
        "Кубический ярд", "Акр-фут"
    };
    JComboBox<String> fromComboBox = new JComboBox<>(volumeUnits);
    JComboBox<String> toComboBox = new JComboBox<>(volumeUnits);

    JTextField fromTextField = new JTextField(10);  // Увеличил размер для удобства
    JTextField toTextField = new JTextField(20);   // Увеличил размер для удобства

    JButton convertButton = new JButton("Конвертировать");
    convertButton.addActionListener(e -> {
        BigDecimal value = new BigDecimal(fromTextField.getText());
        BigDecimal convertedValue = convertVolume(value, fromComboBox.getSelectedIndex(), toComboBox.getSelectedIndex());
        toTextField.setText(convertedValue.toString());
    });

    JPanel panel = new JPanel();
    panel.add(fromComboBox);
    panel.add(fromTextField);
    panel.add(toComboBox);
    panel.add(toTextField);
    panel.add(convertButton);

    converterFrame.add(panel);
    converterFrame.setVisible(true);
}

    private void openDataConverter() {
    JFrame converterFrame = new JFrame("Конвертер данных");
    converterFrame.setSize(400, 200);

    String[] dataUnits = {
        "Байт", "Килобайт", "Мегабайт", "Гигабайт", "Терабайт", "Петабайт",
        "Эксабайт", "Зеттабайт", "Йоттабайт", "Роннабайт", "Кветтабайт",
        "Бит", "Килобит", "Мегабит"
    };
    JComboBox<String> fromComboBox = new JComboBox<>(dataUnits);
    JComboBox<String> toComboBox = new JComboBox<>(dataUnits);

    JTextField fromTextField = new JTextField(10);  // Размер поля ввода
    JTextField toTextField = new JTextField(40);   // Размер поля ввода

    JButton convertButton = new JButton("Конвертировать");
    convertButton.addActionListener(e -> {
        BigDecimal value = new BigDecimal(fromTextField.getText());
        BigDecimal convertedValue = convertData(value, fromComboBox.getSelectedIndex(), toComboBox.getSelectedIndex());
        toTextField.setText(convertedValue.setScale(10, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString());
    });

    JPanel panel = new JPanel();
    panel.add(fromComboBox);
    panel.add(fromTextField);
    panel.add(toComboBox);
    panel.add(toTextField);
    panel.add(convertButton);

    converterFrame.add(panel);
    converterFrame.setVisible(true);
}

private BigDecimal convertData(BigDecimal value, int fromIndex, int toIndex) {
    BigDecimal[] conversionRates = {
        BigDecimal.ONE,
        new BigDecimal("1024"), 
        new BigDecimal("1048576"), 
        new BigDecimal("1073741824"), 
        new BigDecimal("1099511627776"), 
        new BigDecimal("1125899906842624"), 
        new BigDecimal("1152921504606846976"), 
        new BigDecimal("1180591620717411303424"),
        new BigDecimal("1208925819614629174706176"),
        new BigDecimal("1237940039285380274899124224"),
        new BigDecimal("1267650600228229401496703205376"),
        new BigDecimal("0.125"),
        new BigDecimal("128"),
        new BigDecimal("131072"),
    };

    return value.multiply(conversionRates[fromIndex]).divide(conversionRates[toIndex], 10, BigDecimal.ROUND_HALF_UP);
}
    
private BigDecimal convertVolume(BigDecimal value, int fromIndex, int toIndex) {
    BigDecimal[] conversionRates = {
        new BigDecimal("0.001"), new BigDecimal("0.000001"), new BigDecimal("100"),
        new BigDecimal("1"), new BigDecimal("0.1"), new BigDecimal("0.01"),
        new BigDecimal("0.001"), new BigDecimal("28.3168"), new BigDecimal("0.0000163871"),
        new BigDecimal("764.5549"), new BigDecimal("1233.48183754752")
    };
    return value.multiply(conversionRates[fromIndex]).divide(conversionRates[toIndex], 10, BigDecimal.ROUND_HALF_UP);
}
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                CalculatorGUI calculator = new CalculatorGUI();
                calculator.setVisible(true);
            }
        });
    }
}

