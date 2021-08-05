package rebound.apps.caoticdreams.gui;

import static rebound.math.SmallFloatMathUtilities.*;
import static rebound.math.geom2d.GeometryUtilities2D.*;
import static rebound.util.BasicExceptionUtilities.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.annotation.Nullable;
import rebound.apps.caoticdreams.engine.rendering.Camera;
import rebound.apps.caoticdreams.engine.rendering.CameraViewscreenRenderable;
import rebound.apps.caoticdreams.engine.rendering.CompoundRenderable;
import rebound.apps.caoticdreams.engine.rendering.Renderable;
import rebound.apps.caoticdreams.engine.rendering.RenderableWorld;
import rebound.apps.caoticdreams.engine.rendering.SpriteRenderable;
import rebound.util.functional.ContinueSignal;

public class CDRendererForJava2D
{
	protected RenderableWorld world;
	
	
	public void renderWithBlackBackground(Graphics2D g, Camera camera, double widthOfScreen, double heightOfScreen)
	{
		g.setColor(Color.black);
		g.fillRect(0, 0, roundCeilS32(widthOfScreen), roundCeilS32(heightOfScreen));
		
		render((Graphics2D)g, camera, widthOfScreen, heightOfScreen);
	}
	
	public void render(Graphics2D g, Camera camera, double widthOfScreen, double heightOfScreen)
	{
		Point2D c = camera.getCenterOfWorldRegionViewed();
		double zoom = camera.getZoom();
		double widthInWorldSpace = widthOfScreen / zoom;
		double heightInWorldSpace = heightOfScreen / zoom;
		double x = c.getX() - widthInWorldSpace / 2d;
		double y = c.getY() - heightInWorldSpace / 2d;
		Rectangle2D regionInWorldMap = rect(x, y, widthInWorldSpace, heightInWorldSpace);
		
		@Nullable Integer onlyThisMetaroom = camera.isSeeingOtherMetaroom() ? null : camera.getCurrentMetaroom();  //getCurrentMetaroom() can be null even if isSeeingOtherMetaroom() == false and it still has the isSeeingOtherMetaroom() effect ^^'
		
		boolean toplevel = camera.isToplevel();
		
		g.clip(rect(0, 0, widthOfScreen, heightOfScreen));
		g.translate(-x, -y);
		g.scale(zoom, zoom);
		
		world.getRenderablesInZOrder(regionInWorldMap, onlyThisMetaroom, renderable ->
		{
			renderOne(g, renderable, widthInWorldSpace, heightInWorldSpace, x, y, toplevel);
			return ContinueSignal.Continue;
		});
	}
	
	/**
	 * Make sure to set the {@link Graphics2D#clip(java.awt.Shape)} correctly so that this won't draw unnecessary pixels!
	 */
	public void renderOne(Graphics2D g, Renderable renderable, double widthInWorldSpace, double heightInWorldSpace, double x, double y, boolean toplevel)
	{
		Point2D o = renderable.getRenderingOffset();
		
		Graphics2D gt = (Graphics2D) g.create();
		gt.translate(-o.getX(), -o.getY());
		
		if (renderable instanceof SpriteRenderable)
		{
			BufferedImage sprite = ((SpriteRenderable)renderable).getCurrentSprite();
			gt.drawImage(sprite, 0, 0, null);
		}
		else if (renderable instanceof CompoundRenderable)
		{
			Rectangle2D region = rect(o.getX() - x, o.getY() - y, widthInWorldSpace, heightInWorldSpace);
			
			((CompoundRenderable)renderable).getChildrenFor(region, child ->
			{
				renderOne(gt, child, widthInWorldSpace, heightInWorldSpace, x, y, toplevel);
				return ContinueSignal.Continue;
			});
		}
		else if (renderable instanceof CameraViewscreenRenderable)
		{
			if (toplevel)
			{
				CameraViewscreenRenderable c = (CameraViewscreenRenderable) renderable;
				
				Camera otherCamera = c.getCamera();
				
				if (!otherCamera.isToplevel())
				{
					Dimension2D otherSize = c.getSizeOfViewscreen();
					
					double widthOfScreen = otherSize.getWidth();
					double heightOfScreen = otherSize.getHeight();
					
					gt.clip(rect(o.getX(), o.getY(), widthOfScreen, heightOfScreen));
					render(gt, otherCamera, widthOfScreen, heightOfScreen);
				}
			}
		}
		else
		{
			throw newClassCastExceptionOrNullPointerException(renderable);
		}
	}
	
	
	
	public BufferedImage paintToImage(Camera camera, int widthOfScreen, int heightOfScreen)
	{
		BufferedImage img = new BufferedImage(widthOfScreen, heightOfScreen, BufferedImage.TYPE_INT_ARGB);
		render(img.createGraphics(), camera, widthOfScreen, heightOfScreen);
		return img;
	}
	
	
	
	public RenderableWorld getWorld()
	{
		return world;
	}
	
	public void setWorld(RenderableWorld world)
	{
		this.world = world;
	}
}
