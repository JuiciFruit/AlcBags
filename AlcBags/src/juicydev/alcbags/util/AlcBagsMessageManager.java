package juicydev.alcbags.util;

import juicydev.alcbags.AlcBags;
import juicydev.jcore.utils.MessageManager;

/**
 * @author JuicyDev
 */
public class AlcBagsMessageManager {

	private static MessageManager instance;

	public static void setup() {
		instance = new MessageManager(AlcBags.getInstance().getName());
	}

	public static MessageManager getInstance() {
		return instance;
	}
}
