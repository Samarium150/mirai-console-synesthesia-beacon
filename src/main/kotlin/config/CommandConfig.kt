/*
 * Copyright (c) 2023 Samarium
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/apgl-3.0.txt>.
 */
package io.github.samarium150.mirai.plugin.synesthesia_beacon.config

import net.mamoe.mirai.console.data.ReadOnlyPluginConfig
import net.mamoe.mirai.console.data.value

object CommandConfig : ReadOnlyPluginConfig("CommandConfig") {
    val commandSecondaryNames: Array<String> by value(arrayOf("sbc", "synesthesia_beacon"))
}
