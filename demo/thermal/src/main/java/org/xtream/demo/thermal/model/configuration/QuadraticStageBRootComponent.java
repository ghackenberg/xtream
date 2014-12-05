package org.xtream.demo.thermal.model.configuration;

import org.xtream.core.workbench.Workbench;
import org.xtream.demo.thermal.model.objective.QuadraticRootComponent;
import org.xtream.demo.thermal.model.stage.StageB;

public class QuadraticStageBRootComponent extends QuadraticRootComponent
{
	
	public static void main(String[] args)
	{
		new Workbench<>(new QuadraticStageBRootComponent(), DURATION, SAMPLES, CLASSES, ROUNDS, RANDOMNESS, CACHING, CLUSTER_ROUNDS);
	}

	public QuadraticStageBRootComponent()
	{
		super(new StageB());
	}

}
