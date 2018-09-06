package me.aelesia.runescape.tasks.base;

import java.util.Arrays;

import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.region.Banks;

import me.aelesia.runescape.actions.GameActions;
import me.aelesia.runescape.exceptions.IllegalArgumentException;
import me.aelesia.runescape.exceptions.ObjectNotFoundException;
import me.aelesia.runescape.script.Rest.State;
import me.aelesia.runescape.utils.game.Logger;
import me.aelesia.runescape.utils.general.CommonUtils;

public abstract class BankDepositTask extends BaseTask {
	
	protected String[] itemsToDeposit;
	
	public BankDepositTask(String[] itemsToDeposit) {
		if (CommonUtils.isEmpty(itemsToDeposit)) {
			throw new IllegalArgumentException("");
		}
		this.itemsToDeposit = itemsToDeposit;
	}
	
	@Override
	public void validate() {
		if (Banks.newQuery().results().isEmpty()) {
			throw new ObjectNotFoundException("No bank found");
		}
	}
	
	@Override
	public void initialize() {}
	
	@Override
	public void exit() {
		GameActions.closeBank();
	}
	
	@Override
	public void execute() {
		SpriteItem item = Inventory.getItems(itemsToDeposit).first();
		if (item!=null) {
			if (!Bank.isOpen()) {
				GameActions.openBank();
			} else {
				GameActions.depositAll(item);
			}
		}
	}
}
