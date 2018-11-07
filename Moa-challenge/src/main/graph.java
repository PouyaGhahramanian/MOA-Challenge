
package main;

import javax.swing.JFrame;   
import org.jfree.chart.ChartFactory;   
import org.jfree.chart.ChartPanel;   
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;   
import org.jfree.data.xy.XYSeries;   
import org.jfree.data.xy.XYSeriesCollection; 

public class graph extends JFrame{   

	private static final long serialVersionUID = 0L;
	public graph(String title, int data_number, double[] NB_data, double[] HT_data, double[] ensemble_data) throws Exception{   
		super(title);   
		XYDataset dataset = createDataset(data_number, NB_data, HT_data, ensemble_data);   
		JFreeChart chart = createChart(dataset);   
		ChartPanel chartPanel = new ChartPanel(chart);   
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));   
		setContentPane(chartPanel);   
		chartPanel.setMouseZoomable(true, false);
		
		XYPlot xyPlot=(XYPlot) chart.getPlot();
		NumberAxis yAxis=(NumberAxis) xyPlot.getRangeAxis();
		yAxis.setRange(60, 76);
		yAxis.setTickUnit(new NumberTickUnit(1));
		
		
		NumberAxis xAxis=(NumberAxis) xyPlot.getDomainAxis();
		xAxis.setRange(1, 100);
		xAxis.setTickUnit(new NumberTickUnit(10));
		
	}   

	private XYDataset createDataset(int data_number, double[] NB_data, double[] HT_data, double[] ensemble_data) throws Exception {   

		XYSeries series_NB = new XYSeries("Naive Bayes");   
		XYSeries series_HT = new XYSeries("Hoeffding Tree");   
		XYSeries series_ensemble = new XYSeries("Ensemble");   
		for(int i=0; i<data_number; i++) {
			series_NB.add(i, NB_data[i]);
			series_HT.add(i, HT_data[i]);
			series_ensemble.add(i, ensemble_data[i]);
		}

		XYSeriesCollection dataset = new XYSeriesCollection();   
		dataset.addSeries(series_NB);  
		dataset.addSeries(series_HT); 
		dataset.addSeries(series_ensemble); 
		
		return dataset;   
	}   

	private JFreeChart createChart(XYDataset dataset) {   

		JFreeChart chart = ChartFactory.createXYLineChart(   
				"Accuracy of Classifiers",
				"Data Instances (*100)",
				"Accuracy (%)", 
				dataset, 
				PlotOrientation.VERTICAL,   
				true,  
				true, 
				false
		);   
		return chart;   
	}   
}