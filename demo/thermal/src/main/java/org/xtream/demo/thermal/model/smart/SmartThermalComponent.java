package org.xtream.demo.thermal.model.smart;

import org.xtream.core.model.Expression;
import org.xtream.core.model.State;
import org.xtream.demo.thermal.model.ThermalComponent;

public class SmartThermalComponent extends ThermalComponent
{
	
	// Expressions
	
	public Expression<Boolean> commandExpression = new Expression<Boolean>(commandInput, true)
	{
		@Override protected Boolean evaluate(State state, int timepoint)
		{
			if (timepoint != 0 && temperatureOutput.get(state, timepoint - 1) + increase > maximumOutput.get(state, timepoint))
			{
				return true;
			}
			else if (timepoint != 0 && temperatureOutput.get(state, timepoint - 1) - decrease < minimumOutput.get(state, timepoint))
			{
				return false;
			}
			else
			{
				return Math.random() <= probabilityInput.get(state, timepoint) ? true : false;
			}
		}
		
	};
	
}
