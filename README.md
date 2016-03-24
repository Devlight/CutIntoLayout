[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-CutIntoLayout-blue.svg?style=flat-square)](http://android-arsenal.com/details/1/3316)

CutIntoLayout
===================
CutIntoLayout allows you to create clear effect on your background.

![enter image description here](https://lh3.googleusercontent.com/fWz9orE3M8vaAuzkxHi0F2m23iuHUYlXmG-lgonEFCU=w210-h282-no)

U can check the sample app [here](https://github.com/GIGAMOLE/CutIntoLayout/tree/master/app).

Download
------------

You can download a .jar from GitHub's [releases page](https://github.com/GIGAMOLE/CutIntoLayout/releases).

Or use Gradle jCenter:
```groovy
dependencies {
    repositories {
        mavenCentral()
        maven {
            url  'http://dl.bintray.com/gigamole/maven/'
        }
    }
    compile 'com.github.gigamole.cutintolayout:library:+'
}
```

Or Gradle Maven Central:

```groovy
compile 'com.github.gigamole.cutintolayout:library:1.0.1'
```

Or Maven:
```xml
<dependency>
    <groupId>com.github.gigamole.cutintolayout</groupId>
    <artifactId>library</artifactId>
    <version>1.0.1</version>
    <type>aar</type>
</dependency>
```

Android SDK Version
=========
CutIntoLayout requires a minimum sdk version of 11.

Sample
========
CutIntoLayout must have child. Only one child.
You can put any view into layout.

XML init:
```xml
<com.gigamole.cutintolayout.lib.CutIntoLayout
    android:id="@+id/cut_into_layout"
    android:layout_width="200dp"
    android:layout_height="200dp"
    android:layout_gravity="center"
    android:background="@drawable/sample_bg">

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:gravity="center"
        android:text="@string/sample_title"
        android:textColor="@color/white" />

</com.gigamole.cutintolayout.lib.CutIntoLayout>
```

Getting Help
======

To report a specific problem or feature request, [open a new issue on Github](https://github.com/GIGAMOLE/CutIntoLayout/issues/new).

License
======
Apache 2.0. See LICENSE file for details.


Author
=======
Basil Miller - @gigamole
