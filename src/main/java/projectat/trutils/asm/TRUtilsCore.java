package projectat.trutils.asm;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.Name("trutilscore")
@IFMLLoadingPlugin.TransformerExclusions({"projectat.trutils.asm"})
public class TRUtilsCore implements IFMLLoadingPlugin {

    public static final Logger logger = LogManager.getLogger("TRUtils Core");

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{
                "projectat.trutils.asm.common.TRUtilsTransformer"
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
