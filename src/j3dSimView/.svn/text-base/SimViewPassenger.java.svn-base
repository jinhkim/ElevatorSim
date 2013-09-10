package j3dSimView;

import java.awt.Color;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Box;


public class SimViewPassenger {
	
	private BranchGroup parent;
	private BranchGroup group;
	
	private TransformGroup pass;
	private Transform3D pass_trans;
	
	private float[] pos;
	
	public SimViewPassenger(BranchGroup p, float x, float y, float z, Boolean VIP) {
		parent = p;
		pos = new float[3];
		pos[0] = x;
		pos[1] = y;
		pos[2] = z;
		
		group = new BranchGroup();
		group.setCapability(group.ALLOW_DETACH);
		
		PolygonAttributes pa=new PolygonAttributes();
	       pa.setPolygonMode(PolygonAttributes.POLYGON_LINE);
	       pa.setCullFace(PolygonAttributes.CULL_NONE);
	       
	       Appearance pass_app = new Appearance();
	       pass_app.setPolygonAttributes(pa);
	       Color3f white = new Color3f(new Color(255,255,255));
	       ColoringAttributes ca = new ColoringAttributes(white, ColoringAttributes.NICEST); 
	       pass_app.setColoringAttributes(ca);
	       
	       Appearance VIP_pass_app = new Appearance();
	       VIP_pass_app.setPolygonAttributes(pa);
	       Color3f purp = new Color3f(new Color(122,0,230));
	       ColoringAttributes car = new ColoringAttributes(purp, ColoringAttributes.NICEST); 
	       VIP_pass_app.setColoringAttributes(car);
	       
	       pass = new TransformGroup();
	       pass.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	       pass.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	       pass_trans = new Transform3D();
	       
	       group.addChild(pass);
	       
	              Box e;
	              if (VIP)
	            	  e = new Box(0.05f,0.15f,0.05f,VIP_pass_app);
	              else
	            	  e = new Box(0.05f,0.15f,0.05f,pass_app);
	       
	       Vector3f vector = new Vector3f(pos[0],pos[1],pos[2]);
           pass_trans.setTranslation(vector);
           pass.setTransform(pass_trans);
	       pass.addChild(e);
	       
	       parent.addChild(group);
	}
	
	public void remove() {
		group.detach();
	}
	
	public void move(float x, float y, float z) {
		 Vector3f vector = new Vector3f(x,y,z);
         pass_trans.setTranslation(vector);
         pass.setTransform(pass_trans);
	}
}
