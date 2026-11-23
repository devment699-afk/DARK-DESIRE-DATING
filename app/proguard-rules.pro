# Add project specific ProGuard rules here.
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
-keep class com.dark.desire.dating.network.models.** { *; }
