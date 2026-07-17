package com.pocketpiano.official

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.pocketpiano.official.databinding.ActivityMainBinding
import com.pocketpiano.official.ota.OtaPacketCodec

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val permissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
        binding.status.text = if (result.values.all { it }) "Permisos concedidos" else "Faltan permisos Bluetooth"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater); setContentView(binding.root)
        binding.connectButton.setOnClickListener { requestBluetoothPermissions() }
        binding.fileButton.setOnClickListener { binding.status.text = "Selector de firmware: siguiente fase" }
        binding.updateButton.setOnClickListener {
            val sample = OtaPacketCodec.startCommand(1024)
            binding.log.text = "Motor OTA inicializado. START=${sample.joinToString(" ") { "%02X".format(it) }}"
        }
    }

    private fun requestBluetoothPermissions() {
        val requested = if (Build.VERSION.SDK_INT >= 31) arrayOf(Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT)
        else arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        permissions.launch(requested)
    }
}
