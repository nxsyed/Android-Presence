package me.nxsyed.presence

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import java.util.*
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.presence.PNHereNowResult
import com.pubnub.api.callbacks.PNCallback
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val occupancy = findViewById<TextView>(R.id.textOnline)

        val subscribeCallback: SubscribeCallback = object : SubscribeCallback()  {
            override fun status(pubnub: PubNub, status: PNStatus) {

            }

            override fun message(pubnub: PubNub?, message: PNMessageResult?) {
                Log.d("message", message!!.message.toString())
            }
            override fun presence(pubnub: PubNub, presence: PNPresenceEventResult) {
                Log.d("presence", presence.toString())
                runOnUiThread {
                    occupancy.text = presence.occupancy.toString()
                }
            }
        }

        val pnConfiguration = PNConfiguration()
        pnConfiguration.subscribeKey = "your-sub-key"
        pnConfiguration.publishKey = "your-pub-key"
        pnConfiguration.secretKey = "true"
        pnConfiguration.uuid = "Syed"
        val pubNub = PubNub(pnConfiguration)

        pubNub.run {
            addListener(subscribeCallback)
            subscribe()
                    .channels(Arrays.asList("Whiteboard")) // subscribe to channels
                    .withPresence() // also subscribe to related presence information
                    .execute()
        }

    }
}
