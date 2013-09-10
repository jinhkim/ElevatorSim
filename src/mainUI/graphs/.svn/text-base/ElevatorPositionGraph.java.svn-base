package mainUI.graphs;






import java.io.File;

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

import elevatorSystem.Controller;
import elevatorSystem.Log;



   
    

	public class ElevatorPositionGraph extends ApplicationFrame
{
    	 /** The plot. */
       private XYPlot plot;
      
       private Controller controller;
       private Log Log;
       
       // Number of Elevators In System       
       private int ElevatorCount;
 
       // 10 Elevators Initialised 
        private TimeSeries Elevator1 = new TimeSeries("Elevator1", Millisecond.class);
        private TimeSeries Elevator2 = new TimeSeries("Elevator2", Millisecond.class);
        private TimeSeries Elevator3 = new TimeSeries("Elevator3", Millisecond.class);
        private TimeSeries Elevator4 = new TimeSeries("Elevator4", Millisecond.class);
        private TimeSeries Elevator5 = new TimeSeries("Elevator5", Millisecond.class);
        private TimeSeries Elevator6 = new TimeSeries("Elevator6", Millisecond.class);
        private TimeSeries Elevator7 = new TimeSeries("Elevator7", Millisecond.class);
        private TimeSeries Elevator8 = new TimeSeries("Elevator8", Millisecond.class);
        private TimeSeries Elevator9 = new TimeSeries("Elevator9", Millisecond.class);
        private TimeSeries Elevator10 = new TimeSeries("Elevator10", Millisecond.class);
        
        JFreeChart chart;
       
      public ElevatorPositionGraph(final String title, Controller c, Boolean B)
       {
       super(title);
       controller = c;
       ElevatorCount = controller.getElevNum();
     
       ElevatorData myElevatorData = new ElevatorData();
       new Thread(myElevatorData).start();

        TimeSeriesCollection dataset = createDataSet(); // Dataset for Graph
         chart = ChartFactory.createTimeSeriesChart(
           "Elevator Position Graph",
           "Time",
           "Floor",
           dataset,
           true,
           true,
           false
       );
       XYPlot plot = chart.getXYPlot();
       ValueAxis axis = plot.getDomainAxis();
       axis.setAutoRange(true);
       axis.setFixedAutoRange(40000.0);

       JFrame frame = new JFrame("Elevator Position Graph");
       ChartPanel label = new ChartPanel(chart);
       frame.getContentPane().add(label);
            
       frame.setVisible(B);
       frame.pack();
      
       }
       
    
    
    private TimeSeriesCollection createDataSet()
    {   	
    	TimeSeriesCollection dataset = new TimeSeriesCollection();
    	
    	switch(ElevatorCount){
    	case 1:   dataset.addSeries(Elevator1); break;
    	case 2:   dataset.addSeries(Elevator1);
    			  dataset.addSeries(Elevator2); break;
    	case 3:   dataset.addSeries(Elevator1);
    			  dataset.addSeries(Elevator2);
		  		  dataset.addSeries(Elevator3); break;
    	case 4:   dataset.addSeries(Elevator1);
		  		  dataset.addSeries(Elevator2);
		  		  dataset.addSeries(Elevator3);
		          dataset.addSeries(Elevator4); break;
    	case 5:   dataset.addSeries(Elevator1);
		  		  dataset.addSeries(Elevator2);
		  		  dataset.addSeries(Elevator3);
		  		  dataset.addSeries(Elevator4);
		  		  dataset.addSeries(Elevator5); break;
    	case 6:   dataset.addSeries(Elevator1);
		  		  dataset.addSeries(Elevator2);
		  		  dataset.addSeries(Elevator3);
		  		  dataset.addSeries(Elevator4);
		  		  dataset.addSeries(Elevator5);
		  		  dataset.addSeries(Elevator6); break;
    	case 7:   dataset.addSeries(Elevator1);
		  		  dataset.addSeries(Elevator2);
		  		  dataset.addSeries(Elevator3);
		  		  dataset.addSeries(Elevator4);
		  		  dataset.addSeries(Elevator5);
		  		  dataset.addSeries(Elevator6); 
		  		  dataset.addSeries(Elevator7); break;
    	case 8:   dataset.addSeries(Elevator1);
    			  dataset.addSeries(Elevator2);
    			  dataset.addSeries(Elevator3);
    			  dataset.addSeries(Elevator4);
			      dataset.addSeries(Elevator5);
			      dataset.addSeries(Elevator6); 
			      dataset.addSeries(Elevator7); 
			      dataset.addSeries(Elevator8);break;
    	case 9:   dataset.addSeries(Elevator1);
    			  dataset.addSeries(Elevator2);
    			  dataset.addSeries(Elevator3);
    			  dataset.addSeries(Elevator4);
    			  dataset.addSeries(Elevator5);
    			  dataset.addSeries(Elevator6); 
    			  dataset.addSeries(Elevator7); 
    			  dataset.addSeries(Elevator8);
    			  dataset.addSeries(Elevator9);break;
    	case 10:  dataset.addSeries(Elevator1);
    			  dataset.addSeries(Elevator2);
    			  dataset.addSeries(Elevator3);
    			  dataset.addSeries(Elevator4);
    		      dataset.addSeries(Elevator5);
    			  dataset.addSeries(Elevator6); 
    			  dataset.addSeries(Elevator7); 
    			  dataset.addSeries(Elevator8);
    			  dataset.addSeries(Elevator9);
    			  dataset.addSeries(Elevator10);break;  	
    	
    	}    	
    	
    
    	return dataset;
    	
    	
    }

   

     class ElevatorData implements Runnable 
    {
     

        public void run() 
        {
        	
        	float num1 = 0;
        	float num2 = 0;
        	float num3 = 0;
        	float num4 = 0;
        	float num5 = 0;
        	float num6 = 0;
        	float num7 = 0;
        	float num8 = 0;
        	float num9 = 0;
        	float num10 = 0;
        	
        	int temp1=0,temp2=0,temp3=0,temp4=0;
        	
        	
        
            while(true) 
            {
            	float[] temp = Log.getElevatorPos();
            	switch(ElevatorCount){
            	case 1 : num1 = temp[0]; break;
            	case 2 : num1 = temp[0];
            			 num2 = temp[1];break;
            	case 3 : num1 = temp[0];
            			 num2 = temp[1];
   			 			 num3 = temp[2];break;
            	case 4:  num1 = temp[0];
            			 num2 = temp[1];
            			 num3 = temp[2];
		 			     num4 = temp[3];break;
            	case 5:  num1 = temp[0];
            		     num2 = temp[1];
            			 num3 = temp[2];
            			 num4 = temp[3];
            			 num5 = temp[4];break;
            	case 6:  num1 = temp[0];
   		     			 num2 = temp[1];
   		     			 num3 = temp[2];
   		     			 num4 = temp[3];
   		     			 num5 = temp[4];
   		     			 num6 = temp[5];break;
            	case 7:  num1 = temp[0];
            			 num2 = temp[1];
            			 num3 = temp[2];
            			 num4 = temp[3];
            			 num5 = temp[4];
            			 num6 = temp[5];
            			 num7 = temp[6];break;
            	case 8:  num1 = temp[0];
	     			 	 num2 = temp[1];
	     			 	 num3 = temp[2];
	     			 	 num4 = temp[3];
	     			 	 num5 = temp[4];
	     			 	 num6 = temp[5];
	     			 	 num7 = temp[6];
	     			 	 num8 = temp[7];break;
            	case 9:  num1 = temp[0];
			 	 		 num2 = temp[1];
			 	 		 num3 = temp[2];
			 	 		 num4 = temp[3];
			 	 		 num5 = temp[4];
			 	 		 num6 = temp[5];
			 	 		 num7 = temp[6];
			 	 		 num8 = temp[7];
			 	 		 num9 = temp[8];break;
            	case 10: num1 = temp[0];
            			 num2 = temp[1];
            			 num3 = temp[2];
            			 num4 = temp[3];
            			 num5 = temp[4];
            			 num6 = temp[5];
            			 num7 = temp[6];
            			 num8 = temp[7];
            			 num9 = temp[8];
            			 num10 = temp[0];break;
               default:
            	   num1=0;num2=0;num3=0;num4=0;num5=0;num6=0;num7=0;num8=0;num9=0;num10=0;
            	   break;
            	}
                Elevator1.addOrUpdate(new Millisecond(), num1);
                Elevator2.addOrUpdate(new Millisecond(), num2);
                Elevator3.addOrUpdate(new Millisecond(), num3);
                Elevator4.addOrUpdate(new Millisecond(), num4);
                Elevator5.addOrUpdate(new Millisecond(), num5);
                Elevator6.addOrUpdate(new Millisecond(), num6);
                Elevator7.addOrUpdate(new Millisecond(), num7);
                Elevator8.addOrUpdate(new Millisecond(), num8);
                Elevator9.addOrUpdate(new Millisecond(), num9);
                Elevator10.addOrUpdate(new Millisecond(), num10);
                

                
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
         	ChartUtilities.saveChartAsPNG(new File("ElevatorPosition.jpg"), chart, 500, 270, null);
         } catch (Exception e) {
             System.out.println("Problem occurred creating chart.");
         }
     }
     
     public void Close(){
  	   
  	   dispose();
     }
    
   /* public static void main(String[] args){

    	final Main demo = new Main("ElevatorDistance");
    	demo.pack();
    	RefineryUtilities.centerFrameOnScreen(demo);
    	demo.setVisible(true);

    }*/



	
}