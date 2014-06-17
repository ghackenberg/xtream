package org.xtream.core.model.aggregators;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.components.AggregatorComponent;
import org.xtream.core.optimizer.State;

public class AverageAggregatorComponent extends AggregatorComponent
{
	
	public AverageAggregatorComponent(int size)
	{
		super(size);
	}
	
	// Expressions
	
	public Expression<Double> valueExpression = new Expression<Double>(valueOutput)
	{
		@Override public Double evaluate(State state, int timepoint)
		{
			double sum = 0.;
			
			for (Port<Double> item : itemInputs)
			{
				sum += item.get(state, timepoint) / itemInputs.length;
			}
			
			return sum;
		}
	};

}
