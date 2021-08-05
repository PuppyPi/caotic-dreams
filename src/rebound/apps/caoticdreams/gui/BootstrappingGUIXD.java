package rebound.apps.caoticdreams.gui;

import static rebound.util.BasicExceptionUtilities.*;
import java.awt.Window;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import rebound.apps.caoticdreams.caos.library.content.iEngine;
import rebound.apps.caoticdreams.caos.library.content.iWorld;
import rebound.apps.caoticdreams.engine.CDEngine;
import rebound.apps.caoticdreams.engine.content.CDWorld;
import rebound.concurrency.threads.GenericEventQueue;
import rebound.concurrency.threads.SimpleEventQueue;

/**
 * Really crappy, just to get something visible XD'
 */
public class BootstrappingGUIXD
extends JFrame
{
	public static void main(String[] args)
	{
		JFrame window = new JFrame("CaoticDreams Bootstrapping!! :D");
		
		CDEngine engine = new CDEngine();
		CDWorld world = new CDWorld();
		
		//Todo move all this into the engine so that caos can change the tick rate X3
		ScheduledExecutorService ticking = new ScheduledThreadPoolExecutor(1);
		GenericEventQueue gameThread = new SimpleEventQueue(100);
		
		ticking.scheduleAtFixedRate(() ->
		{
			try
			{
				gameThread.invokeAndWaitIfNotDispatchThreadOrRightNowIfSo(() -> engine.tick());
			}
			catch (InvocationTargetException exc)
			{
				rethrowSafe(exc.getTargetException());
			}
			
		}, 0, 50, TimeUnit.MILLISECONDS);
		
		engine.setCloseWindowAndShutdown(() ->
		{
			
		});
		
		setupInputListening(window, engine);
		
		populateWorldWithSomething(world);
		
		window.setVisible(true);
	}
	
	
	
	public static void setupInputListening(Window window, CDEngine engine)
	{
		window.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				engine.userInputMouseButtonStateChanged(e.getButton()-1, false);  //0=left button, 1=middle, 2=right
			}
			
			@Override
			public void mousePressed(MouseEvent e)
			{
				engine.userInputMouseButtonStateChanged(e.getButton()-1, true);  //0=left button, 1=middle, 2=right
			}
			
			@Override
			public void mouseExited(MouseEvent e)
			{
				engine.userInputMousePresenceStateChanged(false);
			}
			
			@Override
			public void mouseEntered(MouseEvent e)
			{
				engine.userInputMousePresenceStateChanged(true);
			}
			
			@Override
			public void mouseClicked(MouseEvent e)
			{
			}
		});
		
		window.addKeyListener(new KeyListener()
		{
			@Override
			public void keyTyped(KeyEvent e)
			{
			}
			
			@Override
			public void keyReleased(KeyEvent e)
			{
				//Todo modifiers
				engine.userInputKeyStateChanged(e.getKeyCode(), false);
			}
			
			@Override
			public void keyPressed(KeyEvent e)
			{
				//Todo modifiers
				engine.userInputKeyStateChanged(e.getKeyCode(), true);
			}
		});
		
		window.addFocusListener(new FocusListener()
		{
			@Override
			public void focusLost(FocusEvent e)
			{
				engine.userInputFocusAndKeyboardStateChanged(false);
			}
			
			@Override
			public void focusGained(FocusEvent e)
			{
				engine.userInputFocusAndKeyboardStateChanged(true);
			}
		});
		
		window.addWindowListener(new WindowListener()
		{
			@Override
			public void windowOpened(WindowEvent e)
			{
			}
			
			@Override
			public void windowIconified(WindowEvent e)
			{
			}
			
			@Override
			public void windowDeiconified(WindowEvent e)
			{
			}
			
			@Override
			public void windowDeactivated(WindowEvent e)
			{
			}
			
			@Override
			public void windowClosing(WindowEvent e)
			{
				engine.userInputWindowCloseClicked();
			}
			
			@Override
			public void windowClosed(WindowEvent e)
			{
			}
			
			@Override
			public void windowActivated(WindowEvent e)
			{
			}
		});
	}
	
	
	
	
	
	
	
	
	
	
	
	public static void populateWorldWithSomething(iWorld world)
	{
		
	}
}
