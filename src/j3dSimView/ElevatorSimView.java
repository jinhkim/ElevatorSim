package j3dSimView;


import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.util.Vector;


import javax.media.j3d.Appearance;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;


import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Text2D;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.Viewer;
import com.sun.j3d.utils.universe.ViewingPlatform;

import elevatorSystem.BossLiftGeneralException;
import elevatorSystem.Passenger;

public class ElevatorSimView {
	/*
	 *	This class will create a wireframe representation of the elevator simulation using Java3D.
	 *	It will also present all the necessary methods to move elevators, create and remove passengers,
	 *	put passengers in elevators and take them out, etc. 
	 *
	 *
	 */

	private Canvas3D canvas3d; // the canvas object to be passed to the gui
	private SimpleUniverse universe;	// self-explanitory
	private BranchGroup group;  // main group holding entire simulation
	
	private TransformGroup viewTransform; // camera view transform object
	
	////// Elevators
	private TransformGroup[] elevators;
	private Transform3D[] elevator_trans;
	private float[] elevatorsY;
	
	///// Doors
	private TransformGroup[][] doorsLeft;
	private Transform3D[][] doorsLeft_trans;
	private TransformGroup[][] doorsRight;
	private Transform3D[][] doorsRight_trans;
	private Box[][] dL;
	private Box[][] dR;
	private Appearance[][] doorAppearances;
	
	//// scaling properties
	private float shaftSeparation;
	private float floorHeight;
	
	//// view control
	private float DEFAULT_ROTATION = 1.6f;
	private Boolean focused;
	private int focusEleID;
	//rotation
	private float theta;
	private float radius;
	private float zoom;
	
	/*
	 * CONSTRUCTOR
	 * 
	 * intiates scaling properties
	 */
	public ElevatorSimView() {		
		init();
	}
	
	public void init() {
		shaftSeparation = 1f;
		floorHeight = 1f;	
		
		focused = false;
		focusEleID = 0;
		zoom = 0; 
		
		// polar coords, for rotation
		theta = DEFAULT_ROTATION;
		
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        canvas3d = new Canvas3D(config);
        
     // Manually create the viewing platform so that we can customize it
        ViewingPlatform viewingPlatform = new ViewingPlatform();

        // **** This is the part I was missing: Activation radius
        viewingPlatform.getViewPlatform().setActivationRadius(300f);
        //viewingPlatform.setNominalViewingTransform();

        // Set the view position back far enough so that we can see things
        viewTransform = viewingPlatform.getViewPlatformTransform();

        // Set back clip distance so things don't disappear 
        Viewer viewer = new Viewer(canvas3d);
        View view = viewer.getView();
        view.setBackClipDistance(300);

        universe = new SimpleUniverse(viewingPlatform, viewer);
	}
	
	public void cleanup() {
		universe.cleanup();
	}
	
	public void restart() {
		// Manually create the viewing platform so that we can customize it
        ViewingPlatform viewingPlatform = new ViewingPlatform();

        // **** This is the part I was missing: Activation radius
        viewingPlatform.getViewPlatform().setActivationRadius(300f);
        //viewingPlatform.setNominalViewingTransform();

        // Set the view position back far enough so that we can see things
        viewTransform = viewingPlatform.getViewPlatformTransform();

        // Set back clip distance so things don't disappear 
        Viewer viewer = new Viewer(canvas3d);
        View view = viewer.getView();
        view.setBackClipDistance(300);

        universe = new SimpleUniverse(viewingPlatform, viewer);
	}
	
	/*
	 * getCanvas()
	 * 
	 * called by mainWindow class to place canvas in the gui
	 * 
	 */
	public Canvas3D getCanvas() {		
		return canvas3d;
	}
	
	/*
	 * createWorld()
	 * 
	 * creates the wireframe world based on number of elevators and floors. details later
	 * 
	 */	
	public void createWorld(int elev, int floors, Boolean penthouse, Vector<Integer>[] bounds) {

        

       group = new BranchGroup();
       group.setCapability(group.ALLOW_CHILDREN_READ);
       group.setCapability(group.ALLOW_CHILDREN_WRITE);
       group.setCapability(group.ALLOW_CHILDREN_EXTEND);
       
       PolygonAttributes pa=new PolygonAttributes();
       pa.setPolygonMode(PolygonAttributes.POLYGON_LINE);
       pa.setCullFace(PolygonAttributes.CULL_NONE);
       
       Appearance elev_app = new Appearance();
       elev_app.setPolygonAttributes(pa);
       Color3f green = new Color3f(0.15f, .7f, .15f);
       ColoringAttributes ca = new ColoringAttributes(green, ColoringAttributes.NICEST); 
       elev_app.setColoringAttributes(ca);
       
       Appearance door_app = new Appearance();
       door_app.setPolygonAttributes(pa);
       Color3f brown = new Color3f(new Color(153,51,0));
       ColoringAttributes car = new ColoringAttributes(brown, ColoringAttributes.NICEST); 
       door_app.setColoringAttributes(car);
       
       Appearance Penthouse_door_app = new Appearance();
       Penthouse_door_app.setPolygonAttributes(pa);
       Color3f pink = new Color3f(new Color(210,0,210));
       ColoringAttributes card = new ColoringAttributes(pink, ColoringAttributes.NICEST); 
       Penthouse_door_app.setColoringAttributes(card);
       
       Appearance Locked_door_app = new Appearance();
       Locked_door_app.setPolygonAttributes(pa);
       Color3f blu = new Color3f(new Color(0,0,210));
       ColoringAttributes cardy = new ColoringAttributes(blu, ColoringAttributes.NICEST); 
       Locked_door_app.setColoringAttributes(cardy);
       
       
       elevators = new TransformGroup[elev];
   		elevator_trans = new Transform3D[elev];
   	elevatorsY = new float[elev];
   	doorsLeft = new TransformGroup[elev][floors];
   	doorsLeft_trans = new Transform3D[elev][floors];
   	doorsRight = new TransformGroup[elev][floors];
   	doorsRight_trans = new Transform3D[elev][floors];
   	dR = new Box[elev][floors];
   	dL = new Box[elev][floors];
   	doorAppearances = new Appearance[elev][floors];
       
       for(int i=0; i < elev; i++) {

    	   float shaftMarker = i*shaftSeparation;
	       elevators[i] = new TransformGroup();
	       elevators[i].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	       elevators[i].setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	              
	       group.addChild(elevators[i]);
	              
	       Box e = new Box(0.1f,0.2f,0.1f,elev_app);
	       elevators[i].addChild(e);
	       elevatorsY[i] = 0;
	       
	       elevator_trans[i] = new Transform3D();
	       Vector3f vectore = new Vector3f(shaftMarker,0f,0f);
           elevator_trans[i].setTranslation(vectore);
           elevators[i].setTransform(elevator_trans[i]);
           
        // text for each elevator
           Text2D te = new Text2D("E"+(i), new Color3f(0.9f,
               1.0f, 1.0f), "Helvetica", 82, Font.ITALIC);
           TransformGroup t = new TransformGroup();
           Transform3D t3d = new Transform3D();
           t3d.setTranslation(new Vector3f(shaftMarker-0.2f,-1f,0f));
           t.setTransform(t3d);
           t.addChild(te);

           group.addChild(t);
	       
	       for (int j=0; j < floors; j++) {
	    	   
	    	   float floorMarker = j*floorHeight;
	    	   
	    	   doorsLeft[i][j] = new TransformGroup();
	    	   doorsRight[i][j] = new TransformGroup();
	    	   doorsLeft_trans[i][j] = new Transform3D();
	    	   doorsRight_trans[i][j] = new Transform3D();
	           group.addChild(doorsLeft[i][j]);
	           group.addChild(doorsRight[i][j]);
	           
	           if (penthouse && j == floors-1) {
	        	   dR[i][j] = new Box(0.13f,0.2f,0.01f,Box.ENABLE_APPEARANCE_MODIFY,Penthouse_door_app);
		           dL[i][j] = new Box(0.13f,0.2f,0.01f,Box.ENABLE_APPEARANCE_MODIFY,Penthouse_door_app);
		           doorAppearances[i][j] = Penthouse_door_app;
	           } else if (bounds[i].indexOf(new Integer(j)) >= 0) {
	        	   dR[i][j] = new Box(0.13f,0.2f,0.01f,Box.ENABLE_APPEARANCE_MODIFY,Locked_door_app);
		           dL[i][j] = new Box(0.13f,0.2f,0.01f,Box.ENABLE_APPEARANCE_MODIFY,Locked_door_app);
		           doorAppearances[i][j] = Locked_door_app;
	           } else {
	        	   dR[i][j] = new Box(0.13f,0.2f,0.01f,Box.ENABLE_APPEARANCE_MODIFY,door_app);
	        	   dL[i][j] = new Box(0.13f,0.2f,0.01f,Box.ENABLE_APPEARANCE_MODIFY,door_app);
	        	   doorAppearances[i][j] = door_app;
	           }
	           
	           Vector3f vector = new Vector3f((shaftMarker+.13f),floorMarker,.1f);
	           doorsLeft_trans[i][j].setTranslation(vector);
	           doorsLeft[i][j].setTransform(doorsLeft_trans[i][j]);
	           doorsLeft[i][j].addChild(dL[i][j]);
	           
	           Vector3f vectorr = new Vector3f((shaftMarker-.13f), floorMarker, .1f);
	           doorsRight_trans[i][j].setTranslation(vectorr);
	           doorsRight[i][j].setTransform(doorsRight_trans[i][j]);
	           doorsRight[i][j].addChild(dR[i][j]);
	           
	        // text for each floor
	           Text2D text2d = new Text2D("F"+(j), new Color3f(0.9f,
	               1.0f, 1.0f), "Helvetica", 82, Font.ITALIC);
	           t = new TransformGroup();
	           t3d = new Transform3D();
	           t3d.setTranslation(new Vector3f(-1f,(floorMarker-.2f),0f));
	           t.setTransform(t3d);
	           t.addChild(text2d);

	           group.addChild(t);
	    	   
	       }
       
       }
       
       adjustViewpoint();
       group.compile();
       universe.addBranchGraph(group);
	}
	
	/*
	 * adjustViewpoint()
	 * 
	 * moves the camera view around based on the location of the elevators to allow the entire scene to be in the frame
	 * 
	 */	
	public void adjustViewpoint() {
		
		int elev = elevators.length;
		float minY = 10000;
		float maxY = 0;
		for (int e=0; e < elev; e++){
			if (elevatorsY[e] < minY)
				minY = elevatorsY[e];
			if (elevatorsY[e] > maxY)
				maxY = elevatorsY[e];
		}
		float separation = maxY-minY;
		
		
		float[] camera = new float[3];
		float[] look = new float[3];
		
		if (focused) {
			radius = 9f+zoom;
			
			camera[0] = (float) (radius * Math.cos(theta));
			camera[1] = elevatorsY[focusEleID];
			camera[2] = (float) (radius * Math.sin(theta));
			
			look[0] = (focusEleID*shaftSeparation)-(shaftSeparation/5);
			look[1] = elevatorsY[focusEleID];
			look[2] = 0;
			
			
		} else {
			radius = 9+separation*2.5f+zoom;
			
			camera[0] = (float) (radius * Math.cos(theta));
			camera[1] = minY+(separation/2);
			camera[2] = (float) (radius * Math.sin(theta));
			
			look[0] = (elev/2*shaftSeparation)-(shaftSeparation/5);
			look[1] = minY+(separation/2);
			look[2] = 0;
		}
		
		Transform3D t3d = new Transform3D();
	    // 						point where camera sits				point it's looking at															
	    t3d.lookAt(new Point3d(camera[0],camera[1],camera[2]), new Point3d(look[0],look[1],look[2]),
	        		new Vector3d(0,1,0)); // don't change
        t3d.invert();
        viewTransform.setTransform(t3d);
	}
	
	public void resetView() {
		focused = false;
		zoom = 0;
		theta = DEFAULT_ROTATION;
		adjustViewpoint();
	}

	public void focusViewOnElevator(int id) throws BossLiftGeneralException {
		if (id > elevators.length || id < 0)
			throw new BossLiftGeneralException("Cannot focus on elevator that doesn't exist!");
		
		focused = true;
		focusEleID = id;
	}
	
	public void rotateViewRight() {
		float delta_theta = 2.0f*3.14f/100f;
		if (theta > 0.2)
		theta -= delta_theta;
		adjustViewpoint();
	}
	
	public void rotateViewLeft() {
		float delta_theta = (float) (2.0f*Math.PI/100f);
		if (theta < 2.8)
		theta += delta_theta;
		adjustViewpoint();
	}
	
	public void zoomIn() {
		float plus = 0.5f;
		if (zoom >= -5)
			zoom -= plus;
		adjustViewpoint();
	}
	
	public void zoomOut() {
		float plus = 0.5f;
		zoom += plus;
		adjustViewpoint();
	}
	
	/*
	 * moveElevator()
	 * 
	 * accepts elevator id starting from zero and the y position within the simulation world to move it
	 * 
	 */
	public void moveElevator(int elevID, float pos) {
		elevator_trans[elevID].setTranslation(new Vector3f(elevID*shaftSeparation,pos,0f));
		elevators[elevID].setTransform(elevator_trans[elevID]);
		adjustViewpoint();
		elevatorsY[elevID] = pos;
	}
	
	/*
	 * createPassenger
	 * 
	 * INPUT:	passenger object
	 * EFFECTS:	creates the 3d box to represent the passenger in the simulation view and connects it
	 * 			to the passenger object.
	 * 	
	 * 		NOTE: has NOTHING to do with the elvator system, only creates the representation of the 
	 * 			  passenger with in the 3d view
	 */
	public void createPassenger(Passenger p) {
		SimViewPassenger s = new SimViewPassenger(group,elevators.length*shaftSeparation,(p.getCurrentFloor())*floorHeight,0,p.isVIP());
		p.setSimView(s);
	}

	/*
	 * getElePos
	 * 
	 * EFFECTS:	returns x position of elevator shaft with id 'iD'
	 */
	public float getElePos(int iD) {
		return iD*shaftSeparation;
	}

	public SimViewPassengerAlertBox createPassengerAlert(int f) {
		return new SimViewPassengerAlertBox(group,f*floorHeight,elevators.length,shaftSeparation);
	}
	
	public SimViewMaintenanceAlertBox createMaintenanceAlertBox(int shaft) {
		return new SimViewMaintenanceAlertBox(group,shaft*shaftSeparation,doorsLeft[0].length,floorHeight);
	}

	public SimViewEmergencyAlert createEmergencyAlertBox(int shaft) {
		return new SimViewEmergencyAlert(group,shaft*shaftSeparation,doorsLeft[0].length,floorHeight);
	}
	
	public void setFloorFault(int f) {
		PolygonAttributes pa=new PolygonAttributes();
	       pa.setPolygonMode(PolygonAttributes.POLYGON_LINE);
	       pa.setCullFace(PolygonAttributes.CULL_NONE);
		Appearance fault_door = new Appearance();
		fault_door.setPolygonAttributes(pa);
	       Color3f grey = new Color3f(new Color(204,204,204));
	       ColoringAttributes cardy = new ColoringAttributes(grey, ColoringAttributes.NICEST); 
	       fault_door.setColoringAttributes(cardy);
	       for (int i=0; i<dR.length; i++){
	    	   dR[i][f].setAppearance(fault_door);
	    	   dL[i][f].setAppearance(fault_door);
	       }
	}
	
	public void resolveFloorFault(int f) {
		for (int i=0; i<dR.length; i++){
	    	   dR[i][f].setAppearance(doorAppearances[i][f]);
	    	   dL[i][f].setAppearance(doorAppearances[i][f]);
	       }
	}
}
