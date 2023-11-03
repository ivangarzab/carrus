package com.ivangarzab.carrus.util.managers

import com.ivangarzab.analytics.AnalyticsRepository
import io.mockk.spyk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

/**
 * Created by Ivan Garza Bermea.
 */
class AnalyticsTest {

    private lateinit var analytics: Analytics

    private lateinit var analyticsRepository: AnalyticsRepository

    @Before
    fun setup() {
        analyticsRepository = spyk()
        analytics = Analytics(analyticsRepository)
    }

    @Test
    fun test_logNotificationPermissionResult_true() {
        analytics.logNotificationPermissionResult(true)
        verify(atLeast = 1) {
            analyticsRepository.logEvent(
                "permission_granted",
                Pair("permission_type", "notifications")
            )
        }
    }

    @Test
    fun test_logNotificationPermissionResult_false() {
        analytics.logNotificationPermissionResult(false)
        verify(atLeast = 1) {
            analyticsRepository.logEvent(
                "permission_denied",
                Pair("permission_type", "notifications")
            )
        }
    }

    @Test
    fun test_logAlarmsPermissionResult_true() {
        analytics.logAlarmsPermissionResult(true)
        verify(atLeast = 1) {
            analyticsRepository.logEvent(
                "permission_granted",
                Pair("permission_type", "alarms")
            )
        }
    }

    @Test
    fun test_logAlarmsPermissionResult_false() {
        analytics.logAlarmsPermissionResult(false)
        verify(atLeast = 1) {
            analyticsRepository.logEvent(
                "permission_denied",
                Pair("permission_type", "alarms")
            )
        }
    }

    @Test
    fun test_logCarCreated() {
        analytics.logCarCreated("id", "name")
        verify(atLeast = 1) {
            analyticsRepository.logEvent(
                "car_created",
                Pair("car_id", "id"),
                Pair("car_name", "name")
            )
        }
    }

    @Test
    fun test_logCarUpdated() {
        analytics.logCarUpdated("id", "name")
        verify(atLeast = 1) {
            analyticsRepository.logEvent(
                "car_updated",
                Pair("car_id", "id"),
                Pair("car_name", "name")
            )
        }
    }

    @Test
    fun test_logCarDeleted() {
        analytics.logCarDeleted("id", "name")
        verify(atLeast = 1) {
            analyticsRepository.logEvent(
                "car_deleted",
                Pair("car_id", "id"),
                Pair("car_name", "name")
            )
        }
    }

    @Test
    fun test_logCarExported() {
        analytics.logCarExported("id", "name")
        verify(atLeast = 1) {
            analyticsRepository.logEvent(
                "car_exported",
                Pair("car_id", "id"),
                Pair("car_name", "name")
            )
        }
    }

    @Test
    fun test_logCarImported() {
        analytics.logCarImported("id", "name")
        verify(atLeast = 1) {
            analyticsRepository.logEvent(
                "car_imported",
                Pair("car_id", "id"),
                Pair("car_name", "name")
            )
        }
    }

    @Test
    fun test_logServiceCreated() {
        analytics.logServiceCreated("id", "name")
        verify(atLeast = 1) {
            analyticsRepository.logEvent(
                "service_created",
                Pair("service_id", "id"),
                Pair("service_name", "name")
            )
        }
    }

    @Test
    fun test_logServiceUpdated() {
        analytics.logServiceUpdated("id", "name")
        verify(atLeast = 1) {
            analyticsRepository.logEvent(
                "service_updated",
                Pair("service_id", "id"),
                Pair("service_name", "name")
            )
        }
    }

    @Test
    fun test_logServiceDeleted() {
        analytics.logServiceDeleted("id", "name")
        verify(atLeast = 1) {
            analyticsRepository.logEvent(
                "service_deleted",
                Pair("service_id", "id"),
                Pair("service_name", "name")
            )
        }
    }

    @Test
    fun test_logServiceListSorted() {
        analytics.logServiceListSorted("type")
        verify(atLeast = 1) {
            analyticsRepository.logEvent(
                "service_list_sorted",
                Pair("sort_type", "type")
            )
        }
    }

    @Test
    fun test_logServiceListDeleted() {
        analytics.logServiceListDeleted("id")
        verify(atLeast = 1) {
            analyticsRepository.logEvent(
                "service_list_deleted",
                Pair("car_id", "id")
            )
        }
    }

    @Test
    fun test_logAlarmScheduled_true() {
        analytics.logAlarmScheduled("name", true)
        verify(atLeast = 1) {
            analyticsRepository.logEvent(
                "alarm_scheduled",
                Pair("alarm_type", "name"),
                Pair("forced", true)
            )
        }
    }

    @Test
    fun test_logAlarmScheduled_false() {
        analytics.logAlarmScheduled("name", false)
        verify(atLeast = 1) {
            analyticsRepository.logEvent(
                "alarm_scheduled",
                Pair("alarm_type", "name"),
                Pair("forced", false)
            )
        }
    }

    @Test
    fun test_logAlarmCancelled() {
        analytics.logAlarmCancelled("name")
        verify(atLeast = 1) {
            analyticsRepository.logEvent(
                "alarm_cancelled",
                Pair("alarm_type", "name")
            )
        }
    }

    @Test
    fun test_logAlarmFeatureToggle_true() {
        analytics.logAlarmFeatureToggled(true)
        verify(atLeast = 1) {
            analyticsRepository.logEvent(
                "alarm_feature_toggled",
                Pair("enabled", true)
            )
        }
    }

    @Test
    fun test_logAlarmFeatureToggle_false() {
        analytics.logAlarmFeatureToggled(false)
        verify(atLeast = 1) {
            analyticsRepository.logEvent(
                "alarm_feature_toggled",
                Pair("enabled", false)
            )
        }
    }

    @Test
    fun test_logAlarmTimeChanged() {
        analytics.logAlarmTimeChanged(6)
        verify(atLeast = 1) {
            analyticsRepository.logEvent(
                "alarm_time_changed",
                Pair("alarm_time", 6)
            )
        }
    }

    @Test
    fun test_logAlarmFrequencyChanged() {
        analytics.logAlarmFrequencyChanged("daily")
        verify(atLeast = 1) {
            analyticsRepository.logEvent(
                "alarm_frequency_changed",
                Pair("alarm_frequency", "daily")
            )
        }
    }

    @Test
    fun test_logNightThemeChanged_true() {
        analytics.logNightThemeChanged(true)
        verify(atLeast = 1) {
            analyticsRepository.logEvent(
                "night_theme_changed",
                Pair("night", true)
            )
        }
    }

    @Test
    fun test_logNightThemeChanged_false() {
        analytics.logNightThemeChanged(false)
        verify(atLeast = 1) {
            analyticsRepository.logEvent(
                "night_theme_changed",
                Pair("night", false)
            )
        }
    }

    @Test
    fun test_logDueDateFormatChanged() {
        analytics.logDueDateFormatChanged("format")
        verify(atLeast = 1) {
            analyticsRepository.logEvent(
                "due_date_format_changed",
                Pair("format", "format")
            )
        }
    }

    @Test
    fun test_logTimeFormatChanged() {
        analytics.logTimeFormatChanged("format")
        verify(atLeast = 1) {
            analyticsRepository.logEvent(
                "time_format_changed",
                Pair("format", "format")
            )
        }
    }

    @Test
    fun test_logLeftHandedChanged_true() {
        analytics.logLeftHandedChanged(true)
        verify(atLeast = 1) {
            analyticsRepository.logEvent(
                "left_handed_changed",
                Pair("lefty", true)
            )
        }
    }

    @Test
    fun test_logLeftHandedChanged_false() {
        analytics.logLeftHandedChanged(false)
        verify(atLeast = 1) {
            analyticsRepository.logEvent(
                "left_handed_changed",
                Pair("lefty", false)
            )
        }
    }

    @Test
    fun test_logAddNewCarClicked() {
        analytics.logAddNewCarClicked()
        verify(atLeast = 1) {
            analyticsRepository.logEvent("add_new_car_clicked")
        }
    }

    @Test
    fun test_logEditCarClicked() {
        analytics.logEditCarClicked()
        verify(atLeast = 1) {
            analyticsRepository.logEvent("edit_car_clicked")
        }
    }

    @Test
    fun test_logEditServiceClicked() {
        analytics.logEditServiceClicked()
        verify(atLeast = 1) {
            analyticsRepository.logEvent("edit_service_clicked")
        }
    }

    @Test
    fun test_logAddNewServiceClicked() {
        analytics.logAddNewServiceClicked()
        verify(atLeast = 1) {
            analyticsRepository.logEvent("add_service_clicked")
        }
    }

    @Test
    fun test_logSettingsClicked() {
        analytics.logSettingsClicked()
        verify(atLeast = 1) {
            analyticsRepository.logEvent("settings_clicked")
        }
    }

    @Test
    fun test_logAppMessageClicked() {
        analytics.logAppMessageClicked()
        verify(atLeast = 1) {
            analyticsRepository.logEvent("message_clicked")
        }
    }

    @Test
    fun test_logAddImageClicked() {
        analytics.logAddImageClicked()
        verify(atLeast = 1) {
            analyticsRepository.logEvent("image_add_clicked")
        }
    }

    @Test
    fun test_logImportButtonClicked() {
        analytics.logImportButtonClicked()
        verify(atLeast = 1) {
            analyticsRepository.logEvent("import_clicked")
        }
    }

    @Test
    fun test_logExportButtonClicked() {
        analytics.logExportButtonClicked()
        verify(atLeast = 1) {
            analyticsRepository.logEvent("export_clicked")
        }
    }

    @Test
    fun test_logPrivacyPolicyClicked() {
        analytics.logPrivacyPolicyClicked()
        verify(atLeast = 1) {
            analyticsRepository.logEvent("privacy_policy_clicked")
        }
    }

    @Test
    fun test_logImageAdded() {
        analytics.logImageAdded()
        verify(atLeast = 1) {
            analyticsRepository.logEvent("image_added")
        }
    }

    @Test
    fun test_logImageDeleted() {
        analytics.logImageDeleted()
        verify(atLeast = 1) {
            analyticsRepository.logEvent("image_deleted")
        }
    }

    @Test
    fun test_logAppMessageAdded() {
        analytics.logAppMessageAdded("message")
        verify(atLeast = 1) {
            analyticsRepository.logEvent(
                "message_added",
                Pair("message_type", "message")
            )
        }
    }

    @Test
    fun test_logAppMessageRemoved() {
        analytics.logAppMessageRemoved("message")
        verify(atLeast = 1) {
            analyticsRepository.logEvent(
                "message_removed",
                Pair("message_type", "message")
            )
        }
    }

    @Test
    fun test_logSortClicked() {
        analytics.logSortClicked("sort")
        verify(atLeast = 1) {
            analyticsRepository.logEvent(
                "sorting_by_type_clicked",
                Pair("sort_type", "sort")
            )
        }
    }

    @Test
    fun test_logDarkModeToggleClicked() {
        analytics.logDarkModeToggleClicked()
        verify(atLeast = 1) {
            analyticsRepository.logEvent("dark_mode_clicked")
        }
    }

    @Test
    fun test_logDeleteCarDataClicked() {
        analytics.logDeleteCarDataClicked()
        verify(atLeast = 1) {
            analyticsRepository.logEvent("car_delete_clicked")
        }
    }

    @Test
    fun test_logDeleteServiceListClicked() {
        analytics.logDeleteServiceListClicked()
        verify(atLeast = 1) {
            analyticsRepository.logEvent("service_list_delete_clicked")
        }
    }

    @Test
    fun test_logAlarmsToggleClicked() {
        analytics.logAlarmsToggleClicked()
        verify(atLeast = 1) {
            analyticsRepository.logEvent("alarms_toggle_clicked")
        }
    }

    @Test
    fun test_logAlarmTimeClicked() {
        analytics.logAlarmTimeClicked()
        verify(atLeast = 1) {
            analyticsRepository.logEvent("alarm_time_clicked")
        }
    }

    @Test
    fun test_logAlarmFrequencyClicked() {
        analytics.logAlarmFrequencyClicked()
        verify(atLeast = 1) {
            analyticsRepository.logEvent("alarm_frequency_clicked")
        }
    }

    @Test
    fun test_logDueDateFormatClicked() {
        analytics.logDueDateFormatClicked()
        verify(atLeast = 1) {
            analyticsRepository.logEvent("due_date_format_clicked")
        }
    }

    @Test
    fun test_logTimeFormatClicked() {
        analytics.logTimeFormatClicked()
        verify(atLeast = 1) {
            analyticsRepository.logEvent("time_format_clicked")
        }
    }

    @Test
    fun test_logServiceSubmitClicked() {
        analytics.logServiceSubmitClicked()
        verify(atLeast = 1) {
            analyticsRepository.logEvent("service_submit_clicked")
        }
    }
}