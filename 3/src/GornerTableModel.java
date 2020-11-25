import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class GornerTableModel extends AbstractTableModel {
    private Double[] coefficients;
    private Double from;
    private Double to;
    private Double step;
    public GornerTableModel(Double from, Double to, Double step,
                            Double[] coefficients) {
        this.from = from;
        this.to = to;
        this.step = step;
        this.coefficients = coefficients;
    }
    public Double getFrom() {
        return from;
    }
    public Double getTo() {
        return to;
    }
    public Double getStep() {
        return step;
    }
    public int getColumnCount() {
// В данной модели четыре столбца
        return 4;
    }
    public int getRowCount() {
// Вычислить количество точек между началом и концом отрезка
// исходя из шага табулирования
        return new Double(Math.ceil((to-from)/step)).intValue();
    }
    public Object getValueAt(int row, int col) {
// Вычислить значение X как НАЧАЛО_ОТРЕЗКА + ШАГ*НОМЕР_СТРОКИ
        double x = from + step*row;
        if (col==0) {
// Если запрашивается значение 1-го столбца, то это X
            return x;
        }
        if(col == 1){
// Если запрашивается значение 2-го столбца, то это значение
// многочлена
// Вычисление значения в точке по схем Горенера
            Double result = 0.0;
            for (Double coefficient : coefficients) {
                result = result * x + coefficient;
            }
            return result;
        }
        if(col == 2){
            // Если запрашивается значение 2-го столбца, то это просто значение
// многочлена
            Double result = 0.0;
            for(int i = 0; i < coefficients.length; ++i)
            {
                result = result + Math.pow(x, coefficients.length - i - 1) * coefficients[i];
            }
            return result;
        }
        else{
            Double result1 = 0.0;
            Double result2 = 0.0;
            for (Double coefficient : coefficients) {
                result1 = result1 * x + coefficient;
            }
            for(int i = 0; i < coefficients.length; ++i)
            {
                result2 = result2 + Math.pow(x, coefficients.length - i - 1) * coefficients[i];
            }
            return Math.abs(result1 - result2);
        }
    }
    public String getColumnName(int col) {
        switch (col) {
            case 0:
// Название 1-го столбца
                return "Значение X";
            case 1:
// Название 2-го столбца
                return "Значение схема Горенера";
            case 2:
// Название 3-го столбца
                return "Значение многочлена";
            default:
// Название 4-го столбца
                return "Разница";
        }
    }
    public Class<?> getColumnClass(int col) {
// Во всех столбцах находятся значения типа Double
        return Double.class;
    }
}