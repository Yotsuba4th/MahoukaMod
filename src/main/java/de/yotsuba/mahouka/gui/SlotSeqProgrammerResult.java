package de.yotsuba.mahouka.gui;

import net.minecraft.inventory.Slot;

public class SlotSeqProgrammerResult extends Slot
{

    private final SequenceProgrammerContainer container;

    public SlotSeqProgrammerResult(SequenceProgrammerContainer container, int index, int x, int y)
    {
        super(container, index, x, y);
        this.container = container;
    }

    @Override
    public int getSlotStackLimit()
    {
        // return 64;
        return container.inventory[SequenceProgrammerContainer.OUT] == null ? 64 : 0;
    }

}
