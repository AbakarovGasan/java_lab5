import java.awt.geom.Rectangle2D;

/**
* Этот класс является подклассом Fractal Generator. Он используется для вычисления фрактала "Пылающий корабль".
 */
public class BurningShip extends FractalGenerator
{
    /**
     * максимальное число итерации
     */
    public static final int MAX_ITERATIONS = 2000;
    
    /**
     * Этот метод позволяет генератору фракталов указать,
     *  какая часть * комплексной плоскости является наиболее
     *  интересной для фрактала. * Ему передается прямоугольный объект,
     *  и метод изменяет * поля прямоугольника, чтобы показать правильный
     *  начальный диапазон для фрактала. * Эта реализация устанавливает
     *  начальный диапазон равным x = -2, y = -2,5, * width = height = 4.
     */
    public void getInitialRange(Rectangle2D.Double range)
    {
        range.x = -2;
        range.y = -2.5;
        range.width = 4;
        range.height = 4;
    }
    
    /**
     * Этот метод реализует итерационную функцию для фрактала "Пылающий корабль"
     */
    public int numIterations(double x, double y)
    {
        /** начать итерации с 0. */
        int iteration = 0;
        /** инициализировать zreal и zimaginary. */
        double zreal = 0;
        double zimaginary = 0;
        
        /**
           Вычислить Zn = (abs [Re (zn-1)] + i (abs [img (zn-1)])) ^ 2 + c, где значения - комплексные числа,
        *  представленные zreal и zimaginary, Z0 = 0, а c - конкретная
        *  точка во фрактале, который мы отображаем (задается x и y).
        *  Он повторяется до тех пор, пока Z ^ 2> 4
        *  (абсолютное значение Z больше 2) или пока не будет
        *  достигнуто максимальное количество итераций.*/
        
        while (iteration < MAX_ITERATIONS &&
               zreal * zreal + zimaginary * zimaginary < 4)
        {
            double zrealUpdated = zreal * zreal - zimaginary * zimaginary + x;
            double zimaginaryUpdated = 2 * Math.abs(zreal)
            * Math.abs(zimaginary) + y;
            
            zreal = zrealUpdated;
            zimaginary = zimaginaryUpdated;
            
            iteration += 1;
        }
        
        /**
          * Если количество максимальных итераций достигнуто, возвращает -1
         */
        if (iteration == MAX_ITERATIONS)
        {
            return -1;
        }
        
        return iteration;
    }
    
    /** 
     * 
     */
    public String toString() {
        return "Пылающий корабль";
    }
    
}
