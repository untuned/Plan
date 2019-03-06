/*
 *  This file is part of Player Analytics (Plan).
 *
 *  Plan is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License v3 as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Plan is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Plan. If not, see <https://www.gnu.org/licenses/>.
 */
package com.djrapitops.plan.extension.annotation;

import com.djrapitops.plan.extension.icon.Color;
import com.djrapitops.plan.extension.icon.Family;

/**
 * Method annotation to provide a boolean value about a Player.
 * <p>
 * Usage: {@code @BooleanProvider boolean method(UUID playerUUID)}
 * <p>
 * The provided boolean can be used as a condition for calls to other Provider
 * methods by defining the name of the condition and using {@link Conditional}
 * on the other method with a Provider annotation.
 * <p>
 * For example:
 * {@code @BooleanProvider(condition="example") boolean condition(UUID playerUUID);}
 * {@code @Conditional("example") @NumberProvider long someValue(UUID playerUUID);}
 *
 * @author Rsl1122
 */
public @interface BooleanProvider {

    /**
     * Text displayed before the value, limited to 50 characters.
     * <p>
     * Should inform the user what the value represents, for example
     * "Banned", "Has Island"
     *
     * @return String of max 50 characters, remainder will be clipped.
     */
    String text();

    /**
     * Text displayed when hovering over the value, limited to 150 characters.
     * <p>
     * Should be used to clarify what the value is if not self evident, for example
     * text: "Boat", description: "Whether or not the player is on a boat."
     *
     * @return String of max 150 characters, remainder will be clipped.
     */
    String description() default "";

    /**
     * Name of Font Awesome icon.
     * <p>
     * See https://fontawesome.com/icons?d=gallery&m=free for icons and their {@link Family}.
     *
     * @return Name of the icon, if name is not valid no icon is shown.
     */
    String iconName() default "question";

    /**
     * Family of Font Awesome icon.
     * <p>
     * See https://fontawesome.com/icons?d=gallery&m=free for icons and their {@link Family}.
     *
     * @return Family that matches an icon, if there is no icon for this family no icon is shown.
     */
    Family iconFamily() default Family.SOLID;

    /**
     * Color preference of the plugin.
     * <p>
     * This color will be set as the default color to use for plugin's elements.
     *
     * @return Preferred color. If none are specified defaults are used.
     */
    Color iconColor() default Color.NONE;

}
