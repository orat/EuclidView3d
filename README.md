# EuclidView3d
Viewer for 3d geometrical objects like planes, spheres, points, circles and also complex objects like robots and sceletons.

This implementation is in an very early stage.

Viewer component for 3d geometrical objects like planes, spheres, points, circles based on [jzy3d](https://www.jzy3d.org).

## Dependencies
The project depends on:

1. The vecmath library in the refactured version of jogamp. Your can find this library [here](https://jogamp.org/deployment/java3d/1.7.0-final/). Unfortunately there is no maven repository available. That is why you need to download the jar file manually and add it as a local depency of the project. To do this in the nebeans ide: Right-click on the depencies of the project and add the dependency manually. The group id is "org.jogamp.java3d", the artifactId is "vecmath" and the type is "jar".
2. Euclid3DViewAPI. This can be found [here](https://github.com/orat/Euclid3DViewAPI). It defines a service provider interface which is implemented by EuclidView3d and allows to plugin the viewer into the toolchain to work with geometric algebra.
