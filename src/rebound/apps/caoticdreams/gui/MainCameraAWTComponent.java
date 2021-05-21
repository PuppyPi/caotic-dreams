package rebound.apps.caoticdreams.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import rebound.apps.caoticdreams.engine.rendering.Camera;

public class MainCameraAWTComponent
extends Component
{
	private static final long serialVersionUID = 1l;
	
	
	protected Camera camera;
	protected CDRendererForJava2D renderer;
	
	@Override
	public void paint(Graphics g)
	{
		int w = this.getWidth();
		int h = this.getHeight();
		
		g.setColor(Color.black);
		g.fillRect(0, 0, w, h);
		
		renderer.render((Graphics2D)g, camera, w, h);
	}
	
	
	public Camera getCamera()
	{
		return camera;
	}
	
	public void setCamera(Camera camera)
	{
		this.camera = camera;
	}
	
	public CDRendererForJava2D getRenderer()
	{
		return renderer;
	}
	
	public void setRenderer(CDRendererForJava2D renderer)
	{
		this.renderer = renderer;
	}
}
