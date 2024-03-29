package me.aelesia.runescape.tasks.multi;

import java.util.HashMap;
import java.util.Map;

import me.aelesia.runescape.tasks.base.BaseTask;

public abstract class MultiTask extends BaseTask {

	protected static final String END = "END";
	protected String state;
	protected boolean skip = false;
	protected Map<String, BaseTask> taskMap = new HashMap<String, BaseTask>();

	/* 
	 * Will call all initializes together.
	 * May be a potential problem in the future.
	 */
	@Override
	public final void initialize() {
		this.state = startState();
		for (BaseTask task : this.taskMap.values()) {
		    task.initialize();
		}
	}
	
	@Override
	public final void exit() {
		this.state = startState();
		for (BaseTask task : this.taskMap.values()) {
		    task.exit();
		}
	}
	
	@Override
	public final void validate() {
		this.taskMap.get(state).validate();
	}


	@Override
	public final String changeState() {
		skip=false;
		String changeState = this.taskMap.get(state).changeState(); 
		if (changeState != null) {
			this.taskMap.get(this.state).exit();
			this.state = changeState;
			skip = true;
		}
		if ("END".equals(this.state)) {
			return nextState();
		}
		return null;
	}
	
	@Override
	public final boolean execute() {
		if (!skip) {
			return this.taskMap.get(state).execute();
		}
		return true;
	}
	

	public abstract String startState();
	
	public abstract String nextState();
}
