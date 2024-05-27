# EuclidView3d

## Description
Viewer for 3d geometrical objects like planes, spheres, points, circles and also complex objects like robots and sceletons.

![grafik](https://github.com/orat/EuclidView3d/assets/76894/7968dfd3-3ac8-4d74-ae38-7ab71c555f63)


Viewer component for 3d geometrical objects like planes, spheres, points, circles based on [jzy3d](https://www.jzy3d.org).

## Disclaimer
The project is in an early state of development, so it is not advised to use it in real world applications. If you have feedback or feature suggestions, please create a new GitHub Issue.

## Dependencies
The project depends on:

1. The vecmath library in the refactured version of jogamp. Your can find this library [here](https://jogamp.org/deployment/java3d/1.7.0-final/). Unfortunately there is no maven repository available. That is why you need to download the jar file manually and add it as a local depency of the project. To do this in the nebeans ide: Right-click on the depencies of the project and add the dependency manually. The group id is "org.jogamp.java3d", the artifactId is "vecmath" and the type is "jar".
2. The Euclid3DViewAPI can be found [here](https://github.com/orat/Euclid3DViewAPI). It defines a service provider interface which is implemented by EuclidView3d and allows to plugin the viewer into the toolchain to work with geometric algebra. Unfortunately there is no maven repository available. That is why you need to download the jar file manually and add it as a local depency of the project. To do this in the nebeans ide: Right-click on the depencies of the project and add the dependency manually. The group id is "de.orat.view3d", the artifactId is "Euclid3DViewAPI" and the type is "jar".

## Execution properties
To execute a program showing the UI component of EuclidView3d needs to set the following properties:

```--add-exports java.base/java.lang=ALL-UNNAMED --add-exports java.desktop/sun.awt=ALL-UNNAMED --add-exports java.desktop/sun.java2d=ALL-UNNAMED -Djava.util.Arrays.useLegacyMergeSort=true```

Inside the [Apache Netbeans-IDE]([https://](https://netbeans.apache.org)) you can set these properties by opening "Properties" in the context menu of your project in the "Run" part under "VM options".
