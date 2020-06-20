package me.ci.Conquest.Misc;

import java.util.Timer;
import java.util.TimerTask;

public class HasDoneChecker{
	private Runnable onend;
	private LoopChecker cancontinue;
	private int seconds;
	private Runnable ontimeout;
	private boolean running = true;
	/*
	 * Waits until a process is confirmed then runs another process, or times out after a certain
	 * number of seconds. The first parameter is ran when the process is complete. The second
	 * parameter is ran once every second. If it returns true, the process is completed. If it returns
	 * false, then the program keeps waiting. The third parameter is the number of seconds to wait
	 * until a timeout is called. Putting a 0 or less will mean it will never timeout. The final
	 * parameter is called if it does happen to timeout.
	 */
	public HasDoneChecker(Runnable onEnd, LoopChecker canContinue, int seconds, Runnable ontimeout){
		this.onend=onEnd;
		this.cancontinue=canContinue;
		this.seconds=seconds;
		new Timer().schedule(new TimerTask(){
			public void run(){
				if(!HasDoneChecker.this.running){
					cancel();
					return;
				}
				if(HasDoneChecker.this.seconds>0)HasDoneChecker.this.seconds--;
				if(HasDoneChecker.this.seconds==0){
					cancel();
					HasDoneChecker.this.ontimeout.run();
					HasDoneChecker.this.running=false;
					return;
				}
				if(HasDoneChecker.this.cancontinue.run()){
					cancel();
					HasDoneChecker.this.onend.run();
					HasDoneChecker.this.running=false;
					return;
				}
			}
		}, 1000, 1000);
	}
	public boolean stillRunning(){
		return this.running;
	}
	public void stop(){
		this.running=false;
	}
}