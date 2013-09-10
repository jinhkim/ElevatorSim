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

public class SimViewMaintenanceAlertBox {

	private BranchGroup parent;
	private BranchGroup group;

	private TransformGroup pass;
	private Transform3D pass_trans;

	private float yPos;
	private float xPos;
	private float height;
	
	private Boolean alive = false;

	public SimViewMaintenanceAlertBox(BranchGroup p, float x, int floors, float h) {
		parent = p;
		xPos = x;
		yPos = (floors*h)/2-0.3f;

		height = (floors*h)/2;

		group = new BranchGroup();
		group.setCapability(group.ALLOW_DETACH);

		pass = new TransformGroup();
		pass.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		pass.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		pass_trans = new Transform3D();

		group.addChild(pass);	
	}

	public void create() {
		if (!alive) {
		PolygonAttributes pa=new PolygonAttributes();

		Appearance pass_app = new Appearance();
		pass_app.setPolygonAttributes(pa);
		Color3f yell = new Color3f(1f,0.8f,0.01f);
		ColoringAttributes ca = new ColoringAttributes(yell, ColoringAttributes.NICEST); 
		pass_app.setColoringAttributes(ca);

		Box e = new Box(0.2f,height,0.05f,pass_app);

		Vector3f vector = new Vector3f(xPos,yPos,-0.3f);
		pass_trans.setTranslation(vector);
		pass.setTransform(pass_trans);
		pass.addChild(e);
		parent.addChild(group);
		
		alive = true;
		}
	}

	public void remove() {
		if (alive)
			group.detach();
		alive = false;
	}
	
}
