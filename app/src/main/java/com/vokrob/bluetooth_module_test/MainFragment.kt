package com.vokrob.bluetooth_module_test

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.vokrob.bluetooth_module_test.databinding.FragmentMainBinding
import com.vokrob.bt_module.BluetoothConstants
import com.vokrob.bt_module.bluetooth.BluetoothController

class MainFragment : Fragment(), BluetoothController.Listener {
    private lateinit var bluetoothController: BluetoothController
    private lateinit var btAdapter: BluetoothAdapter
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBtAdapter()

        val pref =
            activity?.getSharedPreferences(BluetoothConstants.PREFERENCES, Context.MODE_PRIVATE)
        val mac = pref?.getString(BluetoothConstants.MAC, "")

        bluetoothController = BluetoothController(btAdapter)

        binding.apply {
            btList.setOnClickListener {
                findNavController().navigate(R.id.listFragment)
            }
            connect.setOnClickListener {
                bluetoothController.connect(mac ?: "", this@MainFragment)
            }
            takeOn.setOnClickListener {
                bluetoothController.sendMessage("A")
            }
            takeOff.setOnClickListener {
                bluetoothController.sendMessage("B")
            }
        }
    }

    private fun initBtAdapter() {
        val bManager = activity?.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        btAdapter = bManager.adapter
    }

    override fun onReceive(message: String) {
        activity?.runOnUiThread {
            binding.tvMessage.text = message
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        bluetoothController.closeConnection()
    }
}





















