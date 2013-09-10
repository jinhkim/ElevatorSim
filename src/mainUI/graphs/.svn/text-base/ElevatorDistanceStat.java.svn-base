package mainUI.graphs;



import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import elevatorSystem.Controller;
import elevatorSystem.Log;
import elevatorSystem.Controller;
import elevatorSystem.Log;


 
public class ElevatorDistanceStat extends ApplicationFrame {

	private static Controller controller;
    public ElevatorDistanceStat(final String title, Controller c,Boolean B ) throws IOException {

        super(title);
        controller = c;
        final CategoryDataset dataset = createDataset();
        final JFreeChart chart = ChartFactory.createBarChart3D(        		
        		"Elevator Distance Graph",      // chart title
                "Elevator",               // domain axis label
                "Floors Travelled",                  // range axis label
                dataset,                  // data
                PlotOrientation.VERTICAL, // orientation
                true,                     // include legend
                true,                     // tooltips
                false                     // urls
        		
        		);
        
         JFrame frame = new JFrame("GraphTest");
         CategoryPlot plot = chart.getCategoryPlot();
         CategoryAxis axis = plot.getDomainAxis();
         axis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 8.0) );
         BarRenderer3D renderer = (BarRenderer3D) plot.getRenderer();
         renderer.setDrawBarOutline(false);
        
        // add the chart to a panel...
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
        try {
        	ChartUtilities.saveChartAsPNG(new File("ElevatorDistance.jpg"), chart, 500, 270, null);
        } catch (Exception e) {
            System.out.println("Problem occurred creating chart.");
        }
        frame.pack();
        frame.setVisible(B);
        
        
        
    }

   
   private CategoryDataset createDataset() {
	   
	   //final double[][] data = new double[1][3];
	   final double[][] data = new double[1][controller.getElevNum()];
	   for ( int i =0 ; i < controller.getElevNum() ; i++)
	   {
		       data[0][i] = (double)controller.elevatorDistance(i);
	   }
	   
	/*   data[0][0] = 2.0;
	   data[0][1] = 3.0;
	   data[0][2] = 4.0;*/

   
        return DatasetUtilities.createCategoryDataset("Elevator", "Elevator ", data);

    }
   public void Close(){
	   
	   dispose();
   }
   /* public static void main(String[] args){

	ElevatorDistanceStat demo;
	try {
		demo = new ElevatorDistanceStat("ElevatorDistance", controller);
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);

	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
    }*/


    
    

}