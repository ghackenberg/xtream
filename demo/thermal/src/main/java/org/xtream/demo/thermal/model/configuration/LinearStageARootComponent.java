package org.xtream.demo.thermal.model.configuration;

import org.xtream.core.workbench.Workbench;
import org.xtream.demo.thermal.model.objective.LinearRootComponent;
import org.xtream.demo.thermal.model.stage.StageA;

public class LinearStageARootComponent extends LinearRootComponent
{
	
	public static void main(String[] args)
	{
		new Workbench<>(new LinearStageARootComponent(), DURATION, SAMPLES, CLASSES, ROUNDS, RANDOMNESS, CACHING, CLUSTER_ROUNDS);
	}

	public LinearStageARootComponent()
	{
		super(new StageA());
	}

}
