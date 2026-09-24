package de.orat.math.view.euclidview3d.impl;

import org.jzy3d.chart.factories.IChartFactory;
import org.jzy3d.chart.factories.NewtPainterFactory;
import org.jzy3d.plot3d.rendering.canvas.CanvasNewtAwt;
import org.jzy3d.plot3d.rendering.canvas.ICanvas;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.scene.Scene;

/** Adds the missing scheduler cleanup to the Jzy3D 2.2.1 canvas. */
final class ClosingNewtPainterFactory extends NewtPainterFactory {

    @Override
    public ICanvas newCanvas(IChartFactory factory, Scene scene, Quality quality) {
        return new CanvasNewtAwt(factory, scene, quality, getCapabilities()) {
            @Override
            public void dispose() {
                try {
                    super.dispose();
                } finally {
                    // Jzy3D leaves this non-daemon worker alive after closing the window.
                    exec.shutdown();
                }
            }
        };
    }
}
