package rebound.apps.caoticdreams.engine.rendering.impl;

import static java.lang.Math.*;
import static java.util.Objects.*;
import static rebound.math.SmallIntegerMathUtilities.*;
import static rebound.util.collections.CollectionUtilities.*;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import rebound.annotations.semantic.simpledata.Nonempty;
import rebound.apps.caoticdreams.engine.rendering.CompoundRenderable;
import rebound.apps.caoticdreams.engine.rendering.Renderable;
import rebound.apps.caoticdreams.engine.rendering.SpriteRenderable;
import rebound.util.collections.SimpleTable;
import rebound.util.functional.ContinueSignal;
import rebound.util.functional.FunctionInterfaces.UnaryFunction;

/**
 * As far as the rendering engine goes, a metaroom's background is a compound agent at PLNE -1 XD
 */
public class UniformlyTiledBlockBasedCompoundSpriteRenderable
implements CompoundRenderable
{
	protected double x, y;
	protected int blockWidth, blockHeight;
	protected SimpleTable<SpriteRenderable> blocks;
	
	public UniformlyTiledBlockBasedCompoundSpriteRenderable()
	{
	}
	
	public UniformlyTiledBlockBasedCompoundSpriteRenderable(int blockWidth, int blockHeight, SimpleTable<SpriteRenderable> blocks)
	{
		this.blockWidth = blockWidth;
		this.blockHeight = blockHeight;
		this.blocks = blocks;
	}
	
	
	
	/**
	 * @throws IllegalArgumentException if the blocks are not all the same size, or there are no blocks, or etc.
	 */
	public static UniformlyTiledBlockBasedCompoundSpriteRenderable newFromStaticImages(@Nonempty @Nonnull SimpleTable<BufferedImage> imageBlocks) throws IllegalArgumentException
	{
		requireNonNull(imageBlocks);
		requireNonEmpty(imageBlocks);
		
		int blockWidth, blockHeight;
		SimpleTable<SpriteRenderable> blocks;
		{
			blockWidth = -1;
			blockHeight = -1;
			
			int w = imageBlocks.getNumberOfColumns();
			int h = imageBlocks.getNumberOfRows();
			
			blocks = newTableNullfilled(w, h);
			
			for (int r = 0; r < h; r++)
			{
				for (int c = 0; c < w; c++)
				{
					BufferedImage img = imageBlocks.getCellContents(c, r);
					
					if (r == 0 && c == 0)
					{
						blockWidth = img.getWidth();
						blockHeight = img.getHeight();
					}
					else
					{
						if (img.getWidth() != blockWidth || img.getHeight() != blockHeight)
						{
							throw new IllegalArgumentException("Not all blocks are of the same size!  The first one was "+blockWidth+"x"+blockHeight+" pixels while ("+c+", "+r+") was "+img.getWidth()+"x"+img.getHeight()+" pixels!");
						}
					}
					
					int x = c * blockWidth;
					int y = r * blockHeight;
					
					blocks.setCellContents(c, r, new SpriteRenderable()
					{
						@Override
						public Point2D getRenderingOffset()
						{
							return new Point2D.Double(x, y);
						}
						
						@Override
						public BufferedImage getCurrentSprite()
						{
							return img;
						}
					});
				}
			}
		}
		
		
		return new UniformlyTiledBlockBasedCompoundSpriteRenderable(blockWidth, blockHeight, blocks);
	}
	
	
	
	
	@Override
	public Point2D getRenderingOffset()
	{
		return new Point2D.Double(x, y);
	}
	
	@Override
	public void getChildrenFor(@Nullable Rectangle region, UnaryFunction<Renderable, ContinueSignal> observeChildRenderable)
	{
		int firstColumn, pastLastColumn;
		int firstRow, pastLastRow;
		{
			if (region == null)
			{
				firstColumn = 0;
				firstRow = 0;
				pastLastColumn = blocks.getNumberOfColumns();
				pastLastRow = blocks.getNumberOfRows();
			}
			else
			{
				firstColumn = floorDiv(region.x, blockWidth); 
				pastLastColumn = ceildiv(region.x + region.width, blockWidth);
				firstRow = floorDiv(region.y, blockHeight); 
				pastLastRow = ceildiv(region.y + region.height, blockHeight);
			}
		}
		
		for (int r = firstRow; r < pastLastRow; r++)
		{
			for (int c = firstColumn; c < pastLastColumn; c++)
			{
				if (observeChildRenderable.f(blocks.getCellContents(c, r)) == ContinueSignal.Stop)
					return;
			}
		}
	}
	
	
	
	
	
	
	public double getX()
	{
		return x;
	}
	
	public void setX(double x)
	{
		this.x = x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public void setY(double y)
	{
		this.y = y;
	}
	
	public int getBlockWidth()
	{
		return blockWidth;
	}
	
	public void setBlockWidth(int blockWidth)
	{
		this.blockWidth = blockWidth;
	}
	
	public int getBlockHeight()
	{
		return blockHeight;
	}
	
	public void setBlockHeight(int blockHeight)
	{
		this.blockHeight = blockHeight;
	}
	
	public SimpleTable<SpriteRenderable> getImageBlocks()
	{
		return blocks;
	}
	
	public void setImageBlocks(SimpleTable<SpriteRenderable> imageBlocks)
	{
		this.blocks = imageBlocks;
	}
}
