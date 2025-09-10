package simpleclient.module.setting;

import simpleclient.module.Mod;
import simpleclient.module.settings.BooleanSetting;
import simpleclient.module.settings.NumberSetting;

public class TargetHUDSettings extends Mod {

    public NumberSetting range = new NumberSetting("Range", 10, 500, 100, 1);
    public BooleanSetting displayAsHearts = new BooleanSetting("Hearts", false);
    public BooleanSetting noDisplayWhenNoPlayer = new BooleanSetting("DisplayWhenNone", false);
    public BooleanSetting ignoreFriendly = new BooleanSetting("Friend", false);
    public BooleanSetting test = new BooleanSetting("Test", false);

    public TargetHUDSettings() {
        super("TargetHUD", "Settings of target hud", Category.SETTING);
        addSettings(range, displayAsHearts, noDisplayWhenNoPlayer, ignoreFriendly, test);
    }

    @Override
    public void onEnable() {
        setEnabled(false);
    }
}
