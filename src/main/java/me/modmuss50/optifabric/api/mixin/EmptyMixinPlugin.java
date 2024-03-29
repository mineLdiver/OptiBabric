package me.modmuss50.optifabric.api.mixin;

import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public abstract class EmptyMixinPlugin implements IMixinConfigPlugin {
	@Override
	public void onLoad(String mixinPackage) {}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

	@Override
	public List<String> getMixins() {
		return Collections.emptyList();
	}
}