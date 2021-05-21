package rebound.apps.caoticdreams.engine.rendering;

import java.awt.image.BufferedImage;
import rebound.annotations.semantic.allowedoperations.ReadonlyValue;

public interface SpriteRenderable
extends Renderable
{
	/**
	 * Any TINTing should already be done on this returned image.
	 */
	@ReadonlyValue
	public BufferedImage getCurrentSprite();
}
