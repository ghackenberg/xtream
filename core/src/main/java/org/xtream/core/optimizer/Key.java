package org.xtream.core.optimizer;

import org.xtream.core.model.Component;

public class Key implements Comparable<Key>
{
	
	public double[] equivalences;
	
	public Key(Component root, double[] minEquivalences, double[] maxEquivalences, int classes, int timepoint)
	{
		equivalences = new double[root.equivalencesRecursive.size()];
		
		double scale = Math.pow(classes, 1. / root.equivalencesRecursive.size());
		
		if (timepoint >= 0)
		{
			for (int i = 0; i < root.equivalencesRecursive.size(); i++)
			{
				double originalValue = root.equivalencesRecursive.get(0).port.get(timepoint);
				double normalizedValue = (originalValue - minEquivalences[i]) / (maxEquivalences[i] - minEquivalences[i]) * scale;
				double discreteValue = Math.floor(normalizedValue);
				
				equivalences[i] = discreteValue;
			}
		}
	}

	@Override
	public int compareTo(Key other)
	{
		for (int index = 0; index < equivalences.length; index++)
		{
			double difference = equivalences[index] - other.equivalences[index];
			
			if (difference != 0)
			{
				// Difference found!
				return (int) Math.signum(difference);
			}
		}
		
		return 0;
	}
	
}
