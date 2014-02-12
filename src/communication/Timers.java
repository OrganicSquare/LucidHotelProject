package communication;

import game.Menu;

import java.util.Timer;
import java.util.TimerTask;

public class Timers {
	public static Timer timerThread1;
	public static Timer timerThread2;
	public static Timer timerThread3;
	public static Timer timerThread4;
	public static void main(String args[]){
		initiateTimer1(10000,timerThread1, 0);
	}
	public static void initiateTimer1(int seconds, Timer timerToUse, int opCode) {
		timerToUse = new Timer();
		timerToUse.schedule(new initiateTimerComplete1(seconds, timerToUse, opCode), seconds);
	}
	public static void initiateTime2(int seconds, Timer timerToUse, int opCode) {
		timerToUse = new Timer();
		timerToUse.schedule(new initiateTimerComplete2(seconds, timerToUse, opCode), seconds);
	}
	public static void initiateTimer3(int seconds, Timer timerToUse, int opCode) {
		timerToUse = new Timer();
		timerToUse.schedule(new initiateTimerComplete3(seconds, timerToUse, opCode), seconds);
	}
	public static void initiateTimer4(int seconds, Timer timerToUse, int opCode) {
		timerToUse = new Timer();
		timerToUse.schedule(new initiateTimerComplete4(seconds, timerToUse, opCode), seconds);
	}
	static class initiateTimerComplete1 extends TimerTask {
		static Timer globalTimerToUse = null;
		static int globalSeconds = 0;
		static int globalOpCode = 0;
		 public initiateTimerComplete1(int seconds, Timer timerToUse, int opCode) {
			 globalTimerToUse = timerToUse;
			 globalSeconds = seconds;
			 globalOpCode = opCode;
		}

		public void run() {
			// This is the code that should be checked
			if(globalOpCode == 0){
				System.out.println("Operation 0 being called");
			}
			else
				System.out.println("Checking user login");
				if(Menu.checkUserLoginIsOk()){globalTimerToUse.cancel(); System.out.println("User login ok");}
				else {initiateTimer1(2000, globalTimerToUse, globalOpCode);}
			}
		 }
	 }
	class initiateTimerComplete2 extends TimerTask {
		static Timer globalTimerToUse = null;
		static int globalSeconds = 0;
		static int globalOpCode = 0;
		 public initiateTimerComplete2(int seconds, Timer timerToUse, int opCode) {
			 globalTimerToUse = timerToUse;
			 globalSeconds = seconds;
			 globalOpCode = opCode;
		}

		public void run() {
			// This is the code that should be checked
			if(globalOpCode == 0){
				System.out.println("Operation 0 being called");
			}
			else {
				System.out.println("Check user download");
				if(Menu.checkUserDetailsDownloaded()){ 
					System.out.println("WOO check user details ok");
				}
				else {
					Timers.initiateTime2(200,Timers.timerThread2, 2);
				}
			}
		 }
	 }
	class initiateTimerComplete3 extends TimerTask {
		static Timer globalTimerToUse = null;
		static int globalSeconds = 0;
		static int globalOpCode = 0;
		 public initiateTimerComplete3(int seconds, Timer timerToUse, int opCode) {
			 globalTimerToUse = timerToUse;
			 globalSeconds = seconds;
			 globalOpCode = opCode;
		}

		public void run() {
			// This is the code that should be checked
			if(globalOpCode == 0){
				System.out.println("Operation 0 being called");
			}
			else {
				Menu.checkModelsHaveLoaded();
			}
		 }
	 }
	 class initiateTimerComplete4 extends TimerTask {
		static Timer globalTimerToUse = null;
		static int globalSeconds = 0;
		static int globalOpCode = 0;
		 public initiateTimerComplete4(int seconds, Timer timerToUse, int opCode) {
			 globalTimerToUse = timerToUse;
			 globalSeconds = seconds;
			 globalOpCode = opCode;
		}

		public void run() {
			// This is the code that should be checked
			if(globalOpCode == 0){
				System.out.println("Operation 0 being called");
			}
			else {
				Menu.checkAndRunAllFlagsAccepted();
			}
		 }
	 
	
}
