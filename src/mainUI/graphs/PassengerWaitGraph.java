package mainUI.graphs;



import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;


import javax.swing.JFrame;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;

import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import elevatorSystem.Log; 


public class PassengerWaitGraph extends ApplicationFrame {

	JFreeChart chart;
	
	private TimeSeries Passenger = new TimeSeries("Passenger Wait Time", Millisecond.class);
	
	public PassengerWaitGraph(final String title, Boolean B){
		super(title);
		
		PassengerData Wait = new PassengerData();
	    new Thread(Wait).start();
	    
	    TimeSeriesCollection dataset = new TimeSeriesCollection();
	    dataset.addSeries(Passenger);
	    chart = ChartFactory.createTimeSeriesChart(
	            "Average Passenger Wait Time",
	            "Time",
	            "Time Waited(s)",
	            dataset,
	            true,
	            true,
	            false
	        );
	        XYPlot plot = chart.getXYPlot();
	        ValueAxis axis = plot.getDomainAxis();
	        axis.setAutoRange(true);
	        axis.setFixedAutoRange(40000.0);

	        JFrame frame = new JFrame("Average Passenger Wait Time");
	        ChartPanel label = new ChartPanel(chart);
	        frame.getContentPane().add(label);
	             
	        
	        frame.setVisible(B);
	        frame.pack();
	        
	}
	
	 class PassengerData implements Runnable 
	    {
	   

	        public void run() 
	        {
	        	
	        	
	            while(true) 
	            {
	            	
	                Passenger.addOrUpdate(new Millisecond(), Log.getAvgPassWait()/1000.0);
	                
	                
	                try {
	                    Thread.sleep(200);
	                    
	                } catch (InterruptedException ex) {
	                    System.out.println(ex);
	                }
	                
	                 
	            }
	        }
	    }
   public void Print(){
    	   
    	   try {
           	ChartUtilities.saveChartAsPNG(new File("PassengerWaitGraph.jpg"), chart, 500, 270, null);
           } catch (Exception e) {
               System.out.println("Problem occurred creating chart.");
           }
       }
       
       public void Close(){
    	   
    	   dispose();
       }
  /* public static void main(String[] args){

   		final PassengerWaitGraph demo = new PassengerWaitGraph("PassengerWaitGraph.jpg");   		
   		RefineryUtilities.centerFrameOnScreen(demo);
   		demo.setVisible(false);
   		demo.Print();

   }*/

	
    
}
