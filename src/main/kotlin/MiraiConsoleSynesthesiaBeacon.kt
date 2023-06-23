/*
 * Copyright (c) 2023 Samarium
 *
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
 * along with this program.  If not, see <https://www.gnu.org/licenses/agpl-3.0.txt>
 */
package io.github.samarium150.mirai.plugin.synesthesia_beacon

import io.github.samarium150.mirai.plugin.synesthesia_beacon.command.SynesthesiaBeacon
import io.github.samarium150.mirai.plugin.synesthesia_beacon.config.CommandConfig
import io.github.samarium150.mirai.plugin.synesthesia_beacon.config.RenderConfig
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.unregister
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin

object MiraiConsoleSynesthesiaBeacon : KotlinPlugin(
    JvmPluginDescription(
        id = "io.github.samarium150.mirai.plugin.synesthesia-beacon",
        name = "Synesthesia Beacon",
        version = "1.0.0",
    ) {
        author("Samarium")
    }
) {
    override fun onEnable() {
        CommandConfig.reload()
        RenderConfig.reload()
        SynesthesiaBeacon.register()
    }

    override fun onDisable() {
        SynesthesiaBeacon.unregister()
    }
}
