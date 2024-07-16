 Это демонстрационное приложение для построения свечного графика с дополнительными подграфиками (объема и др.)
 Для отображения окон и графиков используется библиотека javax.swing
 Для отображения свечного графика используется библиотека org.jfree https://jfree.org/jfreechart/
 
     <dependency>
     <groupId>org.jfree</groupId>
     <artifactId>jfreechart</artifactId>
     <version>1.5.5</version>
     </dependency>
 
 1. Класс для построения графиков и подграфикоа - JFreeCandlesChart, который наследуется от класса Jpanel из библиотеки
   javax.swing
 2.  Класс CustomHighLowItemLabelGenerator, наследуемый от класса org.jfree.chart.labels.HighLowItemLabelGenerator, 
     используется для реализации создания всплывающей подсказки при наведении курсора мыши на свечу.
 3. Класс JfreeCandlesChartDemo, который наследуется от класса Jpanel из библиотеки
    javax.swing, используется для финального создания окна Jframe и реализации добавления свечей из списка объектов
    свечей List<CandleMoex> и финальной команды создания графиков в окне Jframe 
 4. Класс CrosshairOverlayDemo1.java используется как образец демонстрации перекрестия, которое движется вместе
    с курсором мыши
