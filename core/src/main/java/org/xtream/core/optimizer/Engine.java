package org.xtream.core.optimizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.xtream.core.model.Component;
import org.xtream.core.model.Port;

public class Engine
{
	
	public Class<? extends Component> type;
	public Component root;
	
	public Engine(Class<? extends Component> type)
	{
		this.type = type;
		
		try
		{
			root = type.newInstance();
			root.init();
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}
	
	public void run(int duration, int coverage, double randomness)
	{
		List<State> allPrevious = new ArrayList<>();
		
		State start = new State(root);
		
		start.save();
		
		allPrevious.add(start);
		
		int timepoint;
		
		for (timepoint = 0; timepoint < duration; timepoint++)
		{
			List<State> allCurrent = new ArrayList<>();
			
			SortedMap<Key, List<State>> allGroups = new TreeMap<>();
			
			int invalidCount = 0;
			int dominatedCount = 0;
			
			for (int sample = 0; sample < coverage; sample++)
			{
				// Select state
				
				State previous = allPrevious.get(0);
				
				if (sample < coverage * randomness)
				{
					int random = (int) Math.floor(Math.random() * allPrevious.size());
					
					previous = allPrevious.get(random);
				}
				
				previous.load();
				
				// Create state
				
				State current = new State(root, timepoint, previous);
				
				for (Port<?> port : root.portsRecursive)
				{
					port.get(timepoint);
				}
				
				// Check state
				
				boolean valid = true;
				
				for (Port<Boolean> constraint : root.constraintsRecursive)
				{
					valid = valid && constraint.get(timepoint);
				}
				
				if (valid)
				{
					// Group state
					
					Key key = new Key(root, timepoint);
					
					List<State> group = allGroups.get(key);
					
					if (group == null)
					{
						group = new ArrayList<>();
						
						allGroups.put(key, group);
					}
					
					// Check state
					
					boolean dominant = true;
					
					for (int i = 0; i < group.size(); i++)
					{
						State alternative = group.get(i);
						
						Integer difference = current.compareDominanceTo(alternative);
						
						if (difference != null)
						{
							if (difference < 0)
							{
								dominant = false;
								
								break; // do not keep
							}
							else if (difference == 0)
							{
								dominant = false;
								
								break; // do not keep
							}
							else if (difference > 0)
							{
								group.remove(i--);
								
								dominatedCount++;
							}
						}
					}
					
					// Save state
					
					if (dominant)
					{
						current.save();
						
						group.add(current);
						
						allCurrent.add(current);
					}
					else
					{
						dominatedCount++;
					}
				}
				else
				{
					invalidCount++;
				}
			}
			
			System.out.println("Timepoint " + timepoint + " = " + allCurrent.size() + " / " + invalidCount + " / " + dominatedCount);
			
			if (allCurrent.size() > 0)
			{
				allPrevious = allCurrent;
				
				Collections.sort(allPrevious);
			}
			else
			{
				break;
			}
		}
		
		dump(allPrevious.get(0));
	}
	
	public void dump(State state)
	{
		if (state.previous != null)
		{
			dump(state.previous);
			
			System.out.println();
			System.out.println("Timepoint " + state.timepoint);
			System.out.println();
			
			state.load();
			
			for (Port<?> port : root.portsRecursive)
			{
				System.out.println(port.name + " = " + port.get(state.timepoint));
			}
		}
	}

}
