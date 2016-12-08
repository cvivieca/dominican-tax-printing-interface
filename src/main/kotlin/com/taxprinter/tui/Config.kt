package com.taxprinter.tui

import com.esotericsoftware.yamlbeans.YamlReader
import com.esotericsoftware.yamlbeans.YamlWriter
import com.fazecast.jSerialComm.SerialPort
import com.google.inject.Inject
import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.gui2.*
import com.googlecode.lanterna.input.KeyType
import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.Terminal
import com.taxprinter.configs.TaxprinterConfig
import java.io.FileReader
import java.io.FileWriter
import java.util.concurrent.Executors
import java.util.regex.Pattern
import kotlin.system.exitProcess

/**
 * Created by george on 07/12/16.
 */
class Config (terminal: Terminal) {
    val screen = TerminalScreen(terminal)
    val panel = Panel()
    val window = BasicWindow()

    init {

        terminal.pollInput()
        screen.startScreen()
        panel.layoutManager = GridLayout(2)
        panel.preferredSize = TerminalSize(30, 15)

        panel.addComponent(Label("Driver:"))
        val printerModel = ComboBox<String>()

        printerModel.addItem("bixolonsrp350")
        printerModel.addItem("epsontest")
        printerModel.preferredSize = TerminalSize(15, 1)
        panel.addComponent(printerModel.setReadOnly(true))

        panel.addComponent(Label("Serial Port"))
        val serialPortBox = ComboBox<String>()
        SerialPort.getCommPorts().forEach {
            serialPortBox.addItem(it.systemPortName)
        }
        serialPortBox.preferredSize = TerminalSize(15, 1)
        panel.addComponent(serialPortBox.setReadOnly(false))

        val statLbl = Label("Loaded.")
        statLbl.addTo(panel)
        panel.addComponent(EmptySpace(TerminalSize(0, 6)))
        Button("Write config!", Runnable {
            val taxConfig = TaxprinterConfig()
            taxConfig.driver = printerModel.selectedItem
            taxConfig.portDescriptor = serialPortBox.text
            val writer = YamlWriter(FileWriter("taxprinter.yml"))
            writer.write(taxConfig)
            writer.close()
            statLbl.text = "Wrote!"
        }).addTo(panel)
        Button("Close", Runnable {
           System.exit(0)
        }).addTo(panel)

        window.component = panel
        window.title = "Tax Printer Connector Config"
        window.setHints(arrayOf(Window.Hint.CENTERED).toSet())
        val gui = MultiWindowTextGUI(screen, DefaultWindowManager(), EmptySpace(TextColor.ANSI.BLUE))
        gui.addWindowAndWait(window)
    }

}