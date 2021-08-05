package rebound.apps.caoticdreams.engine.content;

import java.awt.geom.Rectangle2D;
import rebound.apps.caoticdreams.engine.rendering.Renderable;

public class CDMetaroom
{
	public int id;
	public Rectangle2D shape;
	public Renderable background; // THE FACT THAT Renderable IS A GENERIC API MEANS *THE BACKGROUND* OF A METAROOM CAN BE A CAMERA VIEWSCREEN 8D XD!!!
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public Rectangle2D getShape()
	{
		return shape;
	}
	
	public void setShape(Rectangle2D shape)
	{
		this.shape = shape;
	}
	
	public Renderable getBackground()
	{
		return background;
	}
	
	public void setBackground(Renderable background)
	{
		this.background = background;
	}
}
