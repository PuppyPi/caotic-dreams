package rebound.apps.caoticdreams.engine.rendering;

import java.awt.geom.Point2D;
import javax.annotation.Nullable;

public interface Camera
{
	public Point2D getCenterOfWorldRegionViewed();
	public double getZoom();
	
	/**
	 * A "main" camera—ie, one that can never be seen by other cameras (like a {@link CameraViewscreenRenderable non-toplevel camera can}!)
	 */
	public boolean isToplevel();
	
	
	public @Nullable Integer getCurrentMetaroom();
	
	/**
	 * If {@link #getCurrentMetaroom()} is null then it's as if this is true no matter what it is.
	 */
	public boolean isSeeingOtherMetaroom();
	
	
	
	
	
	public void getCenterOfWorldRegionViewed(Point2D v);
	public void getZoom(double v);
	public void setToplevel(boolean v);
	public void getCurrentMetaroom(@Nullable Integer v);
	public void setSeeingOtherMetaroom(boolean v);
}
