package com.hppk.sw.hppkcommuterbus.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.hppk.sw.hppkcommuterbus.R

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceClickListener {

        private val prefAlarmTime: Preference by lazy {
            findPreference<Preference>(getString(R.string.key_alarm_go_office_time)) as Preference
        }
        private val prefAlarmDistance: Preference by lazy {
            findPreference<Preference>(getString(R.string.key_alarm_go_home_distance)) as Preference
        }
        private val prefFeedback: Preference by lazy {
            findPreference<Preference>(getString(R.string.key_feedback)) as Preference
        }
        private val prefVersion: Preference by lazy {
            findPreference<Preference>(getString(R.string.key_version)) as Preference
        }

        private val appVersion: String by lazy {
            context?.applicationContext?.packageManager?.getPackageInfo(
                context?.applicationContext?.packageName,
                0
            )?.versionName ?: "none"
        }

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            prefAlarmTime.onPreferenceClickListener = this
            prefAlarmDistance.onPreferenceClickListener = this
            prefFeedback.onPreferenceClickListener = this

            prefVersion.summary = appVersion
        }

        override fun onPreferenceClick(preference: Preference?) = when (preference?.key) {
            getString(R.string.key_alarm_go_office_time) -> true
            getString(R.string.key_alarm_go_home_distance) -> true
            getString(R.string.key_feedback) -> sendFeedback()
            else -> false
        }

        private fun sendFeedback(): Boolean {
            val emailIntent = ShareCompat.IntentBuilder.from(activity)
                .setType("plain/text")
                .setEmailTo(listOf(getString(R.string.email)).toTypedArray())
                .setSubject(
                    getString(
                        R.string.send_feedback_email_subject,
                        getString(R.string.app_name),
                        appVersion
                    )
                )
                .setText(getString(R.string.send_feedback_email_details))
                .intent

            startActivity(emailIntent)
            return true
        }
    }
}