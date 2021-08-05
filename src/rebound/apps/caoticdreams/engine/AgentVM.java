package rebound.apps.caoticdreams.engine;

import static java.util.Objects.*;
import static rebound.util.BasicExceptionUtilities.*;
import java.util.ArrayDeque;
import java.util.Queue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import rebound.annotations.semantic.operationspecification.IdentityHashableType;
import rebound.apps.caoticdreams.caos.evaluation.CaosExecutionContext;
import rebound.apps.caoticdreams.caos.evaluation.interpreter.LiveCaosBranch;
import rebound.apps.caoticdreams.caos.evaluation.interpreter.LiveCaosOperation;
import rebound.apps.caoticdreams.caos.evaluation.interpreter.LiveCaosStatement;
import rebound.apps.caoticdreams.caos.evaluation.interpreter.util.SimpleCaosExecutionContext;
import rebound.apps.caoticdreams.caos.library.core.AgentMessageSendingConflictResolution;
import rebound.apps.caoticdreams.caos.library.core.AgentMessageSendingConflictResolution.AgentMessageSendingConflictResolutionInterruptAndPushCurrent;
import rebound.apps.caoticdreams.caos.library.core.AgentMessageSendingConflictResolution.AgentMessageSendingConflictResolutionOverwriteEntirety;
import rebound.apps.caoticdreams.caos.library.core.AgentMessageSendingConflictResolution.AgentMessageSendingConflictResolutionWaitForCurrent;
import rebound.apps.caoticdreams.caos.library.core.AgentVMWaitState;
import rebound.apps.caoticdreams.caos.library.core.EventScriptTerminationType;
import rebound.apps.caoticdreams.caos.library.core.iAgentVM;
import rebound.apps.caoticdreams.engine.content.CDWorld;
import rebound.util.functional.FunctionInterfaces.UnaryProcedure;

@IdentityHashableType
public class AgentVM
implements iAgentVM
{
	protected final CDEngine engine;
	protected final CDWorld world;
	protected final AgentContent content;
	
	protected @Nullable AgentVMExecutionState currentExecutionState;
	
	/**
	 * This is a FIFO queue :3
	 */
	protected Queue<AgentMessage> messageWaitQueue = null;
	
	protected boolean locked;
	
	
	public AgentVM(CDEngine engine, CDWorld world, AgentContent content)
	{
		this.engine = engine;
		this.world = world;
		this.content = content;
	}
	
	public AgentContent getContent()
	{
		return content;
	}
	
	public CDEngine getEngine()
	{
		return engine;
	}
	
	public CDWorld getWorld()
	{
		return world;
	}
	
	
	public Queue<AgentMessage> getMessageWaitQueue()
	{
		Queue<AgentMessage> m = messageWaitQueue;
		
		if (m == null)
		{
			m = new ArrayDeque<>();  //so short-lived agents don't need to incur the overhead of one of these :3
			messageWaitQueue = m;
		}
		
		return m;
	}
	
	
	/**
	 * This is the actual CAOS interpreter!! :D
	 */
	public void tickCaos()
	{
		if (currentExecutionState == null)
			bringUpFromQueue();
		
		if (currentExecutionState != null)
		{
			AgentVMWaitState waitState = currentExecutionState.waitState;
			
			if (waitState != null)
			{
				boolean doneWaiting = waitState.tick();
				
				if (doneWaiting)
				{
					currentExecutionState.waitState = null;
					reallyTickCaosXD();
				}
			}
			else
			{
				reallyTickCaosXD();
			}
		}
	}
	
	protected void bringUpFromQueue()
	{
		//Are there any messages waiting nicely on the queue? :>
		if (messageWaitQueue != null)
		{
			AgentMessage m = messageWaitQueue.poll();
			
			if (m != null)
				this._message(m.from, m.eventNumber, false, m.first, m.inst, this.isLOCKed(), m.params, m.callbackOnCompletion, AgentMessageSendingConflictResolutionOverwriteEntirety.I);
		}
	}
	
	protected void reallyTickCaosXD()
	{
		boolean first = true;
		
		try
		{
			while (true)
			{
				if (isInEventScript())
				{
					AgentVMExecutionState currentExecutionState = this.currentExecutionState;  //THIS IS ESSENTIAL otherwise CALL will have the new callee's Execution State instruction pointer incremented instead of the caller's!!
					
					/*
					 * This is the code that makes the uncompiled INST state actually work! XD
					 * Putting this at the start instead of the end handles CALLs correctly because we might be in a new execution context by then!
					 * But the next iteration of the loop will pull the fresh execution context :3
					 */
					if (!first && !currentExecutionState.currentEventScriptContext.isINST())
						break;
					
					LiveCaosStatement s = currentExecutionState.nextStatementInCurrentScript;
					
					if (s instanceof LiveCaosBranch)
					{
						LiveCaosBranch b = (LiveCaosBranch) s;
						
						boolean condition = (Boolean)b.getCondition().evaluate(currentExecutionState.currentEventScriptContext);
						
						currentExecutionState.nextStatementInCurrentScript = condition ? b.getNextIfTrue() : b.getNextIfFalse();
					}
					else
					{
						LiveCaosOperation o = (LiveCaosOperation) s;
						
						o.run(currentExecutionState.currentEventScriptContext);
						
						currentExecutionState.nextStatementInCurrentScript = o.getNext();  //doesn't overwrite the new callee's currentExecutionState if this.currentExecutionState changes, because we snapshotted this.currentExecutionState to a local variable (also named currentExecutionState XD')
					}
					
					if (currentExecutionState.nextStatementInCurrentScript == null)
					{
						stop(EventScriptTerminationType.Success);
					}
				}
				else
				{
					break;
				}
				
				if (first)
					first = false;
			}
		}
		catch (RuntimeException | AssertionError exc)
		{
			handleErrorInEventScript(exc);
			throw exc;
		}
	}
	
	
	
	@Override
	public void applyWaitState(AgentVMWaitState waitState)
	{
		AgentVMExecutionState currentExecutionState = this.currentExecutionState;
		
		if (currentExecutionState == null)
			throw new IllegalArgumentException("No current execution state!");
		
		if (currentExecutionState.waitState != null)
			throw new IllegalArgumentException("Wait state already applied!!");
		
		currentExecutionState.waitState = waitState;
	}
	
	
	
	public void handleErrorInEventScript(Throwable t)
	{
		stop(EventScriptTerminationType.Error);
		
		//Do those before this in case this, itself fails!
		engine.handleErrorInAgentEventScript(this, t);
	}
	
	
	
	@Override
	public void stop(EventScriptTerminationType terminationType)
	{
		while (true)  //unravel the whole stack!
		{
			AgentVMExecutionState currentExecutionState = this.currentExecutionState;
			
			if (currentExecutionState != null)
			{
				this.currentExecutionState = null;  //do this BEFORE running the callback since the callback might overwrite us!  Such as in CALL!
				
				//Do those before this in case this, itself fails!
				
				UnaryProcedure<EventScriptTerminationType> callbackOnCompletion = currentExecutionState.callbackOnCompletion;
				if (callbackOnCompletion != null)
					callbackOnCompletion.f(terminationType);  //this will set this.currentExecutionState if it's not the head of the current CALL/etc. stack!  And it may cause other agents CALT'ing on us to resume execution!
			}
			else
			{
				break;
			}
		}
		
		//don't bring up anything from the queue because this may be run in Overwriting-Type messages!
	}
	
	
	
	
	
	@Override
	public boolean message(Object from, int eventNumber, boolean deferredEventNumberLookup, boolean inst, boolean lock, Object[] params, UnaryProcedure<EventScriptTerminationType> callbackOnCompletion, @Nonnull AgentMessageSendingConflictResolution conflictResolution)
	{
		requireNonNull(params);
		requireNonNull(conflictResolution);
		
		LiveCaosStatement first = world.getScriptorium().getFirstStatementInScriptOrNullIfNone(content, eventNumber);
		
		if (first != null)
		{
			_message(from, eventNumber, deferredEventNumberLookup, first, inst, lock, params, callbackOnCompletion, conflictResolution);
		}
		
		return first != null;
	}
	
	
	/*
	 * Like message() but don't look up the event number, let it be overridden >:3
	 */
	public void _message(Object from, int eventNumber, boolean deferredEventNumberLookup, LiveCaosStatement first, boolean inst, boolean lock, Object[] params, UnaryProcedure<EventScriptTerminationType> callbackOnCompletion, @Nonnull AgentMessageSendingConflictResolution conflictResolution)
	{
		if (first == null)
			first = world.getScriptorium().getFirstStatementInScriptOrNullIfNone(content, eventNumber);
		
		if (first != null)
		{
			boolean doItNow;
			
			if (isInEventScript())
			{
				if (conflictResolution == AgentMessageSendingConflictResolutionOverwriteEntirety.I)
				{
					//Deleeeeeteee the current stack X'D
					doItNow = true;
					
					stop(EventScriptTerminationType.Overwritten);  //call all the callbacks of the current stack in case some other agent is CALT waiting on us!
				}
				else if (conflictResolution == AgentMessageSendingConflictResolutionWaitForCurrent.I)
				{
					getMessageWaitQueue().add(new AgentMessage(from, eventNumber, deferredEventNumberLookup ? null : first, inst, lock, params, callbackOnCompletion));
					doItNow = false;
				}
				else if (conflictResolution instanceof AgentMessageSendingConflictResolutionInterruptAndPushCurrent)
				{
					final AgentVMExecutionState oldState = this.currentExecutionState;
					
					//Don't preserve LOCK, for compatibility  Todo that's how the engine works right?  The INST state is pushed to the stack but not the LOCK state?
					
					UnaryProcedure<EventScriptTerminationType> callbackOnCompletionGiven = callbackOnCompletion;
					
					callbackOnCompletion = terminationType ->
					{
						if (((AgentMessageSendingConflictResolutionInterruptAndPushCurrent)conflictResolution).shouldResumeIfNewTerminatesLikeSo(terminationType))
							currentExecutionState = oldState;
						
						if (callbackOnCompletionGiven != null)
							callbackOnCompletionGiven.f(terminationType);
					};
					
					doItNow = true;
				}
				else
				{
					throw newClassCastExceptionOrNullPointerException(conflictResolution);
				}
			}
			else
			{
				if (getSizeOfPendingMessageQueue() != 0)  //don't cut in line! XD
				{
					if (conflictResolution == AgentMessageSendingConflictResolutionWaitForCurrent.I)
					{
						getMessageWaitQueue().add(new AgentMessage(from, eventNumber, first, inst, lock, params, callbackOnCompletion));
						doItNow = false;
					}
					else
					{
						doItNow = true;
					}
				}
				else
				{
					doItNow = true;
				}
			}
			
			
			
			if (doItNow)
			{
				AgentVMExecutionState newState = new AgentVMExecutionState();
				newState.currentEventScript = eventNumber;
				newState.nextStatementInCurrentScript = first;
				newState.waitState = null;
				newState.callbackOnCompletion = callbackOnCompletion;
				
				newState.currentEventScriptContext = new SimpleCaosExecutionContext(params, from, this, world, engine);
				newState.currentEventScriptContext.setINST(inst);
				
				this.currentExecutionState = newState;
			}
		}
		else
		{
			//Silently discard as per deferredEventNumberLookup
		}
	}
	
	
	
	
	public boolean isInEventScript()
	{
		return currentExecutionState != null;
	}
	
	
	
	
	
	
	protected static class AgentVMExecutionState
	{
		protected CaosExecutionContext currentEventScriptContext;
		protected int currentEventScript;
		protected @Nullable AgentVMWaitState waitState;
		protected @Nullable LiveCaosStatement nextStatementInCurrentScript;
		protected @Nullable UnaryProcedure<EventScriptTerminationType> callbackOnCompletion;
	}
	
	protected static class AgentMessage
	{
		Object from;
		int eventNumber;
		@Nullable LiveCaosStatement first;  //null for deferredEventNumberLookup
		boolean inst;
		boolean lock;
		Object[] params;
		UnaryProcedure<EventScriptTerminationType> callbackOnCompletion;
		
		public AgentMessage(Object from, int eventNumber, LiveCaosStatement first, boolean inst, boolean lock, Object[] params, UnaryProcedure<EventScriptTerminationType> callbackOnCompletion)
		{
			this.from = from;
			this.eventNumber = eventNumber;
			this.inst = inst;
			this.lock = lock;
			this.params = params;
			this.callbackOnCompletion = callbackOnCompletion;
		}
	}
	
	
	
	
	
	
	
	
	@Override
	public boolean isLOCKed()
	{
		return locked;
	}
	
	@Override
	public void setLOCKed(boolean value)
	{
		this.locked = value;
	}
	
	@Override
	public Integer getCurrentlyExecutingEventScriptNumber()
	{
		AgentVMExecutionState s = this.currentExecutionState;
		return s == null ? null : s.currentEventScript;
	}
	
	@Override
	public int getSizeOfPendingMessageQueue()
	{
		return messageWaitQueue == null ? 0 : messageWaitQueue.size();
	}
}
