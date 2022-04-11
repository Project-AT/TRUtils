package ikexing.atutils.asm;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.Name("atutilscore")
@IFMLLoadingPlugin.TransformerExclusions({"ikexing.atutils.asm"})
public class ATUtilsCore implements IFMLLoadingPlugin {

    public static final Logger logger = LogManager.getLogger("AtUtils Core");

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{
                "ikexing.atutils.asm.common.ATUtilsTransformer"
        };
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

}
