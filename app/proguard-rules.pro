# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Output a full report of all the rules that R8 applies when
# building the project.
-printconfiguration /tmp/full-r8-config.txt

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable
-keepattributes *Annotation*, Signature, Exceptions, InnerClasses
# If you keep the line number information, uncomment this to
# hide the original source file name.
-renamesourcefileattribute SourceFile
# Keep all Exceptions.
-keep public class * extends java.lang.Exception
# Keep the bind() function for View Binding.
-keep class * implements androidx.viewbinding.ViewBinding {
    public static *** bind(android.view.View);
}
# Custom exceptions start here:
-keep class com.ivangarzab.carrus.data.models.Car { *; }
-keep class com.ivangarzab.carrus.data.models.Service { *; }
# Needed for Places API
-dontwarn io.grpc.internal.DnsNameResolverProvider
-dontwarn io.grpc.internal.PickFirstLoadBalancerProvider