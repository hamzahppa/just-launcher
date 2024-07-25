# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Optimize code
-optimizationpasses 5
-dontpreverify
-dontobfuscate

# Keep essential Android classes
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.Application
-keep class * extends androidx.lifecycle.ViewModel

# Keep attributes that are used by reflection
-keepattributes *Annotation*
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,EnclosingMethod

# Preserve some generic signatures
-keepattributes Signature

# Don't warn about missing classes
-dontwarn sun.misc.**
-dontwarn java.nio.file.**

# Remove unused code and resources
-keep class androidx.compose.material.icons.** { *; }

# Shrink Material Icons
-keep class androidx.compose.material.icons.** { *; }
-dontwarn androidx.compose.material.icons.**

