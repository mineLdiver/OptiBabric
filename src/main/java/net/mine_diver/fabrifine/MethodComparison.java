package net.mine_diver.fabrifine;

import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.Objects;
import java.util.function.Consumer;

class MethodComparison {
	public final MethodNode node;
	public final boolean equal, effectivelyEqual;

	public MethodComparison(MethodNode original, MethodNode patched) {
		this(original, patched, false);
	}

	MethodComparison(MethodNode original, MethodNode patched, boolean permissive) {
		assert Objects.equals(original.name, patched.name) || permissive;
		assert Objects.equals(original.desc, patched.desc) || permissive;
		node = patched;

		effectivelyEqual = compare(original.instructions, patched.instructions);
		equal = effectivelyEqual;
	}

	private static int nextInterestingNode(InsnList list, int start) {
		AbstractInsnNode node;
		do {
			if (start >= list.size()) {
				return -1;
			}

			node = list.get(start++);
		} while (node.getType() == AbstractInsnNode.LINE || node.getType() == AbstractInsnNode.FRAME);

		return start - 1;
	}

	private boolean compare(InsnList listA, InsnList listB) {
		int a = nextInterestingNode(listA, 0);
		int b = nextInterestingNode(listB, 0);

		while (a >= 0 && b >= 0) {
			AbstractInsnNode insnA = listA.get(a);
			AbstractInsnNode insnB = listB.get(b);

			if (!compare(listA, listB, insnA, insnB)) {
				return false;
			}

			a = nextInterestingNode(listA, a + 1);
			b = nextInterestingNode(listB, b + 1);
		}

		return a == b;
	}

	private boolean compare(InsnList listA, InsnList listB, AbstractInsnNode insnA, AbstractInsnNode insnB) {
		if (insnA.getType() != insnB.getType() || insnA.getOpcode() != insnB.getOpcode()) return false;

		switch (insnA.getType()) {
		case AbstractInsnNode.INT_INSN: {
			IntInsnNode a = (IntInsnNode) insnA;
			IntInsnNode b = (IntInsnNode) insnB;

			return a.operand == b.operand;
		}

		case AbstractInsnNode.VAR_INSN: {
			VarInsnNode a = (VarInsnNode) insnA;
			VarInsnNode b = (VarInsnNode) insnB;

			return a.var == b.var;
		}

		case AbstractInsnNode.TYPE_INSN: {
			TypeInsnNode a = (TypeInsnNode) insnA;
			TypeInsnNode b = (TypeInsnNode) insnB;

			return Objects.equals(a.desc, b.desc);
		}

		case AbstractInsnNode.FIELD_INSN: {
			FieldInsnNode a = (FieldInsnNode) insnA;
			FieldInsnNode b = (FieldInsnNode) insnB;

			return Objects.equals(a.owner, b.owner) && Objects.equals(a.name, b.name) && Objects.equals(a.desc, b.desc);
		}

		case AbstractInsnNode.METHOD_INSN: {
			MethodInsnNode a = (MethodInsnNode) insnA;
			MethodInsnNode b = (MethodInsnNode) insnB;

			if (!Objects.equals(a.owner, b.owner) || !Objects.equals(a.name, b.name) || !Objects.equals(a.desc, b.desc)) {
				return false;
			}
			//More debatable if the actual method is the same, we'll go with it being a change for now
			return a.itf == b.itf;
		}

		case AbstractInsnNode.INVOKE_DYNAMIC_INSN: {
			throw new IllegalStateException("This really should never happen, as old OptiFine was built on Java 5");
		}

		case AbstractInsnNode.JUMP_INSN: {
			JumpInsnNode a = (JumpInsnNode) insnA;
			JumpInsnNode b = (JumpInsnNode) insnB;

			//Check if the 2 jumps have the same direction, possibly should check if it's to the same positioned labels
			return Integer.signum(listA.indexOf(a.label) - listA.indexOf(a)) == Integer.signum(listB.indexOf(b.label) - listB.indexOf(b));
		}

		case AbstractInsnNode.LDC_INSN: {
			LdcInsnNode a = (LdcInsnNode) insnA;
			LdcInsnNode b = (LdcInsnNode) insnB;
			Class<?> typeClsA = a.cst.getClass();

			if (typeClsA != b.cst.getClass()) return false;

			if (typeClsA == Type.class) {
				Type typeA = (Type) a.cst;
				Type typeB = (Type) b.cst;

				if (typeA.getSort() != typeB.getSort()) return false;

				switch (typeA.getSort()) {
					case Type.ARRAY, Type.OBJECT -> {
						return Objects.equals(typeA.getDescriptor(), typeB.getDescriptor());
					}
					case Type.METHOD -> throw new UnsupportedOperationException("Bad sort: " + typeA);
				}
			} else {
				return a.cst.equals(b.cst);
			}
		}

		case AbstractInsnNode.IINC_INSN: {
			IincInsnNode a = (IincInsnNode) insnA;
			IincInsnNode b = (IincInsnNode) insnB;

			return a.incr == b.incr && a.var == b.var;
		}

		case AbstractInsnNode.TABLESWITCH_INSN: {
			TableSwitchInsnNode a = (TableSwitchInsnNode) insnA;
			TableSwitchInsnNode b = (TableSwitchInsnNode) insnB;

			return a.min == b.min && a.max == b.max;
		}

		case AbstractInsnNode.LOOKUPSWITCH_INSN: {
			LookupSwitchInsnNode a = (LookupSwitchInsnNode) insnA;
			LookupSwitchInsnNode b = (LookupSwitchInsnNode) insnB;

			return a.keys.equals(b.keys);
		}

		case AbstractInsnNode.MULTIANEWARRAY_INSN: {
			MultiANewArrayInsnNode a = (MultiANewArrayInsnNode) insnA;
			MultiANewArrayInsnNode b = (MultiANewArrayInsnNode) insnB;

			return a.dims == b.dims && Objects.equals(a.desc, b.desc);
		}

		case AbstractInsnNode.INSN:
		case AbstractInsnNode.LABEL: {
			return true; //Doesn't need any additional comparisons
		}

		case AbstractInsnNode.LINE:
		case AbstractInsnNode.FRAME:
		default:
			throw new IllegalArgumentException("Unexpected instructions: " + insnA + ", " + insnB);
		}
	}

	static boolean isJavaLambdaMetafactory(Handle bsm) {
		return bsm.getTag() == Opcodes.H_INVOKESTATIC
				&& "java/lang/invoke/LambdaMetafactory".equals(bsm.getOwner())
				&& ("metafactory".equals(bsm.getName())
						&& "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;".equals(bsm.getDesc())
						|| "altMetafactory".equals(bsm.getName())
						&& "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite;".equals(bsm.getDesc())
				) && !bsm.isInterface();
	}

	private static void findLambdas(InsnList instructions, int from, Consumer<InvokeDynamicInsnNode> instructionEater) {
		for (; from < instructions.size(); from++) {
			AbstractInsnNode insn = instructions.get(from);

			if (insn.getType() == AbstractInsnNode.INVOKE_DYNAMIC_INSN) {
				InvokeDynamicInsnNode idin = (InvokeDynamicInsnNode) insn;

				if (isJavaLambdaMetafactory(idin.bsm)) {
					instructionEater.accept(idin);
				} else if ("java/lang/invoke/StringConcatFactory".equals(idin.bsm.getOwner()) || "java/lang/runtime/ObjectMethods".equals(idin.bsm.getOwner())) {
					//These won't have any methods within the class to find
				} else {
					throw new IllegalStateException(String.format("Unknown invokedynamic bsm: %s#%s%s (tag=%d iif=%b)", idin.bsm.getOwner(), idin.bsm.getName(), idin.bsm.getDesc(), idin.bsm.getTag(), idin.bsm.isInterface()));
				}
			}
		}
	}

	@Override
	public String toString() {
		return node.name.concat(node.desc);
	}
}