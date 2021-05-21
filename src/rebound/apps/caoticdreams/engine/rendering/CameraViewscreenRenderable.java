package rebound.apps.caoticdreams.engine.rendering;

import java.awt.geom.Dimension2D;

public interface CameraViewscreenRenderable
extends Renderable
{
	public Camera getCamera();
	
	public Dimension2D getSizeOfViewscreen();
}
