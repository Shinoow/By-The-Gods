package com.shinoow.btg.common.rituals;

import com.shinoow.abyssalcraft.api.ritual.NecronomiconRitual;

public abstract class InvocationRitual extends NecronomiconRitual {

	public InvocationRitual(String unlocalizedName, Object...offerings) {
		super(unlocalizedName, 0, 0, 5000, true, offerings);

	}
}
