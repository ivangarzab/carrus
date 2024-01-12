package com.ivangarzab.carrus.util.managers

import com.ivangarzab.analytics.AnalyticsRepository
import com.ivangarzab.carrus.util.extensions.getShortenedDate
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Ivan Garza Bermea.
 */
@Singleton
class Analytics @Inject constructor(
    private val analyticsRepository: AnalyticsRepository
) {

    private fun logEvent(name: String, vararg params: Pair<String, Any>) =
        analyticsRepository.logEvent(name, *params)

    private fun logScreenView(screenName: String, className: String) =
        analyticsRepository.logScreenView(screenName, className)

    private fun logPermissionGranted(permission: String) {
        logEvent(
            "permission_granted",
            Pair("permission_type", permission)
        )
    }

    private fun logPermissionDenied(permission: String) {
        logEvent(
            "permission_denied",
            Pair("permission_type", permission)
        )
    }

    fun logNotificationPermissionResult(granted: Boolean) = when (granted) {
        true -> logPermissionGranted("notifications")
        false -> logPermissionDenied("notifications")
    }

    fun logAlarmsPermissionResult(granted: Boolean) = when (granted) {
        true -> logPermissionGranted("alarms")
        false -> logPermissionDenied("alarms")
    }

    // Instrumentation events
    fun logCarCreated(id: String, name: String) {
        logEvent(
            "car_created",
            Pair("car_id", id),
            Pair("car_name", name)
        )
    }

    fun logCarUpdated(id: String, name: String) {
        logEvent(
            "car_updated",
            Pair("car_id", id),
            Pair("car_name", name)
        )
    }

    fun logCarDeleted(id: String, name: String) {
        logEvent(
            "car_deleted",
            Pair("car_id", id),
            Pair("car_name", name)
        )
    }

    fun logCarExported(id: String, name: String) {
        logEvent(
            "car_exported",
            Pair("car_id", id),
            Pair("car_name", name)
        )
    }

    fun logCarImported(id: String, name: String) {
        logEvent(
            "car_imported",
            Pair("car_id", id),
            Pair("car_name", name)
        )
    }

    fun logServiceCreated(id: String, name: String) {
        logEvent(
            "service_created",
            Pair("service_id", id),
            Pair("service_name", name)
        )
    }

    fun logServiceUpdated(id: String, name: String) {
        logEvent(
            "service_updated",
            Pair("service_id", id),
            Pair("service_name", name)
        )
    }

    fun logServiceCompleted(id: String, name: String) {
        logEvent(
            "service_completed",
            Pair("service_id", id),
            Pair("service_name", name),
            Pair("date", Calendar.getInstance().getShortenedDate())
        )
    }

    fun logServiceRescheduled(id: String, name: String) {
        logServiceCompleted(id, name)
        logServiceCreated(id, name)
    }

    fun logServiceDeleted(id: String, name: String) {
        logEvent(
            "service_deleted",
            Pair("service_id", id),
            Pair("service_name", name)
        )
    }

    fun logServiceListSorted(type: String) {
        logEvent(
            "service_list_sorted",
            Pair("sort_type", type)
        )
    }

    fun logServiceListDeleted(carId: String) {
        logEvent(
            "service_list_deleted",
            Pair("car_id", carId)
        )
    }

    fun logAlarmScheduled(name: String, forced: Boolean) {
        logEvent(
            "alarm_scheduled",
            Pair("alarm_type", name),
            Pair("forced", forced)
        )
    }

    fun logAlarmCancelled(name: String) {
        logEvent(
            "alarm_cancelled",
            Pair("alarm_type", name)
        )
    }

    fun logAlarmFeatureToggled(enabled: Boolean) {
        logEvent(
            "alarm_feature_toggled",
            Pair("enabled", enabled)
        )
    }

    fun logAlarmTimeChanged(alarmTime: Int) {
        logEvent(
            "alarm_time_changed",
            Pair("alarm_time", alarmTime)
        )
    }

    fun logAlarmFrequencyChanged(frequency: String) {
        logEvent(
            "alarm_frequency_changed",
            Pair("alarm_frequency", frequency)
        )
    }

    fun logNightThemeChanged(isNight: Boolean) {
        logEvent(
            "night_theme_changed",
            Pair("night", isNight)
        )
    }

    fun logDueDateFormatChanged(format: String) {
        logEvent(
            "due_date_format_changed",
            Pair("format", format)
        )
    }

    fun logTimeFormatChanged(format: String) {
        logEvent(
            "time_format_changed",
            Pair("format", format)
        )
    }

    fun logLeftHandedChanged(lefty: Boolean) {
        logEvent(
            "left_handed_changed",
            Pair("lefty", lefty)
        )
    }

    fun logAddNewCarClicked() {
        logEvent("add_new_car_clicked")
    }

    fun logEditCarClicked() {
        logEvent("edit_car_clicked")
    }

    fun logEditServiceClicked() {
        logEvent("edit_service_clicked")
    }

    fun logAddNewServiceClicked() {
        logEvent("add_service_clicked")
    }

    fun logSettingsClicked() {
        logEvent("settings_clicked")
    }

    fun logMapClicked() {
        logEvent("map_clicked")
    }

    fun logAppMessageClicked(id: String) {
        logEvent(
            "message_clicked",
            "message_id" to id
        )
    }

    fun logAddImageClicked() {
        logEvent("image_add_clicked")
    }

    fun logImportButtonClicked() {
        logEvent("import_clicked")
    }

    fun logExportButtonClicked() {
        logEvent("export_clicked")
    }

    fun logPrivacyPolicyClicked() {
        logEvent("privacy_policy_clicked")
    }

    fun logImageAdded() {
        logEvent("image_added")
    }

    fun logImageDeleted() {
        logEvent("image_deleted")
    }

    fun logAppMessageAdded(type: String) {
        logEvent(
            "message_added",
            Pair("message_type", type)
        )
    }

    fun logAppMessageRemoved(type: String) {
        logEvent(
            "message_removed",
            Pair("message_type", type)
        )
    }

    fun logSortClicked(type: String) {
        logEvent(
            "sorting_by_type_clicked",
            Pair("sort_type", type)
        )
    }

    fun logDarkModeToggleClicked() {
        logEvent("dark_mode_clicked")
    }

    fun logDeleteCarDataClicked() {
        logEvent("car_delete_clicked")
    }

    fun logDeleteServiceListClicked() {
        logEvent("service_list_delete_clicked")
    }

    fun logAlarmsToggleClicked() {
        logEvent("alarms_toggle_clicked")
    }

    fun logAlarmTimeClicked() {
        logEvent("alarm_time_clicked")
    }

    fun logAlarmFrequencyClicked() {
        logEvent("alarm_frequency_clicked")
    }

    fun logDueDateFormatClicked() {
        logEvent("due_date_format_clicked")
    }

    fun logTimeFormatClicked() {
        logEvent("time_format_clicked")
    }

    fun logServiceSubmitClicked() {
        logEvent("service_submit_clicked")
    }

    fun logCreateScreenView(className: String) {
        logScreenView(
            "create_screen",
            className
        )
    }

    fun logOverviewScreenView(className: String) {
        logScreenView(
            "overview_screen",
            className
        )
    }

    fun logSettingsScreenView(className: String) {
        logScreenView(
            "settings_screen",
            className
        )
    }

    fun logMapScreenView(className: String) {
        logScreenView(
            "map_screen",
            className
        )
    }

    fun logPrivacyPolicyScreenView(className: String) {
        logScreenView(
            "privacy_policy_screen",
            className
        )
    }

    fun logServiceModalScreenView(className: String) {
        logScreenView(
            "service_modal_screen",
            className
        )
    }

    fun logAlarmInterstitialScreenView(className: String) {
        logScreenView(
            "alarm_interstitial_screen",
            className
        )
    }

    fun logNotificationInterstitialScreenView(className: String) {
        logScreenView(
            "notification_interstitial_screen",
            className
        )
    }
}