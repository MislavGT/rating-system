package hr.pmf.project.rating;


import java.awt.Color; 
import java.awt.BasicStroke; 
import org.jfree.chart.ui.ApplicationFrame;
import java.util.ArrayList;
import javax.swing.JFrame;
import org.jfree.chart.ChartPanel; 
import org.jfree.chart.JFreeChart; 
import org.jfree.data.xy.XYDataset; 
import org.jfree.data.xy.XYSeries; 
import org.jfree.chart.plot.XYPlot; 
import org.jfree.chart.ChartFactory; 
import org.jfree.chart.plot.PlotOrientation; 
import org.jfree.data.xy.XYSeriesCollection; 
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

public class Chart extends JFrame{
    
    private String title;
    private ArrayList<Double> list = new ArrayList<>();
    
    public Chart(String title, ArrayList<Double> list){
        super(title);
        this.title = title;
        this.list = list;
        this.draw();
    }
    
    public final XYDataset createDataset(ArrayList<Double> list){
        XYSeries data = new XYSeries("data");
        for(int i = 0; i < list.size(); i++){
            data.add(i + 1, list.get(i));
        }
        
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(data);
        
        return dataset;
    }
    
    public void draw(){
        JFreeChart chart = ChartFactory.createXYLineChart(
        this.title,
        "competition",
        "performance",
        createDataset(this.list),
        PlotOrientation.VERTICAL,
        true, true, false);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(600, 400));
        final XYPlot plot = chart.getXYPlot( );
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
        renderer.setSeriesPaint(0, Color.RED );
        renderer.setSeriesStroke(0, new BasicStroke(4.0f));
        plot.setRenderer(renderer); 
        setContentPane(chartPanel);
        this.pack();
        this.setVisible(true); 
    }
    
    public static void main(String[] args){
        
        ArrayList<Double> data = new ArrayList<>();
        data.add(5d); data.add(6d); data.add(100d); data.add(1000d);
        
        Chart chart = new Chart("Da", data);
    }
}
