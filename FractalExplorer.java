import java.awt.*;
import javax.swing.*;
import java.awt.geom.Rectangle2D;
import java.awt.event.*;
import javax.swing.JFileChooser.*;
import javax.swing.filechooser.*;
import javax.imageio.ImageIO.*;
import java.awt.image.*;

/**
  * Этот класс позволяет исследовать различные части фрактала с
  * созданием и отображением графического интерфейса Swing и обработкой событий, вызванных различными
  * взаимодействиями с пользователем.
  */
public class FractalExplorer
{
    /** Целочисленный размер дисплея - это ширина и высота дисплея в пикселях. **/
    private int displaySize;
    
    /**
      * Ссылка JImageDisplay для обновления отображения с помощью различных методов 
      * после того как фрактал вычислен.
     */
    private JImageDisplay display;
    
    
    private FractalGenerator fractal;
    
    private Rectangle2D.Double range;
    
    /**
      * Конструктор, который принимает размер дисплея, сохраняет его и
      * инициализирует объекты диапазона и фрактал-генератора.
    **/
    
    public FractalExplorer(int size) {
        
        displaySize = size;
        
        
        fractal = new Mandelbrot();
        range = new Rectangle2D.Double();
        fractal.getInitialRange(range);
        display = new JImageDisplay(displaySize, displaySize);
        
    }
    
    /**
      * Этот метод инициализирует графический интерфейс Swing с помощью JFrame, содержащего
      * объект JImageDisplay и кнопка для сброса дисплея, кнопка
      * для сохранения текущего фрактального изображения и JComboBox для выбора
      * тип фрактала. JComboBox содержится в панели JPanel с меткой.
    **/
    
    public void createAndShowGUI()
    {
        display.setLayout(new BorderLayout());
        JFrame myFrame = new JFrame("Фракталы");
        
        /** Добавляем объект отображения изображения в
          * Позиция BorderLayout.CENTER.
          **/
        myFrame.add(display, BorderLayout.CENTER);
        
        /** создаем кнопку "восстановить". **/
        JButton resetButton = new JButton("восстановить");
        /* добавить обработчик нажатия кнопки*/
        resetButton.addActionListener(new ResetHandler());
        
        display.addMouseListener(new MouseHandler());
        
        /** Установить операцию закрытия фрейма по умолчанию на "exit". **/
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    
        JComboBox myComboBox = new JComboBox();
        
        /** добавить все фракталы **/
        myComboBox.addItem(new Mandelbrot());
        myComboBox.addItem(new Tricorn());
        myComboBox.addItem(new BurningShip());
        
        /* добавить обработчик нажатия кнопки*/
        myComboBox.addActionListener(new ComboBoxHandler());
        
        /**
          * Создайте новый объект JPanel, добавьте объект JLabel и JComboBox
          * объект к нему, и добавить панель в рамку в нижней
          * позиция в макете.
         */
        JPanel myPanel = new JPanel();
        JLabel myLabel = new JLabel("Фрактал:");
        myPanel.add(myLabel);
        myPanel.add(myComboBox);
        myFrame.add(myPanel, BorderLayout.NORTH);
        
        /** создаем кнопку "сохранить". **/
        JButton saveButton = new JButton("сохранить");
        JPanel myBottomPanel = new JPanel();
        myBottomPanel.add(saveButton);
        myBottomPanel.add(resetButton);
        myFrame.add(myBottomPanel, BorderLayout.SOUTH);
        /* добавить обработчик нажатия кнопки*/
        saveButton.addActionListener(new SaveHandler());
        
        
        /**
         * Разместить содержимое фрейма так, чтобы оно было видно, и
         * запретить изменение размера окна.
         */
        myFrame.pack();
        myFrame.setVisible(true);
        myFrame.setResizable(false);
        
    }
    
    /**
     * Вспомогательный метод для отображения фрактала. Этот метод зацикливается
     * на каждый пиксель на дисплее и вычисляет количество
     * итераций для соответствующих координат во фрактале
     * Область отображения. Если количество итераций равно -1, установить цвет пикселя
     * в черный. В противном случае выберите значение, основанное на количестве итераций.
     * Обновить дисплей цветом для каждого пикселя и перекрасите
     * JImageDisplay, когда все пиксели нарисованы.
     */
    private void drawFractal()
    {
        /** Перебирать каждый пиксель на дисплее **/
        for (int x=0; x<displaySize; x++){
            for (int y=0; y<displaySize; y++){
                
                /**
                 * Найти соответствующие координаты xCoord и yCoord
                 * в области отображения фрактала.
                 */
                double xCoord = fractal.getCoord(range.x,
                range.x + range.width, displaySize, x);
                double yCoord = fractal.getCoord(range.y,
                range.y + range.height, displaySize, y);
                
                /**
                 * Вычислить количество итераций для координат в
                 * область отображения фрактала.
                 */
                int iteration = fractal.numIterations(xCoord, yCoord);
                
                /* Если количество итераций равно -1, установить черный пиксель. */
                if (iteration == -1){
                    display.drawPixel(x, y, 0);
                }
                
                else {
                    /**
                     * В противном случае выберите значение оттенка на основе числа
                     * итераций.
                     */
                    float hue = 0.7f + (float) iteration / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                
                    /** Обновление дисплея цветом для каждого пикселя. **/
                    display.drawPixel(x, y, rgbColor);
                }
                
            }
        }/*
        * Когда все пиксели будут нарисованы, перекрасить JImageDisplay в соответствии с
          * текущее содержимым его изображения. */
        display.repaint();
    }
    
    private class ComboBoxHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            /*  получить фрактал, выбранный пользователем 
             *  и отобразить его. 
             */
            JComboBox mySource = (JComboBox) e.getSource();
            fractal = (FractalGenerator) mySource.getSelectedItem();
            fractal.getInitialRange(range);
            drawFractal();
        }
    }
    
    private class SaveHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            /** сохраняем текущее  фрактальное изображение. */
            
                /* Разрешить пользователю выбрать файл для сохранения изображения.*/
                JFileChooser myFileChooser = new JFileChooser();
                
                /* сохранять только png файлы*/
                FileFilter extensionFilter =
                new FileNameExtensionFilter("PNG Images", "png");
                myFileChooser.setFileFilter(extensionFilter);
              
              
              /* Гарантирует, что средство выбора файлов 
               * не разрешит другие типы файлов 
               */
                myFileChooser.setAcceptAllFileFilterUsed(false);
            
            
            /*Всплывает окно «Сохранить файл», в котором пользователь
             * может выбрать  каталог и файл для сохранения.
             */
                int userSelection = myFileChooser.showSaveDialog(display);
            
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    /* если пользователь выбрал, куда сохранять файл
                     * то сохранить файл
                     */
                    java.io.File file = myFileChooser.getSelectedFile();
                    
                    try {
                        BufferedImage displayImage = display.getImage();
                        javax.imageio.ImageIO.write(displayImage, "png", file);
                    }
                    catch (Exception exception) {
                        /* если произошла ошибка, то вывести ошибку*/
                        JOptionPane.showMessageDialog(display,
                        exception.getMessage(), "Cannot Save Image",
                        JOptionPane.ERROR_MESSAGE);
                    }
                }
                return;
        }
    }
    
    private class ResetHandler implements ActionListener
    {
        /**
         * Обработчик сбрасывает диапазон до начального диапазона, 
         * заданного параметром, а затем рисует фрактал.
         */
        public void actionPerformed(ActionEvent e)
        {
            fractal.getInitialRange(range);
            drawFractal();
        }
    }
    
    private class MouseHandler extends MouseAdapter
    {
        /**
         * Когда обработчик получает событие щелчка мыши, он отображает пиксель-
          * координаты щелчка в области фрактала, который
          * отображается, а затем вызывает функцию RecenterAndZoomRange () генератора
          * метод с координатами, по которым был выполнен щелчок, и шкалой 0,7.
         */
        @Override
        public void mouseClicked(MouseEvent e)
        {
            /** получить х координату после клика **/
            int x = e.getX();
            double xCoord = fractal.getCoord(range.x,
            range.x + range.width, displaySize, x);
            
            /** получить у координату после клика **/
            int y = e.getY();
            double yCoord = fractal.getCoord(range.y,
            range.y + range.height, displaySize, y);
            
            /**
             * вызывает метод recenterAndZoomRange() с
             * координатами где мы кликнули.
             */
             
             /**
              *  если кликнули правой кнопкой мыши, то увеличить маштаб, если другой,
              * то уменьшить маштаб
              */
            if (e.getButton()==1) fractal.recenterAndZoomRange(range, xCoord, yCoord, 0.5);
            else fractal.recenterAndZoomRange(range, xCoord, yCoord, 2);
            /**
             * перерисовать фрактал.
             */
            drawFractal();
        }
    }
    
    
/** * Статический метод main () для запуска FractalExplorer.
 *  Инициализирует новый экземпляр * FractalExplorer с размером отображения 800,
 *  вызывает * createAndShowGUI () в объекте проводника, а затем вызывает * drawFractal ()
 в проводнике, чтобы увидеть начальное представление. */
    public static void main(String[] args)
    {
        FractalExplorer displayExplorer = new FractalExplorer(800);
        displayExplorer.createAndShowGUI();
        displayExplorer.drawFractal();
    }
}
