package org.xtream.demo.mobile.model.vehicles;

import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.components.AbstractLogicsComponent;
import org.xtream.demo.mobile.datatypes.Edge;

public class LogicsComponent extends AbstractLogicsComponent
{
	
	// Inputs
	
	public Port<Edge> startPositionInput = new Port<>();
	public Port<Edge> destinationPositionInput = new Port<>();
	public Port<Edge> positionOutgoingEdgesInput = new Port<>();
	public Port<Double> positionTraversedLengthInput = new Port<>();
	public Port<Double> positionEdgeLengthInput = new Port<>();
	public Port<Boolean> drivingIndicatorInput = new Port<>();
	
	// Outputs
	
	public Port<Edge> positionOutput = new Port<>();
	public Port<Edge> positionTargetOutput = new Port<>();
	public Port<Double> speedOutput = new Port<>();
	
	// Expressions
	
	public Expression<Edge> positionExpression = new Expression<Edge>(positionOutput)	
	{
		@Override 
		public Edge evaluate(int timepoint)
		{
			if (timepoint == 0) 
			{
				return startPositionInput.get(timepoint);	
			}
			else 
			{
				if (speedOutput.get(timepoint) > 0)
				{
					return positionTargetOutput.get(timepoint-1);
				}
				else 
				{
					return positionOutput.get(timepoint-1);
				}
			}
		}
	};
	
	public Expression<Edge> positionTargetExpression = new Expression<Edge>(positionTargetOutput)	
	{
		@Override 
		public Edge evaluate(int timepoint)
		{
			if (positionTraversedLengthInput.get(timepoint) >= positionEdgeLengthInput.get(timepoint))
			{
				return positionOutgoingEdgesInput.get(timepoint);	
			}
			else 
			{
				return positionOutput.get(timepoint);
			}
		}
	};
	
	public Expression<Double> speedExpression = new Expression<Double>(speedOutput)	
	{
		@Override 
		public Double evaluate(int timepoint)
		{
			if (drivingIndicatorInput.get(timepoint))
			{
				if (!(positionTargetOutput.get(timepoint-1).equals(startPositionInput.get(timepoint))) && !(positionOutput.get(timepoint-1).equals(destinationPositionInput.get(timepoint))))
				{
					// TODO [Dominik] return SetBuilder
					return 50.;
				}
				else 
				{
					return 0.;
				}
			}
			else 
			{
				return 0.;
			}
		}
	};

}