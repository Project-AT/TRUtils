package ikexing.atutils.asm.transformers;

import ikexing.atutils.asm.utils.AsmUtils;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ListIterator;

public class ChoppableTransformer {

    public static void transform(ClassNode classNode) {
        AsmUtils.findMethod(classNode, "getResultsSawmill", null)
            .ifPresent(it -> {
                ListIterator<AbstractInsnNode> iterator = it.instructions.iterator();
                while (iterator.hasNext()) {
                    AbstractInsnNode node = iterator.next();
                    if (AsmUtils.matchMethodInsn(node, Opcodes.INVOKEVIRTUAL,
                        "getOutputMultiplier",
                        null,
                        "()D",
                        false)) {
                        AbstractInsnNode next = node.getNext();

                        if (next instanceof LdcInsnNode) {
                            ((LdcInsnNode) next).cst = 6.0;
                        }
                        break;
                    }
                }
            });

    }
}
