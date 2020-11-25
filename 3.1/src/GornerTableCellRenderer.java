import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class GornerTableCellRenderer implements TableCellRenderer {
    private JPanel panel = new JPanel();
    private JLabel label = new JLabel();
    // Ищем ячейки, строковое представление которых совпадает с needle
// (иголкой). Применяется аналогия поиска иголки в стоге сена, в роли
// стога сена - таблица
    private String needle = null;
    private String type = null;
    private DecimalFormat formatter = (DecimalFormat)NumberFormat.getInstance();
    public GornerTableCellRenderer() {
// Показывать только 5 знаков после запятой
        formatter.setMaximumFractionDigits(5);
// Не использовать группировку (т.е. не отделять тысячи
// ни запятыми, ни пробелами), т.е. показывать число как "1000",
// а не "1 000" или "1,000"
        formatter.setGroupingUsed(false);
// Установить в качестве разделителя дробной части точку, а не
// запятую. По умолчанию, в региональных настройках
// Россия/Беларусь дробная часть отделяется запятой
        DecimalFormatSymbols dottedDouble = formatter.getDecimalFormatSymbols();
        dottedDouble.setDecimalSeparator('.');
        formatter.setDecimalFormatSymbols(dottedDouble);
// Разместить надпись внутри панели
        panel.add(label);
    }
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
// Преобразовать double в строку с помощью форматировщика
        String formattedDouble = formatter.format(value);
// Установить текст надписи равным строковому представлению числа
        if((Double)value < 0.0)
            panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        else if ((Double)value > 0.0)
            panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        else
            panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        label.setText(formattedDouble);

        if (col!=0 && type!=null) {

            switch(type)
            {
                case "FindingNum":
                    if(needle != null && needle.equals(formattedDouble)) {
                        panel.setBackground(Color.RED);
                    }
                    else{
                        panel.setBackground(Color.WHITE);
                    }
                    break;
                case "FindingPrimeNum":
                    boolean primary = false;
                    if((Double)value >= 1 && Math.abs(Math.round((Double)value) - (Double)value) < 0.1){
                        long round = Math.round((Double)value);
                        primary = true;
                        for(int i = 2; i <= Math.sqrt(round); ++i)
                        {
                            if(round % i == 0)
                            {
                                primary = false;
                                break;
                            }
                        }
                    }
                    if(primary){
                        panel.setBackground(Color.GREEN);
                    }
                    else{
                        panel.setBackground(Color.WHITE);
                    }
                    break;
                default:
                    panel.setBackground(Color.WHITE);
                    break;
            }
        }
        else{
            panel.setBackground(Color.WHITE);
        }
        return panel;
    }
    public void setNeedle(String needle) {
        this.needle = needle;
    }

    public void setType(String type) {
        this.type = type;
    }
}