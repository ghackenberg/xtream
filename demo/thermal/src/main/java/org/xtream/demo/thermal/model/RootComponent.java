package org.xtream.demo.thermal.model;

import org.xtream.core.model.Component;
import org.xtream.core.model.Expression;
import org.xtream.core.model.Port;
import org.xtream.core.model.annotations.Objective;
import org.xtream.core.model.enumerations.Direction;
import org.xtream.core.optimizer.Engine;

public class RootComponent extends Component
{
	
	public static void main(String[] args)
	{
		new Engine<>(RootComponent.class).run(96, 100, 0);
	}
	
	////////////
	// INPUTS //
	////////////
	
	/* none */
	
	/////////////
	// OUTPUTS //
	/////////////

	@Objective(Direction.MIN)
	public Port<Double> objective = new Port<>();
	
	////////////////
	// COMPONENTS //
	////////////////
	
	public NetComponent net = new NetComponent(new NetComponent(5), new NetComponent(5));
	
	//////////////
	// CHANNELS //
	//////////////

	/* none */
	
	/////////////////
	// EXPRESSIONS //
	/////////////////
	
	public Expression<Double> objectiveExpression = new Expression<Double>(objective)
	{
		public double previous = 0.;
		
		@Override public Double evaluate(int timepoint)
		{
			return previous += net.balance.get(timepoint) * net.balance.get(timepoint);
		}
	};
	
	////////////
	// CHARTS //
	////////////
	
	/* none */

}
