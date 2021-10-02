package com.alrex.parcool.client.hud;


import com.alrex.parcool.ParCoolConfig;
import com.alrex.parcool.client.ActionPermissions;
import com.alrex.parcool.common.capability.IStamina;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Date;

@OnlyIn(Dist.CLIENT)
public class StaminaHUD extends AbstractHUD {
	private static final ResourceLocation STAMINA = new ResourceLocation("parcool", "textures/gui/stamina_bar.png");

	public StaminaHUD(Position pos) {
		super(pos);
	}

	private float shadowScale = 1f;

	public void render(MatrixStack stack) {
		if (!ParCoolConfig.CONFIG_CLIENT.ParCoolActivation.get()) return;
		ClientPlayerEntity player = Minecraft.getInstance().player;
		if (player == null) return;
		if (player.isCreative()) return;
		IStamina stamina = IStamina.get(player);
		if (stamina == null) return;
		if (ParCoolConfig.CONFIG_CLIENT.hideStaminaHUD.get() && ParCoolConfig.CONFIG_CLIENT.infiniteStamina.get() && ActionPermissions.isAllowedInfiniteStamina())
			return;
		int renderGage = (int) ((new Date().getTime() / 500) % 3);

		MainWindow window = Minecraft.getInstance().getMainWindow();
		final int width = window.getScaledWidth();
		final int height = window.getScaledHeight();
		final int boxWidth = 91;
		final int boxHeight = 17;
		final Tuple<Integer, Integer> pos = position.calculate(boxWidth, boxHeight, width, height);

		float staminaScale = (float) stamina.getStamina() / stamina.getMaxStamina();
		if (staminaScale < 0) staminaScale = 0;
		if (staminaScale > 1) staminaScale = 1;

		Minecraft.getInstance().getTextureManager().bindTexture(STAMINA);
		AbstractHUD.func_238463_a_(stack, pos.getA(), pos.getB(), 0f, 0f, 92, 16, 128, 128);
		if (!stamina.isExhausted()) {
			AbstractHUD.func_238463_a_(stack, pos.getA(), pos.getB(), 0f, 85f, Math.round(16 + 69 * shadowScale) + 1, 16, 128, 128);
			AbstractHUD.func_238463_a_(stack, pos.getA(), pos.getB(), 0f, 17 * (renderGage + 1), Math.round(16 + 69 * staminaScale) + 1, 16, 128, 128);
		} else {
			AbstractHUD.func_238463_a_(stack, pos.getA(), pos.getB(), 0f, 68f, Math.round(16 + 69 * staminaScale) + 1, 16, 128, 128);
		}
		shadowScale = staminaScale - (staminaScale - shadowScale) / 1.1f;
	}
	private static int getColorCodeFromARGB(int a, int r, int g, int b) {
		return a * 0x1000000 + r * 0x10000 + g * 0x100 + b;
	}
}
