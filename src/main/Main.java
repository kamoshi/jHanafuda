package main;

import java.awt.EventQueue;
import gui.Launcher;

// Main
public class Main {
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Launcher();
			}
		});
		
	}
}
