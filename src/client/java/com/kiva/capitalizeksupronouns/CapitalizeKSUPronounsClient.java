package com.kiva.capitalizeksupronouns;

import com.fox2code.foxloader.loader.ClientMod;
import com.fox2code.foxloader.network.ChatColors;

public class CapitalizeKSUPronounsClient extends CapitalizeKSUPronouns implements ClientMod {
    public static final String pronounsStartStr = "[" + ChatColors.GREEN;
    public static final String pronounsEndStr   = ChatColors.RESET + "]";

    @Override
    public void onInit() {
        System.out.println("CapitalizeKSUPronouns initialized");
    }
}
