package game;

import java.util.HashMap;

public class ButtonManager {
	HashMap<String, Boolean> states;
	HashMap<String, String> pairs;

	public ButtonManager() {
		states = new HashMap<String, Boolean>();
		pairs = new HashMap<String, String>();
	}

	public void addPair(String key, String value) {
		pairs.put(key, value);
		states.put(key, true);
		states.put(value, true);
	}

	public void checkIn(String key, Boolean value) {

	}

	public void update() {
		String[] keys = (String[]) pairs.keySet().toArray();
		for (int i = 0; i < keys.length; i++) {
			// Set the state of the caused to that of the causer
			states.put(pairs.get(keys[i]), states.get(keys[i]));
		}
	}
}
