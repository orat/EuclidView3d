# EuclidView3d

## Disclaimer

The project is in an early state of development, so it is not advised to use it in real world 
applications. If you have feedback or feature suggestions, please create a new Issue.

## Description
Viewer for 3d geometrical objects like planes, spheres, points, circles and also complex objects like robots and sceletons.

Viewer component for 3d geometrical objects like planes, spheres, points, circles based on [jzy3d](https://www.jzy3d.org).

## Dependencies
The project depends on the vecmath library in the refactured version of jogamp. Your can find this library [here](https://jogamp.org/deployment/java3d/1.7.0-final/). Unfortunately there is no maven repository available. That is why you need to download the jar file manually and add it as a local depency of the project. To do this in the nebeans ide: Right-click on the depencies of the project and add the dependency manually. The group id is "org.jogamp.java3d", the artifactId is "vecmath" and the type is "jar".
