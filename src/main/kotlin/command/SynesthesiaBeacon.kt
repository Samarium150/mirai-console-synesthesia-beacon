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
@file:Suppress("unused")
package io.github.samarium150.mirai.plugin.synesthesia_beacon.command

import io.github.samarium150.mirai.plugin.synesthesia_beacon.MiraiConsoleSynesthesiaBeacon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runInterruptible
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.ConsoleCommandSender
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.contact.Contact.Companion.uploadImage
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.MessageSource.Key.quote
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.imageio.ImageIO
import kotlin.io.path.writeBytes


object SynesthesiaBeacon : CompositeCommand(
    MiraiConsoleSynesthesiaBeacon, "beacon"
) {

    private fun loadFont(resource: String): Font = Font.createFont(
        Font.TRUETYPE_FONT,
        javaClass.classLoader.getResourceAsStream(resource)
    )

    private val genshinCommonFont = loadFont("fonts/genshin/TeyvatNeue-Regular-1.002.otf")
    private val inazumaFont = loadFont("fonts/genshin/InazumaNeue-Regular-1.000.otf")
    private val sumeruFont = loadFont("fonts/genshin/SumeruNeue-Regular-0.007.otf")
    private val deshretFont = loadFont("fonts/genshin/DeshretNeue-Regular-1.002.otf")
    private val khaenriahFont = loadFont("fonts/genshin/KhaenriahNeue-Regular-2.000.otf")
    private val starRailCommonFont = loadFont("fonts/star_rail/StarRailNeue-Serif-Regular.ttf")
    private val luofuFont = loadFont("fonts/star_rail/LuofuNeue-Regular-0.100.otf")
    private val zenlessA = loadFont("fonts/zenless/ZZZNeue-VariantA-0.003.otf")
    private val zenlessB = loadFont("fonts/zenless/ZZZNeue-VariantB-0.003.otf")

    @ExperimentalCommandDescriptors
    @ConsoleExperimentalApi
    override val prefixOptional = true

    /**
     * Generate image from the given message and font
     *
     * @param font
     * @param message
     * @see <a href="https://stackoverflow.com/a/18800845">Stack Overflow</a>
     * @return the image as a byte array
     */
    private fun generateImage(font: Font, message: String): ByteArray {
        var graphics2d: Graphics2D = BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics()
        graphics2d.font = font.deriveFont(12f)
        var fontMetrics = graphics2d.fontMetrics
        val width = fontMetrics.stringWidth(message)
        val height = fontMetrics.height
        graphics2d.dispose()
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        graphics2d = image.createGraphics()
        graphics2d.setRenderingHint(
            RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY
        )
        graphics2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY)
        graphics2d.font = font.deriveFont(12f)
        fontMetrics = graphics2d.fontMetrics
        graphics2d.color = Color.GREEN
        graphics2d.drawString(message, 0, fontMetrics.ascent)
        graphics2d.dispose()
        val outputStream = ByteArrayOutputStream()
        ImageIO.write(image, "png", outputStream)
        val out = outputStream.toByteArray()
        outputStream.close()
        return out
    }

    private suspend fun CommandSender.saveOrSendImage(image: ByteArray) {
        if (this is ConsoleCommandSender) {
            MiraiConsoleSynesthesiaBeacon.dataFolderPath.resolve(
                "${
                    SimpleDateFormat("yyyyMMddHHmmss").format(
                        Date()
                    )
                }.png"
            ).writeBytes(image)
        } else if (this is CommandSenderOnMessage<*>) {
            if (fromEvent !is GroupMessageEvent && fromEvent !is FriendMessageEvent)
                return
            val stream = image.inputStream()
            sendMessage(fromEvent.source.quote() + fromEvent.subject.uploadImage(stream))
            runInterruptible(Dispatchers.IO) {
                stream.close()
            }
        }
    }

    @SubCommand("genshin")
    suspend fun CommandSender.genshin(location: String, message: String) {
        val font = when (location) {
            "common", "mondstadt", "teyvat" -> {
                genshinCommonFont
            }

            "inazuma" -> {
                inazumaFont
            }

            "sumeru" -> {
                sumeruFont
            }

            "deshret" -> {
                deshretFont
            }

            "khaenriah" -> {
                khaenriahFont
            }

            else -> {
                return
            }
        }
        val image = runInterruptible(Dispatchers.IO) {
            generateImage(font, message)
        }
        saveOrSendImage(image)
    }

    @SubCommand("starrail")
    suspend fun CommandSender.starrail(location: String, message: String) {
        val font = when (location) {
            "common", "herta", "jarilo", "belobog" -> {
                starRailCommonFont
            }

            "luofu" -> {
                luofuFont
            }

            else -> {
                return
            }
        }
        val image = runInterruptible(Dispatchers.IO) {
            generateImage(font, message)
        }
        saveOrSendImage(image)
    }

    @SubCommand("zenless")
    suspend fun CommandSender.zenless(variant: String, message: String) {
        val font = when (variant) {
            "A", "a" -> {
                zenlessA
            }

            "B", "b" -> {
                zenlessB
            }

            else -> {
                return
            }
        }
        val image = runInterruptible(Dispatchers.IO) {
            generateImage(font, message)
        }
        saveOrSendImage(image)
    }
}
