 Это демонстрационное приложение для построения свечного графика с дополнительными подграфиками (объема и др.)
 Для отображения окон и графиков используется библиотека javax.swing
 Для отображения свечного графика используется библиотека org.jfree https://jfree.org/jfreechart/
 
     <dependency>
     <groupId>org.jfree</groupId>
     <artifactId>jfreechart</artifactId>
     <version>1.5.5</version>
     </dependency>
 
 1. Класс JFreeCandlesChart - для построения графиков и подграфикоа и реализации добавления свечей из списка объектов
    свечей List<CandleMoex>-  который наследуется от класса Jpanel из библиотеки javax.swing
 2.  Класс CustomHighLowItemLabelGenerator, наследуемый от класса org.jfree.chart.labels.HighLowItemLabelGenerator, 
     используется для реализации создания всплывающей подсказки при наведении курсора мыши на свечу.
 3. Класс JfreeCandlesChartDemo, используется для финального создания окна Jframe  и финальной команды создания  
     графиков в окне Jframe. 
 4. Класс CrosshairOverlayDemo1.java используется как образец демонстрации перекрестия, которое движется вместе
    с курсором мыши
