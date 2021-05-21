package rebound.apps.caoticdreams.engine.content;

import java.awt.geom.Rectangle2D;
import rebound.apps.caoticdreams.engine.rendering.Renderable;

public class CDMetaroom
{
	public int id;
	public Rectangle2D shape;
	public Renderable background;   //THE FACT THAT Renderable IS GENERIC MEANS THE BACKGROUND OF A METAROOM CAN BE A CAMERA VIEWSCREEN 8D XD!!!
}
