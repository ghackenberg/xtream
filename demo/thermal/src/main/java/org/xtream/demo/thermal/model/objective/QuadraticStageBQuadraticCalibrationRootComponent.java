package org.xtream.demo.thermal.model.objective;

import org.xtream.core.optimizer.calibrators.QuadraticCalibrator;
import org.xtream.demo.thermal.model.stage.StageB;

public class QuadraticStageBQuadraticCalibrationRootComponent extends LinearRootComponent
{
	
	public static int PROCESSORS = Runtime.getRuntime().availableProcessors() - 2;
	
	public static int DURATION = 96;
	
	public static int CLUSTERS_START = 1;
	public static int CLUSTERS_END = 64;
	public static int CLUSTERS_STEPS = 7;
	
	public static int SAMPLES_START = 1;
	public static int SAMPLES_END = 64;
	public static int SAMPLES_STEPS = 7;
	
	public static double RANDOM_START = 0.0;
	public static double RANDOM_END = 1.0;
	public static int RANDOM_STEPS = 2;
	
	public static int ITERATIONS = 2;
	
	public static void main(String[] args)
	{
		new QuadraticCalibrator<>(QuadraticStageBQuadraticCalibrationRootComponent.class).run(PROCESSORS, DURATION, CLUSTERS_START, CLUSTERS_END, CLUSTERS_STEPS, SAMPLES_START, SAMPLES_END, SAMPLES_STEPS, RANDOM_START, RANDOM_END, RANDOM_STEPS, ITERATIONS);
	}

	public QuadraticStageBQuadraticCalibrationRootComponent()
	{
		super(new StageB());
	}

}