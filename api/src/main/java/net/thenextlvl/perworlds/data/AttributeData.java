package net.thenextlvl.perworlds.data;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

/**
 * This interface is designed to manage attributes for a player.
 *
 * @since 0.1.0
 */
@NullMarked
@ApiStatus.NonExtendable
public interface AttributeData {
    /**
     * Retrieves the associated attribute.
     *
     * @return the {@code Attribute} instance associated with this attribute data
     */
    @Contract(pure = true)
    Attribute attribute();

    /**
     * Retrieves the base value associated with this attribute data.
     *
     * @return the base value as a double
     */
    @Contract(pure = true)
    double baseValue();

    /**
     * Sets the base value for this attribute data.
     *
     * @param value the new base value to be set
     * @return a new instance of {@code AttributeData} with the updated base value
     */
    @Contract(value = "_ -> new", pure = true)
    AttributeData baseValue(double value);

    /**
     * Creates a new instance of {@code AttributeData} with the specified attribute and base value.
     *
     * @param attribute the {@code Attribute} to associate with this data
     * @param baseValue the base value of the specified attribute
     * @return a new {@code AttributeData} instance based on the provided attribute and base value
     * @since 1.0.0
     */
    @Contract(value = "_, _ -> new", pure = true)
    static AttributeData of(Attribute attribute, double baseValue) {
        return new AttributeDataImpl(attribute, baseValue);
    }

    /**
     * Creates a new instance of {@code AttributeData} based on the provided {@code AttributeInstance}.
     *
     * @param instance the {@code AttributeInstance} containing the attribute and base value
     * @return a new {@code AttributeData} instance with the attribute and base value from the provided instance
     * @since 1.0.0
     */
    @Contract(value = "_ -> new", pure = true)
    static AttributeData of(AttributeInstance instance) {
        return of(instance.getAttribute(), instance.getBaseValue());
    }
}
