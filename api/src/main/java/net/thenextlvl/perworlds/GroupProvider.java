package net.thenextlvl.perworlds;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Represents a provider responsible for managing and interacting with {@link WorldGroup world groups}.
 * A group contains multiple worlds and is defined alongside its settings and associated data.
 * This interface provides various functionalities to create, retrieve, verify, and remove groups.
 *
 * @since 0.1.0
 */
@NullMarked
@ApiStatus.NonExtendable
public interface GroupProvider {
    /**
     * Retrieves the data folder associated with the group provider.
     * The data folder is used for storing persistent data relevant to groups.
     *
     * @return the path pointing to the data folder used by the group provider
     * @since 1.0.0
     */
    @Contract(pure = true)
    Path getDataFolder();

    /**
     * Retrieves an unmodifiable set of all world groups managed by this provider.
     * <p>
     * This includes both explicitly defined world groups and the unowned world group.
     *
     * @return an unmodifiable set of {@link WorldGroup} instances managed by this provider
     * @see #getGroups()
     */
    @Unmodifiable
    Set<WorldGroup> getAllGroups();

    /**
     * Retrieves an unmodifiable set of all world groups managed by this provider.
     *
     * @return an unmodifiable set of {@link WorldGroup} instances managed by this provider
     * @see #getAllGroups()
     */
    @Unmodifiable
    Set<WorldGroup> getGroups();

    /**
     * Retrieves a {@link WorldGroup} by its name.
     *
     * @param name the name of the world group to retrieve
     * @return an {@link Optional} containing the {@link WorldGroup} if found, otherwise {@link Optional#empty()}
     */
    Optional<WorldGroup> getGroup(String name);

    /**
     * Retrieves the {@link WorldGroup} to which the specified {@link World} belongs.
     *
     * @param world the {@link World} for which the corresponding group is to be retrieved
     * @return an {@link Optional} containing the {@link WorldGroup} if the specified world is
     * part of a group, or {@link Optional#empty()} if the world does not belong to any group
     */
    Optional<WorldGroup> getGroup(World world);

    /**
     * Retrieves the unowned world group.
     * <p>
     * The {@link WorldGroup} represents worlds that do not belong to any explicitly defined group.
     *
     * @return the {@link WorldGroup} instance representing unassociated worlds
     */
    WorldGroup getUnownedWorldGroup();

    /**
     * Creates a new {@link WorldGroup} with the specified name, data, settings, and a collection of worlds.
     * The group must have a unique name and cannot conflict with already existing groups.
     *
     * @param name     the name of the group to be created.
     * @param data     a {@link Consumer} to configure the {@link GroupData} for the new group.
     * @param settings a {@link Consumer} to configure the {@link GroupSettings} for the new group.
     * @param worlds   a collection of {@link World} instances that will be part of the group.
     * @return the created {@link WorldGroup} instance.
     * @throws IllegalStateException if a group with the specified name already exists,
     *                               or if a given world is already part of another group.
     */
    @Contract(value = "_, _, _, _ -> new", mutates = "this")
    WorldGroup createGroup(String name, Consumer<GroupData> data, Consumer<GroupSettings> settings, Collection<World> worlds) throws IllegalStateException;

    /**
     * Creates a new {@link WorldGroup} with the specified name and a collection of worlds.
     * The group must have a unique name and cannot conflict with already existing groups.
     *
     * @param name   the name of the group to be created.
     * @param worlds a collection of {@link World} instances that will be part of the group.
     * @return the created {@link WorldGroup} instance.
     * @throws IllegalStateException if a group with the specified name already exists,
     *                               or if a given world is already part of another group.
     */
    @Contract(value = "_, _ -> new", mutates = "this")
    WorldGroup createGroup(String name, Collection<World> worlds) throws IllegalStateException;

    /**
     * Creates a new {@link WorldGroup} with the specified name, data, and a collection of worlds.
     * The group must have a unique name and cannot conflict with already existing groups.
     *
     * @param name   the name of the group to be created.
     * @param data   a {@link Consumer} to configure the {@link GroupData} for the new group.
     * @param worlds an array of {@link World} instances that will be part of the group.
     * @return the created {@link WorldGroup} instance.
     * @throws IllegalStateException if a group with the specified name already exists,
     *                               or if a given world is already part of another group.
     * @since 1.0.2
     */
    @Contract(value = "_, _, _ -> new", mutates = "this")
    WorldGroup createGroup(String name, Consumer<GroupData> data, World... worlds) throws IllegalStateException;

    /**
     * Creates a new {@link WorldGroup} with the specified name, data, and a collection of worlds.
     * The group must have a unique name and cannot conflict with already existing groups.
     *
     * @param name   the name of the group to be created.
     * @param data   a {@link Consumer} to configure the {@link GroupData} for the new group.
     * @param worlds a collection of {@link World} instances that will be part of the group.
     * @return the created {@link WorldGroup} instance.
     * @throws IllegalStateException if a group with the specified name already exists,
     *                               or if a given world is already part of another group.
     * @since 1.0.2
     */
    @Contract(value = "_, _, _ -> new", mutates = "this")
    WorldGroup createGroup(String name, Consumer<GroupData> data, Collection<World> worlds) throws IllegalStateException;

    /**
     * Creates a new {@link WorldGroup} with the specified name, data, settings, and a collection of worlds.
     * The group must have a unique name and cannot conflict with already existing groups.
     *
     * @param name     the name of the group to be created.
     * @param data     a {@link Consumer} to configure the {@link GroupData} for the new group.
     * @param settings a {@link Consumer} to configure the {@link GroupSettings} for the new group.
     * @param worlds   an array of {@link World} instances that will be part of the group.
     * @return the created {@link WorldGroup} instance.
     * @throws IllegalStateException if a group with the specified name already exists,
     *                               or if a given world is already part of another group.
     */
    @Contract(value = "_, _, _, _ -> new", mutates = "this")
    WorldGroup createGroup(String name, Consumer<GroupData> data, Consumer<GroupSettings> settings, World... worlds) throws IllegalStateException;

    /**
     * Creates a new {@link WorldGroup} with the specified name and a collection of worlds.
     * The group must have a unique name and cannot conflict with already existing groups.
     *
     * @param name   the name of the group to be created.
     * @param worlds an array of {@link World} instances that will be part of the group.
     * @return the created {@link WorldGroup} instance.
     * @throws IllegalStateException if a group with the specified name already exists,
     *                               or if a given world is already part of another group.
     */
    @Contract(value = "_, _ -> new", mutates = "this")
    WorldGroup createGroup(String name, World... worlds) throws IllegalStateException;

    /**
     * Checks if a group with the specified name exists.
     *
     * @param name the name of the group to check for existence
     * @return {@code true} if a group with the specified name exists, {@code false} otherwise
     */
    @Contract(pure = true)
    boolean hasGroup(String name);

    /**
     * Checks if the specified {@link World} is part of any group managed by this provider.
     *
     * @param world the {@link World} to check for group membership
     * @return {@code true} if the specified world is part of a group, {@code false} otherwise
     */
    @Contract(pure = true)
    boolean hasGroup(World world);

    /**
     * Checks if the specified {@link WorldGroup} is managed by this provider.
     *
     * @param group the {@link WorldGroup} to check for management
     * @return {@code true} if this provider manages the specified group, {@code false} otherwise
     */
    @Contract(pure = true)
    boolean hasGroup(WorldGroup group);

    /**
     * Unregisters a group with the specified name from the provider.
     *
     * @param name the name of the group to be removed
     * @return {@code true} if the group was successfully removed, {@code false} otherwise
     * @see #removeGroup(WorldGroup)
     */
    @Contract(mutates = "this")
    boolean removeGroup(String name);

    /**
     * Unregisters the specified {@link WorldGroup} from the provider.
     * This operation does not delete any data associated with the group;
     * it only causes the group to be unloaded.
     *
     * @param group the {@link WorldGroup} to be removed
     * @return whether the group was successfully removed from the provider
     */
    @Contract(mutates = "this")
    boolean removeGroup(WorldGroup group);

    /**
     * Checks whether data for the specified player is currently being loaded.
     *
     * @param player the player for whom the loading status is to be checked
     * @return {@code true} if the player's data is currently being loaded, {@code false} otherwise
     * @since 1.2.0
     */
    @Contract(pure = true)
    boolean isLoadingData(Player player);
}
