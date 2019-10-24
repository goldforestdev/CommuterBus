package com.hppk.sw.hppkcommuterbus.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SeekBarPreference
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceClickListener {

        private val prefAlarmTime: SeekBarPreference by lazy {
            findPreference<SeekBarPreference>(getString(R.string.key_alarm_go_office_time)) as SeekBarPreference
        }
        private val prefAlarmDistance: SeekBarPreference by lazy {
            findPreference<SeekBarPreference>(getString(R.string.key_alarm_go_home_distance)) as SeekBarPreference
        }
        private val prefFeedback: Preference by lazy {
            findPreference<Preference>(getString(R.string.key_feedback)) as Preference
        }
        private val prefVersion: Preference by lazy {
            findPreference<Preference>(getString(R.string.key_version)) as Preference
        }

        private val prefDevelopers: Preference by lazy {
            findPreference<Preference>(getString(R.string.key_developers)) as Preference
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
            prefDevelopers.onPreferenceClickListener = this

            prefVersion.summary = appVersion
        }

        override fun onPreferenceClick(preference: Preference?) = when (preference?.key) {
            getString(R.string.key_alarm_go_office_time) -> true
            getString(R.string.key_alarm_go_home_distance) -> true
            getString(R.string.key_feedback) -> sendFeedback()
            getString(R.string.key_developers) -> showDevelopers()
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

        private fun showDevelopers(): Boolean {
            startActivity(Intent(activity, DevelopersActivity::class.java))
            return true
        }
    }
}