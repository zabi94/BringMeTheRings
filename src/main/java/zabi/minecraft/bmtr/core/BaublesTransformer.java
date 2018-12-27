package zabi.minecraft.bmtr.core;

import java.util.Iterator;
import java.util.function.Predicate;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

import net.minecraft.launchwrapper.IClassTransformer;
import zabi.minecraft.bmtr.ModConfig;

public class BaublesTransformer implements IClassTransformer {

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if ("baubles.api.BaubleType".equals(transformedName)) {
			return transformBaubleType(basicClass);
		}
		if ("baubles.api.cap.BaublesContainer".equals(transformedName)) {
			return transformBaublesContainer(basicClass);
		}
		if ("baubles.common.container.ContainerPlayerExpanded".equals(transformedName)) {
			return transformContainerPlayerExpanded(basicClass);
		}
		if ("baubles.common.event.EventHandlerEntity".equals(transformedName)) {
			return transformEventHandlerEntity(basicClass);
		}
		
		if ("baubles.client.gui.GuiPlayerExpanded".equals(transformedName)) {
			try {
				return transformGuiPlayerExpanded(basicClass);
			} catch (ASMException e) {
				System.err.println("Failed to transform GuiPlayerExpanded, ignoring as this is normal on a server");
				e.printStackTrace();
				return basicClass;
			}
		}
		
		if ("baubles.common.CommonProxy".equals(transformedName)) {
			return transformCommonProxy(basicClass);
		}
		return basicClass;
	}

	private byte[] transformEventHandlerEntity(byte[] basicClass) {
		ClassReader cr = new ClassReader(basicClass);
		ClassNode cn = new ClassNode();
		cr.accept(cn, 0);
		
		
		MethodNode mn = locateMethod(cn, "attachCapabilitiesPlayer");
		AbstractInsnNode inst = locateTargetInsn(mn, n -> Opcodes.NEW == n.getOpcode() && ((TypeInsnNode)n).desc.equals("baubles/api/cap/BaublesContainer") && n.getNext().getNext().getOpcode() == Opcodes.INVOKESPECIAL);
		AbstractInsnNode constructor = inst.getNext().getNext();
		mn.instructions.insert(constructor, new MethodInsnNode(Opcodes.INVOKESPECIAL, "zabi/minecraft/bmtr/components/BaublesStackHandler", "<init>", "()V", false));
		mn.instructions.remove(constructor);
		mn.instructions.insert(inst, new TypeInsnNode(Opcodes.NEW, "zabi/minecraft/bmtr/components/BaublesStackHandler"));
		mn.instructions.remove(inst);
		
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS) {
			@Override
			protected String getCommonSuperClass(String type1, String type2) {
				System.out.println();
				return super.getCommonSuperClass(type1, type2);
			}
		};
		cn.accept(cw);
		return cw.toByteArray();
	}

	private byte[] transformCommonProxy(byte[] basicClass) {
		ClassReader cr = new ClassReader(basicClass);
		ClassNode cn = new ClassNode();
		cr.accept(cn, 0);
		
		MethodNode mn = locateMethod(cn, "getServerGuiElement");
		AbstractInsnNode node = locateTargetInsn(mn, n -> n.getOpcode() == Opcodes.NEW && n.getNext().getOpcode()==Opcodes.DUP);
		mn.instructions.insert(node, new TypeInsnNode(Opcodes.NEW, "zabi/minecraft/bmtr/components/ContainerBaubles"));
		mn.instructions.remove(node);
		
		AbstractInsnNode initContainer = locateTargetInsn(mn, n -> n.getOpcode()==Opcodes.INVOKESPECIAL && ((MethodInsnNode)n).owner.equals("baubles/common/container/ContainerPlayerExpanded") && ((MethodInsnNode)n).name.equals("<init>"));
		mn.instructions.insert(initContainer, new MethodInsnNode(Opcodes.INVOKESPECIAL, "zabi/minecraft/bmtr/components/ContainerBaubles", "<init>", new String(((MethodInsnNode) initContainer).desc), false));
		mn.instructions.remove(initContainer);
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
		cn.accept(cw);
		return cw.toByteArray();
	}

	private byte[] transformGuiPlayerExpanded(byte[] basicClass) {
		ClassReader cr = new ClassReader(basicClass);
		ClassNode cn = new ClassNode();
		cr.accept(cn, 0);
		
		MethodNode mn = locateMethod(cn, "<init>");
		AbstractInsnNode node = locateTargetInsn(mn, n -> n.getOpcode() == Opcodes.NEW && n.getNext().getOpcode()==Opcodes.DUP);
		mn.instructions.insert(node, new TypeInsnNode(Opcodes.NEW, "zabi/minecraft/bmtr/components/ContainerBaubles"));
		mn.instructions.remove(node);
		AbstractInsnNode initContainer = locateTargetInsn(mn, n -> n.getOpcode()==Opcodes.INVOKESPECIAL && ((MethodInsnNode)n).owner.equals("baubles/common/container/ContainerPlayerExpanded") && ((MethodInsnNode)n).name.equals("<init>"));
		mn.instructions.insert(initContainer, new MethodInsnNode(Opcodes.INVOKESPECIAL, "zabi/minecraft/bmtr/components/ContainerBaubles", "<init>", new String(((MethodInsnNode)initContainer).desc), false));
		mn.instructions.remove(initContainer);
		
		
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
		cn.accept(cw);
		return cw.toByteArray();
	}

	private byte[] transformContainerPlayerExpanded(byte[] basicClass) {
		ClassReader cr = new ClassReader(basicClass);
		ClassNode cn = new ClassNode();
		cr.accept(cn, 0);
		MethodNode mn = locateMethod(cn, "<init>");
		AbstractInsnNode ain = locateTargetInsn(mn, n -> n.getOpcode() == Opcodes.BIPUSH && n.getNext().getOpcode() == Opcodes.BIPUSH && n.getNext().getNext().getOpcode()==Opcodes.BIPUSH && ((IntInsnNode)n).operand == 6);
		while (ain.getOpcode() != Opcodes.POP) {
			ain = ain.getNext();
		}
		InsnList list = new InsnList();
		list.add(new IntInsnNode(Opcodes.ALOAD, 0));
		list.add(new IntInsnNode(Opcodes.ALOAD, 3));
		list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "zabi/minecraft/bmtr/core/Snippets", "addSlotsToContainer", "(Lbaubles/common/container/ContainerPlayerExpanded;Ljava/lang/Object;)V", false));
		mn.instructions.insert(ain, list);
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
		cn.accept(cw);
		return cw.toByteArray();
	}

	private byte[] transformBaublesContainer(byte[] basicClass) {
		ClassReader cr = new ClassReader(basicClass);
		ClassNode cn = new ClassNode();
		cr.accept(cn, 0);
		
		transformConstant(cn);
		transformConstructor(cn);
		transformSetSize(cn);
		
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
		cn.accept(cw);
		return cw.toByteArray();
	}

	private void transformSetSize(ClassNode cn) {
		MethodNode mn = locateMethod(cn, "(I)V", "setSize");
		AbstractInsnNode l0 = locateTargetInsn(mn, n -> n instanceof LabelNode);
		AbstractInsnNode l1 = locateTargetInsn(mn, n -> n instanceof LabelNode && !((LabelNode) n).getLabel().equals(((LabelNode) l0).getLabel()));
		while (!l0.getNext().equals(l1)) {
			mn.instructions.remove(l0.getNext());
		}
	}

	private void transformConstructor(ClassNode cn) {
		MethodNode mn = locateMethod(cn, "()V", "<init>");
		AbstractInsnNode bipush = locateTargetInsn(mn, n -> n.getOpcode() == Opcodes.BIPUSH && n.getNext().getOpcode() == Opcodes.INVOKESPECIAL);
		mn.instructions.insert(bipush, new IntInsnNode(Opcodes.BIPUSH, 7 + ModConfig.getExtraSlots()));
		mn.instructions.remove(bipush);

		AbstractInsnNode arraySizeNode = locateTargetInsn(mn, n -> n.getOpcode() == Opcodes.BIPUSH && n.getNext().getOpcode() == Opcodes.NEWARRAY);
		mn.instructions.insert(arraySizeNode, new IntInsnNode(Opcodes.BIPUSH, 7 + ModConfig.getExtraSlots()));
		mn.instructions.remove(arraySizeNode);
	}

	private void transformConstant(ClassNode cn) {
		FieldNode fn = cn.fields.parallelStream()
				.filter(f -> "BAUBLE_SLOTS".equals(f.name))
				.findFirst().orElseThrow(() -> new ASMException("Can't find field BAUBLE_SLOTS"));
		fn.value = ModConfig.getExtraSlots();
	}

	private byte[] transformBaubleType(byte[] basicClass) {
		ClassReader cr = new ClassReader(basicClass);
		ClassNode cn = new ClassNode();
		cr.accept(cn, 0);
		//new rings will have index 7, 8, 9, 10, 11, 12, 13, 14
		MethodNode mn = locateMethod(cn, "()V", "<clinit>");
		addRings(mn);
		addTrinkets(mn);
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES|ClassWriter.COMPUTE_MAXS);
		cn.accept(cw);
		return cw.toByteArray();
	}
	
	private void addTrinkets(MethodNode mn) {
		AbstractInsnNode arraysizeNode = locateTargetInsn(mn, n -> n.getOpcode() == Opcodes.LDC && ((LdcInsnNode)n).cst.equals("TRINKET")).getNext().getNext();
		mn.instructions.insert(arraysizeNode, new IntInsnNode(Opcodes.BIPUSH, 7 + ModConfig.getExtraSlots()));
		mn.instructions.remove(arraysizeNode);
		AbstractInsnNode init = locateTargetInsn(mn, n -> n.getOpcode() == Opcodes.INVOKESPECIAL && ((MethodInsnNode) n).name.equals("<init>") && ((MethodInsnNode) n).desc.equals("(Ljava/lang/String;I[I)V") && n.getNext().getOpcode() == Opcodes.PUTSTATIC && ((FieldInsnNode)n.getNext()).name.equals("TRINKET"));
		InsnList initArrayInsns = new InsnList();
		for (int i = 7; i < 7 + ModConfig.getExtraSlots(); i++) {
			initArrayInsns.add(new InsnNode(Opcodes.DUP));
			initArrayInsns.add(new IntInsnNode(Opcodes.BIPUSH, i));
			initArrayInsns.add(new IntInsnNode(Opcodes.BIPUSH, i));
			initArrayInsns.add(new InsnNode(Opcodes.IASTORE));
		}
		mn.instructions.insertBefore(init, initArrayInsns);
	}

	private void addRings(MethodNode mn) {
		AbstractInsnNode arraysizeNode = locateTargetInsn(mn, n -> n.getOpcode() == Opcodes.LDC && ((LdcInsnNode)n).cst.equals("RING")).getNext().getNext();

		mn.instructions.insert(arraysizeNode, new IntInsnNode(Opcodes.BIPUSH, 2 + ModConfig.getExtraSlots()));
		mn.instructions.remove(arraysizeNode);
		AbstractInsnNode init = locateTargetInsn(mn, n -> n instanceof MethodInsnNode && ((MethodInsnNode) n).name.equals("<init>") && ((MethodInsnNode) n).desc.equals("(Ljava/lang/String;I[I)V") && n.getNext().getOpcode() == Opcodes.PUTSTATIC && ((FieldInsnNode)n.getNext()).name.equals("RING"));
		InsnList initArrayInsns = new InsnList();
		for (int i = 2; i<2 + ModConfig.getExtraSlots(); i++) {
			initArrayInsns.add(new InsnNode(Opcodes.DUP));
			initArrayInsns.add(new IntInsnNode(Opcodes.BIPUSH, i));
			initArrayInsns.add(new IntInsnNode(Opcodes.BIPUSH, i+5));
			initArrayInsns.add(new InsnNode(Opcodes.IASTORE));
		}
		mn.instructions.insertBefore(init, initArrayInsns);
	}

	private static MethodNode locateMethod(ClassNode cn, String desc, String nameIn) {
		return cn.methods.parallelStream()
				.filter(n -> n.desc.equals(desc) && (n.name.equals(nameIn)))
				.findAny().orElseThrow(() -> new ASMException(nameIn +": "+desc+" cannot be found in "+cn.name, cn));
	}
	
	private static MethodNode locateMethod(ClassNode cn, String nameIn) {
		return cn.methods.parallelStream()
				.filter(n -> n.name.equals(nameIn))
				.findAny()
				.orElseThrow(() -> new ASMException(nameIn+" cannot be found in "+cn.name, cn));
	}

	private static AbstractInsnNode locateTargetInsn(MethodNode mn, Predicate<AbstractInsnNode> filter) {
		AbstractInsnNode target = null;
		Iterator<AbstractInsnNode> i = mn.instructions.iterator();
		while (i.hasNext() && target == null) {
			AbstractInsnNode n = i.next();
			if (filter.test(n)) {
				target = n;
			}
		}
		if (target==null) {
			throw new ASMException("Can't locate target instruction in "+mn.name, mn);
		}
		return target;
	}
}
