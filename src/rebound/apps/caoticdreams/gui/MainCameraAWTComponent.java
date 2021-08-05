package rebound.apps.caoticdreams.gui;

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
		renderer.renderWithBlackBackground((Graphics2D)g, camera, this.getWidth(), this.getHeight());
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
