package com.kiva.client.mixins;

import com.kiva.capitalizeksupronouns.CapitalizeKSUPronounsClient;
import net.minecraft.client.Minecraft;
import net.minecraft.src.client.packets.NetClientHandler;
import net.minecraft.src.client.packets.Packet3Chat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = NetClientHandler.class)
public class MixinNetClientHandler {
    @Shadow private Minecraft mc;

    // This could be an @Overwrite, but I could not get it work in Intellij IDEA
    @Inject(method = "handleChat", at = @At("HEAD"), cancellable = true)
    public void onHandleChat(Packet3Chat packet3, CallbackInfo ci) {
        if (!packet3.message.startsWith(CapitalizeKSUPronounsClient.pronounsStartStr))
            return;

        int pronounsIndexStart = packet3.message.indexOf(CapitalizeKSUPronounsClient.pronounsStartStr);
        int pronounsIndexEnd = packet3.message.indexOf(CapitalizeKSUPronounsClient.pronounsEndStr);

        // Hacky, could implement a proper parser but this should work fine.
        if (pronounsIndexStart == -1 || pronounsIndexEnd == -1){
            mc.ingameGUI.addChatMessageTranslate(packet3.message);
            ci.cancel();
            return;
        }

        StringBuilder stringBuilder = new StringBuilder(packet3.message);

        boolean startOfAPronoun = true;
        for (int i = pronounsIndexStart + CapitalizeKSUPronounsClient.pronounsStartStr.length(); i < pronounsIndexEnd; i++){
            char character = packet3.message.charAt(i);

            if (character == '/'){
                startOfAPronoun = true;
                continue;
            }

            if (startOfAPronoun) {
                stringBuilder.setCharAt(i, Character.toUpperCase(character));
                startOfAPronoun = false;
            }
        }

        mc.ingameGUI.addChatMessageTranslate(stringBuilder.toString());
        ci.cancel();
    }
}
