package net.thenextlvl.perworlds.data;

import org.bukkit.attribute.Attribute;
import org.jspecify.annotations.NullMarked;

@NullMarked
record AttributeDataImpl(Attribute attribute, double baseValue) implements AttributeData {
    @Override
    public AttributeData baseValue(double value) {
        return new AttributeDataImpl(attribute, value);
    }
}
