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

public class SimViewPassengerAlertBox {

	private BranchGroup parent;
	private BranchGroup group;

	private TransformGroup pass;
	private Transform3D pass_trans;

	private float yPos;
	private float xPos;
	private float length;

	private int pass_count;

	public SimViewPassengerAlertBox(BranchGroup p, float y, int eles, float shafts) {
		parent = p;
		yPos = y;
		xPos = (shafts*eles)/2;
		pass_count = 0;

		length = shafts*eles;

		group = new BranchGroup();
		group.setCapability(group.ALLOW_DETACH);



		pass = new TransformGroup();
		pass.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		pass.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		pass_trans = new Transform3D();

		group.addChild(pass);	
	}

	private void create() {
		PolygonAttributes pa=new PolygonAttributes();

		Appearance pass_app = new Appearance();
		pass_app.setPolygonAttributes(pa);
		Color3f blue = new Color3f(new Color(0,204,204));
		ColoringAttributes ca = new ColoringAttributes(blue, ColoringAttributes.NICEST); 
		pass_app.setColoringAttributes(ca);

		Box e = new Box(length,0.23f,0.05f,pass_app);

		Vector3f vector = new Vector3f(xPos,yPos,-0.5f);
		pass_trans.setTranslation(vector);
		pass.setTransform(pass_trans);
		pass.addChild(e);
		parent.addChild(group);
	}

	public void passengerExit() {
		if (pass_count == 1) 
			remove();

		pass_count--;
	}

	public void addPassenger() {
		if (pass_count == 0)
			create();
		pass_count++;
	}

	private void remove() {
		group.detach();
	}
}
