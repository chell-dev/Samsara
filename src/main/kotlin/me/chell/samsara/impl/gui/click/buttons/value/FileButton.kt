package me.chell.samsara.impl.gui.click.buttons.value

import me.chell.samsara.api.gui.Rectangle
import me.chell.samsara.api.value.Value
import me.chell.samsara.impl.gui.click.Button
import me.chell.samsara.impl.gui.click.Window
import me.chell.samsara.impl.gui.click.buttons.ValueButton
import net.minecraft.client.util.math.MatrixStack
import org.lwjgl.util.tinyfd.TinyFileDialogs
import java.io.File

class FileButton(val v: Value<File>, override var x: Double, override var y: Double): ValueButton<File>(v, x, y) {

    override fun render(matrices: MatrixStack, mouseX: Double, mouseY: Double, tickDelta: Float) {
        super.render(matrices, mouseX, mouseY, tickDelta)

        val w = fontRenderer.getWidth(value.displayName)
        val rect = Rectangle(x + w, y, Window.width - w, height).subtract(border)
        rect.x += Window.padding.left + Button.border.left
        rect.width -= Window.padding.left + Window.padding.right + Button.border.left + Button.border.right

        drawString(matrices, "File", secondaryText, rect)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if(Rectangle(x, y, Window.width, height).isInBounds(mouseX, mouseY) && button == 0) {
            showDialog()
            return true
        }
        return false
    }

    private fun showDialog() {
        val file = TinyFileDialogs.tinyfd_openFileDialog("Select File", value.value.absolutePath, null, null, false)

        if(file != null)
            value.value = File(file)
    }
}