package com.ivangarzab.carrus.util.managers

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.ivangarzab.carrus.data.exceptions.MemoryLeakException
import leakcanary.EventListener
import leakcanary.LeakCanary
import shark.Leak
import shark.LeakTrace
import shark.LeakTraceReference
import shark.LibraryLeak
import timber.log.Timber

/**
 * The purpose of this class is to automagically upload leak reports from
 * [com.squareup.leakcanary] and into the [com.google.firebase.crashlytics.FirebaseCrashlytics]
 * console.
 *
 * Partially copied from https://square.github.io/leakcanary/uploading/.
 *
 * Created by Ivan Garza Bermea.
 */
class LeakUploader {
    fun setupCrashlyticsLeakUploader() {
        LeakCanary.config = LeakCanary.config.run {
            copy(eventListeners = eventListeners + CrashlyticsLeakUploader())
        }
    }

    inner class CrashlyticsLeakUploader : EventListener {

        private val crashlytics = Firebase.crashlytics

        override fun onEvent(event: EventListener.Event) {
            if (event is EventListener.Event.HeapAnalysisDone.HeapAnalysisSucceeded) {
                val heapAnalysis = event.heapAnalysis
                val allLeakTraces = heapAnalysis
                    .allLeaks
                    .toList()
                    .flatMap { leak ->
                        leak.leakTraces.map { leakTrace -> leak to leakTrace }
                    }
                if (allLeakTraces.isEmpty().not()) {
                    allLeakTraces.forEach { (leak, leakTrace) ->
                        prepareLeakReport(leak, leakTrace)
                    }
                    sendAllLeakReports()
                }
            }
        }

        private fun prepareLeakReport(leak: Leak, leakTrace: LeakTrace) = with(crashlytics) {
            Timber.d("Preparing leak report(s) for Crashlytics")
            val exception = leakTrace.asFakeException(
                "${if (leak is LibraryLeak) "Library" else "Memory"} leaked on ${leak.shortDescription}."
            )
//        setCustomKey("Leak Trace", leakTrace.toString())
            log("Leak Trace:\n\n$leakTrace")
            recordException(exception)
        }

        private fun sendAllLeakReports() = with(crashlytics) {
            Timber.d("Manually sending all unsent reports to Crashlytics")
            sendUnsentReports()
        }

        private fun LeakTrace.asFakeException(message: String): Exception {
            val exception = MemoryLeakException(message)
            val stackTrace = mutableListOf<StackTraceElement>()
            stackTrace.add(StackTraceElement("GcRoot", gcRootType.name, "GcRoot.kt", 42))
            for (cause in referencePath) {
                stackTrace.add(cause.buildStackTraceElement())
            }
            exception.stackTrace = stackTrace.toTypedArray()
            return exception
        }

        private fun LeakTraceReference.buildStackTraceElement():
                StackTraceElement {
            val file = this.owningClassName.substringAfterLast(".") + ".kt"
            return StackTraceElement(
                this.owningClassName,
                this.referenceDisplayName,
                file,
                42
            )
        }
    }
}