package projectat.trutils.core;

import com.google.common.collect.Lists;
import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.List;

public class MixinInit implements ILateMixinLoader {

    @Override
    public List<String> getMixinConfigs() {
        return Lists.newArrayList("mixins.trutils.json");
    }

}
